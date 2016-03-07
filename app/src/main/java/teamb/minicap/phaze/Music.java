package teamb.minicap.phaze;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.Spinner;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;
import teamb.minicap.phaze.Service_Music.MusicBinder;

public class Music extends AppCompatActivity {

    //Creates an array "trackList" to hold all the tracks
    private ArrayList<Tracks> trackList;
    //Defines the list where we'll display the tracks
    private ListView trackView;
    private Service_Music musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        trackView = (ListView)findViewById(R.id.trackView);
        trackList = new ArrayList<Tracks>();

        retrieveMedia();

        // This will be changed later in order to sort differently
        Collections.sort(trackList, new Comparator<Tracks>() {
            public int compare(Tracks one, Tracks two) {
                return one.getTitle().compareTo(two.getTitle());
            }
        });

        MusicAdapter trackAdapter = new MusicAdapter(this, trackList);
        trackView.setAdapter(trackAdapter);

    }

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Service_Music.MusicBinder binder = (Service_Music.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(trackList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, Service_Music.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void retrieveMedia(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!= null && musicCursor.moveToFirst()){

            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ALBUM);
            int yearColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.YEAR);
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisYear = musicCursor.getString(yearColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                trackList.add(new Tracks(thisId, thisTitle, thisArtist, thisAlbum, thisYear));
            }
            while (musicCursor.moveToNext());
        }
    }

    public void trackChosen(View view){
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
    }

}