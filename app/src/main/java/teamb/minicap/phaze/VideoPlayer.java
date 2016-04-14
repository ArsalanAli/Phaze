package teamb.minicap.phaze;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import java.util.ArrayList;

public class VideoPlayer extends AppCompatActivity {
    private ArrayList<vids> vidsList;
    private int currVid;
    private VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        video = (VideoView) findViewById(R.id.videoView);
        if (getIntent().hasExtra("vids")){
            vidsList = (ArrayList<vids>)getIntent().getSerializableExtra("vids");
        }
        if (getIntent().hasExtra("location")){
            currVid = getIntent().getIntExtra("location", 0);
        }

        setController();

        playVideo(vidsList.get(currVid).getID());
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-event"));


    }

    public void playVideo(long id){
        Uri videoUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                id);
        video.setVideoURI(videoUri);
        video.start();
    }

    public void setController(){
        MediaController controller = new MediaController(this);
        controller.setAnchorView(video);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        video.setMediaController(controller);
    }

    public void playNext(){
        currVid++;
        if (currVid > vidsList.size()-1){
            currVid = 0;
        }
        playVideo(vidsList.get(currVid).getID());
    }
    public void playPrev(){
        currVid--;
        if (currVid < 0 ){
            currVid = vidsList.size()-1;
        }
        playVideo(vidsList.get(currVid).getID());
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
    public void onReceive(Context context, Intent intent) {
        // Extract data included in the Intent
        String message = intent.getStringExtra("message");
        Log.d("receiver", "Got message: " + message);
        switch (message){
            case "prev":
                playPrev();
                break;
            case "next":
                playNext();
                break;
            case "play/pause":
                if(video.isPlaying()){
                   video.pause();
                }
                else{
                    int currentpos = video.getCurrentPosition();
                    video.start();
                    video.seekTo(currentpos);
                }
                break;
        }
        }
    };
}
