package com.imaginariumfestival.android.photos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.database.FiltersDataSource;

public class PhotosTakingActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mPreview;
	private List<FilterModel> filters;
	private FilterModel chosenFilter = null;
	private ImageView activePreviewFilter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (checkCameraHardware(PhotosTakingActivity.this)) {
			FiltersDataSource datasource = new FiltersDataSource(PhotosTakingActivity.this);
			datasource.open();
			filters = datasource.getAllFilters();
			datasource.close();
			
			setContentView(R.layout.activity_picture_taking);

			LinearLayout filtersLayout = (LinearLayout) findViewById(R.id.filtersChoice);
			for (FilterModel filter : filters) {
				File filePath = getFileStreamPath( String.valueOf(filter.getId()) );
				Drawable picture = Drawable.createFromPath(filePath.toString());
				ImageView filterView = new ImageView(PhotosTakingActivity.this);
				if (picture != null) {
					filterView.setImageDrawable(picture);
				} else {
					filterView.setImageResource(R.drawable.artist_empty_icon);
				}
				filterView.setContentDescription(String.valueOf(filter.getId()));
				filterView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						FiltersDataSource filtersDataSource = new FiltersDataSource(PhotosTakingActivity.this);
						filtersDataSource.open();

						Long id = Long.parseLong( (String) ((ImageView) v).getContentDescription() );
						chosenFilter = filtersDataSource.getFilterFromId( id );
						filtersDataSource.close();
						
						File filePath = getFileStreamPath( String.valueOf(chosenFilter.getId()) );
						Drawable picture = Drawable.createFromPath(filePath.toString());
						
						ImageView filterImagePreview = new ImageView(PhotosTakingActivity.this);
						filterImagePreview.setImageDrawable(picture);
						
						FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
						preview.removeView(activePreviewFilter);
						activePreviewFilter = filterImagePreview;
						preview.addView(filterImagePreview);						
					}
				});
				
				filterView.setLayoutParams(new FrameLayout.LayoutParams(100,100));
				filtersLayout.addView(filterView);
			}
			
			mCamera = setUpCameraInstance();
			
			mPreview = new CameraPreview(this, mCamera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mPreview);
			

			Button captureButton = (Button) findViewById(R.id.button_capture);
			captureButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mCamera.takePicture(null, null, mPicture);
				}
			});
		} else {
			Toast.makeText(this, "Impossible d'accéder à l'appareil photo !", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	public static Camera setUpCameraInstance() {
		Camera camera = null;
		try {
			camera = Camera.open();
			
			Camera.Parameters params = camera.getParameters();
			params.set("rotation", 90);
			camera.setParameters(params);
		} catch (Exception e) {
			
		}
		return camera;
	}
	
	private PictureCallback mPicture = new PictureCallback() {
	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {
			
	    	PhotoManager photoManager = new PhotoManager(PhotosTakingActivity.this);
	    	Bitmap filteredPicture = photoManager.computePhotoWithfilter(data, chosenFilter);
			
	        File pictureFile = getOutputMediaFile();
	        if (pictureFile == null){
	            Log.d("DEBUG", "Error creating media file, check storage permissions: ");
	            return;
	        }

	        try {
	            FileOutputStream fos = new FileOutputStream(pictureFile);
	            if (fos != null) {
	            	filteredPicture.compress(Bitmap.CompressFormat.PNG, 100, fos);
	            }
	            fos.close();
	        } catch (FileNotFoundException e) {
	            Log.d("DEBUG", "File not found: " + e.getMessage());
	        } catch (IOException e) {
	            Log.d("DEBUG", "Error accessing file: " + e.getMessage());
	        }
	        
	        goToValidationActivity(pictureFile.getAbsolutePath());
	    }
	};

    
    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null){
        	mCamera.setPreviewCallback(null);
        	mPreview.getHolder().removeCallback(mPreview);
        	mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null){
        	mCamera = setUpCameraInstance();
        	mPreview = new CameraPreview(this, mCamera);
        	FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mPreview);
        }
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
	
	private File getOutputMediaFile(){
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "Imaginarium Festival Pictures");
	
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("Imaginarium Festival Pictures", "failed to create directory");
	            return null;
	        }
	    }
	
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.FRANCE).format(new Date());
	    File mediaFile;
	    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_IF_"+ timeStamp + ".jpg");
	
	    return mediaFile;
	}

	private void goToValidationActivity(String pictureFilePath) {
		Intent toPictureValidatingActivityIntent = new Intent(PhotosTakingActivity.this, PhotosValidatingActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("picturePath", pictureFilePath);
		toPictureValidatingActivityIntent.putExtras(bundle);
		startActivity(toPictureValidatingActivityIntent);
	}
}