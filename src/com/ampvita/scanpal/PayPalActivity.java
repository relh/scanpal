package com.ampvita.scanpal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PayPalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_pal);
		
		Intent i = getIntent();
		Bundle b = i.getExtras();
		String contents = b.getString("contents", "contents");
		String amount = b.getString("amount", "amount");
		((TextView) findViewById(R.id.tvPayPal)).setText("Contents: " + contents + ", Amount: " + amount);
	}
	
}
