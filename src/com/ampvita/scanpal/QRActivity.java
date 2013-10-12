package com.ampvita.scanpal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class QRActivity extends Activity {

	String profile = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr);
		
		Intent intent = new Intent(
                "com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
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
