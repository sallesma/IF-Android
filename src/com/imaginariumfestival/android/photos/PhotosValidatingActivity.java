package com.imaginariumfestival.android.photos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import com.imaginariumfestival.android.R;

public class PhotosValidatingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_picture_validating);
		
		String picturePath = (String) getIntent().getSerializableExtra("picturePath");
	    if (picturePath == null || picturePath.equals("")) {
	    	Toast.makeText(this, "Impossible d'afficher la photo.", Toast.LENGTH_LONG).show();
	    } else {
	    	ImageView imageView = (ImageView)findViewById(R.id.picture_taken);
	    	
	    	Bitmap originalPicture =BitmapFactory.decodeFile(picturePath);
	    	int nh = (int) ( originalPicture.getHeight() * (512.0 / originalPicture.getWidth()) );
	    	Bitmap scaledpicture = Bitmap.createScaledBitmap(originalPicture, 512, nh, true);
	    	imageView.setImageBitmap(scaledpicture);
	    }
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
}
