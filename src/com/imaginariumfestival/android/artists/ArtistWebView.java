package com.imaginariumfestival.android.artists;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.imaginariumfestival.android.R;

public class ArtistWebView extends Activity {
	private WebView mWebView = null;
	private String weblink = null;
	private String artistName = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_artist_web_view);
		
		artistName = (String) getIntent().getSerializableExtra("artistName");
		
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		
		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				ArtistWebView.this.setTitle(R.string.loading);
				ArtistWebView.this.setProgress(progress * 100);
				if (progress == 100)
					ArtistWebView.this.setTitle(artistName);
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(ArtistWebView.this, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});
		
		weblink = (String) getIntent().getSerializableExtra("weblink");
		mWebView.loadUrl(weblink);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
	        mWebView.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			 onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(menuItem);
		}
	}
}