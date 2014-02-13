package com.imaginariumfestival.android.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.MySQLiteHelper;
import com.imaginariumfestival.android.infos.InfoActivity;
import com.imaginariumfestival.android.infos.InfoModel;

public class PopupView {
	View popupView;
	Context context;
	
	ImageView arrow;
	ImageView icon;
	TextView label;

	public PopupView(Context context) {
		this.context = context;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		popupView = inflater.inflate(R.layout.map_item_popup, null);
		
		arrow = (ImageView) popupView.findViewById(R.id.map_item_popup_arrow);
		icon = (ImageView) popupView.findViewById(R.id.map_item_popup_icon);
		label = (TextView) popupView.findViewById(R.id.map_item_popup_label);
		
		setInvisible();
		
		Typeface euroFont = Typeface.createFromAsset(context.getAssets(), "eurof55.ttf");
		label.setTypeface(euroFont);
	}

	public void updatePositionOnMapScrolled(int scrollByX, int scrollByY) {
		RelativeLayout.LayoutParams popupLayoutParams = (RelativeLayout.LayoutParams) popupView.getLayoutParams();
		popupLayoutParams.leftMargin -= scrollByX;
		popupLayoutParams.topMargin -= scrollByY;
		popupView.setLayoutParams(popupLayoutParams);
	}
	
	public void changeMapItemModel(MapItemModel mapItemModel, final InfoModel linkedInfo, View clickedMapItem) {
		if (null == linkedInfo) {
			arrow.setVisibility(ImageView.INVISIBLE);
			icon.setImageResource(R.drawable.info_empty_icon);
			popupView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//Do nothing
				}
			});
			popupView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					//Do nothing
					return false;
				}
			});
		} else {
			String filePath = context.getFilesDir() + "/" + MySQLiteHelper.TABLE_INFOS + "/" + linkedInfo.getName();
			((ImageView) popupView.findViewById(R.id.map_item_popup_icon))
					.setImageBitmap(Utils.decodeSampledBitmapFromFile(filePath,
							context.getResources(), R.drawable.info_empty_icon,40, 40));
			((ImageView) popupView.findViewById(R.id.map_item_popup_arrow)).setVisibility(ImageView.VISIBLE);
			
			popupView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent toInfoActivityIntent = new Intent(context, InfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("infoId", String.valueOf(linkedInfo.getId()));
					toInfoActivityIntent.putExtras(bundle);
					toInfoActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(toInfoActivityIntent);
				}
			});
			Utils.addAlphaEffectOnClick(popupView);
		}
		
		label.setText(mapItemModel.getLabel());
		popupView.setVisibility(TextView.VISIBLE);
		popupView.bringToFront();
		popupView.setX( clickedMapItem.getX() - (popupView.getWidth() / 2) + (clickedMapItem.getWidth() /2) );
		popupView.setY( clickedMapItem.getY() - popupView.getHeight() );
	}
	
	public void setInvisible(){
		popupView.setVisibility(View.INVISIBLE);
	}

	public void setVisible(){
		popupView.setVisibility(View.VISIBLE);
	}
	
	public View getPopupView() {
		return popupView;
	}
}
