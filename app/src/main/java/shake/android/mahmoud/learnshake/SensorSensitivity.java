package shake.android.mahmoud.learnshake;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * Created by mahmoud on 10/17/15.
 */
public class SensorSensitivity extends Activity {

    private SeekBar seek;
    private TextView value;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.senor);


        value=(TextView)findViewById(R.id.valueSeek);

        seek=(SeekBar)findViewById(R.id.seekBar);
        float val=PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getFloat("seekbar", 17);
        seek.setProgress((int)val);
        value.setText(seek.getProgress() + "");
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                value.setText(i + "");
                prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putFloat("seekbar", i);
                prefEditor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button reset=(Button)findViewById(R.id.button);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seek.setProgress(17);
            }
        });
    }
}
