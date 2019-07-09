package com.gaiamount.gaia_main.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by haiyang-lu on 16-7-26.
 * 历史记录的帮助类
 */
public class HistoryDatabaseHelper extends SQLiteOpenHelper {
    //名称
    private static final String DB_NAME = "history.db";
    private static final int DB_VERSION = 3;//版本

    public HistoryDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public HistoryDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table history(screenshot text,cover text,name varchar(30) not null,timeStamp long not null,id int not null,contentType int not null);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
