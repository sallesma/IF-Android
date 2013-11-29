package com.imaginariumfestival.android.map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.imaginariumfestival.android.R;

public class MapActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_map);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_map, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_type_filter:
			return true;
		case R.id.action_type_filter_bar:
			Toast.makeText(MapActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_type_filter_food:
			Toast.makeText(MapActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_type_filter_stage:
			Toast.makeText(MapActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_type_filter_asso:
			Toast.makeText(MapActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_type_filter_security:
			Toast.makeText(MapActivity.this, "Not implemented yet", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
}
