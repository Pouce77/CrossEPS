package fr.kunze.crossepsv2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.Comparator;
import java.util.List;

/**
 * L'adaptateur spécifique à nos fichiers
 */

public class FileAdapter extends ArrayAdapter<File> {

    private class FileComparator implements Comparator<File> {
        public int compare(File lhs, File rhs) {
            // Si lhs est un répertoire et pas l'autre, il est plus petit
            if(lhs.isDirectory() && rhs.isFile())
                return -1;
            // Dans le cas inverse, il est plus grand
            if(lhs.isFile() && rhs.isDirectory())
                return 1;

            // Enfin, on ordonne en fonction de l'ordre alphabétique sans tenir compte de la casse
            return lhs.getName().compareToIgnoreCase(rhs.getName());
        }
    }

    public FileAdapter(Context context, int textViewResourceId, List<File> objects) {
        super(context, textViewResourceId, objects);
        mInflater = LayoutInflater.from(context);
    }

    private LayoutInflater mInflater = null;

    /**
     * Construit la vue en fonction de l'item
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView vue = null;

        if(convertView != null)
            // On recycle
            vue = (TextView) convertView;
        else
            // On inflate
            vue = (TextView) mInflater.inflate(android.R.layout.simple_list_item_1, null);

        File item = getItem(position);
        //Si c'est un répertoire, on choisit la couleur dans les préférences
        if(item.isDirectory())
            vue.setTextColor(Color.RED);
        else
            // Sinon, c'est du noir
            vue.setTextColor(Color.BLACK);

        vue.setText(item.getName());
        return vue;
    }
    public void sort () {
        super.sort(new FileComparator());
    }


}