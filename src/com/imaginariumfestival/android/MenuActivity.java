package com.imaginariumfestival.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class MenuActivity extends Activity {
	private ImageButton artistsButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

		artistsButton = (ImageButton) findViewById(R.id.artistsButton);
		artistsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent toArtistsActivity = new Intent(MenuActivity.this, ArtistsActivity.class);
				startActivity(toArtistsActivity);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

}