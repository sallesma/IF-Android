package com.imaginariumfestival.android.infos;

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
import com.imaginariumfestival.android.data.InfosDataSource;

public class InfosActivity extends ListActivity {
	private List<InfoModel> infos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		InfosDataSource datasource = new InfosDataSource(InfosActivity.this);
		datasource.open();
		infos = datasource.getAllInfos();
		datasource.close();
		
		computeListToView();

	}

	private void computeListToView() {
		Collections.sort(infos, new Comparator<InfoModel>(){
			@Override
			public int compare(InfoModel lhs, InfoModel rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}
		}); //TODO:quand on sortira la liste de la db, inutile ?
		
		ArrayList<HashMap<String, String>> listItemToDisplay = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> artistItem;

		for (InfoModel artist : infos) {
			artistItem = new HashMap<String, String>();
			artistItem.put("id", String.valueOf(artist.getId()));
			artistItem.put("title", artist.getName());
			artistItem.put("img", String.valueOf(R.drawable.artist_icon));
			listItemToDisplay.add(artistItem);
		}
		
		SimpleAdapter listAdapter = new SimpleAdapter(this.getBaseContext(),
				listItemToDisplay, R.layout.info_list_item, new String[] {
						"img", "title", "programmation", "id" }, new int[] {
						R.id.infoListItemIcon, R.id.infoListItemName });
		setListAdapter(listAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) getListAdapter().getItem(position);
		Intent toInfoActivityIntent = new Intent(InfosActivity.this, InfosActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("infoId", map.get("id"));
		toInfoActivityIntent.putExtras(bundle);
		startActivity(toInfoActivityIntent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
