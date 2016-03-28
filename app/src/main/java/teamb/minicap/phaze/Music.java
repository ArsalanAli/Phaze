package teamb.minicap.phaze;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.session.MediaController;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.SeekBar;
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
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Toast;


public class Music extends AppCompatActivity implements MediaPlayerControl {

    private ArrayList<Tracks> trackList;
    private ListView trackView;
    private Service_Music musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;

    //stuff for audio volume control see alex if you want to change this
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;
    private PrivateHeadsetPlugReceiver plugReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        trackView = (ListView)findViewById(R.id.trackView);
        trackList = new ArrayList<Tracks>();

        retrieveMedia();

        //receiver for unplug pause/mute
        plugReceiver = new PrivateHeadsetPlugReceiver();

        //Default Volume
        //this will make the audio level show up onscreen for debugging
        //Toast.makeText(this, String.valueOf(vol), Toast.LENGTH_LONG).show();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            int vol = prefs.getInt("defvolseekbar", 5);
            setVolumeControlStream(audioManager.STREAM_MUSIC);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, AudioManager.FLAG_SHOW_UI);



        // This will be changed later in order to sort differently
        Collections.sort(trackList, new Comparator<Tracks>() {
            public int compare(Tracks one, Tracks two) {
                return one.getTitle().compareTo(two.getTitle());
            }
        });

        MusicAdapter trackAdapter = new MusicAdapter(this, trackList);
        trackView.setAdapter(trackAdapter);

        setController();

        //no longer needed
        //broadcastIntent();
    }

    //broadcast custom intent
    //no longer needed
    /*public void broadcastIntent(){
        Intent intent = new Intent();
        intent.setAction("teamb.minicap.phaze.CUSTOM_INTENT");
        //intent.setAction("android.intent.action.HEADSET_PLUG");
        sendBroadcast(intent);
    }
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_music, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //menu item selected
        switch (id) {
            case R.id.action_shuffle:
                //shuffle
                musicSrv.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv=null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Service_Music.MusicBinder binder = (MusicBinder)service;
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
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }
    private void setController(){
        controller = new MusicController(this);
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
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.trackView));
        controller.setEnabled(true);
    }
    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }
    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
        return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
        return musicSrv.getSongPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
        return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }

        //Intent for headset status mute/pause
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(plugReceiver,filter);
        Toast.makeText(this,"registered receiver",Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    //private class for handling the receiver for mute/pause
    private class PrivateHeadsetPlugReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"received broadcast intent",Toast.LENGTH_LONG).show();

            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            //Mute or Pause
            //this may need to be moved to the Music.java

            String on_unplug = prefs.getString("headset_on_unplug", "Nothing");
            switch (on_unplug) {
                case "Nothing":
                    //do nothing
                    break;
                case "Mute":
                    if(!audioManager.isWiredHeadsetOn())
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,AudioManager.FLAG_VIBRATE);
                    Toast.makeText(context, "inside the mute branch", Toast.LENGTH_LONG).show();
                    break;
                case "Pause":
                    if(!audioManager.isWiredHeadsetOn());
                    //what function can i call????
                    //Service_Music Musica = new Service_Music();
                    //Musica.pausePlayer();
                    //playbackPaused=true;
                    //musicSrv.pausePlayer();
                    Toast.makeText(context,"inside the pause branch",Toast.LENGTH_LONG).show();

                    KeyEvent event1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PAUSE);
                    KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PAUSE);
                    //controller.dispatchKeyEvent(event1);
                    //controller.dispatchKeyEvent(event2);
                    if (controller.dispatchKeyEvent(event1)){
                        controller.dispatchKeyEvent(event2);
                    }
                    break;
            }
        }
    }
}