package com.imaginariumfestival.android.programmation;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.artists.ArtistActivity;
import com.imaginariumfestival.android.artists.ArtistModel;
import com.imaginariumfestival.android.database.ArtistDataSource;

public class ProgrammationActivity extends Activity {
	private static final int STARTING_HOUR = 15;
	private static final int HOURS_IN_DAY = 24;
	private static final int MINUTES_IN_HOUR = 60;
	
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
		
		((LinearLayout)view).setX( getOffsetFromStringHour(artist.getBeginHour()) );
		int width = getOffsetFromStringHour(artist.getEndHour()) - getOffsetFromStringHour(artist.getBeginHour());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);

		final String stage = artist.getStage();
		if ( stage.equals(ArtistModel.MAIN_STAGE) ) {
			((RelativeLayout)findViewById(R.id.main_stage_layout)).addView(view, params);
		} else if ( stage.equals(ArtistModel.SECOND_STAGE) ) {
			((RelativeLayout)findViewById(R.id.second_stage_layout)).addView(view, params);			
		}
		
		view.setContentDescription( String.valueOf(artist.getId()) );
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toArtistActivityIntent = new Intent(ProgrammationActivity.this, ArtistActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("artistId", (String) view.getContentDescription());
				toArtistActivityIntent.putExtras(bundle);
				startActivity(toArtistActivityIntent);
			}
		});
	}

	private int getOffsetFromStringHour(String beginHour) {
		int hours = Integer.parseInt( (String) beginHour.subSequence(0, 2) );
		if (hours < 9) //after midnight is considered as the same day
			hours = hours + HOURS_IN_DAY;
		int minutes = Integer.parseInt( (String) beginHour.subSequence(3, 5) );
		return (( hours - STARTING_HOUR )*MINUTES_IN_HOUR) + minutes;
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
