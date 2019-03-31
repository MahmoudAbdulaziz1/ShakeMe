package shake.android.mahmoud.learnshake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by mahmoud on 10/11/15.
 */
public class VerShake extends Fragment {
    private ImageView imagev;
    private String getv;
    private Spinner verSpinner;
    private String spinnerResultv;
    private SharedPreferences prefs;
    private Button seclectedAppv;
    private Bitmap bitmaph;
    private String temph;
    private TextView title_sensor;
    private TextView des_sensor;
    private CheckBox vibrate;
    private CheckBox ScreenOff;
    private ImageView detailsver;
    private String[]Languages;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.ver_shake, container, false);
        imagev=(ImageView)view.findViewById(R.id.imageViewv);

        seclectedAppv=(Button)view.findViewById(R.id.selectAppv);
        seclectedAppv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AppList.class);
                startActivityForResult(intent, 1);
            }
        });



        Languages= new String[]{"Select action", "Run installed app", "Turn Screen on","Turn flashlight on/off", "Turn 3G on/off",
                "Turn screen rotation on/off","Turn Wifi on/off", "Turn Bluetooth on/off", "Turn Ringer Mode on/off","Go to home", "Recent apps list"};

        verSpinner=(Spinner)view.findViewById(R.id.servicesv);
        /*final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner, R.layout.spinner_list_item);
        adapter.setDropDownViewResource(R.layout.spinner_list_item);
        // Apply the adapter to the spinner
        verSpinner.setAdapter(adapter);*/

        verSpinner.setAdapter(new MyAdapter(getActivity(), R.layout.custom,
                Languages));
        spinnerResultv= PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString("savedValuev","");

        for(int i=0;i<11;i++)
            if(spinnerResultv.equals(verSpinner.getItemAtPosition(i).toString())){
                verSpinner.setSelection(i);
                break;
            }
        if (verSpinner.getSelectedItem().toString().equals("Run installed app")) {

            String getimage = PreferenceManager
                    .getDefaultSharedPreferences(getActivity())
                    .getString("imagevaluev", "");
            Bitmap resulBitmaph = StringToBitMap(getimage);
            imagev.setImageBitmap(resulBitmaph);
        }


        verSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //spinnerResult = adapterView.getItemAtPosition(i).toString();
                prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putString("savedValuev", verSpinner.getSelectedItem().toString());
                prefEditor.commit();
                if (verSpinner.getSelectedItem().toString().equals("Run installed app")) {
                    //Toast.makeText(adapterView.getContext(), "on on", Toast.LENGTH_LONG).show();
                    seclectedAppv.setEnabled(true);
                } else {
                    //Toast.makeText(adapterView.getContext(), "off off", Toast.LENGTH_LONG).show();

                    seclectedAppv.setEnabled(false);
                    imagev.setImageBitmap(null);
                }
                // Showing selected spinner item
                //Toast.makeText(adapterView.getContext(), verSpinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        title_sensor=(TextView)view.findViewById(R.id.titles_sensorv);
        des_sensor=(TextView)view.findViewById(R.id.dess_sensorv);


        title_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(getContext(),SensorVertical.class);
                startActivity(in);
            }
        });

        des_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getContext(), SensorVertical.class);
                startActivity(in);
            }
        });

        ScreenOff=(CheckBox)view.findViewById(R.id.checkBoxscv);
        boolean screenOffResult = PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getBoolean("ver_Screen_off_result", false);
        ScreenOff.setChecked(screenOffResult);
        ScreenOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putBoolean("ver_Screen_off_result", b);
                prefEditor.commit();

            }
        });

        vibrate=(CheckBox)view.findViewById(R.id.checkBoxvv);
        boolean vibrateResult = PreferenceManager
                .getDefaultSharedPreferences(getContext())
                .getBoolean("ver_vibrate_result", false);
        vibrate.setChecked(vibrateResult);
        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putBoolean("ver_vibrate_result", b);
                prefEditor.commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       try {
           if (requestCode == 1) {
               getv = data.getStringExtra("package");
               prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
               SharedPreferences.Editor prefEditor = prefs.edit();
               prefEditor.putString("packageValuev", getv);
               prefEditor.commit();
               Toast.makeText(getActivity(), getv, Toast.LENGTH_LONG).show();
               Bitmap id = data.getParcelableExtra("icon");
               imagev.setImageBitmap(
                       id);
               String savedImageH = BitMapToString(id);
               prefEditor.putString("imagevaluev", savedImageH);
               prefEditor.commit();
           }
       }catch (NullPointerException e){
           Toast.makeText(getActivity(), "you do not select app", Toast.LENGTH_SHORT).show();
       }
    }


    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        temph= Base64.encodeToString(b, Base64.DEFAULT);
        return temph;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            bitmaph= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmaph;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }



    //test adapter for compobox
    public class MyAdapter extends ArrayAdapter {

        public MyAdapter(Context context, int textViewResourceId,
                         String[] objects) {
            super(context, textViewResourceId, objects);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

// Inflating the layout for the custom Spinner
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom, parent, false);

// Declaring and Typecasting the textview in the inflated layout
            TextView tvLanguage = (TextView) layout
                    .findViewById(R.id.tvLanguage);

// Setting the text using the array
            tvLanguage.setText(Languages[position]);

// Setting the color of the text
            tvLanguage.setTextColor(Color.rgb(75, 180, 225));


            return layout;
        }

        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }



}
