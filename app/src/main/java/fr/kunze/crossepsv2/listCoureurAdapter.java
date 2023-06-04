package fr.kunze.crossepsv2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Julien on 19/11/2016.
 */

public class listCoureurAdapter extends ArrayAdapter<Coureur> {

    private LayoutInflater mInflater = null;

    public listCoureurAdapter(Context context, int resource, List<Coureur> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public Coureur getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Viewholder holder;
        if(convertView==null){

            convertView=mInflater.inflate(R.layout.liste_coureur,null);
            holder=new Viewholder();
            holder.place=(TextView)convertView.findViewById(R.id.place1);
            holder.temps=(TextView)convertView.findViewById(R.id.temps);
            holder.nom=(TextView)convertView.findViewById(R.id.nom);

            convertView.setTag(holder);
        }else{
            holder=(Viewholder)convertView.getTag();
        }

        Coureur c=getItem(position);
        if (c!=null){
            holder.place.setText(String.valueOf(c.getPlace()));
            holder.nom.setText(c.getNom());
            holder.temps.setText(c.getTemps());
        }
        return convertView;

    }
}
