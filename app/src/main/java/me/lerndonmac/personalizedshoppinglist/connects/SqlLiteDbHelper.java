package me.lerndonmac.personalizedshoppinglist.connects;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlLiteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "buhgalteriy.db";
    private static final int DATABASE_VERSION = 1;


    public SqlLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE wastes ( "+
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "count INTEGER NOT NULL,"+
                "cost REAL NOT NULL);";
        db.execSQL(sql);
        sql = "CREATE TABLE buget ( "+
                "id INTEGER PRIMARY KEY, " +
                "buget REAL NOT NULL, " +
                "bugetText TEXT(4) NOT NULL);";
        db.execSQL(sql);
        sql = "INSERT INTO buget (id, buget, bugetText) VALUES (1, 100, 'P');";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
