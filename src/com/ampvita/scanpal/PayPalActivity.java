package com.ampvita.scanpal;

import java.math.BigDecimal;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

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
	
	String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_pal);



		//		Intent i = getIntent();
		//		Bundle b = i.getExtras();
		//		String contents = b.getString("profile", "profile");
		//		String amount = b.getString("amount", "amount");
		String contents = "contents";
		String amount = "amount";
		tvPayPal = (TextView) findViewById(R.id.tvPayPal);
		tvPayPal.setText("Contents:\n\n" + contents + "\n\n Amount: " + amount);
		

		String token = "";

		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

			protected String doInBackground(String... params) {
				try {
					HttpPost post = new HttpPost("http://scanpal-server.herokuapp.com/authenticate.php");
//					post.setHeader("Accept", "application/json");
//					post.setHeader("Accept-Language", "en_US");
//					List<NameValuePair> postParams = new ArrayList<NameValuePair>();
//					postParams.add(new BasicNameValuePair("AVRK6RDFLOqh0xSesWOgLsG8ULL1lwi6KwVpwh-X4VZXgTgZBckpgj5P8cjI", "EOmIwBCu9uygjS841kaS5L8ZCPRnSSZUpPSHrV5eq496WeKw_t4GbEW2jOBS"));
//					postParams.add(new BasicNameValuePair("grant_type", "client_credentials"));
//					post.setEntity(new UrlEncodedFormEntity(postParams));
					return convertStreamToString(new DefaultHttpClient().execute(post).getEntity().getContent()) ;//.toString();
				} catch (Exception e) {
					tvPayPal.setText(e.toString());
				}
				return "fail";
			}
		};
		try {
			token = task.execute("").get();
		} catch (Exception e) {
			tvPayPal.setText(e.toString());
		}

		tvPayPal.setText(token);

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
