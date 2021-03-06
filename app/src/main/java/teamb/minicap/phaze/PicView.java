package teamb.minicap.phaze;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ZoomButton;
import android.widget.ZoomControls;


import java.util.ArrayList;

public class PicView extends AppCompatActivity {

    private ArrayList<pics> picsList;
    private int currPic;
    private ImageView pictures;
    private float x;
    private float y;
    private static final float  X_MAX_VALUE = 8;
    private static final float  Y_MAX_VALUE = 8;
    private static final float  X_MIN_VALUE = 1;
    private static final float  Y_MIN_VALUE = 1;
    ZoomControls zoom;
    private boolean gone = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer);
        pictures = (ImageView) findViewById(R.id.imageView);
        if (getIntent().hasExtra("pics")) {
            picsList = (ArrayList<pics>) getIntent().getSerializableExtra("pics");
        }
        if (getIntent().hasExtra("location")) {
            currPic = getIntent().getIntExtra("location", 0);
        }

        displayPicture(picsList.get(currPic).getID());

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-event"));

        final Button nextButton = (Button) findViewById(R.id.nextBtn);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currPic += 1;
                if (currPic > picsList.size() - 1)
                    currPic = 0;
                displayPicture(picsList.get(currPic).getID());
            }
        });

        final Button prevButton = (Button) findViewById(R.id.prevBtn);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currPic -= 1;
                if (currPic < 0)
                    currPic = picsList.size() - 1;
                displayPicture(picsList.get(currPic).getID());
            }
        });

        zoom = (ZoomControls) findViewById(R.id.zoomControls);
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = pictures.getScaleX();
                y = pictures.getScaleY();
                if (x != X_MAX_VALUE && y != Y_MAX_VALUE) {
                    pictures.setScaleX(x + 1);
                    pictures.setScaleY(y + 1);
                }
            }
        });

        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = pictures.getScaleX();
                y = pictures.getScaleY();
                if (x != X_MIN_VALUE && y != Y_MIN_VALUE) {
                    pictures.setScaleX(x - 1);
                    pictures.setScaleY(y - 1);
                }
            }
        });

        nextButton.setVisibility(View.INVISIBLE);
        prevButton.setVisibility(View.INVISIBLE);
        zoom.setVisibility(View.INVISIBLE);

        pictures.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    if (gone == false){
                        nextButton.setVisibility(View.INVISIBLE);
                        prevButton.setVisibility(View.INVISIBLE);
                        zoom.setVisibility(View.INVISIBLE);
                        gone = true;
                    }
                    else{
                        nextButton.setVisibility(View.VISIBLE);
                        prevButton.setVisibility(View.VISIBLE);
                        zoom.setVisibility(View.VISIBLE);
                        gone = false;
                    }
                }
                return true;
            }
        });
    }

    public void displayPicture(long id) {
        Uri pictureUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id);
        pictures.setImageURI(pictureUri);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            switch (message){
                case "prev":
                    currPic -= 1;
                    if (currPic < 0)
                        currPic = picsList.size()-1;
                    displayPicture(picsList.get(currPic).getID());
                    break;
                case "next":
                    currPic += 1;
                    if (currPic > picsList.size()-1)
                        currPic = 0;
                    displayPicture(picsList.get(currPic).getID());
                    break;
                case "zoomIn":
                    x = pictures.getScaleX();
                    y = pictures.getScaleY();
                    if ( x != X_MAX_VALUE && y != Y_MAX_VALUE) {
                        pictures.setScaleX(x + 1);
                        pictures.setScaleY(y + 1);
                    }
                    break;
                case "zoomOut":
                    x = pictures.getScaleX();
                    y = pictures.getScaleY();
                    if ( x != X_MIN_VALUE && y != Y_MIN_VALUE) {
                        pictures.setScaleX(x - 1);
                        pictures.setScaleY(y - 1);
                    }
                    break;
            }

        }
    };

}