package teamb.minicap.phaze;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Video extends AppCompatActivity {
    private ArrayList<vids> vidsList;
    private ListView vidsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        int request = 0;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, request);
            }
        }
        vidsView = (ListView)findViewById(R.id.vidsView);
        vidsList = new ArrayList<vids>();

        retrieveMedia();

        Collections.sort(vidsList, new Comparator<vids>() {
            public int compare(vids one, vids two) {
                return one.getTitle().compareTo(two.getTitle());
            }
        });

        videoAdapter vidsAdapter = new videoAdapter(this, vidsList);
        vidsView.setAdapter(vidsAdapter);
    }
    public void retrieveMedia(){
        ContentResolver videoResolver = getContentResolver();
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor videoCursor = videoResolver.query(videoUri, null, null, null, null);

        if(videoCursor!= null && videoCursor.moveToFirst()){

            int title = videoCursor.getColumnIndex
                    (MediaStore.Video.Media.TITLE);
            int idColumn = videoCursor.getColumnIndex
                    (MediaStore.Video.Media._ID);
            do {
                long thisId = videoCursor.getLong(idColumn);
                String thisTitle = videoCursor.getString(title);
                vidsList.add(new vids(thisId, thisTitle));
            }
            while (videoCursor.moveToNext());
        }
    }
    public void vidChosen(View view){
        Intent intent = new Intent(this, VideoPlayer.class);
        intent.putExtra("vids", vidsList);
        intent.putExtra("location", Integer.parseInt(view.getTag().toString()));
        startActivity(intent);
    }
}
