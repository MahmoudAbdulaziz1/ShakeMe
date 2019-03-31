package shake.android.mahmoud.learnshake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartMyServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, HorizantelShake.class);
            context.startService(pushIntent);
        }

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            // START YOUR SERVICE HERE
            Intent pushIntent = new Intent(context, HorizantelShake.class);
            context.startService(pushIntent);

            //manifest
			  /*<action android:name="android.intent.action.PACKAGE_INSTALL" />
                <action android:name="android.intent.action.PACKAGE_ADDED" />

                <data android:scheme="package" />*/

        }
        else if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            Intent pushIntent = new Intent(context, HorizantelShake.class);
            context.startService(pushIntent);
        }
        else if (intent.getAction().equals("android.intent.action.PACKAGE_INSTALL")) {
            Intent pushIntent = new Intent(context, HorizantelShake.class);
            context.startService(pushIntent);
        }

    }
}