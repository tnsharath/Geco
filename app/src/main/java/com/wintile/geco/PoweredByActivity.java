package com.wintile.geco;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PoweredByActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powered_by);
        //BerkshireSwash-Regular
        TextView tvTitle = findViewById(R.id.tvTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BerkshireSwash-Regular.ttf");
        tvTitle.setTypeface(typeface);
    }
}
