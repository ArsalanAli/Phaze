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
    Hub hub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int Request_Result = 1;
        if (android.os.Build.VERSION.SDK_INT >= 23){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Request_Result);
            }
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH},
                        Request_Result);
            }
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_ADMIN)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        Request_Result);
            }
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
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

        hub = Hub.getInstance();
        if (!hub.init(this)) {
            Toast.makeText(this, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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


    public DeviceListener mListener = new AbstractDeviceListener() {
        @Override
        public void onConnect(Myo myo, long timestamp) {

        }


        @Override
        public void onDisconnect(Myo myo, long timestamp) {

        }


        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
        }


        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            //mTextView.setText(R.string.hello_world);
        }


        @Override
        public void onUnlock(Myo myo, long timestamp) {

        }

        @Override
        public void onLock(Myo myo, long timestamp) {

        }
        // onPose() is called whenever a Myo provides a new pose.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Handle the cases of the Pose enumeration, and change the text of the text view
            // based on the pose we receive.
            switch (pose) {
                case REST:
                case DOUBLE_TAP:
                    myo.unlock(Myo.UnlockType.TIMED);
                case FIST:
                    music(null);
                    break;
                case WAVE_IN:
                    gallery(null);
                    break;
                case WAVE_OUT:
                    video(null);
                    break;
                case FINGERS_SPREAD:
                    settings(null);
                    break;
            }

            if (pose != Pose.UNKNOWN && pose != Pose.REST) {
                // Tell the Myo to stay unlocked until told otherwise. We do that here so you can
                // hold the poses without the Myo becoming locked.
                myo.unlock(Myo.UnlockType.HOLD);

                // Notify the Myo that the pose has resulted in an action, in this case changing
                // the text on the screen. The Myo will vibrate.
                myo.notifyUserAction();
            } else {
                // Tell the Myo to stay unlocked only for a short period. This allows the Myo to
                // stay unlocked while poses are being performed, but lock after inactivity.
                myo.unlock(Myo.UnlockType.TIMED);
            }
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_music, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.myosdk__action_scan == id) {
            onScanActionSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onScanActionSelected() {
        // Launch the ScanActivity to scan for Myos to connect to.
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

}

