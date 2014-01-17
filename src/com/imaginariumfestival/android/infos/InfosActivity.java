package com.imaginariumfestival.android.infos;

import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.InfosDataSource;

public class InfosActivity extends Activity {
	public static final long ROOT_ID = 0L;
	private List<InfoModel> infos;
	private Stack<Long> categoryIdPathFromRoot;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_infos);

		categoryIdPathFromRoot = new Stack<Long>();
		categoryIdPathFromRoot.push((long) ROOT_ID);
		
		InfosDataSource datasource = new InfosDataSource(InfosActivity.this);
		datasource.open();
		infos = datasource.getAllInfos();
		datasource.close();
		
		computeListToView(ROOT_ID);
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( categoryIdPathFromRoot.peek() == 0 ) {
					NavUtils.navigateUpFromSameTask(InfosActivity.this);
				} else {
					categoryIdPathFromRoot.pop();
					computeListToView( categoryIdPathFromRoot.peek() );
				}
			}
		});
		Utils.addAlphaEffectOnClick(backButton);
	}

	private void computeListToView(long parentId) {
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
}
