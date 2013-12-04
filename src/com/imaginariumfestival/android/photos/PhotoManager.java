package com.imaginariumfestival.android.photos;

import java.io.File;

import com.imaginariumfestival.android.database.MySQLiteHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class PhotoManager {
	public static final int REQUIRED_PICTURE_WIDTH = 600;
	public static final int REQUIRED_PICTURE_HEIGHT = 600;
	private Context context;
	
	public PhotoManager(Context context) {
		this.context = context;
	}

	public Bitmap computePhotoWithfilter(byte[] pictureTaken, FilterModel filter) {
		Bitmap photo = decodeBitmapFromData(pictureTaken);
		
		if (filter != null) {
			File filePath = new File(context.getFilesDir() + "/" + MySQLiteHelper.TABLE_FILTERS + "/" + filter.getId());
			
			Bitmap overlay = BitmapFactory.decodeFile(filePath.getAbsolutePath());
	
			Canvas canvas = new Canvas(photo);
			canvas.drawBitmap(overlay, new Matrix(), null);
		}
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
