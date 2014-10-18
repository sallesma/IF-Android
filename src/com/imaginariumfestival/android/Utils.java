package com.imaginariumfestival.android;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Utils {
	
	/*
	 * Checks if the phone is connected to the internet
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected());
	}
	
	/*
	 * Button becomes transparent while pressed
	 */
	public static void addAlphaEffectOnClick(View button) {
		button.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					v.setAlpha((float) 0.7);
					break;
				case MotionEvent.ACTION_UP:
					v.setAlpha((float) 1);
					break;
				}
				return false;
			}
		});
	}
	
	/*
	 * Add round corners to bitmap
	 */
	public static Bitmap getRoundedCornerBitmap(final Bitmap source, final int radius) {
	    final Bitmap output = Bitmap.createBitmap(source.getWidth(), source
	            .getHeight(), Config.ARGB_8888);
	    final Canvas canvas = new Canvas(output);
	    final BitmapShader shader = new BitmapShader(source, Shader.TileMode.CLAMP,
	            Shader.TileMode.CLAMP);

	    final Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setShader(shader);

	    // rect contains the bounds of the shape
	    // radius is the radius in pixels of the rounded corners
	    // paint contains the shader that will texture the shape
	    final RectF rect = new RectF(0.0f, 0.0f, source.getWidth(), source.getHeight());
	    canvas.drawRoundRect(rect, radius, radius, paint);

	    return output;
	}
	
	/*
	 * Decodes an image to a resized bitmap object. If the file cannot be loaded, it loads the default drawable
	 */
	public static Bitmap decodeSampledBitmapFromFile(String path, Resources res, int defaultDrawableId, int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    Bitmap picture = BitmapFactory.decodeFile(path, options);
	    
	    if (picture == null) {
	    	picture = BitmapFactory.decodeResource(res, defaultDrawableId, options);
	    }
	    return picture;
	}
	
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}
}
