package com.wintile.geco.Utils;

import android.widget.ImageView;

import com.wintile.geco.models.SingleItemModel;

/**
 * Created by msc10 on 19/02/2017.
 */

public interface ItemClickListener {
    void onAnimalItemClick(int pos, SingleItemModel item, ImageView shareImageView);
}