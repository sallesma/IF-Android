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
		popupView = inflater.inflate(R.layout.map_popup, null);
		
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
	
	public void changeMapItemModel(String newLabel, final InfoModel linkedInfo, View clickedMapItem) {
		if (null == linkedInfo) {
			updatePopupForNoLinkedInfo();
		} else {
			updatePopupWithLinkedInfo(linkedInfo);
		}
		label.setText(newLabel);
		popupView.setVisibility(TextView.VISIBLE);
		popupView.bringToFront();
		
		popupView.setX( clickedMapItem.getX() - (popupView.getWidth() / 2) + (clickedMapItem.getWidth() /2) );
		popupView.setY( clickedMapItem.getY() - popupView.getHeight() );
	}

	private void updatePopupWithLinkedInfo(final InfoModel linkedInfo) {
		String filePath = context.getFilesDir() + "/" + MySQLiteHelper.TABLE_INFOS + "/" + linkedInfo.getName();
		icon.setImageBitmap(Utils.decodeSampledBitmapFromFile(filePath,
						context.getResources(), R.drawable.info_empty_icon,30, 30));
		arrow.setVisibility(ImageView.VISIBLE);
		
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

	private void updatePopupForNoLinkedInfo() {
		icon.setImageResource(R.drawable.info_empty_icon);
		arrow.setVisibility(ImageView.INVISIBLE);
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
	}
	
	public void setInvisible(){
		popupView.setVisibility(View.INVISIBLE);
	}
	
	public View getPopupView() {
		return popupView;
	}
}
