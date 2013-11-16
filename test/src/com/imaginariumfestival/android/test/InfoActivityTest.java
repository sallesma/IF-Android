package com.imaginariumfestival.android.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.ImageView;
import android.widget.TextView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.infos.InfoActivity;

public class InfoActivityTest extends
		ActivityInstrumentationTestCase2<InfoActivity> {

	private InfoActivity mActivity;
	private ImageView infoPicture;
	private TextView infoCategoryName;
	private TextView infoContent;
	
	final String TAG = "tag";
	final String CONTENT = "content";
	final String CATEGORY_NAME = "categoryName";

	public InfoActivityTest() {
		super(InfoActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity = getActivity();
		infoPicture = (ImageView) mActivity.findViewById(R.id.infoPicture);
		infoCategoryName = (TextView) mActivity.findViewById(R.id.infoCategoryName);
		infoContent = (TextView) mActivity.findViewById(R.id.infoContent);
	}
	
	public void testPreConditions() {
		assertNotNull(mActivity.findViewById(android.R.id.content));
		assertNotNull(mActivity.findViewById(R.id.infoPicture));
		assertNotNull(mActivity.findViewById(R.id.infoCategoryName));
		assertNotNull(mActivity.findViewById(R.id.infoContent));
	}
	
	@UiThreadTest
	public void testStateDestroy() {
		infoPicture.setTag(TAG);
		infoContent.setText(CONTENT);
		infoCategoryName.setText(CATEGORY_NAME);
		
		mActivity.finish();
		mActivity = this.getActivity();

		Object currentInfoPicture = infoPicture.getTag();
		String currentInfoContent = (String) infoContent.getText();
		String currentInfoCategoryName = (String) infoCategoryName.getText();

		assertEquals(TAG, currentInfoPicture);
		assertEquals(CONTENT, currentInfoContent);
		assertEquals(CATEGORY_NAME, currentInfoCategoryName);
	}
}
