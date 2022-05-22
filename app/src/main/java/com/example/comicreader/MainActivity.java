package com.example.comicreader;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity {
    int pageNumber = 1;
    int chapter = 1;

    SharedPreferences sharedPref;
    String url = "https://www.comicextra.com/black-widow-2010";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPref = getApplicationContext().getSharedPreferences("appurl",Context.MODE_PRIVATE);
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.INTERNET) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.

        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.INTERNET);
        }
        String spChapter = String.valueOf(sharedPref.getInt("chapter",1));
        String spPage = String.valueOf(sharedPref.getInt("page",1));
        String spLink = sharedPref.getString("url",url);
        chapter = Integer.valueOf(spChapter);
        pageNumber=Integer.valueOf(spPage);
        url = spLink;
        setWebView(pageNumber);
        WebView wv =  findViewById(R.id.webView);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.setWebViewClient(new MyWebViewClient());
    }



    private static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }
    }



    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.settings){
            Intent getToSettingsPage = new Intent(this,search.class);
            getToSettingsPage.putExtra("url",url);
            getToSettingsPage.putExtra("chapter",chapter);
            startActivity(getToSettingsPage);
        }
        return false;
    }









    public void setWebView(int pageNum){

        String urlString =url+"/chapter-"+chapter+"/"+String.valueOf(pageNum);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("url", url);
        editor.putInt("chapter", chapter);
        editor.putInt("page", pageNum);
        editor.apply();

        if(getIntent().getExtras()!=null) {
            url = getIntent().getStringExtra("url");
            chapter = Integer.valueOf(getIntent().getStringExtra("chapter"));
        }
        Log.i("info",urlString);
        WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(urlString);

       // SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    }

    public void setNext(View view){
        pageNumber+=1;
        setWebView(pageNumber);
    }

    public void setPrevious(View view){
        if(pageNumber==1){
            Toast.makeText(getApplicationContext(), "You are already at the first page!", Toast.LENGTH_SHORT).show();
            return;
        }
        pageNumber-=1;
        setWebView(pageNumber);
    }
}