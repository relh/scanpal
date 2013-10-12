package com.ampvita.scanpal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button a = (Button)findViewById(R.id.button1);
		a.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent myIntent = new Intent(MainActivity.this, VoiceActivity.class);
			MainActivity.this.startActivity(myIntent); } });
		
		Button b = (Button)findViewById(R.id.button2);
		b.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent myIntent = new Intent(MainActivity.this, VoiceActivity.class);
			MainActivity.this.startActivity(myIntent); } });
		
		Button c = (Button)findViewById(R.id.button3);
		c.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent myIntent = new Intent(MainActivity.this, VoiceActivity.class);
			MainActivity.this.startActivity(myIntent); } });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
