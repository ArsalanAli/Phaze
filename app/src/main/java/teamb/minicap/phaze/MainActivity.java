package teamb.minicap.phaze;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int Request_Result = 1;
        if (android.os.Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
        String theme = prefs.getString("Theme", "Default");
        /*switch (theme) {
            case "Dark":
                prefs.edit().putInt("button_text").commit();
                prefs.edit().putInt("inactive_color").commit();
                prefs.edit().putInt("active_color").commit();
                prefs.edit().putInt("background").commit();
                break;
            case "Light":
                prefs.edit().putLong("button_text", 0xFF33b5e5).commit();
                prefs.edit().putInt("inactive_color", 0xFF8f9393).commit();
                prefs.edit().putInt("active_color", 0xFF128db6).commit();
                prefs.edit().putInt("background", 0xFF33b5e5).commit();
                break;
            case "Default":
                prefs.edit().putInt("button_text",0xFF000000).commit();
                prefs.edit().putInt("inactive_color",0xFF535353).commit();
                prefs.edit().putInt("active_color",0xFFcacbcb).commit();
                prefs.edit().putInt("background",0xFF424445).commit();
                break;
        }*/
        Boolean connected = prefs.getBoolean("Connected", false);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            if (connected) {
                mbutton.setEnabled(true);
                vbutton.setEnabled(true);
                gbutton.setEnabled(true);
                findViewById(R.id.music).setBackground(getResources().getDrawable(R.drawable.button_active, null));
                findViewById(R.id.video).setBackground(getResources().getDrawable(R.drawable.button_active, null));
                findViewById(R.id.gallery).setBackground(getResources().getDrawable(R.drawable.button_active, null));
            } else {
                mbutton.setEnabled(false);
                vbutton.setEnabled(false);
                gbutton.setEnabled(false);
                findViewById(R.id.music).setBackground(getResources().getDrawable(R.drawable.button_inactive, null));
                findViewById(R.id.video).setBackground(getResources().getDrawable(R.drawable.button_inactive, null));
                findViewById(R.id.gallery).setBackground(getResources().getDrawable(R.drawable.button_inactive, null));
            }
        }
        else{
            if (connected) {
                mbutton.setEnabled(true);
                vbutton.setEnabled(true);
                gbutton.setEnabled(true);
                findViewById(R.id.music).setBackground(getResources().getDrawable(R.drawable.button_active));
                findViewById(R.id.video).setBackground(getResources().getDrawable(R.drawable.button_active));
                findViewById(R.id.gallery).setBackground(getResources().getDrawable(R.drawable.button_active));
            } else {
                mbutton.setEnabled(false);
                vbutton.setEnabled(false);
                gbutton.setEnabled(false);
                findViewById(R.id.music).setBackground(getResources().getDrawable(R.drawable.button_inactive));
                findViewById(R.id.video).setBackground(getResources().getDrawable(R.drawable.button_inactive));
                findViewById(R.id.gallery).setBackground(getResources().getDrawable(R.drawable.button_inactive));
            }
        }
    }
    @Override
    public void onResume(){
        super.onResume();


    }

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

}

