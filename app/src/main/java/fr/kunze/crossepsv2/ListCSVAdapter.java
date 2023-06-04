package fr.kunze.crossepsv2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;

public class ListCSVAdapter extends ArrayAdapter<String> {

    private LayoutInflater mInflater = null;

    public ListCSVAdapter(@NonNull Context context, int resource,ArrayList <String> arrayList) {
        super(context, resource, arrayList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public String getItem(int position) {
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


    @SuppressLint("WrongViewCast")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Viewholder2 holder;
        if(convertView==null){

            convertView=mInflater.inflate(R.layout.listfichiercsv,null);
            holder=new Viewholder2();
            holder.nomFichier=(TextView)convertView.findViewById(R.id.nomFichiercsv);
            holder.ouvrir=(Button)convertView.findViewById(R.id.ouvrir);
            holder.partager=(Button)convertView.findViewById(R.id.partager);
            holder.nomFichier.setText(getItem(position));

            holder.ouvrir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String fichier=holder.nomFichier.getText().toString();
                    File folder=new File(Environment.getExternalStorageDirectory() +
                            File.separator + "app_cross");
                    File fi=new File(folder,fichier);
                    Uri uri= FileProvider.getUriForFile(getContext(),"fr.kunze.crossepsv2.provider",fi);

                    Intent j=new Intent(Intent.ACTION_VIEW);
                    j.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (fichier.endsWith("csv")) {
                        j.setDataAndType(uri,"text/csv");
                    }else{
                        j.setDataAndType(uri,"application/pdf");
                    }
                    getContext().startActivity(j);

                }
            });

            holder.partager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String fichier=holder.nomFichier.getText().toString();
                    File folder=new File(Environment.getExternalStorageDirectory() +
                            File.separator + "app_cross");
                    File fi=new File(folder,fichier);
                    Uri uri= FileProvider.getUriForFile(getContext(),"fr.kunze.crossepsv2.provider",fi);

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    if (fichier.endsWith("csv")) {
                        sharingIntent.setType("text/csv");
                    }else{
                        sharingIntent.setType("application/pdf");
                    }
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    getContext().startActivity(sharingIntent);

                }
            });

            convertView.setTag(holder);
        }else{
            holder=(Viewholder2)convertView.getTag();
        }

        return convertView;

    }

}
