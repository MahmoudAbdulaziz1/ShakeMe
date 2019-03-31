package shake.android.mahmoud.learnshake;

import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by mahmoud on 10/12/15.
 */
public class ViewHolder {
    public int type;
    TextView titleOne;
    TextView desOne;
    TextView titleTwo;
    TextView desTwo;
    Switch serv;


    @Override
    public String toString() {
        return "ViewHolder [type=" + type + ", txtOne=" + titleOne
                + ", txtTwo=" + titleTwo + ", txtThree=" + desOne + "txt4"+desTwo+"sw"+serv+"c"+"]";

    }



}

