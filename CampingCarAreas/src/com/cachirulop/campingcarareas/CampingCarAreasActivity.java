package com.cachirulop.campingcarareas;

import com.cahirulop.campingcarareas.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class CampingCarAreasActivity extends Activity {
	WebView _wvContent;
	
	public static final int MENU_HOME= 1;

	private static final String PREFERENCES_SHARED_NAME = "preferences";
	private static final String PREFERENCES_LAST_URL = "lastURL";
	private static final String PREFERENCES_DEFAULT_URL = "defaultURL";
	private static final String PREFERENCES_DEFAULT_URL_VALUE = "file:///sdcard/Aires camping-cars/index.htm";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.main);

		_wvContent = (WebView) findViewById(R.id.wvContent);
		// wvContent.getSettings().setEnableSmoothTransition(true);
		_wvContent.getSettings().setJavaScriptEnabled(true);
		_wvContent.getSettings().setBuiltInZoomControls(true);
		_wvContent.getSettings().setDefaultZoom(ZoomDensity.FAR);

		_wvContent.setWebViewClient(new CampingCarAreasWebViewClient());

		final Activity activity = this;
		_wvContent.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				activity.setProgress(progress * 1000);
			}
		});

		_wvContent.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(activity, "ERROR: " + description,
						Toast.LENGTH_SHORT).show();
			}
		});

		openDefaultURL();
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		
		saveLastURL(_wvContent.getUrl());
	}

	@Override
	protected void onResume() {
		super.onPause();
		
		openDefaultURL();
	}

	@Override
	protected void onStop() {
		super.onPause();
		
		saveLastURL(_wvContent.getUrl());
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && _wvContent.canGoBack()) {
			_wvContent.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class CampingCarAreasWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			loadUrl(url);
			return true;
		}
	}

	private void openDefaultURL() {
		SharedPreferences prefs;
		String url;

		prefs = getPreferences(MODE_PRIVATE);

		url = prefs.getString(PREFERENCES_LAST_URL, null);
		if (url == null) {
			prefs = getSharedPreferences(PREFERENCES_SHARED_NAME, MODE_PRIVATE);

			url = prefs.getString(PREFERENCES_DEFAULT_URL,
					PREFERENCES_DEFAULT_URL_VALUE);
		}

		loadUrl(url);
	}
	
	private void saveLastURL (String URL) {
	      SharedPreferences prefs;
	      SharedPreferences.Editor editor;

	      prefs = getPreferences(MODE_PRIVATE);
	      editor = prefs.edit();
	      editor.putString (PREFERENCES_LAST_URL, URL);
	      
	      // Commit the edits!
	      editor.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result;
		MenuItem item;
		
		result = super.onCreateOptionsMenu(menu);
		
		item = menu.add(0, MENU_HOME, 0, R.string.menu_home);
		item.setIcon(R.drawable.ic_menu_home);
		
		return result;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_HOME:
    		loadUrl(CampingCarAreasActivity.PREFERENCES_DEFAULT_URL_VALUE);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }	
	
	private void loadUrl (String url) {
		Toast msg;
		
		msg = Toast.makeText(getApplicationContext(), 
							 String.format(getString(R.string.loading), url), 
							 Toast.LENGTH_LONG);
		msg.show();
		
		_wvContent.loadUrl(url);
	}
}
