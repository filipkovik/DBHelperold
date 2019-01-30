package com.example.android.rbtreecrypto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DB_CACHE.db";
    public static final String TABLE_NAME = "JSON_DATA";

    public static final String ID = "id";
    public static final String COIN_LIST = "COIN_LIST";
    public static final String data2 = "data_2";
    public static final String data3 = "data_3";
    public static final String data4 = "data_4";


    public DBHelper(Context context) {          //, String name, SQLiteDatabase.CursorFactory factory, int version
        super(context, DATABASE_NAME, null, 1);     //ovaj metod pravi DB
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS JSON_DATA(ID INTEGER PRIMARY KEY AUTOINCREMENT, COIN_LIST STRING, COI STRING)");
        db.execSQL("CREATE TABLE IF NOT EXISTS COIN_INFO(ID INTEGER PRIMARY KEY AUTOINCREMENT, COIN STRING, VALUE STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS JSON_DATA");
        onCreate(db);
    }

    public void insertData(String tableName, String databaseInput, String databaseInput2, String ColumnName, String ColumnName2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColumnName, databaseInput);
        if (ColumnName2!=null && databaseInput2!=null)
            contentValues.put(ColumnName2, databaseInput2);
        db.insert(tableName, null, contentValues);        //vraca -1 ako ne uspe

    }

    public void update(String tableName, String databaseInput, String ColumnName, String ParameterType , String ParameterValue ) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (ParameterValue.equals("1"))
            db.execSQL("UPDATE "+tableName+" SET "+ColumnName+" = '"+databaseInput+"' WHERE id=1 ");
        else
            db.execSQL("UPDATE "+tableName+" SET "+ColumnName+" = '"+databaseInput+"' WHERE "+ ParameterType+"='"+ParameterValue+"'");

        //      String query = "SELECT "+ ColumnName +" FROM json_data WHERE id=1 LIMIT 1";
    }

    public String get(String tableName, String ColumnName, String ParameterType , String ParameterValue ) {
        String DBoutput = "";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + ColumnName + " FROM "+tableName+" WHERE "+ParameterType+"='"+ParameterValue+"'", null);//id=1

        if (cursor.moveToFirst()) {
            DBoutput = cursor.getString(cursor.getColumnIndex(ColumnName));
        }
        Log.d("DBOUT", DBoutput);
        return  DBoutput;
    }

}