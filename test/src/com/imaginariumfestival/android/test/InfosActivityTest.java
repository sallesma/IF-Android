package com.imaginariumfestival.android.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ListView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.infos.InfoActivity;
import com.imaginariumfestival.android.infos.InfoModel;
import com.imaginariumfestival.android.infos.InfosActivity;
import com.imaginariumfestival.android.infos.InfosAdapter;

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
		mListView = (ListView) mActivity.findViewById(R.id.infosList);
	}
	
	public void testPreConditions() {
		assertTrue(mActivity.findViewById(android.R.id.content) != null);
	}

	public void testListItemIsNotEmpty() {
		assertNotSame(0, mListView.getAdapter().getCount());
	}	
	
	@UiThreadTest
	public void testOnInfoClickStartsActivity() {
	    //Given
		ActivityMonitor monitor = getInstrumentation().addMonitor(InfoActivity.class.getName(), null, true);
		assertEquals(0, monitor.getHits());
		monitor.waitForActivityWithTimeout(1);
		
		//when
		mListView.getAdapter().getView(0, null, null).performClick();
		
		//then
		if ( !((InfoModel) mListView.getAdapter().getItem(0)).getIsCategory() )
			assertTrue(1 == monitor.getHits());
	}
	
	@UiThreadTest
	public void testOnCategoryClickUpdateList() {
	    //Given
		ActivityMonitor monitor = getInstrumentation().addMonitor(InfoActivity.class.getName(), null, true);
		assertEquals(0, monitor.getHits());
		monitor.waitForActivityWithTimeout(1);
		
		//when
		mListView.getAdapter().getView(0, null, null).performClick();
		
		//then
		if ( ((InfoModel)mListView.getAdapter().getItem(0)).getIsCategory() )
			assertTrue(0 == monitor.getHits());
	}
	
	@UiThreadTest
	public void testStateDestroy() {
		//Given
		InfoModel info = new InfoModel(0L, "test", "picture", "0", "test", 0L);
		List<InfoModel> infos = new ArrayList<InfoModel>();
		infos.add(info);
		mListView.setAdapter( new InfosAdapter(getActivity(), infos, 0L) );

		//When
		mActivity.finish();
		mActivity = this.getActivity();

		//Then
		assertEquals( (InfoModel)mListView.getAdapter().getItem(0), info );
	}
}
