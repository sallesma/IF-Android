package com.imaginariumfestival.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ArtistsActivity extends Activity {
	private ListView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artists);

		view = (ListView) findViewById(R.id.artistsList);

		DbHelper dbHelper = DbHelper.getInstance();
		List<ArtistModel> artists = dbHelper.getArtists();

		List<HashMap<String, String>> liste = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> element;
		for (ArtistModel artist : artists) {
			element = new HashMap<String, String>();
			element.put("name", artist.getName());
			element.put("photoUrl", artist.getPhotoUrl());
			liste.add(element);
		}

		ListAdapter adapter = new SimpleAdapter(this,
				liste,
				android.R.layout.simple_list_item_2,

				new String[] { "photo", "name" },
				new int[] { android.R.id.text1, android.R.id.text2 });
		view.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
}
