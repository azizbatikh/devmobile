package com.example.power_home;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class HabitatAdapter extends ArrayAdapter<Habitat> {


    Activity activity;

    List<Habitat> list;
    int itemId;

    public HabitatAdapter(Activity activity,List<Habitat> list,int itemId){

        super(activity,itemId,list);

        this.activity = activity;
        this.itemId = itemId;
        this.list = list;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View layout = convertView;

        if(convertView==null){

            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(itemId,parent,false);
        }

        int nb = 0;
        for(Appliance a : list.get(position).list){
            nb++;
        }


        LinearLayout imagequipement = layout.findViewById(R.id.imagequipement);
        TextView name = layout.findViewById(R.id.name);
        TextView floor = layout.findViewById(R.id.floor);
        TextView nbequipement = layout.findViewById(R.id.nb_equipement);


        name.setText(list.get(position).residentName);
        floor.setText(String.valueOf(list.get(position).floor));
        nbequipement.setText(String.valueOf(nb));

        imagequipement.removeAllViews();
        for(Appliance a : list.get(position).list){

            ImageView imageView = new ImageView(activity);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(   65,65));
            imageView.setImageResource(a.getApplicance());
            imageView.setPadding(5,0,5,0);
            imagequipement.addView(imageView);

        }

        return layout;

    }

}
