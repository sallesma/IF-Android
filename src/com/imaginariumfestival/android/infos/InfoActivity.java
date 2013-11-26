package com.imaginariumfestival.android.infos;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.data.InfosDataSource;

public class InfoActivity extends Activity {
	private static final String DEFAULT_CATEGORY_NAME = "Aucune";
	private InfoModel info;
	private InfoModel parent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_info);
		
		String infoId = (String) getIntent().getSerializableExtra("infoId");
	    if (infoId == null || infoId.equals("")) {
	    	Toast.makeText(this, "Impossible d'afficher l'artiste", Toast.LENGTH_LONG).show();
	    } else {
	    	InfosDataSource datasource = new InfosDataSource(InfoActivity.this);
			datasource.open();
			info = datasource.getInfoFromId(Long.parseLong(infoId));
			parent = datasource.getInfoFromId(info.getParentId());
			datasource.close();
			
			fillViewWithInfoData();
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_info, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_type_see_on_map:
			Toast.makeText(InfoActivity.this, "Quand l'activité existera ;)", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
	
	private void fillViewWithInfoData() {
		getActionBar().setTitle(info.getName());
		
		((ImageView)findViewById(R.id.infoPicture)).setImageResource(R.drawable.artist_empty_icon);
		
		if ( parent != null) {
			((TextView)findViewById(R.id.infoCategoryName)).setText(parent.getName());
		} else {
			((TextView)findViewById(R.id.infoCategoryName)).setText(DEFAULT_CATEGORY_NAME);
		}
		
		((TextView)findViewById(R.id.infoContent)).setText(info.getContent());
		((TextView)findViewById(R.id.infoContent)).setMovementMethod(new ScrollingMovementMethod());
	}
}
