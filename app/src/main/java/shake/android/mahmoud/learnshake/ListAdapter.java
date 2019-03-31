package shake.android.mahmoud.learnshake;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mahmoud on 10/12/15.
 */
public class ListAdapter extends ArrayAdapter<Item>{

    private Intent in;

    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    private LayoutInflater inflater;
    private SharedPreferences prefs;

    public ListAdapter(Context context, ArrayList<Item> itemsArrayList) {
        super(context,R.layout.list_layout ,itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }




    @Override
    public int getCount() {

        return itemsArrayList.size();

    }
    @Override
    public Item getItem(int position) {

        return itemsArrayList.get(position);

    }
    @Override
    public long getItemId(int position) {

        return position;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //in = new Intent(getContext(), HorizantelShake.class);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;
        ViewHolder holder;

        int type = getItemViewType(position);
        if (v == null) {
            holder = new ViewHolder();
            if (type == 0) {
                v = inflater.inflate(R.layout.list_layout, null);

                holder.titleOne = (TextView) v.findViewById(R.id.headline);
                holder.desOne=(TextView)v.findViewById(R.id.details);
            }else if(type==1) {
                v = inflater.inflate(R.layout.second_list, null);

                holder.titleTwo = (TextView) v.findViewById(R.id.headline1);
                holder.desTwo=(TextView)v.findViewById(R.id.details1);
                holder.serv=(Switch)v.findViewById(R.id.Service);
                in=new Intent(getContext(),HorizantelShake.class);
            }

            holder.type = type;
            v.setTag(holder);
        }else {
            holder = (ViewHolder) v.getTag();
            in=new Intent(getContext(),HorizantelShake.class);
            Log.d("Adapter test", " holder ::" + holder);
        }

        Item item = (Item) getItem(position);
        if (item != null) {

            if (type == 0) {
                holder.titleOne.setText(item.getTitle());
                holder.desOne.setText(item.getDescription());
            } else if(type==1){
                holder.titleTwo.setText(item.getTitle());
                holder.desTwo.setText(item.getDescription());
                //SharedPreferences prefs = context.getSharedPreferences("com.mobileapp.smartapplocker", null);
                boolean switchState = PreferenceManager
                        .getDefaultSharedPreferences(getContext())
                        .getBoolean("service_status", false);
                //Toast.makeText(getContext(), "on"+switchState, Toast.LENGTH_LONG).show();
                if(switchState){
                    holder.serv.setChecked(true);
                    //context.startService(in);
                } else {
                    holder.serv.setChecked(false);
                   // context.stopService(in);
                }

                //final Intent in=new Intent(getContext(),HorizantelShake.class);
                holder.serv.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                       // in = new Intent(getContext(), HorizantelShake.class);
                        if (b) {
                            // Toast.makeText(getContext(), "on", Toast.LENGTH_LONG).show();

                            context.startService(in);

                        } else {
                            //Toast.makeText(getContext(), "of", Toast.LENGTH_LONG).show();
                            //in = new Intent(getContext(), HorizantelShake.class);
                            context.stopService(in);
                        }
                        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor prefEditor = prefs.edit();
                        prefEditor.putBoolean("service_status", b);
                        prefEditor.commit();
                    }
                });

            }

        }



        return v;

    }
    int i=1;
    @Override
    public int getItemViewType(int position) {

        String itemss = getItem(position).getTitle();
        //Toast.makeText(getContext(), itemss + i +" "+getItemId(1), Toast.LENGTH_LONG).show();
        i+=1;
        if (itemss=="Service State"&& getItemId(1)==1)
            return 1;
        else if(itemss=="Vibrate when shaken"|| itemss=="Active when Screen is off")
            return  2;
        else
            return 0;

        //return 0;
    }



    @Override
    public int getViewTypeCount() {

        return 3;

    }

}
