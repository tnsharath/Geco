package com.wintile.geco;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import com.wintile.geco.Utils.Constants;
import com.wintile.geco.Utils.DefaultTransformer;
import com.wintile.geco.Utils.FlipHorizontalTransformer;
import com.wintile.geco.Utils.FullScreenClick;
import com.wintile.geco.Utils.ItemClickListener;
import com.wintile.geco.Utils.JSONParser;
import com.wintile.geco.Utils.NetworkUtil;
import com.wintile.geco.Utils.ViewPagerScroller;
import com.wintile.geco.adapter.CategoryDataAdapter;
import com.wintile.geco.adapter.RecyclerViewDataAdapter;
import com.wintile.geco.adapter.SliderAdapter;
import com.wintile.geco.adapter.VideoAdapter;
import com.wintile.geco.models.VideoModel;
import com.wintile.geco.services.NetworkChangeReceiver;
import com.wintile.geco.store.DBHelper;
import com.wintile.geco.models.ProductModel;
import com.wintile.geco.models.SectionDataModel;
import com.wintile.geco.models.SingleItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static com.wintile.geco.Utils.Constants.FETCH_PRODUCT_DATA;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ItemClickListener, OnMapReadyCallback, FullScreenClick {

    public static final String EXTRA_ANIMAL_ITEM = "animal_image_url";
    public static final String EXTRA_ANIMAL_IMAGE_TRANSITION_NAME = "animal_image_transition_name";
    ImageView ivOne;

    private static final String LOG_TAG = "MainActivity";

    private ArrayList<SingleItemModel> allSampleData;
    ViewPager viewPager;
    SliderAdapter sliderAdapter;

    int images[] = {
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3
    };

    Timer timer;
    final long DELAY_MS = 10000;
    final long PERIOD_MS = 5000;

    TextView tvContact, tvEmail;
    EditText etSubject, etMessage, etEmail, etPhone;
    Button btnSend;

    private GoogleMap mMap;
    private ProgressDialog pDialog;

   // private RecyclerView mVideosListView;
    private List<VideoModel> mVideosList = new ArrayList<>();
    private VideoAdapter mVideoAdapter;
    final int REQUEST_CODE = 5000;

    //Permission
    final static String[] PERMISSIONS = {android.Manifest.permission.CALL_PHONE};
    final static int PERMISSION_ALL = 1;

    JSONParser jsonParser = new JSONParser();

    //internet connection check
    DBHelper dbHelper;
    IntentFilter intentFilter;
    NetworkChangeReceiver receiver;
    Snackbar snackbar;
    DrawerLayout constraintLayout;
    RecyclerView recyclerView;

    //video
    VideoView videoView;
    View idNav;
    Handler mHandler;
    ImageView idPlay,idPlayNext, idPlayPrevious, idFullScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        constraintLayout = findViewById(R.id.drawer_layout);
        videoView =  findViewById(R.id.videoView);
        idNav = findViewById(R.id.idNav);
        idPlay = findViewById(R.id.idPlay);
        idPlayNext = findViewById(R.id.idPlayNext);
        idPlayPrevious = findViewById(R.id.idPlayPrevious);
        idFullScreen = findViewById(R.id.idFullScreen);
        mHandler = new Handler();

        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        snackbar = Snackbar.make(constraintLayout, "No internet connection!!!", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.RED);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
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
       // mVideosListView = findViewById(R.id.videoListView);

        //create videos
        VideoModel riverVideo = new VideoModel("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        VideoModel carsVideo = new VideoModel("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        VideoModel townVideo = new VideoModel("https://s3.amazonaws.com/androidvideostutorial/862014159.mp4");
        VideoModel whiteCarVideo = new VideoModel("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        VideoModel parkVideo = new VideoModel("https://s3.amazonaws.com/androidvideostutorial/862014834.mp4");
        VideoModel busyCityVideo = new VideoModel("https://s3.amazonaws.com/androidvideostutorial/862017385.mp4");

        mVideosList.add(riverVideo);
        mVideosList.add(carsVideo);
        mVideosList.add(townVideo);
        mVideosList.add(whiteCarVideo);
        mVideosList.add(parkVideo);
        mVideosList.add(busyCityVideo);
        final String url ="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4" ;
         Uri videoUri = Uri.parse(url);
        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.requestFocus();

            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "success");
                idNav.setVisibility(View.VISIBLE);
                Thread timer = new Thread(){
                    public void run(){
                        try{
                            sleep(3000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }finally {
                           mHandler.post(new Runnable() {
                               @Override
                               public void run() {
                                   idNav.setVisibility(View.GONE);
                               }
                           });
                        }
                    }
                };
                timer.start();
            }
        });

        idPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
                idNav.setVisibility(View.GONE);
            }
        });

        idPlayPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        idPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        idFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videointent = new Intent(MainActivity.this,
                        FullScreen.class);
                videointent.putExtra("currenttime",
                        videoView.getCurrentPosition());
                videointent.putExtra("Url", url);
                startActivityForResult(videointent, REQUEST_CODE);
            }
        });
       /* VideoAdapter adapter = new VideoAdapter(mVideosList, this,this);
        mVideosListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mVideosListView.setAdapter(adapter);*/
        new LoadProductData().execute();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        allSampleData = new ArrayList<>();

      //  createDummyData();
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        }
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        sliderAdapter = new SliderAdapter(this, images);
        viewPager.setAdapter(sliderAdapter);
        viewPager.setPageTransformer(false, new DefaultTransformer());
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
//TODO hot deals and header

        ViewPagerScroller scroller = new ViewPagerScroller(viewPager.getContext());
        scroller.setViewPagerScrollSpeed(viewPager, 3000);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                viewPager.post(new Runnable() {
                    @Override
                    public void run() {
                        int currentPage = (viewPager.getCurrentItem() + 1) % images.length;
                        viewPager.setCurrentItem(currentPage, true);
                    }
                });
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, DELAY_MS, PERIOD_MS);
      //  ivOne = (ImageView) findViewById(R.id.ivOne);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
       /* RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(allSampleData, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);*/



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        tvContact = findViewById(R.id.tvContact);
        tvEmail = findViewById(R.id.tvEmail);
        etMessage = findViewById(R.id.etMessage);
        etSubject = findViewById(R.id.etSubject);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnSend = findViewById(R.id.btnSend);

        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + Constants.CONTACT_NUMBER;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
                        requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    }
                    return;
                }
                startActivity(intent);
            }
        });
       /* tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"+ tvEmail.getText().toString().trim()));
                emailIntent.setType("text/plain");
                startActivity(emailIntent);
            }
        });*/

       btnSend.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if(etPhone.getText().toString().isEmpty()){
                   Toast.makeText(MainActivity.this, "Please enter the phone number", Toast.LENGTH_LONG).show();
               }else if(etPhone.getText().toString().length() < 10){
                   Toast.makeText(MainActivity.this, "Enter the valid Phone number", Toast.LENGTH_LONG).show();
               }else if(etMessage.getText().toString().isEmpty()){
                   Toast.makeText(MainActivity.this, "Message Cannot be Blank", Toast.LENGTH_LONG).show();
               }else {
                   querySend(view, etSubject.getText().toString(), etMessage.getText().toString(), etPhone.getText().toString().trim(), etEmail.getText().toString().trim());
               }
           }
       });
    }
