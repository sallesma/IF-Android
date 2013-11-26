package com.imaginariumfestival.android;

import java.util.Date;

import com.imaginariumfestival.android.artists.ArtistsActivity;
import com.imaginariumfestival.android.data.BackTask;
import com.imaginariumfestival.android.infos.InfosActivity;
import com.imaginariumfestival.android.photos.PhotosTakingActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

public class MainMenuActivity extends Activity {
	private ImageButton artistsButton = null;
	private ImageButton infosButton = null;
	private ImageButton photosButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu);
		
		if (isNetworkConnected(MainMenuActivity.this)) {
			SharedPreferences pref = getApplicationContext().getSharedPreferences(BackTask.LAST_UPDATE_FROM_DISTANT_DATABASE, Context.MODE_PRIVATE);
	        long lastUpdateMillis = pref.getLong(BackTask.LAST_UPDATE_FROM_DISTANT_DATABASE, 0L);
	         
	        Date now = new Date();
	        long nowMillis = now.getTime();
	        
	        final int updateEvery10Min = 600000;
	        if(nowMillis - lastUpdateMillis > updateEvery10Min) {
	            BackTask backtask = new BackTask(this);
	            backtask.execute();
	            
	            Date currentDate = new Date();
	    		Editor globalEditor = pref.edit();
	    		globalEditor.putLong(BackTask.LAST_UPDATE_FROM_DISTANT_DATABASE, currentDate.getTime());
	    		globalEditor.commit();
	        }
		}

		initialiseButtonsLinks();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	private boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable() && cm
				.getActiveNetworkInfo().isConnected());
	}
	
	private void initialiseButtonsLinks() {
		artistsButton = (ImageButton) findViewById(R.id.artistsButton);
		artistsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toArtistsActivity = new Intent(MainMenuActivity.this, ArtistsActivity.class);
				startActivity(toArtistsActivity);
			}
		});
		infosButton = (ImageButton) findViewById(R.id.infosButton);
		infosButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toInfosActivity = new Intent(MainMenuActivity.this, InfosActivity.class);
				startActivity(toInfosActivity);
			}
		});
		photosButton = (ImageButton) findViewById(R.id.photosButton);
		photosButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toPhotosActivity = new Intent(MainMenuActivity.this, PhotosTakingActivity.class);
				startActivity(toPhotosActivity);
			}
		});
	}
}
