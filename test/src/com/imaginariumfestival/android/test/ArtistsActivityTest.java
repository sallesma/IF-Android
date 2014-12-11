package com.imaginariumfestival.android.test;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.ListView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.artists.ArtistActivity;
import com.imaginariumfestival.android.artists.ArtistModel;
import com.imaginariumfestival.android.artists.ArtistsActivity;
import com.imaginariumfestival.android.artists.ArtistsAlphabeticalAdapter;

public class ArtistsActivityTest extends
		ActivityInstrumentationTestCase2<ArtistsActivity> {

	private ArtistsActivity mActivity;
	private ListView mListView;
	
	public ArtistsActivityTest() {
		super(ArtistsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		mListView = (ListView) mActivity.findViewById(R.id.artistsList);
	}
	
	public void testPreConditions() {
		assertTrue(mActivity.findViewById(android.R.id.content) != null);
	}

	public void testListItemIsNotEmpty() {
		assertNotSame(0, mListView.getAdapter().getCount());
	}
	
	@UiThreadTest
	public void testOnArtistClick() {
	    //Given
		ActivityMonitor monitor = getInstrumentation().addMonitor(ArtistActivity.class.getName(), null, true);
		assertEquals(0, monitor.getHits());
		monitor.waitForActivityWithTimeout(1);
		
		//when
		mListView.getAdapter().getView(0, null, null).performClick();
	
		//then
		assertTrue(0 == monitor.getHits());
	}
	
	@UiThreadTest
	public void testAlphabeticalAdapter() {
		//when
		mActivity.findViewById(R.id.action_alpha_sort).performClick();
	
		//then
		ArtistModel artist0 = ((ArtistModel)mListView.getAdapter().getItem(0));
		ArtistModel artist1 = ((ArtistModel)mListView.getAdapter().getItem(1));
		assertTrue(artist0.getName().compareTo(artist1.getName()) < 0);
	}
	
	@UiThreadTest
	public void testStyleAdapter() {
		//when
		mActivity.findViewById(R.id.action_type_sort).performClick();
	
		//then
		ArtistModel artist0 = ((ArtistModel)mListView.getAdapter().getItem(0));
		String firstStyle = Normalizer.normalize(artist0.getStyle(), Normalizer.Form.NFD);
		firstStyle = firstStyle.replaceAll("[^\\p{ASCII}]", "");
		ArtistModel artist1 = ((ArtistModel)mListView.getAdapter().getItem(1));
		String secondStyle = Normalizer.normalize(artist1.getStyle(), Normalizer.Form.NFD);
		secondStyle = secondStyle.replaceAll("[^\\p{ASCII}]", "");
		Log.e("TEST ERROR", "0" + artist0.getStyle());
		Log.e("TEST ERROR", artist1.getStyle());
		assertTrue(firstStyle.compareTo(secondStyle) <= 0);
	}
	
	@UiThreadTest
	public void testStateDestroy() {
		//Given
		ArtistModel artist = new ArtistModel();
		List<ArtistModel> artists = new ArrayList<ArtistModel>();
		artists.add(artist);
		mListView.setAdapter( new ArtistsAlphabeticalAdapter(getActivity(), artists) );

		//When
		mActivity.finish();
		mActivity = this.getActivity();

		//Then
		assertEquals( (ArtistModel)mListView.getAdapter().getItem(0), artist );
	}
}
