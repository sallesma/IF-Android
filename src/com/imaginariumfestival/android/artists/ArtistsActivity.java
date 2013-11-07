package com.imaginariumfestival.android.artists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.data.ArtistDataSource;

public class ArtistsActivity extends ListActivity {
	private List<ArtistModel> artists;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		ArtistDataSource datasource = new ArtistDataSource(ArtistsActivity.this);
		datasource.open();
		artists = datasource.getAllArtists();
		datasource.close();
		
		computeListToView(new Comparator<ArtistModel>(){
			@Override
			public int compare(ArtistModel lhs, ArtistModel rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		});

	}

	private void computeListToView(Comparator<ArtistModel> comparator) {
		Collections.sort(artists, comparator); //TODO:quand on sortira la liste de la db, inutile ?
		
		ArrayList<HashMap<String, String>> listItemToDisplay = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> artistItem;

		for (ArtistModel artist : artists) {
			artistItem = new HashMap<String, String>();
			artistItem.put("id", String.valueOf(artist.getId()));
			artistItem.put("title", artist.getName());
			artistItem.put("programmation", artist.getProgrammation().toString());
			artistItem.put("img", String.valueOf(R.drawable.artist_icon));
			listItemToDisplay.add(artistItem);
		}
		
		SimpleAdapter listAdapter = new SimpleAdapter(this.getBaseContext(),
				listItemToDisplay, R.layout.artist_list_item, new String[] {
						"img", "title", "programmation", "id" }, new int[] {
						R.id.artistListItemIcon, R.id.artistListItemName,
						R.id.artistListItemProgrammation });
		setListAdapter(listAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) getListAdapter().getItem(position);
		Intent toArtistActivityIntent = new Intent(ArtistsActivity.this,ArtistActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("artistId", map.get("id"));
		toArtistActivityIntent.putExtras(bundle);
		startActivity(toArtistActivityIntent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
				computeListToView(new Comparator<ArtistModel>(){
					@Override
					public int compare(ArtistModel lhs, ArtistModel rhs) {
						return lhs.getStyle().compareTo(rhs.getStyle());
					}
				});
				
			} else {
				menuItem.setTitle(R.string.action_type_sort);
				computeListToView(new Comparator<ArtistModel>(){
					@Override
					public int compare(ArtistModel lhs, ArtistModel rhs) {
						return lhs.getName().compareTo(rhs.getName());
					}
				});
			}
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
	
}
