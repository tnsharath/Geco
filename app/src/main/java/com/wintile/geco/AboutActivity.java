package com.wintile.geco;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView tvTitle = findViewById(R.id.tvTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Pacifico-Regular.ttf");
        tvTitle.setTypeface(typeface);
        //TODO contact about details in xml
    }
}
