package com.imaginariumfestival.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ArtistActivity extends Activity {
	private ArtistModel artist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_artist);
		
		Bundle  b = this.getIntent().getExtras();
	    if (!b.containsKey("artist")) {
	    	Toast.makeText(this, "Impossible d'afficher l'artiste", Toast.LENGTH_LONG).show();
	    } else {
	    	String artistName = (String) getIntent().getSerializableExtra("artist");
	    	getActionBar().setTitle(artistName);
	    	
	    	DbHelper dbHelper = DbHelper.getInstance();
	    	artist = dbHelper.getArtist(artistName);
	    	
	    	((ImageView)findViewById(R.id.artist_icon)).setImageResource(R.drawable.artist_icon);
	    	((TextView)findViewById(R.id.artistProgrammationStage)).setText(artist.getProgrammation().getStage());
	    	((TextView)findViewById(R.id.artistProgrammationDay)).setText(artist.getProgrammation().getDay());
	    	((TextView)findViewById(R.id.artistProgrammationHour)).setText(artist.getProgrammation().getHour());
	    	((TextView)findViewById(R.id.artistDescription)).setText(artist.getDescription());
	    	((TextView)findViewById(R.id.artistDescription)).setMovementMethod(new ScrollingMovementMethod());
	    	
	    	
	    	//TODO : mettre plutot une image "no link"
	    	if (null != artist.getYoutube() && !artist.getYoutube().equals("")) {
		    	((ImageButton)findViewById(R.id.youtubeIcon)).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(artist.getYoutube()));
						startActivity(intent);
					}
				});
	    	} else {
	    		((ImageButton)findViewById(R.id.youtubeIcon)).setVisibility(View.INVISIBLE);
	    	}
	    	if (null != artist.getFacebook() && !artist.getFacebook().equals("")) {
		    	((ImageButton)findViewById(R.id.facebookIcon)).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(artist.getFacebook()));
						startActivity(intent);
					}
				});
		    } else {
	    		((ImageButton)findViewById(R.id.facebookIcon)).setVisibility(View.INVISIBLE);
	    	}
	    	if (null != artist.getTwitter() && !artist.getTwitter().equals("")) {
		    	((ImageButton)findViewById(R.id.twitterIcon)).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(artist.getTwitter()));
						startActivity(intent);
					}
				});
			} else {
				((ImageButton)findViewById(R.id.twitterIcon)).setVisibility(View.INVISIBLE);
			}
		    if (null != artist.getWebsite() && !artist.getWebsite().equals("")) {
		    	((ImageButton)findViewById(R.id.websiteIcon)).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(artist.getWebsite()));
						startActivity(intent);
					}
				});	
			} else {
				((ImageButton) findViewById(R.id.websiteIcon)).setVisibility(View.INVISIBLE);
			}
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
	
}
