package com.imaginariumfestival.android.infos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.data.InfosDataSource;

public class InfosActivity extends ListActivity {
	private List<InfoModel> infos;
	private Stack<Long> CategoryIdPathFromRoot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		CategoryIdPathFromRoot = new Stack<Long>();
		CategoryIdPathFromRoot.push((long) 0);
		
		InfosDataSource datasource = new InfosDataSource(InfosActivity.this);
		datasource.open();
		infos = datasource.getAllInfos();
		datasource.close();
		
		Collections.sort(infos, new Comparator<InfoModel>(){
			@Override
			public int compare(InfoModel lhs, InfoModel rhs) {
				if ( lhs.getIsCategory() && !rhs.getIsCategory() )
					return -1;
				else if ( !lhs.getIsCategory() && rhs.getIsCategory() )
					return 1;
				else
					return lhs.getName().compareTo(rhs.getName());
			}
		}); //TODO:quand on sortira la liste de la db, inutile ?
		
		computeListToView(0);
	}

	private void computeListToView(long parentId) {
		ArrayList<HashMap<String, String>> listItemToDisplay = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> infoItem;

		for (InfoModel info : infos) {
			if (info.getParent() == parentId) {
				infoItem = new HashMap<String, String>();
				infoItem.put("id", String.valueOf(info.getId()));
				infoItem.put("title", info.getName());
				infoItem.put("img", String.valueOf(R.drawable.artist_icon));
				infoItem.put("isCategory", String.valueOf(info.getIsCategory()));
				listItemToDisplay.add(infoItem);
			}
		}
		
		SimpleAdapter listAdapter = new SimpleAdapter(this.getBaseContext(),
				listItemToDisplay, R.layout.info_list_item, new String[] {
						"img", "title", "isCategory", "id" }, new int[] {
						R.id.infoListItemIcon, R.id.infoListItemName });
		setListAdapter(listAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> mapItem = (HashMap<String, String>) getListAdapter().getItem(position);
		if ( Boolean.valueOf(mapItem.get("isCategory")) ) {
			Long selectedId = Long.valueOf( mapItem.get("id") );
			CategoryIdPathFromRoot.push(selectedId);
			computeListToView( selectedId );
		} else {
			Toast.makeText(InfosActivity.this, "Quand l'activité existera ;)", Toast.LENGTH_SHORT).show();
//			Intent toInfoActivityIntent = new Intent(InfosActivity.this, InfosActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("infoId", mapItem.get("id"));
//			toInfoActivityIntent.putExtras(bundle);
//			startActivity(toInfoActivityIntent);
		}
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
			if ( CategoryIdPathFromRoot.peek() == 0 ) {
				NavUtils.navigateUpFromSameTask(this);
			} else {
				CategoryIdPathFromRoot.pop();
				computeListToView( CategoryIdPathFromRoot.peek() );
			}
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
	
}
