package com.imaginariumfestival.android.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.MapItemsDataSource;

public class MapActivity extends Activity {
	private ImageView map;
	private List<ImageView> mapItems;
	private List<MapItemModel> mapItemModels;
	
	float bmWidth, bmHeight;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
		
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NavUtils.navigateUpFromSameTask(MapActivity.this);
			}
		});
		Utils.addAlphaEffectOnClick(backButton);
		
		initializeMap();
//		addMapItemsOverMap();
	}

	private void initializeMap() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map);		
		map = (ImageView)findViewById(R.id.map);
		map.setImageBitmap(bitmap);
		
		//get the size of the image and  the screen
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
		int bitmapWidth = bitmapDrawable.getIntrinsicWidth();
		int bitmapHeight = bitmapDrawable.getIntrinsicHeight();
		
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;
		
		// set maximum scroll amount (based on center of image)
		int maxX = (int)((bitmapWidth / 2) - (screenWidth / 2));
		int maxY = (int)((bitmapHeight / 2) - (screenHeight / 2));
		
		// set scroll limits
		final int maxLeft = (maxX * -1);
		final int maxRight = maxX;
		final int maxTop = (maxY * -1);
		final int maxBottom = maxY;
		
		map.setOnTouchListener(new View.OnTouchListener() {
			float downX, downY;
			int totalX, totalY;
			int scrollByX, scrollByY;
			public boolean onTouch(View view, MotionEvent event) {
				float currentX, currentY;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX = event.getX();
					downY = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					currentX = event.getX();
					currentY = event.getY();
					scrollByX = (int)(downX - currentX);
					scrollByY = (int)(downY - currentY);
					// scrolling to left side of image (pic moving to the right)
					if (currentX > downX) {
						if (totalX == maxLeft) {
							scrollByX = 0;
						}
						if (totalX > maxLeft) {
							totalX = totalX + scrollByX;
						}
						if (totalX < maxLeft) {
							scrollByX = maxLeft - (totalX - scrollByX);
							totalX = maxLeft;
						}
					}
					// scrolling to right side of image (pic moving to the left)
					if (currentX < downX) {
						if (totalX == maxRight) {
							scrollByX = 0;
						}
						if (totalX < maxRight) {
							totalX = totalX + scrollByX;
						}
						if (totalX > maxRight) {
							scrollByX = maxRight - (totalX - scrollByX);
							totalX = maxRight;
						}
					}
					// scrolling to top of image (pic moving to the bottom)
					if (currentY > downY) {
						if (totalY == maxTop) {
							scrollByY = 0;
						}
						if (totalY > maxTop) {
							totalY = totalY + scrollByY;
						}
						if (totalY < maxTop) {
							scrollByY = maxTop - (totalY - scrollByY);
							totalY = maxTop;
						}
					}
					// scrolling to bottom of image (pic moving to the top)
					if (currentY < downY) {
						if (totalY == maxBottom) {
							scrollByY = 0;
						}
						if (totalY < maxBottom) {
							totalY = totalY + scrollByY;
						}
						if (totalY > maxBottom) {
							scrollByY = maxBottom - (totalY - scrollByY);
							totalY = maxBottom;
						}
					}
					map.scrollBy(scrollByX, scrollByY);
					downX = currentX;
					downY = currentY;
					break;
				}
				return true;
			}
		});
	}
	
//	private void addMapItemsOverMap() {
//		MapItemsDataSource datasource = new MapItemsDataSource(getApplicationContext());
//		datasource.open();
//		mapItemModels = datasource.getAllMapItems();
//		datasource.close();
//		
//		mapItems = new ArrayList<ImageView>();
//		for (final MapItemModel mapItemModel : mapItemModels) {
//			ImageView point = new ImageView(this, null);
//			((RelativeLayout)findViewById(R.id.map_relative_layout)).addView(point);
//
//			point.setLeft(mapItemModel.getX());
//			point.setTop(mapItemModel.getY());
//			point.setImageResource(R.drawable.switch_button);
//			point.bringToFront();
//			map.invalidate();
//			
//			point.setContentDescription( String.valueOf(mapItemModels.indexOf(mapItemModel)) );
//			mapItems.add(point);
//		}
//	}
	
//	public void showPopup() {
//		ImageView mainLayout = (ImageView)findViewById(R.id.map_anchor);
//	    PopupMenu popupMenu = new PopupMenu(this, mainLayout);
//	    
//	    popupMenu.getMenuInflater().inflate(R.menu.menu_map_advanced, popupMenu.getMenu());
//	    popupMenu.show();
//	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_map, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_type_filter:
//			showPopup();
			return true;
		case R.id.action_type_filter_bar:
			Toast.makeText(MapActivity.this, getResources().getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_type_filter_food:
			Toast.makeText(MapActivity.this, getResources().getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_type_filter_stage:
			Toast.makeText(MapActivity.this, getResources().getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_type_filter_asso:
			Toast.makeText(MapActivity.this, getResources().getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_type_filter_security:
			Toast.makeText(MapActivity.this, getResources().getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
}
