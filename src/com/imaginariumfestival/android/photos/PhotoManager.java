package com.imaginariumfestival.android.photos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;

import com.imaginariumfestival.android.database.MySQLiteHelper;

public class PhotoManager {
	public static final int REQUIRED_PICTURE_WIDTH = 600;
	public static final int REQUIRED_PICTURE_HEIGHT = 600;
	private Context context;
	
	public PhotoManager(Context context) {
		this.context = context;
	}

	public Bitmap computePhotoWithfilter(Uri imageUri, FilterModel filter) {
//		Bitmap photo = decodeBitmapFromData(pictureTaken);
		Bitmap photo = decodeBitmapFromData(imageUri);
	
		if (filter != null) {
			File filePath = new File(context.getFilesDir() + "/" + MySQLiteHelper.TABLE_FILTERS + "/" + filter.getId());
			
			Bitmap overlay = BitmapFactory.decodeFile(filePath.getAbsolutePath());
	
			Bitmap mutablePhoto = convertToMutable(photo);
			Canvas canvas = new Canvas(mutablePhoto);
			canvas.drawBitmap(overlay, new Matrix(), null);
			return mutablePhoto;
		}
		return photo;
	}
	
	private Bitmap decodeBitmapFromData(Uri uri) {
		try {
			InputStream input = context.getContentResolver().openInputStream(uri);

	        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
	        onlyBoundsOptions.inJustDecodeBounds = true;
	        onlyBoundsOptions.inDither=true;//optional
	        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
	        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
	        input.close();
	        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
	            return null;
	
	        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	        bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions);
	        bitmapOptions.inDither=true;
	        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;
			input = context.getContentResolver().openInputStream(uri);
	        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
			input.close();
			return bitmap;
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return null;
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
	
	public Bitmap convertToMutable(Bitmap imgIn) {
	    try {
	        //this is the file going to use temporally to save the bytes. 
	        // This file will not be a image, it will store the raw image data.
	        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

	        //Open an RandomAccessFile
	        //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
	        //into AndroidManifest.xml file
	        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

	        // get the width and height of the source bitmap.
	        int width = imgIn.getWidth();
	        int height = imgIn.getHeight();
	        Config type = imgIn.getConfig();

	        //Copy the byte to the file
	        //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
	        FileChannel channel = randomAccessFile.getChannel();
	        MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
	        imgIn.copyPixelsToBuffer(map);
	        //recycle the source bitmap, this will be no longer used.
	        imgIn.recycle();
	        System.gc();// try to force the bytes from the imgIn to be released

	        //Create a new bitmap to load the bitmap again. Probably the memory will be available. 
	        imgIn = Bitmap.createBitmap(width, height, type);
	        map.position(0);
	        //load it back from temporary 
	        imgIn.copyPixelsFromBuffer(map);
	        //close the temporary file and channel , then delete that also
	        channel.close();
	        randomAccessFile.close();

	        // delete the temp file
	        file.delete();

	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } 
	    return imgIn;
	}
}
