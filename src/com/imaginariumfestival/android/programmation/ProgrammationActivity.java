package com.imaginariumfestival.android.programmation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.artists.ArtistActivity;
import com.imaginariumfestival.android.artists.ArtistModel;
import com.imaginariumfestival.android.database.ArtistDataSource;
import com.imaginariumfestival.android.database.MySQLiteHelper;

public class ProgrammationActivity extends Activity {
	public static final String FIRST_DAY = "vendredi";
	public static final String SECOND_DAY = "samedi";
	private static final String SEPARATION_BETWEEN_DAYS = "31/05/2014 05:00";

	public static final int HOURS_IN_DAY = 24;
	private static final int STARTING_HOUR = 14;
	private static final int ENDING_HOUR = 28; //04 AM late in the night
	private static final int MINUTES_IN_HOUR = 60;
	private static final int SIZE_COEFFICIENT = 2;
	
	private static final boolean FIRST_DAY_SWITCH_VALUE = false;
	private static final boolean SECOND_DAY_SWITCH_VALUE = true;

	private List<ArtistModel> artists;
	private RelativeLayout mainStageLayout;
	private RelativeLayout secondStageLayout;
	private Switch switchButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_programmation);

		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NavUtils.navigateUpFromSameTask(ProgrammationActivity.this);
			}
		});
		Utils.addAlphaEffectOnClick(backButton);

		final ArtistDataSource artistDataSource = new ArtistDataSource(this);
		artistDataSource.open();
		artists = artistDataSource.getAllArtists();
		artistDataSource.close();

		mainStageLayout = ((RelativeLayout)findViewById(R.id.main_stage_layout));
		secondStageLayout = ((RelativeLayout)findViewById(R.id.second_stage_layout));

		initializeTimelineBackground();
		initializeSwitchButton();
		initializeSelectedDay();
	}

	private void initializeTimelineBackground() {
		for (int i = STARTING_HOUR ; i <= ENDING_HOUR ; i++) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View view = inflater.inflate(R.layout.programmation_hour_item, null);
			final View secondView = inflater.inflate(R.layout.programmation_hour_item, null);
			((LinearLayout)view).setX( getOffsetFromStringHour(String.valueOf(i)+"h00") );
			((LinearLayout)secondView).setX( getOffsetFromStringHour(String.valueOf(i)+"h00") );
			fillHourItemData(i, view);
			fillHourItemData(i, secondView);
			mainStageLayout.addView(view);
			secondStageLayout.addView(secondView);
		}
	}

	private void initializeSelectedDay() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(SEPARATION_BETWEEN_DAYS);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long now = new Date().getTime();
		if (now - date.getTime() < 0) {
			switchButton.setChecked( FIRST_DAY_SWITCH_VALUE );
		} else {
			switchButton.setChecked( SECOND_DAY_SWITCH_VALUE );
		}
		//Ugly hack to effectively initialize the switch on its normal value
		 switchButton.performClick();
		 switchButton.performClick();
	}

	private void initializeSwitchButton() {
		switchButton = ((Switch) findViewById(R.id.day_toggle));
		switchButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked == FIRST_DAY_SWITCH_VALUE) {
					mainStageLayout.removeAllViews();
					secondStageLayout.removeAllViews();
					initializeTimelineBackground();
					for (ArtistModel artist : artists) {
						if (artist.getDay().equals(FIRST_DAY))
							displayProgrammationOnScreen(artist);
					}
				} else if (isChecked == SECOND_DAY_SWITCH_VALUE) {
					mainStageLayout.removeAllViews();
					secondStageLayout.removeAllViews();
					initializeTimelineBackground();
					for (ArtistModel artist : artists) {
						if (artist.getDay().equals(SECOND_DAY))
							displayProgrammationOnScreen(artist);
					}
				}
			}
		});
		((TextView) findViewById(R.id.button_first_day)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchButton.setChecked(FIRST_DAY_SWITCH_VALUE);
			}
		});
		((TextView) findViewById(R.id.button_second_day)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchButton.setChecked(SECOND_DAY_SWITCH_VALUE);
			}
		});
		
		Typeface euroFont = Typeface.createFromAsset(getAssets(), "eurof55.ttf");
		((TextView) findViewById(R.id.button_first_day)).setTypeface(euroFont);
		((TextView) findViewById(R.id.button_second_day)).setTypeface(euroFont);
	}

	private void displayProgrammationOnScreen(final ArtistModel artist) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate(R.layout.programmation_artist_item, null);

		fillChildrenData(artist, view);

		((LinearLayout)view).setX( getOffsetFromStringHour(artist.getBeginHour()) );
		int width = getOffsetFromStringHour(artist.getEndHour()) - getOffsetFromStringHour(artist.getBeginHour());
		int height = ((View)findViewById(R.id.vertical_bar)).getLayoutParams().height - 10;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

		final String stage = artist.getStage();
		if ( stage.equals(ArtistModel.MAIN_STAGE) ) {
			mainStageLayout.addView(view, params);
		} else if ( stage.equals(ArtistModel.SECOND_STAGE) ) {
			secondStageLayout.addView(view, params);
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
		return ( (( hours - STARTING_HOUR )*MINUTES_IN_HOUR) + minutes ) * SIZE_COEFFICIENT;
	}

	private void fillChildrenData(final ArtistModel artist, final View view) {
		int childCount = ((ViewGroup) view).getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = ((ViewGroup) view).getChildAt(i);

			if ( child.getId() == R.id.programmation_name ) {
				((TextView) child).setText(artist.getName());
				Typeface euroFont = Typeface.createFromAsset(getAssets(), "eurof55.ttf");
				((TextView) child).setTypeface(euroFont);
			} else if (child.getId() == R.id.programmation_icon ) {
				String filePath = getApplicationContext().getFilesDir() + "/" + MySQLiteHelper.TABLE_ARTIST + "/" + artist.getName();
				((ImageView) child).setImageBitmap(Utils
						.decodeSampledBitmapFromFile(filePath,
								getResources(),
								R.drawable.artist_empty_icon, 70, 70));
			}
		}
	}

	private void fillHourItemData(final int hour, final View view) {
		int childCount = ((ViewGroup) view).getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = ((ViewGroup) view).getChildAt(i);
			if (child.getId() == R.id.hour ) {
				((TextView) child).setText((hour % HOURS_IN_DAY) + "h");
			}
		}
	}
}
