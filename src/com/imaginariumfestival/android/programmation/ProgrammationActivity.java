package com.imaginariumfestival.android.programmation;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.artists.ArtistModel;
import com.imaginariumfestival.android.database.ArtistDataSource;

public class ProgrammationActivity extends Activity {
	private List<ArtistModel> artists;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_programmation);
		
		final ArtistDataSource artistDataSource = new ArtistDataSource(this);
		artistDataSource.open();
		artists = artistDataSource.getAllArtists();
		artistDataSource.close();
		
		for (ArtistModel artist : artists) {
			displayProgrammationOnScreen(artist);
		}
	}

	private void displayProgrammationOnScreen(final ArtistModel artist) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.programmation_item, null);
		
		fillChildrenData(artist, view);
		
		final String stage = artist.getStage();
		RelativeLayout stageLayout = null;
		if ( stage.equals(ArtistModel.MAIN_STAGE) ) {
			stageLayout = ((RelativeLayout)findViewById(R.id.main_stage_layout));
		} else if ( stage.equals(ArtistModel.SECOND_STAGE) ) {
			stageLayout = ((RelativeLayout)findViewById(R.id.second_stage_layout));			
		}
		stageLayout.addView(view);
		
		((LinearLayout)view).setX( getOffsetFromStringHour(artist.getBeginHour()) );
	}

	private int getOffsetFromStringHour(String beginHour) {
		int hours = Integer.parseInt( (String) beginHour.subSequence(0, 1) );
		int minutes = Integer.parseInt( (String) beginHour.subSequence(3, 4) );
		return (hours*60) + minutes;
	}

	private void fillChildrenData(final ArtistModel artist, final View view) {
		int childCount = ((ViewGroup) view).getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = ((ViewGroup) view).getChildAt(i);
			
			if (child.getId() == R.id.programmation_icon ) {
				File filePath = getFileStreamPath(artist.getName());
				Drawable picture = Drawable.createFromPath(filePath.toString());
				if (picture != null) {
					((ImageView) child).setImageDrawable(picture);
				} else {
					((ImageView) child).setImageResource(R.drawable.artist_empty_icon);
				}
			} else if ( child.getId() == R.id.programmation_name ) {
				((TextView) child).setText(artist.getName());
			}
		}
	}
}
