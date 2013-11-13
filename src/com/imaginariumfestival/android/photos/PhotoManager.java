package com.imaginariumfestival.android.photos;

import com.imaginariumfestival.android.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class PhotoManager {
	private static final int REQUIRED_PICTURE_WIDTH = 500;
	private static final int REQUIRED_PICTURE_HEIGHT = 500;
	private Resources resources;
	
	public PhotoManager(Resources resources) {
		this.resources = resources;
	}

	public Bitmap computePhotoWithfilter(byte[] pictureTaken) {
		Bitmap photo = decodeBitmapFromData(pictureTaken);
		
		Bitmap overlay = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);

		Canvas canvas = new Canvas(photo);
		canvas.drawBitmap(overlay, new Matrix(), null);
		return photo;
	}
	
	private Bitmap decodeBitmapFromData(byte[] pictureTaken) {
		final BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(pictureTaken, 0, pictureTaken.length, options);

		options.inSampleSize = calculateInSampleSize(options);
		
		options.inJustDecodeBounds = false;
		options.inMutable = true;
		Bitmap photo = BitmapFactory.decodeByteArray(pictureTaken, 0, pictureTaken.length, options);
		photo = Bitmap.createScaledBitmap(photo, photo.getWidth(), photo.getHeight(), true);
		return photo;
	}
	
	private int calculateInSampleSize(BitmapFactory.Options options) {
		int imageHeight = options.outHeight;
		int imageWidth = options.outWidth;

		int inSampleSize = 1;
		
	    if (imageHeight > REQUIRED_PICTURE_HEIGHT || imageWidth > REQUIRED_PICTURE_WIDTH) {
	        final int halfHeight = imageHeight / 2;
	        final int halfWidth = imageWidth / 2;

	        while ((halfHeight / inSampleSize) > REQUIRED_PICTURE_HEIGHT
	                && (halfWidth / inSampleSize) > REQUIRED_PICTURE_WIDTH) {
	            inSampleSize *= 2;
	        }
	    }
	    
	    return inSampleSize;
	}
}
