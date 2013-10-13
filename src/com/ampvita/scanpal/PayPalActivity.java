package com.ampvita.scanpal;

import java.io.Serializable;
import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalPayment;
import com.paypal.android.MEP.PayPalResultDelegate;

public class PayPalActivity extends Activity {

	TextView tvPayPal;
	
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
}
