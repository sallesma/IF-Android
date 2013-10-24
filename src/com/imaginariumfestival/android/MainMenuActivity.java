package com.imaginariumfestival.android;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class MainMenuActivity extends Activity {
	private ImageButton artistsButton = null;
	private ImageButton photosButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu);
		
		if (isNetworkConnected(MainMenuActivity.this)) {
			SharedPreferences pref = getApplicationContext().getSharedPreferences("lastSync", 0); // 0 - for private mode
	        long millis = pref.getLong("lastSync", 0L);
	         
	        Date today = new Date();
	        long todayms = today.getTime();
	             
	        if(todayms - millis > 86400000){
	            BackTask backtask = new BackTask(this);
	            backtask.execute();
	        }
		}

		artistsButton = (ImageButton) findViewById(R.id.artistsButton);
		artistsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toArtistsActivity = new Intent(MainMenuActivity.this, ArtistsActivity.class);
				startActivity(toArtistsActivity);
			}
		});
		photosButton = (ImageButton) findViewById(R.id.photosButton);
		photosButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toPhotosActivity = new Intent(MainMenuActivity.this, PhotosActivity.class);
				startActivity(toPhotosActivity);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable() && cm
				.getActiveNetworkInfo().isConnected());
	}
}
