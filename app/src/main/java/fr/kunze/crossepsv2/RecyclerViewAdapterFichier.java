package fr.kunze.crossepsv2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class RecyclerViewAdapterFichier extends RecyclerView.Adapter<RecyclerViewAdapterFichier.ViewHolder3> {

    Context mContext;
    ArrayList<String> mItems;

    public RecyclerViewAdapterFichier(ArrayList<String> objects, Context context) {
        mItems=objects;
        mContext=context;
    }



    @Override
    public RecyclerViewAdapterFichier.ViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.listfichiercsv,parent,false);
        RecyclerViewAdapterFichier.ViewHolder3 vh= new RecyclerViewAdapterFichier.ViewHolder3(v);
        return vh;


    }



    @Override
    public void onBindViewHolder(RecyclerViewAdapterFichier.ViewHolder3 holder, int position) {

        holder.mTextView.setText(mItems.get(position));
        holder.ouvrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fichier=holder.mTextView.getText().toString();
                File folder=new File(Environment.getExternalStorageDirectory() +
                        File.separator + "app_cross");
                File fi=new File(folder,fichier);
                Uri uri= FileProvider.getUriForFile(mContext,"fr.kunze.crossepsv2.provider",fi);

                Intent j=new Intent(Intent.ACTION_VIEW);
                j.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (fichier.endsWith("csv")) {
                    j.setDataAndType(uri,"text/csv");
                }else{
                    j.setDataAndType(uri,"application/pdf");
                }
                mContext.startActivity(j);


            }
        });
        holder.partager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fichier=holder.mTextView.getText().toString();
                File folder=new File(Environment.getExternalStorageDirectory() +
                        File.separator + "app_cross");
                File fi=new File(folder,fichier);
                Uri uri= FileProvider.getUriForFile(mContext,"fr.kunze.crossepsv2.provider",fi);

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                if (fichier.endsWith("csv")) {
                    sharingIntent.setType("text/csv");
                }else{
                    sharingIntent.setType("application/pdf");
                }
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                mContext.startActivity(sharingIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();

    }

    public static class ViewHolder3 extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public View rootView;
        public Button ouvrir;
        public Button partager;

        public ViewHolder3 (View itemView){
            super(itemView);
            rootView=itemView;
            mTextView=(TextView)itemView.findViewById(R.id.nomFichiercsv);
            ouvrir=itemView.findViewById(R.id.ouvrir);
            partager=itemView.findViewById(R.id.partager);


        }
    }

    public void removeItem(int position){

        mItems.remove(position);
        notifyItemRemoved(position);
    }
}

