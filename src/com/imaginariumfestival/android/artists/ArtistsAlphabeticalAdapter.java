package com.imaginariumfestival.android.artists;

import java.text.Normalizer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
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

public class ArtistsAlphabeticalAdapter extends BaseAdapter implements SectionIndexer {
	List<ArtistModel> artists;
	private Context context;
	
    public ArtistsAlphabeticalAdapter(Context context, List<ArtistModel> artists) {
    	Collections.sort(artists, new Comparator<ArtistModel>(){
			@Override
			public int compare(ArtistModel lhs, ArtistModel rhs) {
				String firstName = Normalizer.normalize(lhs.getName().toUpperCase(), Normalizer.Form.NFD);
				firstName = firstName.replaceAll("[^\\p{ASCII}]", "");
				
				String secondName = Normalizer.normalize(rhs.getName().toUpperCase(), Normalizer.Form.NFD);
				secondName = secondName.replaceAll("[^\\p{ASCII}]", "");
				
				return firstName.compareTo(secondName);
			}
    	});
        this.artists = artists;
        this.context = context;
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
		char firstChar = artist.getName().toUpperCase().charAt(0);
		if (position == 0) {
			setSection(header, artist.getName());
		} else {
			String preLabel = artists.get(position - 1).getName();
			char preFirstChar = preLabel.toUpperCase().charAt(0);
			if (firstChar != preFirstChar) {
				setSection(header, artist.getName());
			} else {
				header.setVisibility(View.GONE);
			}
		}
		
		fillArtistData(view, artist);
		
		return view;
	}

	private void fillArtistData(View view, ArtistModel artist) {
		((TextView) view.findViewById(R.id.artistListItemName)).setText(artist.getName());
		((TextView) view.findViewById(R.id.artistListItemProgrammation)).setText(artist.getProgrammation());
		
		String filePath = context.getFilesDir() + "/" + MySQLiteHelper.TABLE_ARTIST + "/" + artist.getName();
		
		((ImageView) view.findViewById(R.id.artistListItemIcon))
				.setImageBitmap(Utils.decodeSampledBitmapFromFile(filePath,
						context.getResources(), R.drawable.artist_empty_icon,
						80, 80));

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
		header.setBackgroundColor(0xffaabbcc);
		text.setTextColor(Color.WHITE);
		text.setText(label.substring(0, 1).toUpperCase());
		text.setTextSize(20);
		text.setPadding(5, 0, 0, 0);
		text.setGravity(Gravity.CENTER_VERTICAL);
		header.addView(text);
	}

	@Override
	public int getPositionForSection(int sectionIndex) {  
        if (sectionIndex == 35) {  
            return 0;  
        }  
        for (int i = 0; i < artists.size(); i++) {  
            char firstChar = artists.get(i).getName().toUpperCase().charAt(0);  
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
