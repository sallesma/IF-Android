package com.imaginariumfestival.android.artists;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.database.ArtistDataSource;

public class ArtistsActivity extends Activity {
	private List<ArtistModel> artists;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_artists);

		ArtistDataSource datasource = new ArtistDataSource(ArtistsActivity.this);
		datasource.open();
		artists = datasource.getAllArtists();
		datasource.close();
		
		initializeButtons();
		computeListToView( new ArtistsAlphabeticalAdapter(this, artists) );
	}

	private void initializeButtons() {
		((ImageButton) findViewById(R.id.back_button)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NavUtils.navigateUpFromSameTask(ArtistsActivity.this);
			}
		});
		((Button) findViewById(R.id.action_type_sort)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				computeListToView( new ArtistsStyleAdapter(ArtistsActivity.this, artists) );
			}
		});
		((Button) findViewById(R.id.action_alpha_sort)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				computeListToView( new ArtistsAlphabeticalAdapter(ArtistsActivity.this, artists) );
			}
		});
	}

	private void computeListToView(ListAdapter adapter) {
		ListView list = (ListView) findViewById(R.id.artistsList);
		list.removeAllViewsInLayout();
		list.setAdapter(adapter);
	}
}
