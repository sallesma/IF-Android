package com.imaginariumfestival.android;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imaginariumfestival.android.artists.ArtistsActivity;
import com.imaginariumfestival.android.database.BackTask;
import com.imaginariumfestival.android.database.NewsDataSource;
import com.imaginariumfestival.android.infos.InfosActivity;
import com.imaginariumfestival.android.map.MapActivity;
import com.imaginariumfestival.android.news.NewsModel;
import com.imaginariumfestival.android.partners.PartnersActivity;
import com.imaginariumfestival.android.photos.PhotosTakingActivity;
import com.imaginariumfestival.android.programmation.ProgrammationActivity;

public class MainMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu);
		
		if (Utils.isNetworkConnected(MainMenuActivity.this)) {
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

		initializeMenuButtonsLinks();
		initializeSocialNetworkButtonsLinks();
		addNewsView();
		addTwitterView();
		Typeface euroFont = Typeface.createFromAsset(getAssets(), "eurof55.ttf");
		((TextView) findViewById(R.id.about)).setTypeface(euroFont);
	}
	
	private void initializeMenuButtonsLinks() {
		ImageButton artistsButton = (ImageButton) findViewById(R.id.artistsButton);
		artistsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toArtistsActivity = new Intent(MainMenuActivity.this, ArtistsActivity.class);
				startActivity(toArtistsActivity);
			}
		});
		ImageButton infosButton = (ImageButton) findViewById(R.id.infosButton);
		infosButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toInfosActivity = new Intent(MainMenuActivity.this, InfosActivity.class);
				startActivity(toInfosActivity);
			}
		});
		ImageButton programmationButton= (ImageButton) findViewById(R.id.programmationButton);
		programmationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toProgrammationActivity = new Intent(MainMenuActivity.this, ProgrammationActivity.class);
				startActivity(toProgrammationActivity);
			}
		});
		ImageButton mapButton= (ImageButton) findViewById(R.id.mapButton);
		mapButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toMapActivity = new Intent(MainMenuActivity.this, MapActivity.class);
				startActivity(toMapActivity);
			}
		});
		ImageButton photosButton = (ImageButton) findViewById(R.id.photosButton);
		photosButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toPhotosActivity = new Intent(MainMenuActivity.this, PhotosTakingActivity.class);
				startActivity(toPhotosActivity);
			}
		});
		ImageButton partnersButton = (ImageButton) findViewById(R.id.partnersButton);
		partnersButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toPartnersActivity = new Intent(MainMenuActivity.this, PartnersActivity.class);
				startActivity(toPartnersActivity);
			}
		});
	}
	
	private void initializeSocialNetworkButtonsLinks() {
		((ImageButton) findViewById(R.id.facebook_home_icon)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Utils.isNetworkConnected(MainMenuActivity.this)) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse( getResources().getString(R.string.facebook_link ) ));
					startActivity(intent);
				} else {
					Toast.makeText(MainMenuActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
				}
			}
		});
		((ImageButton) findViewById(R.id.twitter_home_icon)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Utils.isNetworkConnected(MainMenuActivity.this)) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse( getResources().getString(R.string.twitter_link ) ));
					startActivity(intent);
				} else {
					Toast.makeText(MainMenuActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
				}
			}
		});
		((ImageButton) findViewById(R.id.google_plus_home_icon)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Utils.isNetworkConnected(MainMenuActivity.this)) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse( getResources().getString(R.string.google_plus_link ) ));
					startActivity(intent);
				} else {
					Toast.makeText(MainMenuActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void addNewsView() {
		NewsDataSource newsDataSource = new NewsDataSource(this);
		newsDataSource.open();
		NewsModel news = newsDataSource.getLastNews();
		newsDataSource.close();
		
		if (news != null) {
			((TextView)findViewById(R.id.news_content)).setText(news.getContent());
		} else {
			((TextView)findViewById(R.id.news_content)).setText( getResources().getString(R.string.no_news) );
		}
		((ImageButton)findViewById(R.id.show_news)).setOnTouchListener(new OnTouchListener() {
			int _xDeltaButton;
			int _xDeltaContent;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int X = (int) event.getRawX();
				RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) ((ImageButton)findViewById(R.id.show_news)).getLayoutParams();
				RelativeLayout.LayoutParams contentLayoutParams = (RelativeLayout.LayoutParams) ((TextView)findViewById(R.id.news_content)).getLayoutParams();
			    switch (event.getAction() & MotionEvent.ACTION_MASK) {
			        case MotionEvent.ACTION_DOWN:
			            _xDeltaButton = X - buttonLayoutParams.leftMargin;
			            _xDeltaContent = X - contentLayoutParams.leftMargin;
			            break;
			        case MotionEvent.ACTION_UP:
			            break;
			        case MotionEvent.ACTION_POINTER_DOWN:
			            break;
			        case MotionEvent.ACTION_POINTER_UP:
			            break;
			        case MotionEvent.ACTION_MOVE:
			            buttonLayoutParams.leftMargin = X - _xDeltaButton;
			            contentLayoutParams.leftMargin = X - _xDeltaContent;
			            ((ImageButton)findViewById(R.id.show_news)).setLayoutParams(buttonLayoutParams);
			            ((TextView)findViewById(R.id.news_content)).setLayoutParams(contentLayoutParams);
			            break;
			    }
				return false;
			}
		});
	}
	
	private void addTwitterView() {
		((TextView)findViewById(R.id.twitter_content)).setText( getResources().getString(R.string.no_news) );
		((ImageButton)findViewById(R.id.show_twitter)).setOnTouchListener(new OnTouchListener() {
			int _xDeltaButton;
			int _xDeltaContent;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int X = (int) event.getRawX();
				RelativeLayout.LayoutParams buttonLayoutParams = (RelativeLayout.LayoutParams) ((ImageButton)findViewById(R.id.show_twitter)).getLayoutParams();
				RelativeLayout.LayoutParams contentLayoutParams = (RelativeLayout.LayoutParams) ((TextView)findViewById(R.id.twitter_content)).getLayoutParams();
			    switch (event.getAction() & MotionEvent.ACTION_MASK) {
			        case MotionEvent.ACTION_DOWN:
			            _xDeltaButton = X - buttonLayoutParams.leftMargin;
			            _xDeltaContent = X - contentLayoutParams.leftMargin;
			            break;
			        case MotionEvent.ACTION_UP:
			            break;
			        case MotionEvent.ACTION_POINTER_DOWN:
			            break;
			        case MotionEvent.ACTION_POINTER_UP:
			            break;
			        case MotionEvent.ACTION_MOVE:
			            buttonLayoutParams.leftMargin = X - _xDeltaButton;
			            contentLayoutParams.leftMargin = X - _xDeltaContent;
			            ((ImageButton)findViewById(R.id.show_twitter)).setLayoutParams(buttonLayoutParams);
			            ((TextView)findViewById(R.id.twitter_content)).setLayoutParams(contentLayoutParams);
			            break;
			    }
				return false;
			}
		});
	}
}
