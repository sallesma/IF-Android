package com.imaginariumfestival.android.artists;

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

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.data.ArtistDataSource;

public class ArtistActivity extends Activity {
	private ArtistModel artist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_artist);
		
		String artistId = (String) getIntent().getSerializableExtra("artistId");
	    if (artistId == null || artistId.equals("")) {
	    	Toast.makeText(this, "Impossible d'afficher l'artiste", Toast.LENGTH_LONG).show();
	    } else {
	    	ArtistDataSource datasource = new ArtistDataSource(ArtistActivity.this);
			datasource.open();
			artist = datasource.getArtistFromId(Long.parseLong(artistId));
			datasource.close();
			
			fillViewWithArtistData();
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

	private void fillViewWithArtistData() {
		getActionBar().setTitle(artist.getName());
		((ImageView)findViewById(R.id.artist_icon)).setImageResource(R.drawable.artist_icon);
		((TextView)findViewById(R.id.artistProgrammationStage)).setText(artist.getScene());
		((TextView)findViewById(R.id.artistProgrammationDay)).setText(artist.getJour());
		((TextView)findViewById(R.id.artistProgrammationHour)).setText(artist.getDebut());
		((TextView)findViewById(R.id.artistDescription)).setText(artist.getDescription());
		((TextView)findViewById(R.id.artistDescription)).setMovementMethod(new ScrollingMovementMethod());
		
		//TODO : mettre plutot une image "no link" dans ces méthodes
		updateWebsiteLink();
		updateFacebookLink();
		updateTwitterLink();
		updateYoutubeLink();
	}

	private void updateWebsiteLink() {
		if (null != artist.getWebsite() && !artist.getWebsite().equals("")) {
			((ImageButton)findViewById(R.id.websiteIcon)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = artist.getWebsite();
					if (!url.startsWith("https://") && !url.startsWith("http://")){
						url = "http://" + url;
					}
					Intent toWebViewIntent = new Intent(ArtistActivity.this, ArtistWebView.class);
					Bundle bundle = new Bundle();
					bundle.putString("weblink", url);
					bundle.putString("artistName", artist.getName());
					toWebViewIntent.putExtras(bundle);
					startActivity(toWebViewIntent);
				}
			});	
		} else {
			((ImageButton) findViewById(R.id.websiteIcon)).setVisibility(View.INVISIBLE);
		}
	}

	private void updateFacebookLink() {
		if (null != artist.getFacebook() && !artist.getFacebook().equals("")) {
			((ImageButton)findViewById(R.id.facebookIcon)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					String url = artist.getFacebook();
					if (!url.startsWith("https://") && !url.startsWith("http://")){
					    url = "http://" + url;
					}
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
			});
		} else {
			((ImageButton)findViewById(R.id.facebookIcon)).setVisibility(View.INVISIBLE);
		}
	}

	private void updateTwitterLink() {
		if (null != artist.getTwitter() && !artist.getTwitter().equals("")) {
			((ImageButton)findViewById(R.id.twitterIcon)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					String url = artist.getTwitter();
					if (!url.startsWith("https://") && !url.startsWith("http://")){
						url = "http://" + url;
					}
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
			});
		} else {
			((ImageButton)findViewById(R.id.twitterIcon)).setVisibility(View.INVISIBLE);
		}
	}
	
	private void updateYoutubeLink() {
		if (null != artist.getYoutube() && !artist.getYoutube().equals("")) {
			((ImageButton)findViewById(R.id.youtubeIcon)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					String url = artist.getYoutube();
					if (!url.startsWith("https://") && !url.startsWith("http://")){
						url = "http://" + url;
					}
					intent.setData(Uri.parse(url));
					startActivity(intent);
				}
			});
		} else {
			((ImageButton)findViewById(R.id.youtubeIcon)).setVisibility(View.INVISIBLE);
		}
	}
}
