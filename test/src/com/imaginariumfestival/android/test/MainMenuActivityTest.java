package com.imaginariumfestival.android.test;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageButton;

import com.imaginariumfestival.android.MainMenuActivity;
import com.imaginariumfestival.android.artists.ArtistsActivity;
import com.imaginariumfestival.android.infos.InfosActivity;
import com.imaginariumfestival.android.photos.PhotosTakingActivity;

public class MainMenuActivityTest extends
		ActivityInstrumentationTestCase2<MainMenuActivity> {
	private MainMenuActivity mActivity;
	private ImageButton artistsButton;
	private ImageButton infosButton;
	private ImageButton photosButton;

	public MainMenuActivityTest() {
		super(MainMenuActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		artistsButton = (ImageButton) mActivity.findViewById(com.imaginariumfestival.android.R.id.artistsButton);
		infosButton = (ImageButton) mActivity.findViewById(com.imaginariumfestival.android.R.id.infosButton);
		photosButton = (ImageButton) mActivity.findViewById(com.imaginariumfestival.android.R.id.photosButton);
	}
	
	public void testPreConditions() {
		assertTrue(artistsButton.hasOnClickListeners());
		assertTrue(infosButton.hasOnClickListeners());
		assertTrue(photosButton.hasOnClickListeners());
	}
	
	public void testArtistLink() {
		//Given
		ActivityMonitor am = getInstrumentation().addMonitor(ArtistsActivity.class.getName(), null, true);
		am.waitForActivityWithTimeout(1);
		//when
		artistsButton.callOnClick();

		//then
		assertEquals(1, am.getHits());
	}
	public void testInfosLink() {
		//Given
		ActivityMonitor am = getInstrumentation().addMonitor(InfosActivity.class.getName(), null, true);
		am.waitForActivityWithTimeout(1);
		//when
		infosButton.callOnClick();
		
		//then
		assertEquals(1, am.getHits());
	}
	public void testPhotoLink() {
		//Given
		ActivityMonitor am = getInstrumentation().addMonitor(PhotosTakingActivity.class.getName(), null, true);
		am.waitForActivityWithTimeout(1);
		//when
		photosButton.callOnClick();
		
		//then
		assertEquals(1, am.getHits());
	}
}
