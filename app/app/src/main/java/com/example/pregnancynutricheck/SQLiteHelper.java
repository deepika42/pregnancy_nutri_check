package com.example.pregnancynutricheck;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void queryData(String sql){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String name, String detail, String date, String time, String note){
        SQLiteDatabase database=getWritableDatabase();
        String sql="INSERT INTO RECORD VALUES(NULL,?,?,?,?,?)";

        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,name);
        statement.bindString(2,detail);
        statement.bindString(3,date);
        statement.bindString(4,time);
        statement.bindString(5,note);

        statement.executeInsert();

    }

    public void updateData(String name,String detail, String date,String time,String note,int id){
        SQLiteDatabase database=getWritableDatabase();

        String sql="UPDATE RECORD SET name=?,detail=?,date=?,time=?,note=? WHERE id=?";

        SQLiteStatement statement=database.compileStatement(sql);

        statement.bindString(1,name);
        statement.bindString(2,detail);
        statement.bindString(3,date);
        statement.bindString(4,time);
        statement.bindString(5,note);
        statement.bindDouble(6,(double)id);

        statement.execute();
        database.close();
    }

    public void deleteData(int id){
        SQLiteDatabase database=getWritableDatabase();

        String sql="DELETE FROM RECORD WHERE id=?";

        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1,(double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sql,null);
    }

}
