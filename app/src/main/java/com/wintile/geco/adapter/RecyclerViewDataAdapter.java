package com.wintile.geco.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.wintile.geco.CatagoryListActivity;
import com.wintile.geco.Utils.ItemClickListener;
import com.wintile.geco.R;
import com.wintile.geco.models.SectionDataModel;

import java.util.ArrayList;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder>{

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private SnapHelper snapHelper;
    private ItemClickListener itemClickListener;

    public RecyclerViewDataAdapter(ArrayList<SectionDataModel> dataList, Context mContext, ItemClickListener itemClickListener) {
        this.dataList = dataList;
        this.mContext = mContext;
        recycledViewPool = new RecyclerView.RecycledViewPool();
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ItemRowHolder rowHolder = new ItemRowHolder(v);
        snapHelper = new GravitySnapHelper(Gravity.START);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, int position) {
        final String sectionName = dataList.get(position).getHeaderTitle();
        ArrayList singleSectionItems = dataList.get(position).getAllItemInSection();
        holder.itemTitle.setText(sectionName);
        SectionListDataAdapter adapter = new SectionListDataAdapter(singleSectionItems, mContext, itemClickListener);
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setRecycledViewPool(recycledViewPool);
        snapHelper.attachToRecyclerView(holder.recyclerView);
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CatagoryListActivity.class);

                intent.putExtra("category", sectionName);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView itemTitle;
        protected RecyclerView recyclerView;
        protected Button btnMore;

        public ItemRowHolder(View itemView) {
            super(itemView);
            this.itemTitle = itemView.findViewById(R.id.itemTitle);
            this.recyclerView = itemView.findViewById(R.id.recycler_view_list);
            this.btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
