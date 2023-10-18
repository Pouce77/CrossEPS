package fr.kunze.crossepsv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Julien on 19/11/2016.
 */

public class CoureurDAO {
    protected final static int VERSION = 1;
    protected final static String NAME = "basecrosseps.db";
    protected SQLiteDatabase eDb = null;
    protected DBHelper eHandler = null;
    public static final String NOM_TABLE = "course";
    public static final String NOM_COURSE="nomCourse";
    public static final String PLACE="place";
    public static final String NOM_COUREUR="nom";
    public static final String TEMPS="temps";

    public static final String ID="_id";

    public CoureurDAO(Context context) {
        eHandler=new DBHelper(context,NAME,null,VERSION);
    }
    public void openDb (){
        eDb=eHandler.getWritableDatabase();
    }
    public void closeDb (){
        eDb.close();
    }

    public void addCoureur(Coureur coureur){

        ContentValues value=new ContentValues();
        value.put(NOM_COURSE,coureur.getNomCourse());
        value.put(PLACE,coureur.getPlace());
        value.put(NOM_COUREUR,coureur.getNom());
        value.put(TEMPS,coureur.getTemps());

        eDb.insert(NOM_TABLE, null, value);
    }

    public void deleteCoureur(Coureur coureur){

        eDb.delete(NOM_TABLE,NOM_COUREUR+"=?",new String[]{coureur.getNom()});

        ContentValues value=new ContentValues();
        value.put("PLACE",coureur.getPlace()+1);
        eDb.execSQL("UPDATE "+NOM_TABLE+" SET place = (SELECT (COUNT(*) + 1) FROM (SELECT * from "+NOM_TABLE+") AS T1 WHERE  T1.place < course.place)");
    }

    public ArrayList<HashMap<String, String>> getListeCoureur(String nomCourse){

        Cursor c = eDb.rawQuery("select place, nom from " + NOM_TABLE + " where " + NOM_COURSE + "=?", new String[]{String.valueOf(nomCourse)});
        ArrayList<HashMap<String, String>> el=new ArrayList<>();
        HashMap<String, String> element;
        while (c.moveToNext()){
            String place=c.getString(0);
            String nom=c.getString(1);
            element=new HashMap<String, String>();
            element.put("place",place);
            element.put("nom",nom);

            el.add(element);
        }
        c.close();
        return el;
    }

    public ArrayList<String> getlistNomCourse(){
        ArrayList<String> liste=new ArrayList<>();
        Cursor c=eDb.rawQuery("select distinct nomCourse from "+NOM_TABLE+";",null);
        while (c.moveToNext()){
            liste.add(c.getString(0));
        }
        c.close();
        return liste;
    }

    public Cursor getCursorResultatCourse (String nomcourse){

        Cursor c=eDb.rawQuery("select place, nom, temps from "+NOM_TABLE+" where "+NOM_COURSE+"='"+nomcourse+"';",null);

        return c;
    }

    public void supprimerCourse(String nomCourse){

        eDb.delete(NOM_TABLE,NOM_COURSE+"=?",new String[]{nomCourse});

    }
}
