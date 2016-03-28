package teamb.minicap.phaze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by Alexandre on 26/03/2016.
 * this is my class for receiving the head set plugging in / unplug action and what to do with it
 *
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 * THIS MAY NO LONGER BE NCESSARY BUT KEEPING IT INCASE WE HAVE TO DO MORE WORK ON BROADCASTS, AND CUSTOM RECEIVERS
 *
 */

public class HeadsetPlugReceiver extends BroadcastReceiver {

    private AudioManager audioManager = null;

    @Override
    public void onReceive (Context context, Intent intent){

        /*

        //audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        //do something when intent is received
        //Mute or Pause
        //this may need to b moved to the onResume or elsewhere
        //must be tested
        //error on the pause fcn maybe being called too early?
        //instead of if, maybe for loops, that way they keep running, unless there is another way for android to monitor the unplug action, make it act like an interupt.
        String on_unplug = prefs.getString("headset_on_unplug", "Nothing");
        switch (on_unplug) {
            case "Nothing":
                //do nothing
                break;
            case "Mute"://will this work? or will it just mute/pause when playing on outer speaker, i need to to be on the unplug action not if it is unpluged
                //if(musicSrv!=null && musicBound)
                    if(!audioManager.isWiredHeadsetOn())
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,AudioManager.FLAG_VIBRATE);
                Toast.makeText(context, "inside the mute loop", Toast.LENGTH_LONG).show();
                break;
            case "Pause":
                //if(musicSrv!=null && musicBound)
                    if(!audioManager.isWiredHeadsetOn());
                //pause();
                //why cant i call this function?
                //do this instead?
                //playbackPaused=true;
                //musicSrv.pausePlayer();
                Toast.makeText(context,"inside the pause loop",Toast.LENGTH_LONG).show();
                break;
        }
    */

    Toast.makeText(context,"gotcha intent",Toast.LENGTH_LONG).show();
    }
    public void HeadsetPlugReceiver(){};
}

