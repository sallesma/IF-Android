package com.imaginariumfestival.android.partners;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.database.MySQLiteHelper;
import com.imaginariumfestival.android.database.PartnersDataSource;

public class PartnersActivity extends Activity {
	List<PartnerModel> partners;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_partners);
		
		PartnersDataSource partnerDataSource = new PartnersDataSource(PartnersActivity.this);
		partnerDataSource.open();
		partners = partnerDataSource.getAllPartners();
		partnerDataSource.close();
		
		for (PartnerModel partner : partners) {
			fillViewWithPartnerData(partner);
		}
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
	
	private void fillViewWithPartnerData( PartnerModel partner) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.partner_item, null);
		int childCount = ((ViewGroup) view).getChildCount();
		
		for (int i = 0; i < childCount; i++) {
			View child = ((ViewGroup) view).getChildAt(i);
			
			if (child.getId() == R.id.partner_icon) {
				File filePath = new File(getApplicationContext().getFilesDir() + "/" + MySQLiteHelper.TABLE_PARTNERS + "/" + partner.getName());
				Drawable picture = Drawable.createFromPath(filePath.toString());
				if (picture != null) {
					((ImageView) child).setImageDrawable(picture);
				} else {
					((ImageView) child).setImageResource(R.drawable.artist_empty_icon);
				}
			} else if ( child.getId() == R.id.partner_name ) {
				((TextView) child).setText(partner.getName());
			}
		}
		((GridLayout)findViewById(R.id.partnerGridLayout)).addView(view);
	}
}

