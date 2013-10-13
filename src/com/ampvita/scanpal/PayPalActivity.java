package com.ampvita.scanpal;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;

public class PayPalActivity extends Activity {

	TextView tvPayPal;
	String contents = "contents";
	String amount = "amount";
//	String hc = "";

	String token  = "";
	String amazon = "";
	String ebay   = "";
	String img    ;//= "https://raw.github.com/agentwaj/scanpal-server/3d5c9bf61de6d91dd8c2b4a088560baf2a039a3f/img.jpg";
	//	String img    = "http://s15.postimg.org/om5la87yj/iphone.jpg"; // IPHONE
	//	String img    = "http://s10.postimg.org/7yaouawfb/img.jpg"; // HANDBAG
	//	String img    = "https://raw.github.com/agentwaj/scanpal-server/master/iphone.jpg";

	String convertStreamToString(HttpPost post) {
		java.util.Scanner s = null;
		try {
			s = new java.util.Scanner(new DefaultHttpClient().execute(post).getEntity().getContent()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (Exception e) {}
		return "fail";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_pal);

		//FROM WHEN WE READ THINGS FROM THE QR ACTIVITY (DEPRECATED)
		Intent i = getIntent();
		final Bundle b = i.getExtras();
		amazon = b.getString("profile", "profile");
//		hc = b.getString("profile", "profile");

		int option = b.getInt("cropped");
		
		if (amazon.equals("profile")) {
			ImageView ivCropped = (ImageView) findViewById(R.id.ivCropped);
			switch (option) {
			case 0:
				img = "https://raw.github.com/agentwaj/scanpal-server/c6dc72dd845f985e3ca241cd5c9824d370d77616/images/cropped_img_1.jpg";
				ivCropped.setImageResource(R.raw.cropped_img_1);
				amazon = "Apple-shuffle-Slate-Generation-NEWEST";
				break;
			case 1:
				img = "https://raw.github.com/agentwaj/scanpal-server/c6dc72dd845f985e3ca241cd5c9824d370d77616/images/cropped_img_2.jpg";
				ivCropped.setImageResource(R.raw.cropped_img_2);
				amazon = "Stop-Blank-Billboard-Your-Advertising";
				break;
			case 2:
				img = "https://raw.github.com/agentwaj/scanpal-server/c6dc72dd845f985e3ca241cd5c9824d370d77616/images/cropped_img_3.jpg";
				ivCropped.setImageResource(R.raw.cropped_img_3);
				amazon = "Motorola-A855-Android-Verizon-Wireless";
				break;
			}
		}
		
		
		//THIS ASYNC TASK IS FOR AUTHENTICATING OUR KEY WITH PAYPAL
		tvPayPal = (TextView) findViewById(R.id.tvPayPal);

		try {
			new AsyncTask<String, Void, String>() {
				protected String doInBackground(String... params) {
					try {
						HttpPost tokenPost = new HttpPost("http://scanpal-server.herokuapp.com/authenticate.php");
						token = convertStreamToString(tokenPost);

						if (amazon.equals("profile")) {
							HttpPost amazonPost = new HttpPost("http://scanpal-server.herokuapp.com/scrape.php");
							List<NameValuePair> amazonParams = new ArrayList<NameValuePair>();
							amazonParams.add(new BasicNameValuePair("img", URLEncoder.encode(params[0], "UTF-8")));
							amazonPost.setEntity(new UrlEncodedFormEntity(amazonParams));
							amazon = convertStreamToString(amazonPost).replace("-", " ");
						}

						HttpPost ebayPost = new HttpPost("http://scanpal-server.herokuapp.com/index.php");
						List<NameValuePair> ebayParams = new ArrayList<NameValuePair>();
						String encoded = URLEncoder.encode(amazon, "UTF-8");
						ebayParams.add(new BasicNameValuePair("keywords", encoded));
						ebayPost.setEntity(new UrlEncodedFormEntity(ebayParams));
						ebay = convertStreamToString(ebayPost);
					} catch (Exception e) {
						tvPayPal.setText(e.toString());
					}
					return "";
				}
			}.execute(img).get();
		} catch (Exception e) {
			tvPayPal.setText(e.toString());
		}

		Log.i("zzz", token);
		Log.i("zzz", amazon);
		Log.i("zzz", ebay);
		//tvPayPal.setText(token + "\n\n" + amazon + "\n\n" + ebay);
		tvPayPal.setText("Amazon: " + amazon + "\n\nEbay: " + ebay);
		
		
		// PAY PAL DONATE BUTTON
		PayPal pp = PayPal.getInstance();

		if (pp == null) {
			pp = PayPal.initWithAppID(this, "APP-80W284485P519543T", PayPal.ENV_NONE);
			pp.setLanguage("en_US");
			pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);
			pp.setShippingEnabled(true);
		}

		CheckoutButton button = pp.getCheckoutButton(this, PayPal.BUTTON_278x43, CheckoutButton.TEXT_PAY);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				PayPalPayment payment = new PayPalPayment();
				payment.setCurrencyType("USD");
				payment.setRecipient("scanpalhack@gmail.com");
				payment.setSubtotal(new BigDecimal("1.00"));
				payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
				startActivityForResult(PayPal.getInstance().checkout(payment, PayPalActivity.this, new ResultDelegate()), 1);
			}
		});

		button.setLayoutParams(
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		((LinearLayout) findViewById(R.id.llPayPal)).addView(button, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
