package com.wintile.geco.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.universalvideoview.UniversalMediaController;
import com.wintile.geco.FullScreen;
import com.wintile.geco.R;
import com.wintile.geco.Utils.FullScreenClick;
import com.wintile.geco.models.VideoModel;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.SingleItemRowHolder> {

    private Context mContext;
    private List<VideoModel> mVideos;
    FullScreenClick fullScreenClick;

    public VideoAdapter(List<VideoModel> itemModels, Context mContext, FullScreenClick fullScreenClick) {
        this.mVideos = itemModels;
        this.mContext = mContext;
        this.fullScreenClick = fullScreenClick;
    }
    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        SingleItemRowHolder singleItemRowHolder = new SingleItemRowHolder(v);
        return singleItemRowHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoAdapter.SingleItemRowHolder singleItemRowHolder, int i) {
        try {
            VideoModel video = mVideos.get(i);
            final String url = video.getVideoUrl();
            Uri videoUri = Uri.parse(url);
            singleItemRowHolder.videoView.setVideoURI(videoUri);
            singleItemRowHolder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                    singleItemRowHolder.videoView.requestFocus();
                    singleItemRowHolder.videoView.start();
                }
            });
            singleItemRowHolder.idFullScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = singleItemRowHolder.videoView.getCurrentPosition();
                    fullScreenClick.fullScreen(pos, url);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return (null != mVideos ? mVideos.size() : 0);
    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        ImageView idFullScreen;
        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.videoView =  itemView.findViewById(R.id.videoView);
            idFullScreen = itemView.findViewById(R.id.idFullScreen);
        }
    }
}
