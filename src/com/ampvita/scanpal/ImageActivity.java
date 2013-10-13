package com.ampvita.scanpal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		ImageView iv1 = (ImageView) findViewById(R.id.iv1);
		ImageView iv2 = (ImageView) findViewById(R.id.iv2);
		ImageView iv3 = (ImageView) findViewById(R.id.iv3);
		Button bCamera = (Button) findViewById(R.id.bCamera);
		
		iv1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ImageActivity.this, PayPalActivity.class);
				i.putExtra("cropped", 0);
				startActivity(i);
			}
		});
		
		iv2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ImageActivity.this, PayPalActivity.class);
				i.putExtra("cropped", 1);
				startActivity(i);
			}
		});
		
		iv3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ImageActivity.this, PayPalActivity.class);
				i.putExtra("cropped", 2);
				startActivity(i);
			}
		});

		bCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent("android.media.action.IMAGE_CAPTURE"));
			}
		});

	}
}
