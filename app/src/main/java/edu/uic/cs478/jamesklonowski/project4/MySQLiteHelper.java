package edu.uic.cs478.jamesklonowski.project4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLData;

/**
 * Created by Jimmy on 4/25/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Transactions.db";
    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_ID = "_id";
    //public static final String COLUMN_DATE = "date";
    //public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_CLIPNUMBER = "clipnumber";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_STATUS = "status";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_TRANSACTIONS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            //+ COLUMN_DATE + " text not null, "
            //+ COLUMN_TIME + " text not null, "
            + COLUMN_DATETIME + " text not null, "
            + COLUMN_CLIPNUMBER + " integer not null, "
            + COLUMN_TYPE + " text not null, "
            + COLUMN_STATUS + " text not null"
            + " )";


    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(DATABASE_CREATE);
     //   System.out.println("Database created in mysqlhelper.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }





}