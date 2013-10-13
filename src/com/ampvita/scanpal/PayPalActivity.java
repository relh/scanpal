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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

	String token  = "";
	String amazon = "";
	String ebay   = "";
//	String img    = "https://raw.github.com/agentwaj/scanpal-server/3d5c9bf61de6d91dd8c2b4a088560baf2a039a3f/img.jpg";
	String img    = "http://s15.postimg.org/om5la87yj/iphone.jpg";

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
		//		Intent i = getIntent();
		//		Bundle b = i.getExtras();
		//		final String content = b.getString("profile", "profile");
		//		String amount = b.getString("amount", "amount");

		//THIS ASYNC TASK IS FOR AUTHENTICATING OUR KEY WITH PAYPAL
		tvPayPal = (TextView) findViewById(R.id.tvPayPal);

		try {
			new AsyncTask<String, Void, String>() {
				protected String doInBackground(String... params) {
					try {
						HttpPost tokenPost = new HttpPost("http://scanpal-server.herokuapp.com/authenticate.php");
						token = convertStreamToString(tokenPost);

						HttpPost amazonPost = new HttpPost("http://scanpal-server.herokuapp.com/scrape.php");
						List<NameValuePair> amazonParams = new ArrayList<NameValuePair>();
						amazonParams.add(new BasicNameValuePair("img", URLEncoder.encode(params[0], "UTF-8")));
						amazonPost.setEntity(new UrlEncodedFormEntity(amazonParams));
						amazon = convertStreamToString(amazonPost).replace("-", " ");

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
		tvPayPal.setText(token + "\n\n" + amazon + "\n\n" + ebay);


		//	EBAY POST EXAMPLE
		//		String keywords = "surface%20pro";
		//		String eBayURL = "http://svcs.ebay.com/services/search/FindingService/v1?SECURITY-APPNAME=scanpalg-41d3-4ac7-8ea3-dd855899e8a1&OPERATION-NAME=findItemsByKeywords&SERVICE-VERSION=1.0.0&RESPONSE-DATA-FORMAT=JSON&callback=_cb_findItemsByKeywords&REST-PAYLOAD&paginationInput.entriesPerPage=1&keywords=" + keywords;
		//
		//		HttpPost post = new HttpPost(eBayURL);
		//		String result = "not working";
		//		try {
		//			result = convertStreamToString(new DefaultHttpClient().execute(post).getEntity().getContent());
		//			Log.i("zzz", result);
		//		} catch (Exception e) {
		//			Log.i("zzz", e.getMessage());
		//			result = "failed";
		//		}
		//		
		//		tvPayPal.setText(eBayURL);

		// EBAY SERVER HEROKUAPP
		//		HttpPost post = new HttpPost("http://scanpal-server.herokuapp.com");
		//		String result = "failzzzz";
		//		try {
		////			result = convertStreamToString(new DefaultHttpClient().execute(post).getEntity().getContent());
		//			result = new DefaultHttpClient().execute(post).toString();
		//		} catch (Exception e) {result = e.getMessage() + "lol";}
		//
		//		tvPayPal.setText(result);

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
		Bundle b = data.getExtras();
		for (String s : b.keySet()) {
			tvPayPal.setText("--" + b.getByte(s) + "\n");
		}
	}

}
