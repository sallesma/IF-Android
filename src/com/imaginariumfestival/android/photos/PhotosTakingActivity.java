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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.imaginariumfestival.android.R;
import com.imaginariumfestival.android.Utils;
import com.imaginariumfestival.android.database.FiltersDataSource;
import com.imaginariumfestival.android.database.MySQLiteHelper;

public class PhotosTakingActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mPreview;
	private List<FilterModel> filters;
	private FilterModel chosenFilter = null;
	private ImageView activePreviewFilter = null;
	private Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_picture_taking);
		
		ImageButton backButton = (ImageButton) findViewById(R.id.back_button);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NavUtils.navigateUpFromSameTask(PhotosTakingActivity.this);
			}
		});
		Utils.addAlphaEffectOnClick(backButton);

		if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			FiltersDataSource datasource = new FiltersDataSource(
					PhotosTakingActivity.this);
			datasource.open();
			filters = datasource.getAllFilters();
			datasource.close();

			LinearLayout filtersLayout = (LinearLayout) findViewById(R.id.filtersChoice);
			for (FilterModel filter : filters) {
				addFilterToFilterBarLayout(filtersLayout, filter);
			}

			mCamera = setUpCameraInstance();

			mPreview = new CameraPreview(this, mCamera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mPreview);

			Button captureButton = (Button) findViewById(R.id.button_capture);
			captureButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File photo = new File(Environment.getExternalStorageDirectory(), "test.jpg");
					imageUri = Uri.fromFile(photo);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(intent, 0);
//					ProgressDialog progress = new ProgressDialog(PhotosTakingActivity.this);
//					progress.setMessage(PhotosTakingActivity.this.getResources().getString(R.string.photo_taking_wait_message));
//					progress.show();
//					mCamera.takePicture(null, null, mPicture);
				}
			});
			Typeface euroFont = Typeface.createFromAsset(getAssets(), "eurof55.ttf");
			captureButton.setTypeface(euroFont);
		} else {
			Toast.makeText(this, getResources().getString(R.string.unavailable_camera),
					Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    switch (requestCode) {
	    case 0:
	        if (resultCode == Activity.RESULT_OK) {
				PhotoManager photoManager = new PhotoManager(
						PhotosTakingActivity.this);
				Bitmap filteredPicture = photoManager.computePhotoWithfilter(imageUri, chosenFilter);
				File pictureFile = getOutputMediaFile();
				if (pictureFile == null) {
					Log.d("DEBUG",
							"Error creating media file, check storage permissions: ");
					return;
				}
				try {
					FileOutputStream fos = new FileOutputStream(pictureFile);
					if (fos != null) {
						filteredPicture.compress(Bitmap.CompressFormat.PNG, 100,
								fos);
					}
					fos.close();
				} catch (FileNotFoundException e) {
					Log.d("DEBUG", "File not found: " + e.getMessage());
				} catch (IOException e) {
					Log.d("DEBUG", "Error accessing file: " + e.getMessage());
				}
				goToValidationActivity(pictureFile.getAbsolutePath());

//				Intent intent = new Intent();
//	        	intent.setAction(Intent.ACTION_VIEW);
//	        	intent.setDataAndType(imageUri, "image/*");
//	        	startActivity(intent);
	        }
	    }
	}

	private void addFilterToFilterBarLayout(LinearLayout filtersLayout, FilterModel filter) {
		
		String filePath = getApplicationContext().getFilesDir() + "/" + MySQLiteHelper.TABLE_FILTERS
				+ "/" + String.valueOf(filter.getId());

		ImageView filterView = new ImageView(PhotosTakingActivity.this);
		filterView.setImageBitmap(Utils.decodeSampledBitmapFromFile(filePath,
				getResources(), R.drawable.artist_empty_icon, 100, 100));
		
		filterView.setContentDescription(String.valueOf(filter.getId()));
		filterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FiltersDataSource filtersDataSource = new FiltersDataSource(
						PhotosTakingActivity.this);
				filtersDataSource.open();

				Long id = Long.parseLong((String) ((ImageView) v)
						.getContentDescription());
				chosenFilter = filtersDataSource.getFilterFromId(id);
				filtersDataSource.close();

				String filePath = getApplicationContext().getFilesDir() + "/"
						+ MySQLiteHelper.TABLE_FILTERS + "/"
						+ String.valueOf(chosenFilter.getId());

				ImageView filterImagePreview = new ImageView(PhotosTakingActivity.this);
				filterImagePreview.setImageBitmap(Utils
						.decodeSampledBitmapFromFile(filePath, getResources(),
								R.drawable.artist_empty_icon,
								PhotoManager.REQUIRED_PICTURE_WIDTH,
								PhotoManager.REQUIRED_PICTURE_HEIGHT));

				FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
				preview.removeView(activePreviewFilter);
				activePreviewFilter = filterImagePreview;
				preview.addView(filterImagePreview);
			}
		});
		
		filterView.setLayoutParams( new FrameLayout.LayoutParams(100, 100) );
		filterView.setBackgroundResource( R.drawable.photo_filter_frame );
		filtersLayout.addView(filterView);
	}

	private Camera setUpCameraInstance() {
		Camera camera = null;
		try {
			camera = Camera.open();

			Camera.Parameters params = camera.getParameters();
			params.setPictureSize(PhotoManager.REQUIRED_PICTURE_WIDTH,
					PhotoManager.REQUIRED_PICTURE_HEIGHT);
			params.setPreviewSize(PhotoManager.REQUIRED_PICTURE_WIDTH,
					PhotoManager.REQUIRED_PICTURE_HEIGHT);
			params.set("rotation", 90);
			camera.setParameters(params);
		} catch (Exception e) {

		}
		return camera;
	}

