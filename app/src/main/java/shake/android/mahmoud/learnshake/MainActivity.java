package shake.android.mahmoud.learnshake;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener{

  /*  //////////////
    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private ApplicationAdapter listadaptor = null;
/////////////////////////*/

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
    private static final float ERROR = (float) 7.0;
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

    private ViewPager viewPager;
    private ActionBar actionBar;
    private TabsFragmentPagerAdapter tabsAdapter;
    private String[] days = new String[]{"Horizontal","Vertical","Forward"};



    private  boolean mobileDataEnabled = false; // Assume disabled

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabsAdapter = new TabsFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabsAdapter);
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (String tab_name : days) {
            TextView tv = (TextView) inflater.inflate(R.layout.wswipe, null);
            tv.setText(tab_name);
            actionBar.addTab(actionBar.newTab().setCustomView(tv).setTabListener(this));
        }

        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setDisplayShowHomeEnabled(false);

        /*for(int i=0; i<3; i++){
            actionBar.addTab(actionBar.newTab().setText(days[i]).setTabListener(this));
        }*/
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg) {
                // TODO Auto-generated method stub
                actionBar.setSelectedNavigationItem(arg);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

    }







    //Register the Listener when the Activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        //mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    //Unregister the Listener when the Activity is paused
    protected void onPause() {
        super.onPause();
        //mSensorManager.unregisterListener(this);
        if(camera!=null){
            camera.release();
            camera=null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction arg1) {
        // TODO Auto-generated method stub
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }


}