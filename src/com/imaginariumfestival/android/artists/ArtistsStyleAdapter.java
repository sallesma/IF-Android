package com.imaginariumfestival.android.artists;

import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.MySQLiteHelper;

public class ArtistsStyleAdapter extends BaseAdapter implements SectionIndexer {
	List<ArtistModel> artists;
	Map<Long, Bitmap> artistsIcons;
	private Context context;
	
    public ArtistsStyleAdapter(Context context, List<ArtistModel> artists) {
    	Collections.sort(artists, new Comparator<ArtistModel>(){
			@Override
			public int compare(ArtistModel lhs, ArtistModel rhs) {
				String firstStyle = computeWithoutAccent(lhs.getStyle().toUpperCase());
				
				String secondStyle = computeWithoutAccent(rhs.getStyle().toUpperCase());
				
				return firstStyle.compareTo(secondStyle);
			}
    	});
        this.artists = artists;
        this.context = context;
        artistsIcons = new HashMap<Long, Bitmap>();
        
        for (ArtistModel artist : artists) {
        	String filePath = context.getFilesDir() + "/" + MySQLiteHelper.TABLE_ARTIST + "/" + artist.getName();
    		Bitmap artistIcon = Utils.decodeSampledBitmapFromFile(filePath,context.getResources(),
    				R.drawable.artist_empty_icon, 80, 80);
    		artistIcon = Utils.getRoundedCornerBitmap(artistIcon, 20);
    		artistsIcons.put(artist.getId(), artistIcon);
		}
    }

	@Override
	public int getCount() {
		return artists.size();
	}

	@Override
	public Object getItem(int position) {
		return artists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return artists.get(position).getId();
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		LayoutInflater inflate = ((Activity) context).getLayoutInflater();
		View view = (View) inflate.inflate(R.layout.artist_list_item, null);
		LinearLayout header = (LinearLayout) view.findViewById(R.id.artist_section);
		
		ArtistModel artist = artists.get(position);
		String firstLabel = computeWithoutAccent( artist.getStyle().toUpperCase() );
		if (position == 0) {
			setSection(header, artist.getStyle());
		} else {
			String preLabel = computeWithoutAccent( artists.get(position - 1).getStyle().toUpperCase());
			if ( !firstLabel.equals(preLabel) ) {
				setSection(header, artist.getStyle());
			} else {
				header.setVisibility(View.GONE);
			}
		}
		
		fillArtistData(view, artist);
		
		return view;
	}

	private void fillArtistData(View view, ArtistModel artist) {
		Typeface euroFont = Typeface.createFromAsset(context.getAssets(), "eurof55.ttf");  
		((TextView) view.findViewById(R.id.artistListItemName)).setTypeface(euroFont);
		((TextView) view.findViewById(R.id.artistListItemProgrammation)).setTypeface(euroFont);
		((TextView) view.findViewById(R.id.artistListItemName)).setText(artist.getName());
		((TextView) view.findViewById(R.id.artistListItemProgrammation)).setText(artist.getProgrammation());
		
		((ImageView) view.findViewById(R.id.artistListItemIcon))
			.setImageBitmap( artistsIcons.get(artist.getId()) );
		
		((LinearLayout) view.findViewById(R.id.artist_list_item)).setContentDescription(String.valueOf(artist.getId()));
		((LinearLayout) view.findViewById(R.id.artist_list_item)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toArtistActivityIntent = new Intent(context,ArtistActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("artistId", v.getContentDescription().toString());
				toArtistActivityIntent.putExtras(bundle);
				context.startActivity(toArtistActivityIntent);
			}
		});
	}
	
	private void setSection(LinearLayout header, String label) {
		TextView text = new TextView(context);
		text.setTextColor(Color.parseColor("#E66524"));
		text.setText(label);
		text.setTextSize(20);
		text.setPadding(5, 0, 0, 0);
		text.setGravity(Gravity.CENTER);
		
		View separation = new View(context);
		separation.setBackgroundColor(Color.parseColor("#E66524"));
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
		separation.setLayoutParams(params);
		
		header.addView(text);
		header.addView(separation);
	}

	@Override
	public int getPositionForSection(int sectionIndex) {  
        if (sectionIndex == 35) {  
            return 0;  
        }  
        for (int i = 0; i < artists.size(); i++) {  
            char firstChar = artists.get(i).getStyle().toUpperCase().charAt(0);  
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

	private String computeWithoutAccent(String string) {
		String firstStyle = Normalizer.normalize(string, Normalizer.Form.NFD);
		firstStyle = firstStyle.replaceAll("[^\\p{ASCII}]", "");
		return firstStyle;
	}
}
