package com.wintile.geco;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wintile.geco.Utils.Constants;
import com.wintile.geco.Utils.ImageClickListerner;
import com.wintile.geco.Utils.ViewPagerScroller;
import com.wintile.geco.adapter.DetailsListDataAdapter;
import com.wintile.geco.adapter.SliderAdapter;
import com.wintile.geco.models.ProductModel;
import com.wintile.geco.models.SingleItemModel;
import com.wintile.geco.store.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.wintile.geco.MainActivity.PERMISSIONS;
import static com.wintile.geco.MainActivity.PERMISSION_ALL;

public class itemDetailsActivity extends Activity implements ImageClickListerner {

    Activity mActivity;
    TextView textView, tvTitle, tvDesc;
    String productName, category, desc, shareUrl, specsUrl, imageUrl;
    int productID;
    ImageView ivSpecs, image_view;
    CheckBox likeIcon;
    DBHelper dbHelper;
    boolean like;

    String phone = "";
    String email = "";
    String subject = "";
    String message = "";
    Typeface typeface;

    ArrayList<String> allSampleData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        dbHelper = new DBHelper(getApplicationContext());
        image_view = findViewById(R.id.imageView);
        ivSpecs = findViewById(R.id.ivSpecs);
        tvTitle = findViewById(R.id.tvTitle);
        tvDesc = findViewById(R.id.tvDesc);
        likeIcon = findViewById(R.id.likeIcon);
        // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        mActivity = this;

        Bundle extras = getIntent().getExtras();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString(MainActivity.EXTRA_ANIMAL_IMAGE_TRANSITION_NAME);
            image_view.setTransitionName(imageTransitionName);
        }

        typeface = Typeface.createFromAsset(getAssets(), "fonts/BreeSerif-Regular.ttf");
        tvTitle.setTypeface(typeface);

        Bundle bundle = getIntent().getExtras();
        imageUrl = bundle.getString("URL");
        productID = bundle.getInt("ProductID");
        productName = bundle.getString("ProductName");
        category = bundle.getString("Category");
        desc = bundle.getString("Desc");
        shareUrl = bundle.getString("ShareURL");
        specsUrl = bundle.getString("Specs");
        like = bundle.getBoolean("like");
        if (like) {
            likeIcon.setChecked(true);
        }
        Picasso.get()
                .load(imageUrl)
                .noFade()
                .into(image_view);
        tvTitle.setText(productName);

        tvDesc.setText(desc);
        Picasso.get()
                .load(specsUrl)
                .noFade()
                .into(ivSpecs);
        fetchImages();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        DetailsListDataAdapter adapter = new DetailsListDataAdapter(allSampleData, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("button", "CheckBox");

                if (likeIcon.isChecked()) {

                    dbHelper.CreateTableSaved(new ProductModel(productName, category, imageUrl, productID, shareUrl, desc, specsUrl));

                } else {
                    dbHelper.deleteRecord(productID);

                }
            }
        });
    }


    public void getQuote(View view) {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.get_quote_dialog, null);


        final EditText etPhone = promptsView.findViewById(R.id.etPhone);
        final EditText etEmail = promptsView.findViewById(R.id.etEmail);
        final EditText etMessage = promptsView.findViewById(R.id.etMessage);


        Log.e("itemDetailsActivity", phone + "\t" + email + "\t" + message);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);


        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Send",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                phone = etPhone.getText().toString().trim();
                                email = etEmail.getText().toString().trim();
                                message = etMessage.getText().toString();
                                Log.e("itemDetailsActivity", phone + "\t" + email + "\t" + message);
                                if(phone.isEmpty()){
                                    Toast.makeText(itemDetailsActivity.this, "Please enter the phone number", Toast.LENGTH_LONG).show();
                                }else if(phone.length() < 10){
                                    Toast.makeText(itemDetailsActivity.this, "Enter the valid Phone number", Toast.LENGTH_LONG).show();
                                }else {
                                    querySend(String.valueOf(productID), message, phone, email);
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(true);

        alertDialog.show();
    }

    //TODO Images.
    public void querySend(final String productID, final String message, final String phone, final String email) {


        RequestQueue queue = Volley.newRequestQueue(itemDetailsActivity.this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.GET_QUOTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Snackbar.make(view, "Successful", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Successful You will be contacted soon!!!", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong!!! Retry.", Toast.LENGTH_LONG).show();
            }
        }) {

            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("message", message);
                params.put("phone", phone);
                params.put("email", email);
                params.put("productID", productID);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    public void share(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        //TODO change url
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out the awesome Product at: " + shareUrl);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void call(View view) {
        String uri = "tel:" + Constants.CONTACT_NUMBER;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(itemDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
                requestPermissions(PERMISSIONS, PERMISSION_ALL);
            }
            return;
        }
        startActivity(intent);
    }

    private boolean isPermissionGranted() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onImageClick(int pos, String item, ImageView shareImageView) {
        Picasso.get()
                .load(item)
                .noFade()
                .into(image_view);
    }

    private final String android_image_urls[] = {
            "http://api.learn2crack.com/android/images/donut.png",
            "http://api.learn2crack.com/android/images/eclair.png",
            "http://api.learn2crack.com/android/images/froyo.png",
            "http://api.learn2crack.com/android/images/ginger.png",
            "http://api.learn2crack.com/android/images/honey.png",
            "http://api.learn2crack.com/android/images/icecream.png",
            "http://api.learn2crack.com/android/images/froyo.png",
            "http://api.learn2crack.com/android/images/ginger.png",
            "http://api.learn2crack.com/android/images/honey.png",
            "http://api.learn2crack.com/android/images/icecream.png",
            "http://api.learn2crack.com/android/images/jellybean.png",
            "http://api.learn2crack.com/android/images/kitkat.png",
            "http://api.learn2crack.com/android/images/lollipop.png",
            "http://api.learn2crack.com/android/images/marshmallow.png"
    };

    public void fetchImages() {
        for (int j = 0; j < 13; j++) {
            allSampleData.add(android_image_urls[j]);
        }

        RequestQueue queue = Volley.newRequestQueue(itemDetailsActivity.this);
//TODO write a query to fetch Images save query should fetch both specs and product images.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FETCH_IMAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("productID", String.valueOf(productID));

                return params;
            }
        };
        queue.add(stringRequest);


    }
}
