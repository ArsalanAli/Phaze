package teamb.minicap.phaze;

import java.security.Provider;
import java.util.ArrayList;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.PendingIntent;


/**
 * Created by Kareem on 2016-03-05.
 */
public class Service_Music extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //media player
    private MediaPlayer player;
    //song list
    private ArrayList<Tracks> tracks;
    //current position
    private int songPosn;
    private final IBinder musicBind = new MusicBinder();

    private String songTitle = "";
    private static final int NOTIFY_ID = 1;
    private boolean shuffle=false;
    private Random rand;

    //needed for fade in
    private int iVolume;
    private final static int INT_VOLUME_MAX = 15;
    private final static int INT_VOLUME_MIN = 0;
    private final static float FLOAT_VOLUME_MAX = 1;
    private final static float FLOAT_VOLUME_MIN = 0;


    public void onCreate(){
        //create the service
         super.onCreate();
        player = new MediaPlayer();
         initMusicPlayer();
        //initialize position
        songPosn=0;
//create player
        rand=new Random();
    }

    public void initMusicPlayer(){
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void setList(ArrayList<Tracks> theTracks){
        tracks = theTracks;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(player.getCurrentPosition()>0){
            mp.reset();
            playNext();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if(mp.isPlaying())
            mp.stop();
        mp.start();
        Intent notIntent = new Intent(this, MainActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(songTitle)
                .setOngoing(true)
                .setContentTitle("Playing")
        .setContentText(songTitle);
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
    }

    public class MusicBinder extends Binder {
        Service_Music getService() {
            return Service_Music.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {

        return musicBind;
        }
    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }

    public void playSong(){
        player.reset();
        //get song
        Tracks playSong = tracks.get(songPosn);
        songTitle=playSong.getTitle();
        //get id
        long currSong = playSong.getID();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try{
            player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }

        //Set current volume, depending on fade or not
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int fadeDuration = prefs.getInt("fadeseekbar", 5000);


        if (fadeDuration > 0)
            iVolume = INT_VOLUME_MIN;
        else
            iVolume = INT_VOLUME_MAX;

        updateVolume(0);

        //Start increasing volume in increments
        if(fadeDuration > 0) {
            final Timer timer = new Timer(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    updateVolume(1);
                    if (iVolume == INT_VOLUME_MAX) {
                        timer.cancel();
                        timer.purge();
                    }
                }
            };

            // calculate delay, cannot be zero, set to 1 if zero
            int delay = fadeDuration / INT_VOLUME_MAX;
            if (delay == 0) delay = 1;

            timer.schedule(timerTask, delay, delay);
        }

        player.prepareAsync();
    }

    public void setSong(int songIndex){
        songPosn=songIndex;
    }

    public int getSongPosn(){
        return player.getCurrentPosition();
    }
    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer() {
        player.pause();
    }

    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }
    public void playPrev(){
        songPosn--;
        if(songPosn<0) songPosn=tracks.size()-1;
        playSong();
    }
    public void playNext(){
        if (shuffle){
            int newSong = songPosn;
            while(newSong == songPosn){
                newSong = rand.nextInt(tracks.size());
            }
            songPosn=newSong;
        }
        else {
            songPosn++;
            if (songPosn >= tracks.size()) songPosn = 0;
        }
        playSong();
    }
    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
    }
    private void updateVolume(int change)
    {
        //increment or decrement depending on type of fade
        iVolume = iVolume + change;

        //ensure iVolume within boundaries
        if (iVolume < INT_VOLUME_MIN)
            iVolume = INT_VOLUME_MIN;
        else if (iVolume > INT_VOLUME_MAX)
            iVolume = INT_VOLUME_MAX;

        //convert to float value
        float fVolume = 1 - ((float) Math.log(INT_VOLUME_MAX - iVolume) / (float) Math.log(INT_VOLUME_MAX));

        //ensure fVolume within boundaries
        if (fVolume < FLOAT_VOLUME_MIN)
            fVolume = FLOAT_VOLUME_MIN;
        else if (fVolume > FLOAT_VOLUME_MAX)
            fVolume = FLOAT_VOLUME_MAX;

        player.setVolume(fVolume, fVolume);
    }
}
