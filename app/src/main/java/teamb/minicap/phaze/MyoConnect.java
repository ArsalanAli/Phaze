package teamb.minicap.phaze;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyoConnect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myo_connect);
    }
    @Override
    public void onStart(){
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //this stuff is just a place holder till we can get a connnection test with the myo
        Boolean connected = prefs.getBoolean("Connected", false);
        if (connected){
            prefs.edit().putBoolean("Connected",false).commit();
        }
        else {
            prefs.edit().putBoolean("Connected", true).commit();
        }
    }
}
