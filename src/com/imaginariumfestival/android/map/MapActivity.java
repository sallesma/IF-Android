package com.imaginariumfestival.android.map;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.InfosDataSource;
import com.imaginariumfestival.android.database.MapItemsDataSource;
import com.imaginariumfestival.android.infos.InfoModel;

public class MapActivity extends Activity implements FilteringDialog.FilteringDialogListener{
	protected static final int NO_INFO_LINKED = -1;
	protected static final int NO_PARENT = 0;
	protected static final String NO_LINKED_INFO_CATEGORY = "Divers";
	protected static final String LINKED_INFO_HAS_NO_PARENT = "Divers";
	
	private ImageView map;
	private TreeMap<MapItemView, String> mapItems;
	private View selectedItem;
	private PopupView popup;
	private FilteringDialog dialog;
	private boolean isGlobalMapView = false;
	
	int maxX, maxY;
	int bitmapWidth, bitmapHeight;
	
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
		
		popup = new PopupView(getApplicationContext());
		((RelativeLayout)findViewById(R.id.map_relative_layout)).addView(popup.getPopupView());
		selectedItem = map; //initializing selectedItem with dummy view so that the first onClick call works

		MapItemsDataSource datasource = new MapItemsDataSource(getApplicationContext());
		datasource.open();
		List<MapItemModel> mapItemModels = datasource.getAllMapItems();
		datasource.close();
		
		addMapItemsOverMap(mapItemModels);
		
		String infoId = (String) getIntent().getSerializableExtra("infoId");
	    if (infoId != null && !infoId.equals("")) { //We come from InfoActivity
	    	int infoIdInt = Integer.parseInt(infoId);
	    	for (MapItemView mapItem : mapItems.keySet()) {
	    		if ( mapItem.getMapItemModel().getInfoId() == infoIdInt )
		    		selectedItem = mapItem;
					selectedItem.setSelected(true);
			}
	    }
	    
