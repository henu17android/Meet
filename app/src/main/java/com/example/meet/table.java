package com.example.meet;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class table extends SQLiteOpenHelper {

    public static final String CREATE_USERDATA="create table userData(" + "id integer primary key autoincrement," +"name text," +"password text)";

    private Context mContext;

    public table(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version){
        super(context,name,cursorFactory,version);
        mContext=context;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_USERDATA);
    }

    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        //onCreate(db);     }
    }}