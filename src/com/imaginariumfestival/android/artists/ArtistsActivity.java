package com.imaginariumfestival.android.artists;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.database.ArtistDataSource;

public class ArtistsActivity extends Activity {
	private List<ArtistModel> artists;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		ArtistDataSource datasource = new ArtistDataSource(ArtistsActivity.this);
		datasource.open();
		artists = datasource.getAllArtists();
		datasource.close();
		
		computeListToView( new ArtistsAlphabeticalAdapter(this, artists) );
	}

	private void computeListToView(ListAdapter adapter) {
		setContentView(R.layout.activity_artists);
		ListView list = (ListView) findViewById(R.id.artistsList);
		list.removeAllViewsInLayout();
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_artists, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_type_sort:
			if (menuItem.getTitle().toString() == getString(R.string.action_type_sort)) {
				menuItem.setTitle(R.string.action_alpha_sort);
				computeListToView( new ArtistsStyleAdapter(this, artists) );
				
			} else {
				menuItem.setTitle(R.string.action_type_sort);
				computeListToView( new ArtistsAlphabeticalAdapter(this, artists) );
			}
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
	
}
