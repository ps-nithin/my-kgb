package com.app.mykgb;

import android.content.Intent;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent=getIntent();
        Toolbar detail_toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        Bundle bun=intent.getExtras();
        this.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        if(bun!=null){
            String detail_title=(String)bun.get(MainActivity.MENU_NAME);
            detail_title=detail_title.split("\\s+")[1];
            detail_toolbar.setTitle(detail_title.toUpperCase());
            setSupportActionBar(detail_toolbar);

            WebView wv=(WebView)findViewById(R.id.detail_webview);
            wv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
            wv.setLongClickable(false);
            WebSettings wv_settings=wv.getSettings();
            wv_settings.setJavaScriptEnabled(true);
            wv_settings.setDefaultFontSize(16);
            AssetManager am=getResources().getAssets();
            try {
                am.open(detail_title+".html");
                wv.loadUrl("file:///android_asset/"+detail_title+".html");
            } catch (IOException e) {
                wv.loadUrl("file:///android_asset/oops.html");
            }
        }
    }

}
