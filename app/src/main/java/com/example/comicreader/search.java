package com.example.comicreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

public class search extends AppCompatActivity {
    int chapter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        EditText ui = (EditText) findViewById(R.id.urlinput);
        TextView c = findViewById(R.id.textView3);
        if(getIntent().getExtras()!=null) {
            ui.setText(getIntent().getStringExtra("url").toString());
            String cs = String.valueOf(getIntent().getIntExtra("chapter",1));
           c.setText(cs);
        }


    }



    public void setNextChapter(View view){
        chapter+=1;
        ((TextView) findViewById(R.id.textView3)).setText(String.valueOf(chapter));
    }

    public void setPreviousChapter(View view){
        chapter-=1;
        ((TextView) findViewById(R.id.textView3)).setText(String.valueOf(chapter));
    }

    public void saveSettings(View view){
        Intent getToMainPage = new Intent(this,MainActivity.class);
        EditText enteredUrl = (EditText) findViewById(R.id.urlinput);
        TextView enteredChapter = findViewById(R.id.textView3);
        getToMainPage.putExtra("url",enteredUrl.getText().toString());
        getToMainPage.putExtra("chapter",enteredChapter.getText().toString());

        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("appurl", Context.MODE_PRIVATE).edit();
        editor.putString("url", enteredUrl.getText().toString());
        editor.putInt("chapter", Integer.valueOf(enteredChapter.getText().toString()));
        editor.putInt("page", 1);
        editor.apply();

        startActivity(getToMainPage);
    }
}