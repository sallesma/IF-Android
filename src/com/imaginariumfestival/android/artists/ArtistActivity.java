package com.imaginariumfestival.android.artists;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.ArtistDataSource;
import com.imaginariumfestival.android.database.MySQLiteHelper;

public class ArtistActivity extends Activity {
	private ArtistModel artist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_artist);
		
		String artistId = (String) getIntent().getSerializableExtra("artistId");
	    if (artistId == null || artistId.equals("")) {
	    	Toast.makeText(this, getResources().getString(R.string.cannot_display_artist), Toast.LENGTH_LONG).show();
	    } else {
	    	ArtistDataSource datasource = new ArtistDataSource(ArtistActivity.this);
			datasource.open();
			artist = datasource.getArtistFromId(Long.parseLong(artistId));
			datasource.close();
			
			fillViewWithArtistData();
	    }
	    ((ImageButton) findViewById(R.id.back_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void fillViewWithArtistData() {
		((TextView)findViewById(R.id.artist_title)).setText(artist.getName());
		
		((TextView)findViewById(R.id.artistProgrammationDay)).setText(artist.getDay());
		((TextView)findViewById(R.id.artistProgrammationHour)).setText(artist.getBeginHour().substring(0, 5));
		((TextView)findViewById(R.id.artistProgrammationStage)).setText(artist.getStage());
		
		TextView artistDescription = (TextView)findViewById(R.id.artistDescription);
		artistDescription.setText(artist.getDescription());
		artistDescription.setMovementMethod(new ScrollingMovementMethod());
		Typeface euroFont = Typeface.createFromAsset(getAssets(), "eurof55.ttf");
		artistDescription.setTypeface(euroFont);
		
		updateLink(artist.getWebsite(), R.id.websiteIcon);
		updateLink(artist.getFacebook(), R.id.facebookIcon);
		updateLink(artist.getTwitter(), R.id.twitterIcon);
		updateLink(artist.getYoutube(), R.id.youtubeIcon);

		String filePath = getApplicationContext().getFilesDir() + "/" + MySQLiteHelper.TABLE_ARTIST + "/" + artist.getName();
		((ImageView) findViewById(R.id.artist_icon)).setImageBitmap(Utils
				.decodeSampledBitmapFromFile(filePath, getResources(),
						R.drawable.artist_empty_icon, 200, 200));
	}

	private void updateLink(final String url, final int viewId) {
		if (null != url && !url.equals("")) {
			((ImageButton)findViewById(viewId)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (Utils.isNetworkConnected(ArtistActivity.this)) {
						String finalUrl = url;
						if (!url.startsWith("https://") && !url.startsWith("http://")){
							finalUrl = "http://" + url;
						}
						Intent toWebViewIntent = new Intent(ArtistActivity.this, ArtistWebView.class);
						Bundle bundle = new Bundle();
						bundle.putString("weblink", finalUrl);
						bundle.putString("artistName", artist.getName());
						toWebViewIntent.putExtras(bundle);
						startActivity(toWebViewIntent);
					} else {
						Toast.makeText(ArtistActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
					}
				}
			});	
		} else {
			((ImageButton) findViewById(viewId)).setImageAlpha(50);
		}
	}
}
