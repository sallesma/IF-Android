package com.imaginariumfestival.android.programmation;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.artists.ArtistModel;
import com.imaginariumfestival.android.data.ArtistDataSource;

public class ProgrammationActivity extends Activity {
	private List<ArtistModel> artists;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_programmation);
		
		ArtistDataSource artistDataSource = new ArtistDataSource(this);
		artistDataSource.open();
		artists = artistDataSource.getAllArtists();
		artistDataSource.close();
	}
}
