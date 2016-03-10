package teamb.minicap.phaze;

import android.annotation.SuppressLint;
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
    Button cbutton;
    Button mbutton;
    Button vbutton;
    Button gbutton;
    Button sbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }
    @Override
    public void onResume(){
        super.onResume();
        String theme = prefs.getString("Themes", "Default");
        switch (theme) {
            case "Dark":
                //themechange(2);
                break;
            case "Light":
                themechange(1);
                break;
            case "Default":
                themechange(0);
                break;
        }
    }
    public void themechange(int i){
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
            case 1:
                findViewById(R.id.main).setBackgroundColor(Color.argb(255, 51, 181, 229));
                button_text = R.color.button_text_light;
                button_active = R.drawable.button_active_light;
                button_inactive = R.drawable.button_inactive_light;
                break;
            case 2:
                break;
        }
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            cbutton.setTextColor(ContextCompat.getColor(this,button_text));
            sbutton.setTextColor(ContextCompat.getColor(this,button_text));
            mbutton.setTextColor(ContextCompat.getColor(this,button_text));
            vbutton.setTextColor(ContextCompat.getColor(this,button_text));
            gbutton.setTextColor(ContextCompat.getColor(this,button_text));
        }
        else {
            cbutton.setTextColor(getResources().getColor(button_text));
            sbutton.setTextColor(getResources().getColor(button_text));
            mbutton.setTextColor(getResources().getColor(button_text));
            vbutton.setTextColor(getResources().getColor(button_text));
            gbutton.setTextColor(getResources().getColor(button_text));
        }
        if(android.os.Build.VERSION.SDK_INT >= 21){
            cbutton.setBackground(getResources().getDrawable(button_active, null));
            sbutton.setBackground(getResources().getDrawable(button_active, null));
            if (connected){
                mbutton.setEnabled(true);
                vbutton.setEnabled(true);
                gbutton.setEnabled(true);
                mbutton.setBackground(getResources().getDrawable(button_active,null));
                vbutton.setBackground(getResources().getDrawable(button_active,null));
                gbutton.setBackground(getResources().getDrawable(button_active,null));
            }
            else{
                mbutton.setEnabled(false);
                vbutton.setEnabled(false);
                gbutton.setEnabled(false);
                mbutton.setBackground(getResources().getDrawable(button_inactive,null));
                vbutton.setBackground(getResources().getDrawable(button_inactive,null));
                gbutton.setBackground(getResources().getDrawable(button_inactive,null));
            }
        }
        else{
            cbutton.setBackground(getResources().getDrawable(button_active));
            sbutton.setBackground(getResources().getDrawable(button_active));
            if (connected){
                mbutton.setEnabled(true);
                vbutton.setEnabled(true);
                gbutton.setEnabled(true);
                mbutton.setBackground(getResources().getDrawable(button_active));
                vbutton.setBackground(getResources().getDrawable(button_active));
                gbutton.setBackground(getResources().getDrawable(button_active));
            }
            else{
                mbutton.setEnabled(false);
                vbutton.setEnabled(false);
                gbutton.setEnabled(false);
                mbutton.setBackground(getResources().getDrawable(button_inactive));
                vbutton.setBackground(getResources().getDrawable(button_inactive));
                gbutton.setBackground(getResources().getDrawable(button_inactive));
            }
        }
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

