package com.imaginariumfestival.android.map;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.imaginariumfestival.android.R;

public class MapItemView extends ImageView implements Comparable<MapItemView> {
	MapItemModel mapItemModel;
	int maxX, maxY;
	int mapBitmapWidth, mapBitmapHeigt;

	public MapItemView(Context context, MapItemModel mapItemModel, int maxX, int maxY, int mapBitmapWidth, int mapBitmapHeigt) {
		super(context);
		this.mapItemModel = mapItemModel;
		this.maxX = maxX;
		this.maxY = maxY;
		this.mapBitmapWidth = mapBitmapWidth;
		this.mapBitmapHeigt = mapBitmapHeigt;
		
		setImageResource(R.drawable.map_item);
	}
	
	public void initPosition() {
		int absoluteXPos = mapItemModel.getX() * mapBitmapWidth / 1000;
		int absoluteYPos = mapItemModel.getY() * mapBitmapHeigt / 1000;
		RelativeLayout.LayoutParams pointLayoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
		pointLayoutParams.width = LayoutParams.WRAP_CONTENT;
		pointLayoutParams.height = LayoutParams.WRAP_CONTENT;
		pointLayoutParams.leftMargin = (int) (absoluteXPos - maxX - (pointLayoutParams.width / 2));
		pointLayoutParams.topMargin = (int)  (absoluteYPos - maxY - (pointLayoutParams.height / 2));
		setLayoutParams(pointLayoutParams);
	}
	
	public void updatePositionOnMapScrolled(int scrollByX, int scrollByY) {
		RelativeLayout.LayoutParams pointLayoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
		pointLayoutParams.leftMargin -= scrollByX;
		pointLayoutParams.topMargin -= scrollByY;
		setLayoutParams(pointLayoutParams);
	}
	
	@Override
	public int compareTo(MapItemView another) {
		return mapItemModel.getLabel().compareTo(another.getMapItemModel().getLabel());
	}

	public MapItemModel getMapItemModel() {
		return mapItemModel;
	}
}
