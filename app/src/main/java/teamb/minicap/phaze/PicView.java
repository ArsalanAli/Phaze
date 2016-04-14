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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import java.util.ArrayList;

public class PicView extends AppCompatActivity {

    private ArrayList<pics> picsList;
    private int currPic;
    private ImageView pictures;
    private int arraySize;

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
                case "play/pause":
                    break;
            }

        }
    };

}