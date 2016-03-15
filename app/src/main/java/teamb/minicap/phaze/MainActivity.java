package teamb.minicap.phaze;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.Manifest;

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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private AudioManager MuteManager;

    private boolean headsetConnected = false;

    /*public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("state")){
            if (headsetConnected && intent.getIntExtra("state", 0) == 0){
                headsetConnected = false;
            } else if (!headsetConnected && intent.getIntExtra("state", 0) == 1){
                headsetConnected = true;
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        MuteManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

    /*IDs for everything on main activity
    background: main
    Connect to Myo button: connect
    Music button: music
    Video button: video
    Gallery button: gallery
    Settings button: settings
    */
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
        String on_unplug = prefs.getString("headset_on_unplug", "Nothing");
        switch (on_unplug) {
            case "Nothing":
                //do nothing
                break;
            case "Mute":
                //Muteme;
                //MuteManager.adjustSuggestedStreamVolume(AudioManager.ADJUST_LOWER,AudioManager.STREAM_MUSIC,100);
                break;
            case "Pause":
                //Pauseme;
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
/*  this is alex's stuff for the mute and pause settings
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
*/
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
}

