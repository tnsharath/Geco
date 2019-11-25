package com.wintile.geco.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wintile.geco.R;
import com.wintile.geco.Utils.ImageClickListerner;
import com.wintile.geco.Utils.ItemClickListener;

import java.util.ArrayList;

public class DetailsListDataAdapter extends RecyclerView.Adapter<DetailsListDataAdapter.SingleItemRowHolder>{

    private ArrayList<String> itemModels;
    private Context mContext;

    ImageClickListerner itemClickListener;
    String data[];
    int pos = 0;

    public DetailsListDataAdapter(ArrayList<String> itemModels, Context mContext, ImageClickListerner itemClickListener) {
        this.itemModels = itemModels;
        this.mContext = mContext;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemdetailsrec, parent, false);
        SingleItemRowHolder singleItemRowHolder = new SingleItemRowHolder(v);
        return singleItemRowHolder;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {

        Picasso.get().load(itemModels.get(position)).into(holder.itemImage);

        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onImageClick(holder.getAdapterPosition(), itemModels.get(position), holder.itemImage);

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

        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.itemImage = itemView.findViewById(R.id.itemImage);

        }
    }
}
