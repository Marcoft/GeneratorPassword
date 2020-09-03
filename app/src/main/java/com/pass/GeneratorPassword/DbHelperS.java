package com.pass.GeneratorPassword;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelperS extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "listtDb.db";
    static final String TABLE_CONTACTS = "passwordsTable";

    public static final String KEY_ID = "id";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_K = "keeeyss";


    public DbHelperS(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE passwordsTable (" + DbHelperS.KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + DbHelperS.KEY_K
                + "  TEXT, " + DbHelperS.KEY_PASSWORD + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONTACTS);
        onCreate(db);
    }


}
