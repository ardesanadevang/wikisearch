package com.example.wikisearch.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.wikisearch.R;
import com.example.wikisearch.util.Util;
import com.example.wikisearch.util.VolleyWebservices;
import com.example.wikisearch.util.WebServices_Url;

import org.json.JSONException;
import org.json.JSONObject;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    Activity activity;
    VolleyWebservices volleyWebservices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activity = WebViewActivity.this;
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new AppWebViewClients());


        int pageId = getIntent().getIntExtra("pageId", 0);
        volleyWebservices = new VolleyWebservices();

        fetchPageDetails(pageId);
    }

    public void fetchPageDetails(final int pageId) {
        volleyWebservices.callWSmethodGET(activity, WebServices_Url.getPageDetailUrl(String.valueOf(pageId)), new VolleyWebservices.Callback() {
            @Override
            public void onSuccess(String responce, String TAG) {
                try {
                    JSONObject obj = new JSONObject(responce);
                    JSONObject objQuery = obj.getJSONObject("query");
                    JSONObject objPages = objQuery.getJSONObject("pages");
                    JSONObject objPageNumber = objPages.getJSONObject(String.valueOf(pageId));
                    String url = objPageNumber.getString("canonicalurl");
                    webView.loadUrl(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.showToast(activity, "Invalid Data");
                }
            }

            @Override
            public void onFailure(String message, String TAG) {
                Util.showToast(activity, message);
            }
        }, "");
    }

    public class AppWebViewClients extends WebViewClient {
        private ProgressDialog progressDialog;

        public AppWebViewClients() {
            if (progressDialog == null)
                this.progressDialog = new ProgressDialog(activity);

            if (!progressDialog.isShowing())
                progressDialog.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}
