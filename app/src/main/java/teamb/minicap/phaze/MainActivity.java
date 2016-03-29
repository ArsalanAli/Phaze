package teamb.minicap.phaze;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    Button cbutton;
    Button mbutton;
    Button vbutton;
    Button gbutton;
    Button sbutton;
    Button dcbutton;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Intent MyoIntent;
    private BackgroundService MyoSrvc;
    private Boolean MyoBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int Request_Result = 1;
        if (android.os.Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_Result);
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Request_Result);
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
        dcbutton = (Button) findViewById(R.id.disconnect);
        cbutton.setTypeface(PTfont);
        mbutton.setTypeface(PTfont);
        vbutton.setTypeface(PTfont);
        gbutton.setTypeface(PTfont);
        sbutton.setTypeface(PTfont);
        dcbutton.setTypeface(PTfont);



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private ServiceConnection MyoConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BackgroundService.MyoBinder binder = (BackgroundService.MyoBinder)service;
            //get service
            MyoSrvc = binder.getService();
            MyoBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            MyoSrvc = null;
            MyoBound = false;
        }
    };

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

    }

    @Override
    public void onResume(){
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
            dcbutton.setTextColor(ContextCompat.getColor(this, button_text));
        } else {
            cbutton.setTextColor(getResources().getColor(button_text));
            sbutton.setTextColor(getResources().getColor(button_text));
            mbutton.setTextColor(getResources().getColor(button_text));
            vbutton.setTextColor(getResources().getColor(button_text));
            gbutton.setTextColor(getResources().getColor(button_text));
            dcbutton.setTextColor(getResources().getColor(button_text));
        }
        if (Build.VERSION.SDK_INT >= 21) {
            cbutton.setBackground(getResources().getDrawable(button_active, null));
            sbutton.setBackground(getResources().getDrawable(button_active, null));
            dcbutton.setBackground(getResources().getDrawable(button_active, null));
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
            dcbutton.setBackground(getResources().getDrawable(button_active));
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
    public void connect(View view) {
        if(MyoIntent==null){
            MyoIntent = new Intent(this, BackgroundService.class);
            bindService(MyoIntent, MyoConnection, Context.BIND_AUTO_CREATE);
            startService(MyoIntent);
        }
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }
    public void disconnect(View view){
     if (MyoIntent != null){
         MyoSrvc.disconnect();
         unbindService(MyoConnection);
         stopService(MyoIntent);
         MyoIntent = null;
     }
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
    }
}

