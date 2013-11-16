package com.imaginariumfestival.android.test;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.infos.InfoActivity;
import com.imaginariumfestival.android.infos.InfosActivity;

public class InfosActivityTest extends
		ActivityInstrumentationTestCase2<InfosActivity> {

	private InfosActivity mActivity;
	private ListView mListView;
	
	public InfosActivityTest() {
		super(InfosActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		mListView = mActivity.getListView();
	}
	
	public void testPreConditions() {
		assertTrue(mActivity.findViewById(android.R.id.content) != null);
	}

	public void testListItemIsNotEmpty() {
		assertNotSame(0, mListView.getAdapter().getCount());
	}	
	
	@SuppressWarnings("unchecked")
	public void testOnInfoClickStartsActivity() {
	    //Given
		ActivityMonitor monitor = getInstrumentation().addMonitor(InfoActivity.class.getName(), null, true);
		assertEquals(0, monitor.getHits());
		monitor.waitForActivityWithTimeout(1);
		
		//when
		mListView.getAdapter().getView(0, null, null).performClick();
		
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
		mListView.getAdapter().getView(0, null, null).performClick();
		
		//then
		if ( !Boolean.valueOf(((HashMap<String, String>) mActivity.getListAdapter().getItem(0)).get("isCategory")) )
			assertTrue(0 == monitor.getHits());
	}
	
	@SuppressWarnings("unchecked")
	@UiThreadTest
	public void testStateDestroy() {
		//Given
		HashMap<String, String> infoItem;
		infoItem = new HashMap<String, String>();
		infoItem.put("id", "0");
		infoItem.put("title", "name");
		infoItem.put("img", "picture");
		infoItem.put("isCategory", "0");

		ArrayList<HashMap<String, String>> listItemToDisplay = new ArrayList<HashMap<String, String>>();
		listItemToDisplay.add(infoItem);
		
		SimpleAdapter listAdapter = new SimpleAdapter(mActivity,
				listItemToDisplay, R.layout.info_list_item, new String[] {
						"img", "title", "isCategory", "id" }, new int[] {
						R.id.infoListItemIcon, R.id.infoListItemName });
		mActivity.setListAdapter(listAdapter);
		
		//When
		mActivity.finish();
		mActivity = this.getActivity();

		//Then
		assertEquals((HashMap<String, String>)mListView.getAdapter().getItem(0), infoItem);
	}
}
