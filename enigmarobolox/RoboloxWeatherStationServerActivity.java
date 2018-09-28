package com.enigmarobolox.arm_avi.enigmarobolox;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

public class RoboloxWeatherStationServerActivity extends AppCompatActivity {

    WebView weatherWebview;
    SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robolox_weather_station_server);

        //Weather and Web server Webview
        weatherWebview = (WebView) findViewById(R.id.weatherWebview);

        //Call all WebPage Rendering Function
        WebSettings webSettings = weatherWebview.getSettings();
        weatherWebview.setWebViewClient(new WeatherBrowser());

        weatherWebview.getSettings().setLoadsImagesAutomatically(true);
        weatherWebview.getSettings().setJavaScriptEnabled(true);
        weatherWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        weatherWebview.getSettings().setPluginState(WebSettings.PluginState.ON);
        weatherWebview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        weatherWebview.getSettings().setBuiltInZoomControls(false);
        weatherWebview.getSettings().setDisplayZoomControls(false);
        weatherWebview.getSettings().setAppCacheEnabled(true);
        weatherWebview.setInitialScale(0);
        weatherWebview.getSettings().setLoadWithOverviewMode(true);
        weatherWebview.getSettings().setUseWideViewPort(true);
        weatherWebview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        weatherWebview.getSettings().setDomStorageEnabled(true);

        //Swipe Refresh of Webview
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);

        //Load Weather Station URL


        weatherWebview.loadUrl(getIntent().getExtras().getString("web_289"));

        weatherWebview.loadUrl(getIntent().getExtras().getString("weather_station_url_key_00258"));

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                swipeRefresh.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        swipeRefresh.setRefreshing(false);
                        weatherWebview.reload();
                    }
                }, 2000);
            }
        });


    }


    //WebViewClient is a Class That can give Basic Browsing Ability To WebView
    private class WeatherBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            weatherWebview.loadUrl(url);
            return true;
        }

        public void onLoadResource(WebView view,String url){

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//           webViewRender.loadUrl(errorConnection);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if ( weatherWebview.canGoBack()) {
                        weatherWebview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
