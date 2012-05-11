package com.cahirulop.campingcarareas;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class CampingCarAreasActivity extends Activity {
    WebView _wvContent;        

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.main);
        

        _wvContent = (WebView) findViewById(R.id.wvContent);
        //wvContent.getSettings().setEnableSmoothTransition(true);
        _wvContent.getSettings().setJavaScriptEnabled(true);
        _wvContent.getSettings().setBuiltInZoomControls(true);
        _wvContent.getSettings().setDefaultZoom(ZoomDensity.FAR);
        _wvContent.setWebViewClient(new CampingCarAreasWebViewClient());
        
        final Activity activity = this;
        _wvContent.setWebChromeClient(new WebChromeClient() {
          public void onProgressChanged(WebView view, int progress) {
            // Activities and WebViews measure progress with different scales.
            // The progress meter will automatically disappear when we reach 100%
            activity.setProgress(progress * 1000);
          }
        });
        
        _wvContent.setWebViewClient(new WebViewClient() {
          public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
          }
        });
        
        _wvContent.loadUrl("file:///sdcard/Aires camping-cars/index.htm");
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
            view.loadUrl(url);
            return true;
        }
    }    
}