	    dialog = new FilteringDialog(this, mapItems);
	    ((Button)findViewById(R.id.show_filter_dialog)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show(getFragmentManager(), "FilteringDialog");
			}
		});
	    
	    ((Button)findViewById(R.id.toggle_view)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isGlobalMapView) {
					isGlobalMapView = false;
					((Button)findViewById(R.id.toggle_view)).setText(R.string.action_global_view);
					map.setScaleType(ScaleType.CENTER);
					((Button)findViewById(R.id.show_filter_dialog)).setVisibility(View.VISIBLE);
					for (MapItemView mapItemView : mapItems.keySet()) {
						mapItemView.setVisibility(View.VISIBLE);
					}
				} else {
					isGlobalMapView = true;
					((Button)findViewById(R.id.toggle_view)).setText(R.string.action_detailed_view);
					map.setScaleType(ScaleType.CENTER_INSIDE);
					map.setScrollX(0);
					map.setScrollY(0);
					((Button)findViewById(R.id.show_filter_dialog)).setVisibility(View.INVISIBLE);
					popup.setInvisible();
					selectedItem.setSelected(false);
					for (MapItemView mapItemView : mapItems.keySet()) {
						mapItemView.setVisibility(View.INVISIBLE);
						mapItemView.initPosition();
					}
				}
			}
		});
	}

	private void initializeMap() {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map);		
		map = (ImageView)findViewById(R.id.map);
		map.setImageBitmap(bitmap);
		
		//get the size of the image and  the screen
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
		bitmapWidth = bitmapDrawable.getIntrinsicWidth();
		bitmapHeight = bitmapDrawable.getIntrinsicHeight();
		
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		int screenWidth = size.x;
		int screenHeight = (int) (size.y);
		int headerHeight = ((ImageView)findViewById(R.id.header_icon)).getDrawable().getIntrinsicHeight();
		int footerHeight = ((LinearLayout.LayoutParams) ((LinearLayout)findViewById(R.id.map_footer_layout)).getLayoutParams()).height;
		
		// set maximum scroll amount (based on center of image)
		maxX = (int)((bitmapWidth / 2) - (screenWidth / 2));
		maxY = (int)((bitmapHeight / 2) - ((screenHeight - headerHeight - footerHeight) / 2));
		
		// set scroll limits
		final int maxLeft = (maxX * -1);
		final int maxRight = maxX;
		final int maxTop = (maxY * -1);
		final int maxBottom = maxY;
		
		map.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.setInvisible();
				selectedItem.setSelected(false);
			}
		});
		
		map.setOnTouchListener(new View.OnTouchListener() {
			float originX, originY;
			float lastX, lastY;
			int totalX, totalY;
			int scrollByX, scrollByY;
			public boolean onTouch(View view, MotionEvent event) {
				float currentX, currentY;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = event.getX();
					lastY = event.getY();
					originX = lastX;
					originY = lastY;
					break;
				case MotionEvent.ACTION_UP:
					currentX = event.getX();
					currentY = event.getY();
					// If it is a click on the map, hide map item popup
					if (Math.abs(currentX - originX) < 10
							&& Math.abs(currentY - originY) < 10 )
						map.performClick();
					break;
				case MotionEvent.ACTION_MOVE:
					currentX = event.getX();
					currentY = event.getY();
					scrollByX = (int)(lastX - currentX);
					scrollByY = (int)(lastY - currentY);
					// scrolling to left side of image (pic moving to the right)
					if (currentX > lastX) {
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
					if (currentX < lastX) {
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
					if (currentY > lastY) {
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
					if (currentY < lastY) {
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
					for (MapItemView mapItem : mapItems.keySet()) {
						mapItem.updatePositionOnMapScrolled(scrollByX, scrollByY);
					}
					popup.updatePositionOnMapScrolled(scrollByX, scrollByY);
					
					lastX = currentX;
					lastY = currentY;
					break;
				}
				return true;
			}
		});
	}
	
	private void addMapItemsOverMap(List<MapItemModel> mapItemModels) {
		mapItems = new TreeMap<MapItemView, String>();
		
		for (final MapItemModel mapItemModel : mapItemModels) {
			MapItemView point = new MapItemView(this, mapItemModel, maxX, maxY, bitmapWidth, bitmapHeight);
			((RelativeLayout)findViewById(R.id.map_relative_layout)).addView(point);
			
			String categoryName = getLinkedInfoCategoryName(mapItemModel);
			mapItems.put(point, categoryName);
			
			point.initPosition();
			point.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View clickedMapItem) {
					selectedItem.setSelected(false);
					selectedItem = clickedMapItem;
					selectedItem.setSelected(true);
					MapItemModel mapItemModel = ((MapItemView) clickedMapItem).getMapItemModel();
					
					if (mapItemModel.getInfoId() != MapActivity.NO_INFO_LINKED ) {
						InfosDataSource infoDatasource = new InfosDataSource(MapActivity.this);
						infoDatasource.open();
						final InfoModel info = infoDatasource.getInfoFromId(mapItemModel.getInfoId());
						infoDatasource.close();
						
						popup.changeMapItemModel(mapItemModel.getLabel(), info, clickedMapItem);
					} else {
						popup.changeMapItemModel(mapItemModel.getLabel(), null, clickedMapItem);
					}
				}
			});
		}
	}
	
	private String getLinkedInfoCategoryName(MapItemModel mapItemModel) {
		if (mapItemModel.getInfoId() == NO_INFO_LINKED) {
			return NO_LINKED_INFO_CATEGORY;
		} else {
			InfosDataSource datasource = new InfosDataSource(MapActivity.this);
			datasource.open();
			InfoModel info = datasource.getInfoFromId( mapItemModel.getInfoId() );
			if (info.getParentId() == NO_PARENT) {
				datasource.close();
				return LINKED_INFO_HAS_NO_PARENT;
			} else {
				InfoModel infoParent = datasource.getInfoFromId(info.getParentId());
				datasource.close();
				return infoParent.getName();
			}
		}
	}

	@Override
    public void onDialogPositiveClick(DialogFragment dialog, List<String> selectedCategories) {
		for (Entry<MapItemView, String> mapItem : mapItems.entrySet()) {
			if (selectedCategories.contains(mapItem.getValue())) {
				mapItem.getKey().setVisibility(View.VISIBLE);
			} else {
				mapItem.getKey().setVisibility(View.INVISIBLE);
			}
		}
    }
}
