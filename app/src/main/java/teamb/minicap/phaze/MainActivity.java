package teamb.minicap.phaze;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    Button cbutton;
    Button mbutton;
    Button vbutton;
    Button gbutton;
    Button sbutton;

    private GoogleApiClient client;
    private PrivateHeadsetPlugReceiver plugReceiver;
    private AudioManager audioManager = null;
    private MediaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //receiver for unplug pause/mute
        plugReceiver= new PrivateHeadsetPlugReceiver();

        int Request_Result = 1;
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Request_Result);
            }
        }
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_main);
        Typeface PTfont = Typeface.createFromAsset(getAssets(), "Prototype.ttf");
        cbutton = (Button) findViewById(R.id.connect);
        mbutton = (Button) findViewById(R.id.music);
        vbutton = (Button) findViewById(R.id.video);
        gbutton = (Button) findViewById(R.id.gallery);
        sbutton = (Button) findViewById(R.id.settings);
        cbutton.setTypeface(PTfont);
        mbutton.setTypeface(PTfont);
        vbutton.setTypeface(PTfont);
        gbutton.setTypeface(PTfont);
        sbutton.setTypeface(PTfont);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    {/*IDs for everything on main activity
    background: main
    Connect to Myo button: connect
    Music button: music
    Video button: video
    Gallery button: gallery
    Settings button: settings
    */}

    @Override
    public void onStart() {
        super.onStart();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://teamb.minicap.phaze/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onResume() {
        //Intent for headset status mute/pause
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(plugReceiver,filter);
        Toast.makeText(this,"registered receiver",Toast.LENGTH_LONG).show();


        super.onResume();
        String theme = prefs.getString("Themes", "Default");
        switch (theme) {
            case "Dark":
                themechange(2);
                break;
            case "Light":
                themechange(1);
                break;
            case "Default":
                themechange(0);
                break;
        }
    }

    public void themechange(int i) {
        Boolean connected = prefs.getBoolean("Connected", false);
        int button_text = 0;
        int button_active = 0;
        int button_inactive = 0;
        switch (i) {
            case 0:
                findViewById(R.id.main).setBackgroundColor(Color.argb(255, 66, 68, 69));
                button_text = R.color.button_text;
                button_active = R.drawable.button_active;
                button_inactive = R.drawable.button_inactive;
                break;
            //this is the dark, original theme
            case 1:
                findViewById(R.id.main).setBackgroundColor(Color.argb(255, 51, 181, 229));
                button_text = R.color.button_text_light;
                button_active = R.drawable.button_active_light;
                button_inactive = R.drawable.button_inactive_light;
                break;
            //this is the light, blue theme
            case 2:
                findViewById(R.id.main).setBackgroundColor(Color.argb(255, 66, 68, 69));
                button_text = R.color.button_text_dark;
                button_active = R.drawable.button_active_dark;
                button_inactive = R.drawable.button_inactive_dark;
                break;
            //this is the dark, blacker theme
        }
        if (Build.VERSION.SDK_INT >= 23) {
            cbutton.setTextColor(ContextCompat.getColor(this, button_text));
            sbutton.setTextColor(ContextCompat.getColor(this, button_text));
            mbutton.setTextColor(ContextCompat.getColor(this, button_text));
            vbutton.setTextColor(ContextCompat.getColor(this, button_text));
            gbutton.setTextColor(ContextCompat.getColor(this, button_text));
        } else {
            cbutton.setTextColor(getResources().getColor(button_text));
            sbutton.setTextColor(getResources().getColor(button_text));
            mbutton.setTextColor(getResources().getColor(button_text));
            vbutton.setTextColor(getResources().getColor(button_text));
            gbutton.setTextColor(getResources().getColor(button_text));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            cbutton.setBackground(getResources().getDrawable(button_active, null));
            sbutton.setBackground(getResources().getDrawable(button_active, null));
            if (connected) {
                mbutton.setEnabled(true);
                vbutton.setEnabled(true);
                gbutton.setEnabled(true);
                mbutton.setBackground(getResources().getDrawable(button_active, null));
                vbutton.setBackground(getResources().getDrawable(button_active, null));
                gbutton.setBackground(getResources().getDrawable(button_active, null));
            } else {
                mbutton.setEnabled(true);
                vbutton.setEnabled(true);
                gbutton.setEnabled(true);
                mbutton.setBackground(getResources().getDrawable(button_active, null));
                vbutton.setBackground(getResources().getDrawable(button_active, null));
                gbutton.setBackground(getResources().getDrawable(button_active, null));
            }
        } else {
            cbutton.setBackground(getResources().getDrawable(button_active));
            sbutton.setBackground(getResources().getDrawable(button_active));
            if (connected) {
                mbutton.setEnabled(true);
                vbutton.setEnabled(true);
                gbutton.setEnabled(true);
                mbutton.setBackground(getResources().getDrawable(button_active));
                vbutton.setBackground(getResources().getDrawable(button_active));
                gbutton.setBackground(getResources().getDrawable(button_active));
            } else {
                mbutton.setEnabled(true);
                vbutton.setEnabled(true);
                gbutton.setEnabled(true);
                mbutton.setBackground(getResources().getDrawable(button_active));
                vbutton.setBackground(getResources().getDrawable(button_active));
                gbutton.setBackground(getResources().getDrawable(button_active));
            }
        }
    }
    {/*  this is alex's stuff for the mute and pause settings
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra("state")){
            if (headsetConnected && intent.getIntExtra("state", 0) == 0){
                Music.setVolume((float) 0);
                headsetConnected = false;

            } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1){
                headsetConnected = true;
                myActivity.music.setVolume((float) 1);
            }
        }
    }
*/}
    public void connect(View view) {
        Intent intent = new Intent(MainActivity.this, MyoConnect.class);
        startActivity(intent);
    }

    public void music(View view) {
        Intent intent = new Intent(MainActivity.this, Music.class);
        startActivity(intent);
    }

    public void video(View view) {
        Intent intent = new Intent(MainActivity.this, Video.class);
        startActivity(intent);
    }

    public void gallery(View view) {
        Intent intent = new Intent(MainActivity.this, Gallery.class);
        startActivity(intent);
    }

    public void settings(View view) {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://teamb.minicap.phaze/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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

