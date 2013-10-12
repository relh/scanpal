package com.ampvita.scanpal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class QRActivity extends Activity {

	String profile = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);
		
		Intent launchQR = new Intent(
                "com.google.zxing.client.android.SCAN");
        launchQR.putExtra("SCAN_MODE", "PRODUCT_MODE");
        startActivityForResult(launchQR, 0);
	}

	// ZXing Result Handler

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

	if (requestCode == 0) {
	    if (resultCode == RESULT_OK) {
	    	
	    }
	           profile = intent.getStringExtra("SCAN_RESULT"); // This will contain your scan result
	           String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	           
	           Intent toVoice = new Intent(QRActivity.this, VoiceActivity.class);
		       toVoice.putExtra("profile", profile);
		       startActivity(toVoice);
	    }
	 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qr, menu);
		return true;
	}

}
