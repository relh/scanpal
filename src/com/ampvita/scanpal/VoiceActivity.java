package com.ampvita.scanpal;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import java.util.ArrayList;
import java.util.regex.*;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class VoiceActivity extends Activity {

	protected static final int RESULT_SPEECH = 1;
	
	private TextView txtText;
	
	String profile = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice);
		
		txtText = (TextView) findViewById(R.id.txtText);

		Intent intent = new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
		
		try {
			startActivityForResult(intent, RESULT_SPEECH);
			txtText.setText("");
		} catch (ActivityNotFoundException a) {
			Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
		}
		
		Intent fromQR = getIntent();
		profile = fromQR.getStringExtra("profile");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		String amount = "";
		float amount_f = 0;
		
		switch (requestCode) {
		case RESULT_SPEECH: {
			if (resultCode == RESULT_OK && null != data) {
				ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				txtText.setText(text.get(0));
				amount = text.get(0);
				amount_f = 0;
				Pattern money = Pattern.compile("\\d+(\\.\\d{2})?");
				try {
					//Matcher matched = money.matcher(text.get(0));
					Matcher matched = money.matcher(amount);
					while (matched.find()) {
						amount_f = Float.parseFloat(matched.group());
					}
					Log.d("voice",amount);
				} catch (Exception e) {
					Log.d("voice","no match");
				}
			}
		}
		}
		
		Intent toPayPal = new Intent(VoiceActivity.this, PayPalActivity.class);
		toPayPal.putExtra("profile", profile);
		toPayPal.putExtra("amount", amount_f);
	    startActivity(toPayPal);
	}
	
}