//	private PictureCallback mPicture = new PictureCallback() {
//		@Override
//		public void onPictureTaken(byte[] data, Camera camera) {
//
//			PhotoManager photoManager = new PhotoManager(
//					PhotosTakingActivity.this);
//			Bitmap filteredPicture = photoManager.computePhotoWithfilter(data,
//					chosenFilter);
//
//			File pictureFile = getOutputMediaFile();
//			if (pictureFile == null) {
//				Log.d("DEBUG",
//						"Error creating media file, check storage permissions: ");
//				return;
//			}
//
//			try {
//				FileOutputStream fos = new FileOutputStream(pictureFile);
//				if (fos != null) {
//					filteredPicture.compress(Bitmap.CompressFormat.PNG, 100,
//							fos);
//				}
//				fos.close();
//			} catch (FileNotFoundException e) {
//				Log.d("DEBUG", "File not found: " + e.getMessage());
//			} catch (IOException e) {
//				Log.d("DEBUG", "Error accessing file: " + e.getMessage());
//			}
//
//			goToValidationActivity(pictureFile.getAbsolutePath());
//		}
//	};

	@Override
	protected void onPause() {
		super.onPause();
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mPreview.getHolder().removeCallback(mPreview);
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mCamera == null) {
			mCamera = setUpCameraInstance();
			mPreview = new CameraPreview(this, mCamera);
			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
			preview.addView(mPreview);
		}
	}

	private File getOutputMediaFile() {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"Imaginarium Festival Pictures");

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("Imaginarium Festival Pictures",
						"failed to create directory");
				return null;
			}
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.FRANCE).format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_IF_" + timeStamp + ".jpg");

		return mediaFile;
	}

	private void goToValidationActivity(String pictureFilePath) {
		Intent toPictureValidatingActivityIntent = new Intent(
				PhotosTakingActivity.this, PhotosValidatingActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("picturePath", pictureFilePath);
		toPictureValidatingActivityIntent.putExtras(bundle);
		startActivity(toPictureValidatingActivityIntent);
	}
}