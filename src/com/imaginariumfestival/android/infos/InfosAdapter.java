package com.imaginariumfestival.android.infos;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.MySQLiteHelper;

public class InfosAdapter extends BaseAdapter implements SectionIndexer {
	List<InfoModel> infos;
	private Context context;
	
    public InfosAdapter(Context context, List<InfoModel> infos, long parentId) {
    	Collections.sort(infos, new Comparator<InfoModel>(){
			@Override
			public int compare(InfoModel lhs, InfoModel rhs) {
				if ( lhs.getIsCategory() && !rhs.getIsCategory() )
					return -1;
				else if ( !lhs.getIsCategory() && rhs.getIsCategory() )
					return 1;
				else {
					String firstName = Normalizer.normalize(lhs.getName().toUpperCase(), Normalizer.Form.NFD);
					firstName = firstName.replaceAll("[^\\p{ASCII}]", "");
					
					String secondName = Normalizer.normalize(rhs.getName().toUpperCase(), Normalizer.Form.NFD);
					secondName = secondName.replaceAll("[^\\p{ASCII}]", "");
					
					return firstName.compareTo(secondName);
				}
			}
		});
    	
    	this.infos = new ArrayList<InfoModel>();
    	for (InfoModel info : infos) {
    		if (info.getParentId() == parentId) {
    			this.infos.add(info);
    		}
    	}
        this.context = context;
    }

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return infos.get(position).getId();
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		LayoutInflater inflate = ((Activity) context).getLayoutInflater();
		View view = (View) inflate.inflate(R.layout.info_list_item, null);
		
		InfoModel info = infos.get(position);
		
		Typeface euroFont = Typeface.createFromAsset(context.getAssets(), "eurof55.ttf");  
		((TextView) view.findViewById(R.id.infoListItemName)).setTypeface(euroFont);
		((TextView) view.findViewById(R.id.infoListItemName)).setText(info.getName());
		
		String filePath = context.getFilesDir() + "/" + MySQLiteHelper.TABLE_INFOS + "/" + info.getName();
		
		((LinearLayout) view.findViewById(R.id.info_list_item)).setContentDescription(String.valueOf(info.getId()));
		if ( info.getIsCategory() ) {
			((ImageView) view.findViewById(R.id.infoListItemIcon))
			.setImageBitmap(Utils.decodeSampledBitmapFromFile(
					filePath, context.getResources(),
					R.drawable.category_empty_icon, 80, 80));
			((ImageView) view.findViewById(R.id.infoListCategoryArrow)).setVisibility(View.VISIBLE);
			((LinearLayout) view.findViewById(R.id.info_list_item)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((InfosActivity) context).onCategoryItemClick( Long.valueOf(v.getContentDescription().toString()) );
				}
			});
		} else {
			((ImageView) view.findViewById(R.id.infoListItemIcon))
			.setImageBitmap(Utils.decodeSampledBitmapFromFile(
					filePath, context.getResources(),
					R.drawable.info_empty_icon, 80, 80));
			((LinearLayout) view.findViewById(R.id.info_list_item)).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((InfosActivity) context).onInfoItemClick( Long.valueOf(v.getContentDescription().toString()) );
				}
			});
		}
		return view;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {  
        if (sectionIndex == 35) {  
            return 0;  
        }  
        for (int i = 0; i < infos.size(); i++) {  
            char firstChar = infos.get(i).getName().toUpperCase().charAt(0);  
            if (firstChar == sectionIndex) {  
                return i;  
            }  
        }  
        return -1;  
    }

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

}
