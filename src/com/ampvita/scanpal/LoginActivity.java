package com.ampvita.scanpal;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	
		File temp = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/bus3.jpg");

		if(temp.exists()){
		    Bitmap myBitmap = BitmapFactory.decodeFile(temp.getAbsolutePath());
			Toast.makeText(getApplicationContext(), myBitmap.toString(), Toast.LENGTH_SHORT).show();
		    
		    ImageView myImage = (ImageView) findViewById(R.id.ImageView);
		    myImage.setImageBitmap(myBitmap);
		}
		
		AsyncTask<String, Void, String> startTask = new AsyncTask<String, Void, String>() {

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				Intent toPayPal = new Intent(LoginActivity.this, PayPalActivity.class);
			    startActivity(toPayPal);
			}

			protected String doInBackground(String... params) {
				try {
					HttpPost post = new HttpPost("http://scanpal-server.herokuapp.com/start.php");
					return convertStreamToString(new DefaultHttpClient().execute(post).getEntity().getContent());//.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "fail";
			}
			
		};
		try {
			startTask.execute("").get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
