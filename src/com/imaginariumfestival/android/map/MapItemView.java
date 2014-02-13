package com.imaginariumfestival.android.map;

import com.imaginariumfestival.android.R;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MapItemView extends ImageView {
	MapItemModel mapItemModel;
	int maxX, maxY;

	public MapItemView(Context context, MapItemModel mapItemModel, int maxX, int maxY) {
		super(context);
		this.mapItemModel = mapItemModel;
		this.maxX = maxX;
		this.maxY = maxY;
		
		setImageResource(R.drawable.map_item);
	}
	
	public void initPosition() {
		RelativeLayout.LayoutParams pointLayoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
		pointLayoutParams.width = 40;
		pointLayoutParams.height = 40;
		pointLayoutParams.leftMargin = (int) (mapItemModel.getX() - maxX - (pointLayoutParams.width / 2));
		pointLayoutParams.topMargin = (int) ( (mapItemModel.getY() - maxY - (pointLayoutParams.height / 2)) );
		setLayoutParams(pointLayoutParams);
	}
	
	public void updatePositionOnMapScrolled(int scrollByX, int scrollByY) {
		RelativeLayout.LayoutParams pointLayoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
		pointLayoutParams.leftMargin -= scrollByX;
		pointLayoutParams.topMargin -= scrollByY;
		setLayoutParams(pointLayoutParams);
	}

	public MapItemModel getMapItemModel() {
		return mapItemModel;
	}
}
