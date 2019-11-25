package com.wintile.geco.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.wintile.geco.SavedActivity;
import com.wintile.geco.Utils.ItemClickListener;
import com.wintile.geco.R;
import com.wintile.geco.models.ProductModel;
import com.wintile.geco.models.SingleItemModel;
import com.wintile.geco.store.DBHelper;

import java.util.ArrayList;

public class CategoryDataAdapter extends RecyclerView.Adapter<CategoryDataAdapter.SingleItemRowHolder> {

    private ArrayList<SingleItemModel> itemModels;
    private Context mContext;
    ItemClickListener itemClickListener;
    SingleItemModel itemModel;
    DBHelper dbHelper;
    String className;
    Typeface typeface;

    public CategoryDataAdapter(ArrayList<SingleItemModel> itemModels, Context mContext, ItemClickListener itemClickListener, String className) {
        this.itemModels = itemModels;
        this.mContext = mContext;
        this.itemClickListener = itemClickListener;
        dbHelper = new DBHelper(mContext);
        this.className = className;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        SingleItemRowHolder singleItemRowHolder = new SingleItemRowHolder(v);
        return singleItemRowHolder;
    }

    @Override
    public void onBindViewHolder(final CategoryDataAdapter.SingleItemRowHolder holder, final int position) {

        itemModel = itemModels.get(position);

        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/BreeSerif-Regular.ttf");
        holder.tvTitle.setTypeface(typeface);
        holder.tvTitle.setText(itemModel.getName());
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = holder.itemImage.getWidth();

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };


        Picasso.get().load(itemModel.getUrl()).transform(transformation)
                .into(holder.itemImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        //holder.itemImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }

                });
//        Log.e("data" , itemModel.getUrl());

        ViewCompat.setTransitionName(holder.itemImage, itemModel.name);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleItemModel item;
                item = itemModels.get(position);
                itemClickListener.onAnimalItemClick(holder.getAdapterPosition(), item, holder.itemImage);
            }
        });
        //
        if (className.equals("SavedActivity")) {
            //holder.likeIcon.setVisibility(View.GONE);
            holder.likeIcon.setChecked(true);
        }
        Log.e("context", String.valueOf(mContext));
        holder.likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("button", "CheckBox");
                SingleItemModel item;
                item = itemModels.get(position);
                if (holder.likeIcon.isChecked()) {
                    dbHelper.CreateTableSaved(new ProductModel(item.getName(), item.getCategory(), item.getUrl(), item.getProdutID(), item.getShareUrl(), item.getDesc(), item.getSpecs()));
                    Log.e("check", "Checked " + item.getProdutID() + " " + item.getName() + " " + item.getUrl());

                } else {

                    dbHelper.deleteRecord(item.getProdutID());
                    if (className.equals("SavedActivity")) {
                        //holder.likeIcon.setVisibility(View.GONE);
                        mContext.startActivity(new Intent("com.wintile.geco.SAVEDACTIVITY"));
                        Toast.makeText(mContext, "Item " + item.getName() + " Removed from SAVED", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return (null != itemModels ? itemModels.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;
        protected ImageView itemImage;
        CheckBox likeIcon;

        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.tvTitle);
            this.itemImage = itemView.findViewById(R.id.itemImage);
            this.likeIcon = itemView.findViewById(R.id.likeIcon);
        }
    }

}

