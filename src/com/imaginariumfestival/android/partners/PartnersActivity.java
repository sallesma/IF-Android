package com.imaginariumfestival.android.partners;

import java.util.List;

import android.app.Activity;
import android.content.Context;
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
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.MySQLiteHelper;
import com.imaginariumfestival.android.database.PartnersDataSource;

public class PartnersActivity extends Activity {
	private static int PARTNERS_COLUMNS = 2;
	private GridLayout gridLayout = null;
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
		
		int total = partners.size();
		int rowNumber = total / PARTNERS_COLUMNS;
		gridLayout = ((GridLayout)findViewById(R.id.partnerGridLayout));
		
		gridLayout.setRowCount(rowNumber + 1);
		for (int i = 0, column = 0, row = 0; i < total; i++, column++) {
			if (column == PARTNERS_COLUMNS) {
				column = 0;
				row++;
			}
			fillViewWithPartnerData(partners.get(i), column, row);
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
	
	private void fillViewWithPartnerData( PartnerModel partner, int column, int row) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.partner_item, null);
		int childCount = ((ViewGroup) view).getChildCount();
		
		for (int i = 0; i < childCount; i++) {
			View child = ((ViewGroup) view).getChildAt(i);
			
			if (child.getId() == R.id.partner_icon) {
				String filePath = getApplicationContext().getFilesDir() + "/"
						+ MySQLiteHelper.TABLE_PARTNERS + "/" + partner.getName();
				((ImageView) child).setImageBitmap(Utils
						.decodeSampledBitmapFromFile(filePath, getResources(),
								R.drawable.artist_empty_icon, 80, 80));
			} else if ( child.getId() == R.id.partner_name ) {
				((TextView) child).setText(partner.getName());
			}
		}
		GridLayout.LayoutParams param =new GridLayout.LayoutParams();
		param.columnSpec = GridLayout.spec(column);
        param.rowSpec = GridLayout.spec(row);
        param.rightMargin = 5;
        param.topMargin = 5;
        view.setLayoutParams (param);
		((GridLayout)findViewById(R.id.partnerGridLayout)).addView(view);
	}
}

