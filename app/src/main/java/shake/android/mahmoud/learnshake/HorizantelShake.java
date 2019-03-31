package shake.android.mahmoud.learnshake;

import android.app.KeyguardManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by mahmoud on 10/10/15.
 */
public class HorizantelShake extends Service implements SensorEventListener {

    private Camera camera;
    private Camera.Parameters p;
    private boolean isLighOn = false;
    private boolean screenOn=false;
    private PowerManager powermanager;
    private PowerManager.WakeLock wakeLock;
    private int count1=0;
    int count = 1;
    private boolean init;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private float x1, x2, x3;
    private static final float ERROR = (float) 17.0;
    private TextView counter;
    private WifiManager wifi;
    private  String provider;
    private ConnectivityManager conman;
    private Field iConnectivityManagerField;
    private Object iConnectivityManager;
    private Class iConnectivityManagerClass;
    private Method setMobileDataEnabledMethod;
    private boolean misEnabled;
    private Class conmanClass;
    ImageView v;
    private String get;
    private Spinner horzSpinner;
    private String spinnerResult;
    private SharedPreferences prefs;
    private Button seclectedApp;
    private boolean vibrateState;
    private boolean screenState;
    private KeyguardManager.KeyguardLock kl;
    private KeyguardManager km;
    private PowerManager powerManagerLock;
    private PowerManager.WakeLock wakeLockLock;
    private float horz_Error;
    private float ver_Error;
    private float for_Error;
    private boolean verVibrateResult;
    private boolean verScreenOffResult;
    private boolean horVibrateResult;
    private boolean horScreenOffResult;
    private boolean forVibrateResult;
    private boolean forScreenOffResult;


