package com.wintile.geco;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wintile.geco.Utils.ItemClickListener;
import com.wintile.geco.adapter.CategoryDataAdapter;
import com.wintile.geco.services.NetworkChangeReceiver;
import com.wintile.geco.store.DBHelper;
import com.wintile.geco.models.ProductModel;
import com.wintile.geco.models.SingleItemModel;

import java.util.ArrayList;
import java.util.List;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.wintile.geco.MainActivity.EXTRA_ANIMAL_IMAGE_TRANSITION_NAME;
import static com.wintile.geco.MainActivity.EXTRA_ANIMAL_ITEM;

public class SavedActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, ItemClickListener {

    private ArrayList<SingleItemModel> allSampleData;
    DBHelper dbHelper;
     LinearLayout savedEmpty;
    IntentFilter intentFilter;
    NetworkChangeReceiver receiver;
    Snackbar snackbar;
    DrawerLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);
        dbHelper = new DBHelper(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        constraintLayout = findViewById(R.id.drawer_layout);

        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        snackbar = Snackbar.make(constraintLayout, "No internet connection!!!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SavedActivity.this, SavedActivity.class));
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
savedEmpty = findViewById(R.id.savedEmpty);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        allSampleData = new ArrayList<>();

        createDummyData();
        if (!allSampleData.isEmpty()){
            savedEmpty.setVisibility(View.GONE);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        CategoryDataAdapter adapter = new CategoryDataAdapter(allSampleData, this, this, "SavedActivity");
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    private void createDummyData() {
            ArrayList<ProductModel> productModels = (ArrayList<ProductModel>) dbHelper.getSaved();
            for (ProductModel productModel: productModels){
                Log.e("Product", productModel.getProductName());
                allSampleData.add(new SingleItemModel(productModel.getProductName(), productModel.getImgUrl(), productModel.getCategory(), productModel.getProdutID(),productModel.getShareUrl(), productModel.getDesc(), productModel.getSpecs()));
            }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, GalleryActivity.class));
            finish();
        } else if (id == R.id.nav_products) {
            startActivity(new Intent(this, ProductActivity.class));
            finish();
        } else if (id == R.id.nav_deals) {
            startActivity(new Intent(this, DealsActivity.class));
            finish();
        } else if (id == R.id.nav_saved) {
            startActivity(new Intent(this, SavedActivity.class));
            finish();
        } else if (id == R.id.nav_share) {
//TODO write a brief description about app
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }else if (id == R.id.nav_contact) {
            startActivity(new Intent(this, AboutActivity.class));
        }else if (id == R.id.nav_powered) {
            startActivity(new Intent(this, PoweredByActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAnimalItemClick(int pos, SingleItemModel animalItem, ImageView sharedImageView) {
        Intent intent = new Intent(this, itemDetailsActivity.class);
        intent.putExtra(EXTRA_ANIMAL_ITEM, animalItem);
        intent.putExtra(EXTRA_ANIMAL_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(sharedImageView));
        Log.e("yup", animalItem.getUrl());
        Bundle bundle = new Bundle();
        bundle.putString("URL", animalItem.getUrl());
        bundle.putInt("ProductID", animalItem.getProdutID());
        bundle.putString("ProductName", animalItem.getName());
        bundle.putString("ShareURL", animalItem.getShareUrl());
        bundle.putString("Desc", animalItem.getDesc());
        bundle.putString("Specs", animalItem.getSpecs());
        bundle.putString("Category", animalItem.getCategory());
        bundle.putBoolean("like", true);
        intent.putExtras(bundle);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                sharedImageView,
                ViewCompat.getTransitionName(sharedImageView));
        Log.e("oops", String.valueOf(pos));
        startActivity(intent, options.toBundle());
    }
}
