package com.lavish.indiscan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {


    static String DATABASE_NAME="AndroidJSonDataBase20";

    public static final String TABLE_NAME="AndroidJSonTable1";
    public static final String Table_Column_ID="project_id";
    public static final String Table_Primary="number";
    public static final String Table_Column_Name="project_name";
    public static final String Table_Column_Date="project_date";

    public static final String TABLE_TWO="AndroidJSonTable2";
    public static final String Table_Primary_TWO="number";
    public static final String Table_Column_ID_TWO="project_id";
    public static final String IMAGE="image";


    public SQLiteHelper(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+" " +
                "(" +
                Table_Primary + "INTEGER PRIMARY KEY,"+
                Table_Column_ID +" TEXT, "+
                Table_Column_Name+" TEXT,"+
                Table_Column_Date+" TEXT)";

        String CREATE_TABLE_TWO="CREATE TABLE "+TABLE_TWO+" " +
                "(" +
                Table_Primary_TWO + " INTEGER PRIMARY KEY,"+
                Table_Column_ID_TWO + " TEXT,"+
                IMAGE + " BLOB)";

        database.execSQL(CREATE_TABLE);
        database.execSQL(CREATE_TABLE_TWO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TWO);
            onCreate(db);

    }

    public boolean addData(String id,String name, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table_Column_ID,id);
        contentValues.put(Table_Column_Name,name);
        contentValues.put(Table_Column_Date,date);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getProjects(){
        SQLiteDatabase db = this.getWritableDatabase();
        String QUERY="SELECT * FROM "+TABLE_NAME;
        Cursor data = db.rawQuery(QUERY,null);
        return data;
    }

    public Cursor getImage(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String qq ="SELECT number,image FROM "+TABLE_TWO+" WHERE project_id LIKE "+"'%"+id+"%'";
        Cursor data = db.rawQuery(qq,null);
        return data;
    }

    public void addPic(String id, byte[] img){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put(Table_Column_ID_TWO,id);
        cv.put(IMAGE,img);
        database.insert(TABLE_TWO,null,cv);
    }


    public void deleteProject(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TWO,"project_id = ?",new String[] {id});
        db.delete(TABLE_NAME,"project_id = ?",new String[] {id});
    }

    public void deletePic(String num){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TWO,"number = ?",new String[] {num});
    }


}
