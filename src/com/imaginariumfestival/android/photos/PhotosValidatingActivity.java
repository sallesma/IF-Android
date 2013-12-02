package com.imaginariumfestival.android.photos;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.imaginariumfestival.android.R;

public class PhotosValidatingActivity extends Activity {
	private String picturePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_picture_validating);
		
		picturePath = (String) getIntent().getSerializableExtra("picturePath");
	    if (picturePath == null || picturePath.equals("")) {
	    	Toast.makeText(this, "Impossible d'afficher la photo.", Toast.LENGTH_LONG).show();
	    } else {
	    	ImageView imageView = (ImageView)findViewById(R.id.picture_taken);
	    	
	    	Bitmap originalPicture =BitmapFactory.decodeFile(picturePath);
	    	int nh = (int) ( originalPicture.getHeight() * (512.0 / originalPicture.getWidth()) );
	    	Bitmap scaledpicture = Bitmap.createScaledBitmap(originalPicture, 512, nh, true);
	    	imageView.setImageBitmap(scaledpicture);
	    }
	    
	    setUpShareButton();
	}

	private void setUpShareButton() {
		Button shareButton = (Button) findViewById(R.id.button_share);
		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.putExtra(Intent.EXTRA_SUBJECT, "Imaginarium Festival");
				i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile( new File(picturePath) ));

				i.setType("image/png");
				
				startActivity(Intent.createChooser(i, "Partagez !"));
			}
		});
	}
}
