package com.cahirulop.campingcarareas;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class CampingCarAreasActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.main);
        
        WebView wvContent;
        

        wvContent = (WebView) findViewById(R.id.wvContent);
        //wvContent.getSettings().setEnableSmoothTransition(true);
        wvContent.getSettings().setJavaScriptEnabled(true);
        
        final Activity activity = this;
        wvContent.setWebChromeClient(new WebChromeClient() {
          public void onProgressChanged(WebView view, int progress) {
            // Activities and WebViews measure progress with different scales.
            // The progress meter will automatically disappear when we reach 100%
            activity.setProgress(progress * 1000);
          }
        });
        
        wvContent.setWebViewClient(new WebViewClient() {
          public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
          }
        });
        
        wvContent.loadUrl("file:///sdcard/Aires camping-cars/index.htm");
    }
}