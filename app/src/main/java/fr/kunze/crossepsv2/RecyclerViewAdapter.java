package fr.kunze.crossepsv2;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Julien on 22/11/2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> mItems;
    CoureurDAO base;
    public RecyclerViewAdapter(ArrayList<String> objects, Context context) {
        mItems=objects;
        mContext=context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_course,parent,false);
        ViewHolder vh= new ViewHolder(v);
        return vh;


    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        base=new CoureurDAO(mContext);
        base.openDb();
        ArrayList<HashMap<String, String>> listCoureur=base.getListeCoureur(mItems.get(position));
        SimpleAdapter eadapter = new SimpleAdapter(mContext, listCoureur, R.layout.liste_item_perso, new String[]{"place", "nom"}, new int[]{R.id.textView3, R.id.textView4});
        holder.mListView.setAdapter(eadapter);
        holder.mListView.setEnabled(false);
        holder.mTextView.setText(mItems.get(position));
        base.closeDb();


    }

    @Override
    public int getItemCount() {
        return mItems.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public ListView mListView;
        public View rootView;


        public ViewHolder (View itemView){
            super(itemView);
            rootView=itemView;
            mTextView=(TextView)itemView.findViewById(R.id.nomCourse);
            mListView=(ListView)itemView.findViewById(R.id.listCoureur);

        }
    }

    public void removeItem(int position){

        mItems.remove(position);
        notifyItemRemoved(position);
    }

}
