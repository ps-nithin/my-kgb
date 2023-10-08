package com.app.mykgb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class dbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "menu_db.db";
    private Context context_private;
    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        context_private=context;
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS MENU_DICT (HINT VARCHAR, MENUS VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS MENU_INFO (MENU VARCHAR, INFO VARCHAR);");
        BufferedReader dictReader=null;
        try {
            dictReader=new BufferedReader(new InputStreamReader(context_private.getAssets().open("mdict.txt")));
            String mLine;

            while((mLine=dictReader.readLine()) != null){
                String[] mLineSplit=mLine.split("\\:");
                db.execSQL("INSERT INTO MENU_DICT VALUES (\'"+mLineSplit[0]+"\','"+mLineSplit[1]+"\');");
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            if(dictReader!=null){
                try {
                    dictReader.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }
        BufferedReader infoReader=null;
        try {
            infoReader=new BufferedReader(new InputStreamReader(context_private.getAssets().open("info.txt")));
            String mLine;

            while((mLine=infoReader.readLine()) != null){
                String[] mLineSplit=mLine.split("\\s+");
                //String[] mInfoArr=Arrays.copyOfRange(mLineSplit,1,mLineSplit.length);
                //String info=Arrays.toString(mInfoArr);
                StringBuilder info=new StringBuilder();
                for(int i=1;i<mLineSplit.length;i++){
                    info.append(mLineSplit[i]);
                    info.append(" ");
                }
                info.toString().trim();

                db.execSQL("INSERT INTO MENU_INFO VALUES (\'"+mLineSplit[0]+"\','"+info+"\');");
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } finally {
            if(infoReader!=null){
                try {
                    infoReader.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}