//TODO size issues in saved and cat view

     private boolean isPermissionGranted() {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED);
    }


    private void createDummyData() {
        /*for (int i = 0; i < 20; i++) {
            SectionDataModel dm = new SectionDataModel();
            dm.setHeaderTitle("Section " + i);
            ArrayList<SingleItemModel> singleItemModels = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                singleItemModels.add(new SingleItemModel(android_version_names[j], android_image_urls[j]));
            }
            dm.setAllItemInSection(singleItemModels);
            allSampleData.add(dm);
        }*/

       /* List<String> categories = dbHelper.getCategory();
        //dbHelper.getAllProducts();
        for (String cat : categories){
            Log.e("cat", cat);
            SingleItemModel dm = new SingleItemModel();
            ArrayList<SingleItemModel> singleItemModels = new ArrayList<>();
           // dm.setHeaderTitle(cat);
            ArrayList<ProductModel> productModels = (ArrayList<ProductModel>) dbHelper.getAllProducts();
            for (ProductModel productModel: productModels){
                Log.e("Product", productModel.getProductName());
                singleItemModels.add(new SingleItemModel(productModel.getProductName(), productModel.getImgUrl(), productModel.getCategory(), productModel.getProdutID(), productModel.getShareUrl(), productModel.getDesc(), productModel.getSpecs()));
            }
           // dm.setAllItemInSection(singleItemModels);
            allSampleData.add(dm);
        }
*/

        ArrayList<ProductModel> productModels = (ArrayList<ProductModel>) dbHelper.getAllProducts();
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
        } else if (id == R.id.nav_products) {
            startActivity(new Intent(this, ProductActivity.class));
        } else if (id == R.id.nav_deals) {
            startActivity(new Intent(this, DealsActivity.class));
        } else if (id == R.id.nav_saved) {
            startActivity(new Intent(this, SavedActivity.class));
        } else if (id == R.id.nav_share) {
            //TODO write a brief description about app
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.nav_powered) {
            startActivity(new Intent(this, PoweredByActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //TODO change Deals nav icon and aboutus logo
    @Override
    public void onAnimalItemClick(int pos, SingleItemModel animalItem, ImageView sharedImageView) {
        Intent intent = new Intent(this, itemDetailsActivity.class);
        intent.putExtra(EXTRA_ANIMAL_ITEM, animalItem);

        intent.putExtra(EXTRA_ANIMAL_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(sharedImageView));

        Log.e("yup", animalItem.getName() + "\n" + animalItem.getProdutID() + "\n" + animalItem.getCategory() + "\n" + animalItem.getDesc()+ "\n" +animalItem.getSpecs()+ "\n" + animalItem.getShareUrl());

        Bundle bundle = new Bundle();
        bundle.putString("URL", animalItem.getUrl());
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


   @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in geco and move the camera
        LatLng geco = new LatLng(10.998791, 77.023263);
       // mMap.addMarker(new MarkerOptions().position(geco).title("Geco Crushers"));
       MarkerOptions markerOptions = new MarkerOptions();
       markerOptions.position(geco);
       markerOptions.title("Geco Crushers");
       Marker locationMarker = mMap.addMarker(markerOptions);
       locationMarker.setDraggable(true);
       locationMarker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.998791, 77.023263), 12.0f));
        mMap.getUiSettings().setMapToolbarEnabled(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data.hasExtra("currenttime")) {
                int result = data.getExtras().getInt("currenttime", 0);
                if (result > 0) {

                }
            }
        }
    }

    //querySend
    public void querySend(final View view, final String subject,final String message, final String phone, final String email) {


        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.QUERY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        etEmail.setText("");
                        etPhone.setText("");
                        etMessage.setText("");
                        etSubject.setText("");
                        Snackbar.make(view, "Successful", Snackbar.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view, "Unsuccessful", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        querySend( view,  subject, message, phone, email);
                    }
                }).show();
            }
        }) {

            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("subject", subject);
                params.put("message", message);
                params.put("phone", phone);
                params.put("email", email);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void fullScreen(int pos, String url) {
        Intent videointent = new Intent(this,
                FullScreen.class);
        videointent.putExtra("currenttime",
                pos);
        videointent.putExtra("Url", url);
        startActivityForResult(videointent, REQUEST_CODE);
    }

    /**
     * AsyncTask to getProduct
     */
    private class LoadProductData extends AsyncTask<URL, Void, ProductModel> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting things Ready");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(ProductModel constable) {
            super.onPostExecute(constable);
            pDialog.dismiss();
            createDummyData();

            recyclerView.setHasFixedSize(true);
            CategoryDataAdapter adapter = new CategoryDataAdapter(allSampleData, MainActivity.this, MainActivity.this, "saved");
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected ProductModel doInBackground(URL... urls) {

            URL urlOffender = jsonParser.createUrl(FETCH_PRODUCT_DATA);
            Log.e(LOG_TAG, "do in bg");
            String jsonResponseOffender = "";
            Log.e(LOG_TAG, "do in bg2");
            try{
                Log.e(LOG_TAG, "do in bg3");
                jsonResponseOffender = jsonParser.makeHttpRequest(urlOffender);
                Log.e("next", jsonResponseOffender);

            }catch (IOException e){
                Log.e(LOG_TAG, "let me see", e);
            }
            Log.e("next", jsonResponseOffender);
            extractFeatureFromJsonOffender(jsonResponseOffender);
            return null;
        }

        //method to parse offender json.
        private void extractFeatureFromJsonOffender(String offenderJson) {
            try {
                Log.e("string", offenderJson);
                JSONObject baseJsonResponse = new JSONObject(offenderJson);
                JSONArray productArray = baseJsonResponse.getJSONArray("products");

                for(int i = 0; i< productArray.length(); i++){
                    JSONObject firstlist = productArray.getJSONObject(i);
                    int ProductID  = Integer.parseInt(firstlist.getString("ProductID"));
                    String ProductName = firstlist.getString("ProductName");
                    String Category = firstlist.getString("Category");
                    String ImgID = firstlist.getString("ImgUrl");
                    String SpecsUrl = firstlist.getString("SpecsUrl");
                    String WebUrl = firstlist.getString("WebUrl");
                    String Description = firstlist.getString("Description");
                    dbHelper.createTableProductList(new ProductModel(  ProductName, Category, ImgID, ProductID, WebUrl, Description, SpecsUrl));
                    //Log.e("offender", name + "\t" + id + "\t" + type + "\t" + category + "\t" + beatnumber+ "\n");
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the Offender JSON results", e);
            }
        }
    }
}
