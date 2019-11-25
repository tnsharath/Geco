package com.wintile.geco;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wintile.geco.store.DBHelper;
import com.wintile.geco.models.ProductModel;

public class SplashActivity extends AppCompatActivity {
    Animation zoom_in;
    ImageView imageView;
    TextView tvText;
    DBHelper dbHelper;
    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbHelper = new DBHelper(getApplicationContext());
        dbHelper.createTableProductList(new ProductModel("Donut", "category1", "http://api.learn2crack.com/android/images/donut.png", 1234, "shareUrl", "desc", "specs"));
        dbHelper.createTableProductList(new ProductModel("Eclair", "category1", "http://api.learn2crack.com/android/images/eclair.png", 1235, "shareUrl", "desc", "specs"));
        dbHelper.createTableProductList(new ProductModel("Froyo", "category1", "http://api.learn2crack.com/android/images/froyo.png", 1236, "shareUrl", "desc", "specs"));
        dbHelper.createTableProductList(new ProductModel("Gingerbread", "category1", "http://api.learn2crack.com/android/images/ginger.png", 1237, "shareUrl", "desc", "specs"));
        dbHelper.createTableProductList(new ProductModel("Honeycomb", "category1", "http://api.learn2crack.com/android/images/honey.png", 1238, "shareUrl", "desc", "specs"));
        dbHelper.createTableProductList(new ProductModel("Ice Cream Sandwich", "category1", "http://api.learn2crack.com/android/images/icecream.png", 1239, "shareUrl", "desc", "specs"));
        dbHelper.createTableProductList(new ProductModel("Jelly Bean", "category1", "http://api.learn2crack.com/android/images/jellybean.png", 1240, "shareUrl", "desc", "specs"));
        dbHelper.createTableProductList(new ProductModel("KitKat", "category1", "http://api.learn2crack.com/android/images/kitkat.png", 1241, "shareUrl", "desc", "specs"));
        dbHelper.createTableProductList(new ProductModel("Lollipop", "category1", "http://api.learn2crack.com/android/images/lollipop.png", 1242, "shareUrl", "desc", "specs"));
        dbHelper.createTableProductList(new ProductModel("Marshmallow", "category1", "http://api.learn2crack.com/android/images/marshmallow.png", 1243, "shareUrl", "desc", "specs"));


        dbHelper.closeDB();


        tvText = (TextView) findViewById(R.id.tvText);

        imageView = (ImageView) findViewById(R.id.imageView);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Pacifico-Regular.ttf");
        tvText.setTypeface(typeface);
        zoom_in = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in);

        imageView.setAnimation(zoom_in);
        tvText.setAnimation(zoom_in);

        // start thread
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                startActivity( new Intent(SplashActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, 2000);

    }
}
