package edu.uic.cs478.jamesklonowski.project4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 4/25/2015.
 */
public class TransactionDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_DATETIME, MySQLiteHelper.COLUMN_CLIPNUMBER, MySQLiteHelper.COLUMN_TYPE, MySQLiteHelper.COLUMN_STATUS};

    public TransactionDataSource(Context context){
        dbHelper = new MySQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
        //System.out.println("dbhelper created...");
    }

    public void open() throws SQLException{
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    //Delete all rows from transactions table
    public void clearTransactions(){
        database.execSQL("delete from "+MySQLiteHelper.TABLE_TRANSACTIONS);
    }

    //Delete entire transaction database
    public void deleteDatabase(){
        database.execSQL("DROP TABLE IF EXISTS " + MySQLiteHelper.TABLE_TRANSACTIONS);
    }

    //create a new transaction
    public Transaction createTransaction(String datetime, int clipnumber, String type, String status){
        ContentValues values = new ContentValues();
        //values.put(MySQLiteHelper.COLUMN_DATE, date);
        //values.put(MySQLiteHelper.COLUMN_TIME, time);
        values.put(MySQLiteHelper.COLUMN_DATETIME, datetime);
        values.put(MySQLiteHelper.COLUMN_CLIPNUMBER, clipnumber);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        values.put(MySQLiteHelper.COLUMN_STATUS, status);
        long insertId = database.insert(MySQLiteHelper.TABLE_TRANSACTIONS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSACTIONS, allColumns, MySQLiteHelper.COLUMN_ID+" = "+insertId, null, null, null, null);
        cursor.moveToFirst();
        Transaction transaction = cursorToTransaction(cursor);
        cursor.close();
        return transaction;
    }

    //delete a specified transaction
    public void deleteTransaction(Transaction transaction){
        long id = transaction.getId();
        //System.out.println("Transaction deleted with id: "+id);
        database.delete(MySQLiteHelper.TABLE_TRANSACTIONS, MySQLiteHelper.COLUMN_ID+" = "+id, null);
    }

    //return a list of all transactions
    public List<Transaction> getAllTransactions(){
        List<Transaction> transactions = new ArrayList<Transaction>();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TRANSACTIONS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Transaction transaction = cursorToTransaction(cursor);
            transactions.add(transaction);
            cursor.moveToNext();
        }
        cursor.close();
        return transactions;
    }

    //return the specified transaction
    private Transaction cursorToTransaction(Cursor cursor){
        Transaction transaction = new Transaction();
        transaction.setId(cursor.getLong(0));
        //transaction.setDate(cursor.getString(1));
        //transaction.setTime(cursor.getString(2));
        transaction.setDateTime(cursor.getString(1));
        transaction.setClipnumber(cursor.getInt(2));
        transaction.setType(cursor.getString(3));
        transaction.setStatus(cursor.getString(4));
        return transaction;
    }


}
