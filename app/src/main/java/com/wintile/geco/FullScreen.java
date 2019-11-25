package com.wintile.geco;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.wintile.geco.Utils.CustomVideoView;

public class FullScreen extends Activity {
    Button btn;
    VideoView videoView = null;
    int currenttime = 0;
    String Url = "";
    private static ProgressDialog progressDialog;
    ImageView idFullScreenExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            currenttime = extras.getInt("currenttime", 0);
            Url = extras.getString("Url");
        }
        setContentView(R.layout.activity_full_screen);
        idFullScreenExit = findViewById(R.id.idFullScreenExit);
        idFullScreenExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("currenttime", videoView.getCurrentPosition());
                setResult(RESULT_OK, data);
                finish();
            }
        });
        videoView = (VideoView) findViewById(R.id.VideoViewfull);
        CustomVideoView player = new CustomVideoView(FullScreen.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,
                (int) (videoView.getWidth() * 9f / 16f));
        player.setNewDimension(metrics.widthPixels, metrics.heightPixels);
        player.getHolder().setFixedSize(metrics.heightPixels,
                metrics.widthPixels);
        player.setLayoutParams(params);
        progressDialog = ProgressDialog.show(this, "", "Loading...", true);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        Uri video = Uri.parse(Url);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer arg0) {
                progressDialog.dismiss();
                videoView.start();
                videoView.seekTo(currenttime);
            }
        });
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("currenttime", videoView.getCurrentPosition());
        setResult(RESULT_OK, data);
        super.finish();
    }
}