    @Override
    public void onCreate() {
        super.onCreate();

        //if(camera==null) {
        camera = Camera.open();
        p = camera.getParameters();
        powermanager=  ((PowerManager)getApplicationContext().getSystemService(Context.POWER_SERVICE));
        wakeLock=powermanager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        km = (KeyguardManager) getApplicationContext()
                .getSystemService(Context.KEYGUARD_SERVICE);

        kl = km
                .newKeyguardLock("MyKeyguardLock");
        powerManagerLock = (PowerManager) getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        wakeLockLock =powerManagerLock.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");







        screenState=PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("screen_result", false);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> listOfSensorsOnDevice = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < listOfSensorsOnDevice.size(); i++) {
            if (listOfSensorsOnDevice.get(i).getType() == Sensor.TYPE_ACCELEROMETER) {

                //Toast.makeText(this, "ACCELEROMETER sensor is available on device", Toast.LENGTH_SHORT).show();


                init = false;

                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

            } else {

                //Toast.makeText(this, "ACCELEROMETER sensor is NOT available on device", Toast.LENGTH_SHORT).show();
            }
        }



    }


    @Override
    public void onSensorChanged(SensorEvent e) {

        horz_Error=PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getFloat("seekbar", 17);
        ver_Error=PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getFloat("seekbarv", 17);
        for_Error=PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getFloat("seekbarf", 17);

        verVibrateResult = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("ver_vibrate_result", false);
        verScreenOffResult = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("ver_Screen_off_result", false);
        horVibrateResult=PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("hor_vibrate_result", false);
        horScreenOffResult = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("hor_Screen_off_result", false);



        forScreenOffResult = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("for_Screen_off_result", false);

        forVibrateResult=PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("for_vibrate_result", false);

            //Get x,y and z values
            float x, y, z;
            x = e.values[0];
            y = e.values[1];
            z = e.values[2];


            if (!init) {
                x1 = x;
                x2 = y;
                x3 = z;
                init = true;
            } else {

                float diffX = Math.abs(x1 - x);
                float diffY = Math.abs(x2 - y);
                float diffZ = Math.abs(x3 - z);

                //Handling ACCELEROMETER Noise
                if (diffX < horz_Error) {

                    diffX = (float) 0.0;
                }
                if (diffY < ver_Error) {
                    diffY = (float) 0.0;
                }
                if (diffZ < for_Error) {

                    diffZ = (float) 0.0;
                }


                x1 = x;
                x2 = y;
                x3 = z;


                //Horizontal Shake Detected!
                if (diffX > diffY) {

                    if(horVibrateResult){
                        vibrate();
                    }
                    if(horScreenOffResult){
                        //screenTurnOn();
                        unlockDevice();
                    }
                    switch (PreferenceManager
                            .getDefaultSharedPreferences(getApplication())
                            .getString("savedValueh","")){
                        case "Select action":




                           // Read more: http://www.androidhub4you.com/2013/07/how-to-unlock-android-phone.html#ixzz3oHbAo6nG


                        break;
                        case "Run installed app":

                            try {
                                Intent intent = getPackageManager()
                                        .getLaunchIntentForPackage(PreferenceManager
                                        .getDefaultSharedPreferences(getApplication())
                                        .getString("packageValueh", ""));
                                Toast.makeText(getApplicationContext(),PreferenceManager
                                        .getDefaultSharedPreferences(getApplication())
                                        .getString("packageValueh", ""),Toast.LENGTH_LONG).show();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                intent.setAction("android.intent.action.ACTION_REQUEST_");

                                if (null != intent) {
                                    startActivity(intent);
                                }
                        } catch (ActivityNotFoundException e1) {
                        Toast.makeText(getApplicationContext(), e1.getMessage(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e1) {
                        Toast.makeText(getApplicationContext(), e1.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                    break;
                        case "Turn Screen on":
                            if(powermanager.isScreenOn())
                            {
                                screenTurnOff();
                            }else {
                                screenTurnOn();
                            }
                            break;
                        case "Turn flashlight on/off":
                            if(isLighOn){
                                flashTurnOff();
                            }else {
                                flashTurnOn();
                            }
                            break;
                        case "Turn 3G on/off":
                            if(isConnected(getApplicationContext())){
                                setMobileDataEnabledd(getApplicationContext(),false);
                            }else {
                                setMobileDataEnabledd(getApplicationContext(),true);
                            }
                            break;
                        case "Turn screen rotation on/off":
                            if (android.provider.Settings.System.getInt(getContentResolver(),
                                    Settings.System.ACCELEROMETER_ROTATION, 0) == 1){
                                android.provider.Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0);
                            }else {
                                android.provider.Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 1);
                            }
                            break;
                        case "Turn Wifi on/off":
                            if(wifi.isWifiEnabled())
                            {
                                wifiDisable();
                            }else {
                                wifiEnable();
                            }
                            break;
                        case "Turn Bluetooth on/off":
                            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if(bluetoothAdapter.isEnabled())
                            {
                                bluetoothAdapter.disable();
                            }else {
                                bluetoothAdapter.enable();
                            }
                            break;
                        case "Turn Ringer Mode on/off":
                            switchRingerMode();
                            break;
                        case "Go to home":
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            break;

                        case "Recent apps list":
                            Intent intent = new Intent ("com.android.systemui.recent.action.TOGGLE_RECENTS");
                            intent.setComponent (new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity"));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;

                        /*case "Number dialar bad":
                            Intent i = new Intent(Intent.ACTION_DIAL);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            break;*/

                        default:
                            break;
                    }



                }

                if (diffY > diffX) {
                    if(verVibrateResult){
                        vibrate();
                    }
                    if(verScreenOffResult){
                        //screenTurnOn();
                        unlockDevice();
                    }
                    switch (PreferenceManager
                            .getDefaultSharedPreferences(getApplication())
                            .getString("savedValuev","")){
                        case "Select action":

                            break;
                        case "Run installed app":
                            try {
                                Intent intent = getPackageManager()
                                        .getLaunchIntentForPackage(PreferenceManager
                                                .getDefaultSharedPreferences(getApplication())
                                                .getString("packageValuev", ""));
                                Toast.makeText(getApplicationContext(),PreferenceManager
                                        .getDefaultSharedPreferences(getApplication())
                                        .getString("packageValuev", ""),Toast.LENGTH_LONG).show();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                                if (null != intent) {
                                    startActivity(intent);
                                }
                            } catch (ActivityNotFoundException e1) {
                                Toast.makeText(getApplicationContext(), e1.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } catch (Exception e1) {
                                Toast.makeText(getApplicationContext(), e1.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "Turn Screen on":
                            if(powermanager.isScreenOn())
                            {
                                screenTurnOff();
                            }else {
                                screenTurnOn();
                            }
                            break;
                        case "Turn flashlight on/off":
                            if(isLighOn){
                                flashTurnOff();
                            }else {
                                flashTurnOn();
                            }
                            break;
                        case "Turn 3G on/off":
                            if(isConnected(getApplicationContext())){
                                setMobileDataEnabledd(getApplicationContext(), false);
                            }else {
                                setMobileDataEnabledd(getApplicationContext(), true);
                            }
                            break;
                        case "Turn screen rotation on/off":
                            if (android.provider.Settings.System.getInt(getContentResolver(),
                                    Settings.System.ACCELEROMETER_ROTATION, 0) == 1){
                                android.provider.Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
                            }else {
                                android.provider.Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);
                            }
                            break;
                        case "Turn Wifi on/off":
                            if(wifi.isWifiEnabled())
                            {
                                wifiDisable();
                            }else {
                                wifiEnable();
                            }
                            break;
                        case "Turn Bluetooth on/off":
                            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if(bluetoothAdapter.isEnabled())
                            {
                                bluetoothAdapter.disable();
                            }else {
                                bluetoothAdapter.enable();
                            }
                            break;
                        case "Turn Ringer Mode on/off":
                            switchRingerMode();
                            break;
                        case "Go to home":
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            /*ActivityManager manager =       (ActivityManager)getSystemService(ACTIVITY_SERVICE);
                            final List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
                            for (ActivityManager.RunningAppProcessInfo process: runningProcesses) {
                                manager.killBackgroundProcesses(process.processName);
                            }*/
                            break;
                        case "Recent apps list":
                            Intent intent = new Intent ("com.android.systemui.recent.action.TOGGLE_RECENTS");
                            intent.setComponent (new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity"));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;

                        /*case "Number dialar bad":
                            Intent i = new Intent(Intent.ACTION_DIAL);
                            startActivity(i);
                            break;*/

                        default:
                            break;
                    }


                }


                if (diffZ > diffX && diffZ > diffY) {
                    if(forVibrateResult){
                        vibrate();
                    }
                    if(forScreenOffResult){
                        //screenTurnOn();
                        unlockDevice();
                    }
                    switch (PreferenceManager
                            .getDefaultSharedPreferences(getApplication())
                            .getString("savedValuef","")){
                        case "Select action":

                            break;
                        case "Run installed app":
                            try {
                                Intent intent = getPackageManager()
                                        .getLaunchIntentForPackage(PreferenceManager
                                                .getDefaultSharedPreferences(getApplication())
                                                .getString("packageValuef", ""));
                                //Toast.makeText(getApplicationContext(),PreferenceManager
                                    //    .getDefaultSharedPreferences(getApplication())
                                      //  .getString("packageValuef", ""),Toast.LENGTH_LONG).show();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                                if (null != intent) {
                                    startActivity(intent);
                                }
                            } catch (ActivityNotFoundException e1) {
                                Toast.makeText(getApplicationContext(), e1.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            } catch (Exception e1) {
                                Toast.makeText(getApplicationContext(), e1.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "Turn Screen on/off":
                            if(powermanager.isScreenOn())
                            {
                                screenTurnOff();
                            }else {
                                screenTurnOn();
                            }
                            break;
                        case "Turn flashlight on":
                            if(isLighOn){
                                flashTurnOff();
                            }else {
                                flashTurnOn();
                            }
                            break;
                        case "Turn 3G on/off":
                            if(isConnected(getApplicationContext())){
                                setMobileDataEnabledd(getApplicationContext(),false);
                            }else {
                                setMobileDataEnabledd(getApplicationContext(),true);
                            }
                            break;
                        case "Turn screen rotation on/off":
                            if (android.provider.Settings.System.getInt(getContentResolver(),
                                    Settings.System.ACCELEROMETER_ROTATION, 0) == 1){
                                android.provider.Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0);
                            }else {
                                android.provider.Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 1);
                            }
                            break;
                        case "Turn Wifi on/off":
                            if(wifi.isWifiEnabled())
                            {
                                wifiDisable();
                            }else {
                                wifiEnable();
                            }
                            break;
                        case "Turn Bluetooth on/off":
                            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if(bluetoothAdapter.isEnabled())
                            {
                                bluetoothAdapter.disable();
                            }else {
                                bluetoothAdapter.enable();
                            }
                            break;
                        case "Turn Ringer Mode on/off":
                            switchRingerMode();
                            break;
                        case "Go to home":
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            /*ActivityManager manager =       (ActivityManager)getSystemService(ACTIVITY_SERVICE);
                            final List<ActivityManager.RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();
                            for (ActivityManager.RunningAppProcessInfo process: runningProcesses) {
                                manager.killBackgroundProcesses(process.processName);
                            }*/

                            break;

                        case "Recent apps list":
                            Intent intent = new Intent ("com.android.systemui.recent.action.TOGGLE_RECENTS");
                            intent.setComponent (new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity"));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            break;
                        /*case "Number dialar bad":
                            Intent i = new Intent(Intent.ACTION_DIAL);
                            startActivity(i);
                            break;*/

                        default:

                            break;
                    }

                }

            }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void vibrate()
    {
        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        vibe.vibrate(100);
    }
    private void flashTurnOff()
    {
        //p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(p);
        camera.stopPreview();
        isLighOn = false;
        if(camera!=null){
            camera.release();
            camera=null;
            camera=Camera.open();
        }
    }
    private void flashTurnOn() {
       // p = camera.getParameters();
        p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(p);
        camera.startPreview();
        isLighOn = true;
    }
    private void screenTurnOn()
    {
        wakeLock.acquire();
        screenOn=true;
    }
    private void screenTurnOff()
    {
        if(wakeLock.isHeld())
        {
            Toast.makeText(getApplicationContext(), "make of", Toast.LENGTH_LONG).show();
            wakeLock.release();

            Toast.makeText(getApplicationContext(),"55555555555",Toast.LENGTH_LONG).show();

        }
        if(wakeLockLock.isHeld()){
            wakeLockLock.release();
        }

        screenOn=false;
    }
    public void wifiEnable()
    {
        //WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
    }
    public void wifiDisable()
    {
        wifi.setWifiEnabled(false);
    }
    public void switchRingerMode()
    {
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_NORMAL:
                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            case AudioManager.RINGER_MODE_SILENT:
                Log.i("MyApp", "Silent mode");
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                break;
        }
    }

    public static void setAutoOrientationEnabled(Context context, boolean enabled)
    {
        Settings.System.putInt( context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }


    private void setMobileDataEnabledd(Context context, boolean enabled) {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass;
        try {
            conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);

            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            // android.net.NetworkInfo wifi = cm
            //         .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnected()))
                //|| (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }
    private void unlockDevice()
    {


        kl.disableKeyguard();

        wakeLockLock.acquire();


    }

    private void lockDevice()
    {


        kl.reenableKeyguard();


        if(wakeLockLock.isHeld())
        wakeLockLock.release();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        if(camera!=null){
            camera.release();
            camera=null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);

    }
}
