package com.imaginariumfestival.android.partners;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.MySQLiteHelper;
import com.imaginariumfestival.android.database.PartnersDataSource;

public class PartnersActivity extends Activity {
	private LinearLayout linearLayout = null;
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
		
		linearLayout = ((LinearLayout)findViewById(R.id.partnerLinearLayout));
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
	
	private void fillViewWithPartnerData( final PartnerModel partner) {
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
								R.drawable.artist_empty_icon, 150, 150));
			} else if ( child.getId() == R.id.partner_name ) {
				((TextView) child).setText(partner.getName());
			}
		}
		
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String url = partner.getWebsite();
				if (!url.startsWith("https://") && !url.startsWith("http://")){
					url = "http://" + url;
				}
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
		});
		linearLayout.addView(view);
	}
}

