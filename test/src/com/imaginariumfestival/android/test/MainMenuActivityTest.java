package com.imaginariumfestival.android.test;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageButton;

import com.imaginariumfestival.android.MainMenuActivity;
import com.imaginariumfestival.android.artists.ArtistsActivity;
import com.imaginariumfestival.android.infos.InfosActivity;
import com.imaginariumfestival.android.map.MapActivity;
import com.imaginariumfestival.android.partners.PartnersActivity;
import com.imaginariumfestival.android.photos.PhotosTakingActivity;
import com.imaginariumfestival.android.programmation.ProgrammationActivity;

public class MainMenuActivityTest extends
		ActivityInstrumentationTestCase2<MainMenuActivity> {
	private MainMenuActivity mActivity;
	private ImageButton artistsButton;
	private ImageButton infosButton;
	private ImageButton programmationButton;
	private ImageButton mapButton;
	private ImageButton photosButton;
	private ImageButton partnersButton;

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
		programmationButton = (ImageButton) mActivity.findViewById(com.imaginariumfestival.android.R.id.programmationButton);
		mapButton = (ImageButton) mActivity.findViewById(com.imaginariumfestival.android.R.id.mapButton);
		photosButton = (ImageButton) mActivity.findViewById(com.imaginariumfestival.android.R.id.photosButton);
		partnersButton = (ImageButton) mActivity.findViewById(com.imaginariumfestival.android.R.id.partnersButton);
	}
	
	public void testPreConditions() {
		assertTrue(artistsButton.hasOnClickListeners());
		assertTrue(infosButton.hasOnClickListeners());
		assertTrue(programmationButton.hasOnClickListeners());
		assertTrue(mapButton.hasOnClickListeners());
		assertTrue(photosButton.hasOnClickListeners());
		assertTrue(partnersButton.hasOnClickListeners());
		assertTrue(mActivity.findViewById(android.R.id.content) != null);
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
	
	public void testProgrammationLink() {
		//Given
		ActivityMonitor am = getInstrumentation().addMonitor(ProgrammationActivity.class.getName(), null, true);
		am.waitForActivityWithTimeout(1);
		//when
		programmationButton.callOnClick();
		
		//then
		assertEquals(1, am.getHits());
	}
	
	public void testMapLink() {
		//Given
		ActivityMonitor am = getInstrumentation().addMonitor(MapActivity.class.getName(), null, true);
		am.waitForActivityWithTimeout(1);
		//when
		mapButton.callOnClick();
		
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
	
	public void testPartnersLink() {
		//Given
		ActivityMonitor am = getInstrumentation().addMonitor(PartnersActivity.class.getName(), null, true);
		am.waitForActivityWithTimeout(1);
		//when
		partnersButton.callOnClick();
		
		//then
		assertEquals(1, am.getHits());
	}
}
