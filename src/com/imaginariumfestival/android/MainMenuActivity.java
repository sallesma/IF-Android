package com.imaginariumfestival.android;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imaginariumfestival.android.artists.ArtistsActivity;
import com.imaginariumfestival.android.database.BackTask;
import com.imaginariumfestival.android.database.NewsDataSource;
import com.imaginariumfestival.android.infos.InfosActivity;
import com.imaginariumfestival.android.map.MapActivity;
import com.imaginariumfestival.android.news.NewsModel;
import com.imaginariumfestival.android.photos.PhotosTakingActivity;
import com.imaginariumfestival.android.programmation.ProgrammationActivity;

public class MainMenuActivity extends Activity {
	private ImageButton artistsButton = null;
	private ImageButton infosButton = null;
	private ImageButton photosButton = null;
	private ImageButton programmationButton = null;
	private ImageButton mapButton = null;

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
		addNewsView();
	}


	private void addNewsView() {
		NewsDataSource newsDataSource = new NewsDataSource(this);
		newsDataSource.open();
		NewsModel news = newsDataSource.getLastNews();
		newsDataSource.close();
		
		if (news != null) {
			RelativeLayout footerLayout = (RelativeLayout) findViewById(R.id.main_activity_footer);
			
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View newsView = inflater.inflate(R.layout.news_view, null);
			footerLayout.addView(newsView);

			((TextView)findViewById(R.id.news_content)).setText(news.getContent());
		}
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
		programmationButton= (ImageButton) findViewById(R.id.programmationButton);
		programmationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toProgrammationActivity = new Intent(MainMenuActivity.this, ProgrammationActivity.class);
				startActivity(toProgrammationActivity);
			}
		});
		mapButton= (ImageButton) findViewById(R.id.mapButton);
		mapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toMapActivity = new Intent(MainMenuActivity.this, MapActivity.class);
				startActivity(toMapActivity);
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
