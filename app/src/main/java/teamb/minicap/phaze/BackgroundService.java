/*
 * Copyright (C) 2014 Thalmic Labs Inc.
 * Distributed under the Myo SDK license agreement. See LICENSE.txt for details.
 */

package teamb.minicap.phaze;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.session.MediaController;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
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
    private boolean video;
    private boolean gallery;
    MusicController controller;

    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {
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
                        if (music){
                            showToast(pose.toString()+" Music");
                        }
                        else {
                            showToast(pose.toString());
                        }
                    }
                    break;
                case WAVE_IN:
                    if(!locked) {
                        showToast(pose.toString());
                        if (music)
                            musicSrv.playPrev();
                    }
                    break;
                case WAVE_OUT:
                    if(!locked) {
                        showToast(pose.toString());
                        if (music)
                            musicSrv.playNext();
                    }
                    break;
                case FINGERS_SPREAD:
                    if(!locked) {
                        showToast(pose.toString());
                        if (music) {
                            if (musicPlayStatus) {
                                musicSrv.pausePlayer();
                                musicPlayStatus = false;
                            } else {
                                musicSrv.go();
                                musicPlayStatus = true;
                            }
                        }
                    }
                    break;
                case REST:
                    break;
                case UNKNOWN:
                    break;
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
        // Finally, scan for Myo devices and connect to the first one found that is very near.
        //hub.attachToAdjacentMyo();
        //Intent intent = new Intent(this, ScanActivity.class);
        //startActivity(intent);
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

    public void musicOff(){
        music = false;
    }
}

