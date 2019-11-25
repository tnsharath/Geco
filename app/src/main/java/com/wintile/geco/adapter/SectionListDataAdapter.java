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
import com.wintile.geco.Utils.ItemClickListener;
import com.wintile.geco.R;
import com.wintile.geco.store.DBHelper;
import com.wintile.geco.models.ProductModel;
import com.wintile.geco.models.SingleItemModel;

import java.util.ArrayList;

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder>{

    private ArrayList<SingleItemModel> itemModels;
    private Context mContext;
    ItemClickListener itemClickListener;
    SingleItemModel itemModel ;

    DBHelper dbHelper;
    public SectionListDataAdapter(ArrayList<SingleItemModel> itemModels, Context mContext, ItemClickListener itemClickListener) {
        this.itemModels = itemModels;
        this.mContext = mContext;
        this.itemClickListener = itemClickListener;
        dbHelper = new DBHelper(mContext);
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_card, parent, false);
        SingleItemRowHolder singleItemRowHolder = new SingleItemRowHolder(v);
        return singleItemRowHolder;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
        itemModel =  itemModels.get(position);
        holder.tvTitle.setText(itemModel.getName());
        Picasso.get().load(itemModel.getUrl()).into(holder.itemImage);
        Log.e("data" , itemModel.getUrl());

        ViewCompat.setTransitionName(holder.itemImage, itemModel.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleItemModel item;
                item = itemModels.get(position);
                itemClickListener.onAnimalItemClick(holder.getAdapterPosition(), item, holder.itemImage);

            }
        });
        holder.likeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("button", "CheckBox");
                SingleItemModel item;
                item = itemModels.get(position);
                if (holder.likeIcon.isChecked()){

                    dbHelper.CreateTableSaved(new ProductModel(item.getName(),  item.getCategory(), item.getUrl(),  item.getProdutID(), item.getShareUrl(), item.getDesc(), item.getSpecs()));
                    Log.e("check", "Checked "+ item.getProdutID() + " " + item.getName() + " " + item.getUrl());

                }else{
                    Log.e("check", "unchecked");
                    dbHelper.deleteRecord(item.getProdutID());

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
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    *//*Toast.makeText(view.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("Title", tvTitle.getText().toString());
                    Intent intent = new Intent("com.noeurng.playstoresample.ITEMDETAILS");
                    intent.putExtras(bundle);

                    mContext.startActivity(intent);*//*
                }
            });*/

        }
    }

}
