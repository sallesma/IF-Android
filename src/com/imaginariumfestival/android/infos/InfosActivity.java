package com.imaginariumfestival.android.infos;

import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ListView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.database.InfosDataSource;

public class InfosActivity extends Activity {
	public static final long ROOT_ID = 0L;
	private List<InfoModel> infos;
	private Stack<Long> categoryIdPathFromRoot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		categoryIdPathFromRoot = new Stack<Long>();
		categoryIdPathFromRoot.push((long) ROOT_ID);
		
		InfosDataSource datasource = new InfosDataSource(InfosActivity.this);
		datasource.open();
		infos = datasource.getAllInfos();
		datasource.close();
		
		computeListToView(ROOT_ID);
	}

	private void computeListToView(long parentId) {
		setContentView(R.layout.activity_infos);
		ListView list = (ListView) findViewById(R.id.infosList);
		list.removeAllViewsInLayout();
		
		list.setAdapter( new InfosAdapter(InfosActivity.this, infos, parentId) );
	}

	public void onCategoryItemClick(long id) {
		categoryIdPathFromRoot.push( id );
		computeListToView( id );
	}
	public void onInfoItemClick(long id) {
		Intent toInfoActivityIntent = new Intent(InfosActivity.this, InfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("infoId", String.valueOf(id));
		toInfoActivityIntent.putExtras(bundle);
		startActivity(toInfoActivityIntent);
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
