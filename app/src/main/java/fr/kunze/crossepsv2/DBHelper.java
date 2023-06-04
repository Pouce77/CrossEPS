package fr.kunze.crossepsv2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Julien on 19/11/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String NOM_TABLE = "course";
    public static final String KEY = "_id";
    public static final String NOM_COURSE="nomCourse";
    public static final String PLACE="place";
    public static final String NOM_COUREUR="nom";
    public static final String TEMPS="temps";

    public  static final String TABLE_CREATE = "CREATE TABLE " + NOM_TABLE + " ("+ KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "+NOM_COURSE+" TEXT, "+PLACE+" INTEGER, "+NOM_COUREUR+" TEXT, "+ TEMPS+" TEXT);";
    public static final String TABLE_DROP= "DROP TABLE IF EXISTS " + NOM_TABLE + ";";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(TABLE_DROP);
    }
}
