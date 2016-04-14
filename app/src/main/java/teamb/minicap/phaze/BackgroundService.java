/*
 * Copyright (C) 2014 Thalmic Labs Inc.
 * Distributed under the Myo SDK license agreement. See LICENSE.txt for details.
 */

package teamb.minicap.phaze;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.session.MediaController;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;


public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    private Toast mToast;
    private MyoBinder MyoBind = new MyoBinder();
    private Activity currentActivity;
    private Hub hub;
    private Service_Music musicSrv;
    private boolean locked;
    private boolean music;
    private boolean musicPlayStatus = true;
    private boolean volume;
    private boolean zoom;
    private boolean fist=false;
    private float initial_roll;
    private float initial_pitch;
    private boolean not_first_data=false;
    private Context currentCon;
    MusicController controller;
    private AudioManager audioManager = null;
    private ImageView pictures;

    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {
        private XDirection mXDirection = XDirection.UNKNOWN;
        @Override
        public void onConnect(Myo myo, long timestamp) {
            String Connected = getString(R.string.connected) + myo.getName();
            showToast(Connected);
            myo.lock();
            locked = true;
        }
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            showToast(getString(R.string.disconnected));
        }
        @Override
        public void onLock(Myo myo, long timestamp){
            showToast("Locked");
            locked = true;
        }
        @Override
        public void onUnlock(Myo myo, long timestamp){
            showToast("Unlocked");
            locked = false;
        }

        // onPose() is called whenever the Myo detects that the person wearing it has changed their pose, for example,
        // making a fist, or not making a fist anymore.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Show the name of the pose in a toast.
            Intent intent = new Intent("my-event");

            if (pose.equals(Pose.FIST)) {
                fist = true;
            } else {
                fist = false;
                not_first_data = false;
            }

            switch(pose){
                case DOUBLE_TAP:
                    if (myo.isUnlocked()) {
                        myo.lock();
                        break;
                    }
                    myo.unlock(Myo.UnlockType.HOLD);
                    break;
                case FIST:
                    if(!locked) {
                        showToast(pose.toString());
                        if(volume){
                            volume = false;
                            zoom = true;
                        }
                        else{
                            volume = true;
                            zoom = false;
                        }

                    }
                    break;
                case WAVE_IN:
                    if(!locked) {
                        showToast(pose.toString());
                        if(!volume) {
                            intent.putExtra("message", "prev");
                            LocalBroadcastManager.getInstance(currentCon).sendBroadcast(intent);
                        }
                        else{
                            intent.putExtra("message", "rewind");
                            LocalBroadcastManager.getInstance(currentCon).sendBroadcast(intent);
                        }
                    }
                    break;
                case WAVE_OUT:
                    if(!locked) {
                        showToast(pose.toString());
                        if(!volume) {
                            intent.putExtra("message", "next");
                            LocalBroadcastManager.getInstance(currentCon).sendBroadcast(intent);
                        }
                        else{
                            intent.putExtra("message", "forward");
                            LocalBroadcastManager.getInstance(currentCon).sendBroadcast(intent);
                        }
                    }
                    break;
                case FINGERS_SPREAD:
                    if(!locked) {
                        showToast(pose.toString());
                        intent.putExtra("message", "play/pause");
                        LocalBroadcastManager.getInstance(currentCon).sendBroadcast(intent);
                    }
                    break;
                case REST:
                    break;
                case UNKNOWN:
                    break;
            }
        }

        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {

            Intent intent = new Intent("my-event");

            if(fist){
                float roll =(float) Math.toDegrees(Quaternion.roll(rotation));
                float pitch = (float) Math.toDegrees(Quaternion.pitch(rotation));

                if (myo.getXDirection() == XDirection.TOWARD_ELBOW) {
                    roll *= -1;
                   pitch *= -1;
                }

                if(not_first_data){
                    initial_roll=roll;
                    not_first_data=true;
                    initial_pitch =pitch;
                }
                else{
                    float rotation_angle = roll - initial_roll;
                    if(Math.abs(rotation_angle)>=10){
                        if(volume) {
                            if (rotation_angle > 0) {
                                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

                                initial_roll = roll;
                                initial_pitch = pitch;
                            } else {
                                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                                        AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

                                initial_roll = roll;
                                initial_pitch = pitch;
                            }
                        }
                        else{
                            if (rotation_angle > 0){
                                initial_roll = roll;
                                initial_pitch = pitch;

                                intent.putExtra("message", "zoomIn");
                                LocalBroadcastManager.getInstance(currentCon).sendBroadcast(intent);
                            }
                            else{
                                initial_roll = roll;
                                initial_pitch = pitch;

                                intent.putExtra("message", "zoomOut");
                                LocalBroadcastManager.getInstance(currentCon).sendBroadcast(intent);
                            }
                        }
                    }
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return MyoBind;
    }

    @Override
    public boolean onUnbind (Intent intent){
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // First, we initialize the Hub singleton with an application identifier.
        hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            showToast("Couldn't initialize Hub");
            stopSelf();
            return;
        }
        // Disable standard Myo locking policy. All poses will be delivered.
        hub.setLockingPolicy(Hub.LockingPolicy.NONE);
        // Next, register for DeviceListener callbacks.
        hub.addListener(mListener);
        volume = false;
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Service is gone, so unregister the listener.
        Hub.getInstance().removeListener(mListener);
        Hub.getInstance().shutdown();
    }

    private void showToast(String text) {
        Log.w(TAG, text);
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public class MyoBinder extends Binder {
        BackgroundService getService() {return BackgroundService.this;
        }
    }

    public void disconnect(){
        hub.detach(hub.getConnectedDevices().get(0).getMacAddress());
    }

    public Boolean anyDevicesConnected(){
        if (hub.getConnectedDevices() == null)
            return false;
        return true;
    }

    public void setController(MusicController m){
        controller = m;
    }

    public void musicOn(){
        music = true;
    }

    public void setCurrentCon(Context c){
        currentCon = c;
    }

    public void musicOff(){
        music = false;
    }
}

