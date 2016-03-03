package teamb.minicap.phaze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Typeface PTfont = Typeface.createFromAsset(getAssets(), "Prototype.ttf");
        Button cbutton = (Button) findViewById(R.id.connect);
        Button mbutton = (Button) findViewById(R.id.music);
        Button vbutton = (Button) findViewById(R.id.video);
        Button gbutton = (Button) findViewById(R.id.gallery);
        Button sbutton = (Button) findViewById(R.id.settings);
        cbutton.setTypeface(PTfont);
        mbutton.setTypeface(PTfont);
        vbutton.setTypeface(PTfont);
        gbutton.setTypeface(PTfont);
        sbutton.setTypeface(PTfont);
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
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean connected = prefs.getBoolean("Connected", false);
        if (connected == true) {
            findViewById(R.id.music).setBackground(getResources().getDrawable(R.drawable.button_active, null));
            findViewById(R.id.video).setBackground(getResources().getDrawable(R.drawable.button_active, null));
            findViewById(R.id.gallery).setBackground(getResources().getDrawable(R.drawable.button_active, null));
        } else {
            findViewById(R.id.music).setBackground(getResources().getDrawable(R.drawable.button_inactive,null));
            findViewById(R.id.video).setBackground(getResources().getDrawable(R.drawable.button_inactive,null));
            findViewById(R.id.gallery).setBackground(getResources().getDrawable(R.drawable.button_inactive,null));
        }
        String theme = prefs.getString("Theme", "Default");
        switch (theme) {
            case "Dark":

                break;
            case "Light":

                break;
            case "Default":

                break;
        }
    }

    public void connect(View view) {
        Intent intent = new Intent(MainActivity.this, MyoConnect.class);
        startActivity(intent);
    }

    public void music(View view) {
        Boolean connected = prefs.getBoolean("Connected", false);
        if (connected == true) {
            Intent intent = new Intent(MainActivity.this, Music.class);
            startActivity(intent);
        }
    }

    public void video(View view) {
        Boolean connected = prefs.getBoolean("Connected", false);
        if (connected == true) {
            Intent intent = new Intent(MainActivity.this, Video.class);
            startActivity(intent);
        }
    }

    public void gallery(View view) {
        Boolean connected = prefs.getBoolean("Connected", false);
        if (connected == true) {
            Intent intent = new Intent(MainActivity.this, Gallery.class);
            startActivity(intent);
        }
    }

    public void settings(View view) {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
    }
}

