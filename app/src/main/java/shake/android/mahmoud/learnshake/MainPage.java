package shake.android.mahmoud.learnshake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by mahmoud on 10/11/15.
 */
public class MainPage extends Activity {

    private ListAdapter adapter;
    private boolean switchState;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        intent=new Intent(MainPage.this,HorizantelShake.class);
        ListView list=(ListView)findViewById(R.id.list);
    // 1. pass context and data to the custom adapter
        adapter = new ListAdapter(this, generateData());

        //2. setListAdapter
        //setListAdapter(adapter);
       list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Item itemss = adapter.getItem(i);
                if (itemss.getTitle() == "Add action") {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });
        switchState = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("service_status", false);
        if(switchState){
            //Toast.makeText(getApplicationContext(),"ser cre",Toast.LENGTH_LONG).show();
            startService(intent);
        }else {
           // Toast.makeText(getApplicationContext(),"stop cre",Toast.LENGTH_LONG).show();

            stopService(intent);
        }
    }

    private ArrayList<Item> generateData(){
        ArrayList<Item> items = new ArrayList<Item>();
        items.add(new Item("Add action","Click to set yor actions"));
        items.add(new Item("Service State","on/off service"));



        return items;
    }

    @Override
    protected void onResume() {
        super.onResume();


    }
}
