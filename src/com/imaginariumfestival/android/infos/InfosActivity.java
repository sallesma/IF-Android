package com.imaginariumfestival.android.infos;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.database.InfosDataSource;
import com.imaginariumfestival.android.database.MySQLiteHelper;

public class InfosActivity extends ListActivity {
	private List<InfoModel> infos;
	private Stack<Long> categoryIdPathFromRoot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		categoryIdPathFromRoot = new Stack<Long>();
		categoryIdPathFromRoot.push((long) 0);
		
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
			if (info.getParentId() == parentId) {
				infoItem = new HashMap<String, String>();
				infoItem.put("id", String.valueOf(info.getId()));
				infoItem.put("title", info.getName());
				infoItem.put("isCategory", String.valueOf(info.getIsCategory()));
				
				File filePath = new File(getApplicationContext().getFilesDir() + "/" + MySQLiteHelper.TABLE_INFOS + "/" + info.getName());
				if (filePath != null) {
					infoItem.put("img", String.valueOf(filePath));
				}

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
			categoryIdPathFromRoot.push(selectedId);
			computeListToView( selectedId );
		} else {
			Intent toInfoActivityIntent = new Intent(InfosActivity.this, InfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("infoId", mapItem.get("id"));
			toInfoActivityIntent.putExtras(bundle);
			startActivity(toInfoActivityIntent);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			if ( categoryIdPathFromRoot.peek() == 0 ) {
				NavUtils.navigateUpFromSameTask(this);
			} else {
				categoryIdPathFromRoot.pop();
				computeListToView( categoryIdPathFromRoot.peek() );
			}
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
	
}
