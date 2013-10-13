package com.ampvita.scanpal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		//launch login with paypal here?
	}
	
	//result after login with paypal
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

	if (requestCode == 0) {
	    if (resultCode == RESULT_OK) {
	    	
	    }
	           //profile = intent.getStringExtra("SCAN_RESULT"); // This will contain your scan result
	           String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	           
//	           Intent toQR = new Intent(QRActivity.this, VoiceActivity.class);
//		       toQR.putExtra("profile", profile);
//		       startActivity(toQR);
	    }
	 }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
