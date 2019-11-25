package com.wintile.geco;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wintile.geco.Utils.ItemClickListener;
import com.wintile.geco.adapter.CategoryDataAdapter;
import com.wintile.geco.adapter.SectionListDataAdapter;
import com.wintile.geco.models.ProductModel;
import com.wintile.geco.models.SectionDataModel;
import com.wintile.geco.models.SingleItemModel;
import com.wintile.geco.services.NetworkChangeReceiver;
import com.wintile.geco.store.DBHelper;

import java.util.ArrayList;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.wintile.geco.MainActivity.EXTRA_ANIMAL_IMAGE_TRANSITION_NAME;
import static com.wintile.geco.MainActivity.EXTRA_ANIMAL_ITEM;

public class CatagoryListActivity extends AppCompatActivity implements ItemClickListener {

    private ArrayList<SingleItemModel> allSampleData;
    DBHelper dbHelper;
String category;

    IntentFilter intentFilter;
    NetworkChangeReceiver receiver;
    Snackbar snackbar;
    RelativeLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catagory_list);

        dbHelper = new DBHelper(getApplicationContext());
        allSampleData = new ArrayList<>();

        constraintLayout = findViewById(R.id.drawer_layout);

        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        snackbar = Snackbar.make(constraintLayout, "No internet connection!!!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CatagoryListActivity.this, CatagoryListActivity.class));
                finish();
            }
        });
        receiver = new NetworkChangeReceiver() {
            @Override
            protected void change() {
                snackbar.show();
            }
            @Override
            protected void dismissSnack(){
                if(snackbar.isShown())
                    snackbar.dismiss();
            }
        };
        category = getIntent().getStringExtra("category");
        Log.e("category", category);
        createDummyData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        CategoryDataAdapter adapter = new CategoryDataAdapter(allSampleData, this, this, "CatagoryListActivity");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void createDummyData() {

            ArrayList<ProductModel> productModels = (ArrayList<ProductModel>) dbHelper.getProducts(category);
            for (ProductModel productModel : productModels) {
                Log.e("Product", productModel.getProductName());
                allSampleData.add(new SingleItemModel(productModel.getProductName(), productModel.getImgUrl(), productModel.getCategory(), productModel.getProdutID(), productModel.getShareUrl(), productModel.getDesc(), productModel.getSpecs()));
            }

    }

    private final String android_image_urls[] = {
            "http://api.learn2crack.com/android/images/donut.png",
            "http://api.learn2crack.com/android/images/eclair.png",
            "http://api.learn2crack.com/android/images/froyo.png",
            "http://api.learn2crack.com/android/images/ginger.png",
            "http://api.learn2crack.com/android/images/honey.png",
            "http://api.learn2crack.com/android/images/icecream.png",
            "http://api.learn2crack.com/android/images/jellybean.png",
            "http://api.learn2crack.com/android/images/kitkat.png",
            "http://api.learn2crack.com/android/images/lollipop.png",
            "http://api.learn2crack.com/android/images/marshmallow.png",
            "http://api.learn2crack.com/android/images/donut.png",
            "http://api.learn2crack.com/android/images/eclair.png",
            "http://api.learn2crack.com/android/images/froyo.png",
            "http://api.learn2crack.com/android/images/ginger.png",
            "http://api.learn2crack.com/android/images/honey.png",
            "http://api.learn2crack.com/android/images/icecream.png",
            "http://api.learn2crack.com/android/images/jellybean.png",
            "http://api.learn2crack.com/android/images/kitkat.png",
            "http://api.learn2crack.com/android/images/lollipop.png",
            "http://api.learn2crack.com/android/images/marshmallow.png",
            "http://api.learn2crack.com/android/images/donut.png",
            "http://api.learn2crack.com/android/images/eclair.png",
            "http://api.learn2crack.com/android/images/froyo.png",
            "http://api.learn2crack.com/android/images/ginger.png",
            "http://api.learn2crack.com/android/images/honey.png",
            "http://api.learn2crack.com/android/images/icecream.png",
            "http://api.learn2crack.com/android/images/jellybean.png",
            "http://api.learn2crack.com/android/images/kitkat.png",
            "http://api.learn2crack.com/android/images/lollipop.png",
            "http://api.learn2crack.com/android/images/marshmallow.png"
    };

    private final String android_version_names[] = {
            "Donut",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Ice Cream Sandwich",
            "Jelly Bean",
            "KitKat",
            "Lollipop",
            "Marshmallow",
            "Donut",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Ice Cream Sandwich",
            "Jelly Bean",
            "KitKat",
            "Lollipop",
            "Marshmallow",
            "Donut",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "Ice Cream Sandwich",
            "Jelly Bean",
            "KitKat",
            "Lollipop",
            "Marshmallow"
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAnimalItemClick(int pos, SingleItemModel animalItem, ImageView sharedImageView) {
        Intent intent = new Intent(this, itemDetailsActivity.class);
        intent.putExtra(EXTRA_ANIMAL_ITEM, animalItem);
        intent.putExtra(EXTRA_ANIMAL_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(sharedImageView));
        Log.e("yup", animalItem.getUrl());
        Bundle bundle = new Bundle();
        bundle.putString("URL", android_image_urls[pos]);
        bundle.putInt("ProductID", animalItem.getProdutID());
        bundle.putString("ProductName", animalItem.getName());
        bundle.putString("ShareURL", animalItem.getShareUrl());
        bundle.putString("Desc", animalItem.getDesc());
        bundle.putString("Specs", animalItem.getSpecs());
        bundle.putString("Category", animalItem.getCategory());
        bundle.putBoolean("like", false);
        intent.putExtras(bundle);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedImageView,
                ViewCompat.getTransitionName(sharedImageView));
        Log.e("oops", String.valueOf(pos));
        startActivity(intent, options.toBundle());
    }
}
