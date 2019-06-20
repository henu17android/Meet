package com.example.meet.provider;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.meet.bean.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Task任务的数据库
 */
public class TaskDatabase  {

    public static final String DATABASE_NAME = "MeetTask.db";

    public static final int DATABASE_VERSION = 1;

    public static final String DB_TABLE_TASK = "Task";


    private SQLiteDatabase mSQLiteDatabase = null;
    private DBHelper mDbHelper = null;
    private Context mContext;
    private long result;

    public TaskDatabase(Context context) {
        this.mContext = context;
    }

    public static class TaskColumns implements BaseColumns {
        public static final String _ID = "id";//表中的一条数据Id
        public static final String TASK_CONTENT = "task_content";//任务内容
        public static final String IS_FINISH = "is_finish";
        public static final String TO_DO_TIME = "to_do_time";
        public static final String USER_ID = "user_id";
        public static final String CREATE_TIME = "create_time";

        static final String[] TASK_QUERY_COLUMNS = {
           _ID,TASK_CONTENT,TO_DO_TIME,IS_FINISH,CREATE_TIME,USER_ID
        };

        public static final int _ID_INDEX = 0;
        public static final int TASK_CONTENT_INDEX = 1;
        public static final int IS_FINISH_INDEX = 2;
        public static final int TO_DO_TIME_INDEX = 3;
        public static final int CREATE_TIME_INDEX = 4;
        public static final int USER_ID_INDEX = 5;

    }

    //创建task表
    private static final String DB_CREATE_TABLE_TASK = "CREATE TABLE "
            +DB_TABLE_TASK + "(" + TaskColumns._ID + " INTEGER PRIMARY KEY,"
            +TaskColumns.TASK_CONTENT + " TEXT NOT NULL,"
            +TaskColumns.TO_DO_TIME + " UNSIGNED BIG INT,"
            +TaskColumns.IS_FINISH + " BOOLEAN NOT NULL,"
            +TaskColumns.CREATE_TIME + " UNSIGNED BIG INT,"
            +TaskColumns.USER_ID + " INTEGER);";

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper( Context context,  String dbname, int version) {
            super(context, dbname, null,version);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TABLE_TASK);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public long insertTask(Task task,int userID) {
        ContentValues values = new ContentValues();
        values.put(TaskColumns.TASK_CONTENT,task.getContent());
        values.put(TaskColumns.IS_FINISH,task.isFinish());
        values.put(TaskColumns.TO_DO_TIME,task.getToDoTime());
        values.put(TaskColumns.USER_ID,userID);
        values.put(TaskColumns.CREATE_TIME,task.getCreateTime());
        result = mSQLiteDatabase.insert(TaskDatabase.DB_TABLE_TASK,null,values);
        mSQLiteDatabase.close();
        return  result;
    }

    public long deleteTask(int id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long result = db.delete(TaskDatabase.DB_TABLE_TASK,TaskColumns._ID + "=?", new String[]{id + ""});
        return result;
    }

    private Task getTask (int taskId) {
        Cursor cursor = mSQLiteDatabase.query(DB_CREATE_TABLE_TASK,
                null,TaskColumns._ID + "=?",new String[]{taskId + ""},
                null,null,null);
        cursor.moveToNext();
        Task task = new Task();
        task.setId(cursor.getInt(TaskColumns._ID_INDEX));
        task.setContent(cursor.getString(TaskColumns.TASK_CONTENT_INDEX));
        task.setCreateTime(cursor.getLong(TaskColumns.CREATE_TIME_INDEX));
        task.setToDoTime(cursor.getString(TaskColumns.TO_DO_TIME_INDEX));
        boolean isFinish = cursor.getLong(TaskColumns.IS_FINISH_INDEX) > 0;
        task.setFinish(isFinish);
        cursor.close();
        return task;
    }

    private List<Task> getAllTask (long toDoTime) {
        Cursor cursor = mSQLiteDatabase.query(DB_CREATE_TABLE_TASK,
                null,TaskColumns.TO_DO_TIME + "=?",new String[]{toDoTime + ""},
                null,null,TaskColumns.TO_DO_TIME + "DESC");
        List<Task> taskList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Task task = new Task();
            task.setId(cursor.getInt(TaskColumns._ID_INDEX));
            task.setContent(cursor.getString(TaskColumns.TASK_CONTENT_INDEX));
            task.setCreateTime(cursor.getLong(TaskColumns.CREATE_TIME_INDEX));
            task.setToDoTime(cursor.getString(TaskColumns.TO_DO_TIME_INDEX));
            boolean isFinish = cursor.getLong(TaskColumns.IS_FINISH_INDEX) > 0;
            task.setFinish(isFinish);
            taskList.add(task);
        }
        cursor.close();
        return taskList;
    }

    public long updateContent(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskColumns.TASK_CONTENT,task.getContent());
        long result = db.update(TaskDatabase.DB_TABLE_TASK,values,TaskColumns._ID +"=?",new String[]{task.getId() + ""});
        return result;
    }

    public long updateTodoTime(Task task) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskColumns.TO_DO_TIME,task.getToDoTime());
        long result = db.update(DB_TABLE_TASK,values,TaskColumns._ID + "=?",new String[]{task.getId() + ""});
        return result;
    }


}
