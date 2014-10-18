package com.imaginariumfestival.android.infos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.InfosDataSource;
import com.imaginariumfestival.android.database.MySQLiteHelper;
import com.imaginariumfestival.android.map.MapActivity;

public class InfoActivity extends Activity {
	private static final String DEFAULT_CATEGORY_NAME = "Aucune";
	private InfoModel info;
	private InfoModel parent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_info);
		
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NavUtils.navigateUpFromSameTask(InfoActivity.this);
			}
		});
		Utils.addAlphaEffectOnClick(backButton);
		
		String infoId = (String) getIntent().getSerializableExtra("infoId");
	    if (infoId == null || infoId.equals("")) {
	    	Toast.makeText(this, getResources().getString(R.string.cannot_display_info), Toast.LENGTH_LONG).show();
	    } else {
	    	InfosDataSource datasource = new InfosDataSource(InfoActivity.this);
			datasource.open();
			info = datasource.getInfoFromId(Long.parseLong(infoId));
			parent = datasource.getInfoFromId(info.getParentId());
			datasource.close();
			
			fillViewWithInfoData();
			
			if (info.getIsDisplayedOnMap(this.getApplicationContext())) {
				((Button) findViewById(R.id.action_type_see_on_map)).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent toInfoActivityIntent = new Intent(InfoActivity.this, MapActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("infoId", String.valueOf(info.getId()));
						toInfoActivityIntent.putExtras(bundle);
						startActivity(toInfoActivityIntent);
					}
				});
			} else {
				((Button) findViewById(R.id.action_type_see_on_map)).setVisibility(View.GONE);
			}
	    }
	}
	
	private void fillViewWithInfoData() {
		((TextView)findViewById(R.id.info_title)).setText(info.getName());
		
		String filePath = getApplicationContext().getFilesDir() + "/" + MySQLiteHelper.TABLE_INFOS + "/" + info.getName();
		((ImageView) findViewById(R.id.infoPicture)).setImageBitmap(Utils
				.decodeSampledBitmapFromFile(filePath, getResources(),
						R.drawable.info_empty_icon, 80, 80));
		
		if ( parent != null) {
			((TextView)findViewById(R.id.infoCategoryName)).setText(parent.getName());
		} else {
			((TextView)findViewById(R.id.infoCategoryName)).setText(DEFAULT_CATEGORY_NAME);
		}
		
		Typeface euroFont = Typeface.createFromAsset(getAssets(), "eurof55.ttf");
		TextView infoContent = (TextView)findViewById(R.id.infoContent);
		infoContent.setTypeface( euroFont );
		infoContent.setText( info.getContent() );
		infoContent.setMovementMethod( new ScrollingMovementMethod() );
	}
}
