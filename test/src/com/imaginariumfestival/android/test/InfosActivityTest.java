package com.imaginariumfestival.android.test;

import java.util.HashMap;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;

import com.imaginariumfestival.android.infos.InfoActivity;
import com.imaginariumfestival.android.infos.InfosActivity;

public class InfosActivityTest extends
		ActivityInstrumentationTestCase2<InfosActivity> {

	private InfosActivity mActivity;
	
	public InfosActivityTest() {
		super(InfosActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
	}
	
	public void testPreConditions() {
		assertTrue(mActivity.findViewById(android.R.id.content) != null);
	}

	public void testListItemIsNotEmpty() {
		assertNotSame(0, mActivity.getListView().getAdapter().getCount());
	}	
	
	@SuppressWarnings("unchecked")
	public void testOnInfoClickStartsActivity() {
	    //Given
		ActivityMonitor monitor = getInstrumentation().addMonitor(InfoActivity.class.getName(), null, true);
		assertEquals(0, monitor.getHits());
		monitor.waitForActivityWithTimeout(1);
		
		//when
		mActivity.getListView().getAdapter().getView(0, null, null).performClick();
		
		//then
		if ( !Boolean.valueOf(((HashMap<String, String>) mActivity.getListAdapter().getItem(0)).get("isCategory")) )
			assertTrue(1 == monitor.getHits());
	}
	
	@SuppressWarnings("unchecked")
	public void testOnCategoryClickUpdateList() {
	    //Given
		ActivityMonitor monitor = getInstrumentation().addMonitor(InfoActivity.class.getName(), null, true);
		assertEquals(0, monitor.getHits());
		monitor.waitForActivityWithTimeout(1);
		
		//when
		mActivity.getListView().getAdapter().getView(0, null, null).performClick();
		
		//then
		if ( !Boolean.valueOf(((HashMap<String, String>) mActivity.getListAdapter().getItem(0)).get("isCategory")) )
			assertTrue(0 == monitor.getHits());
	}
}
