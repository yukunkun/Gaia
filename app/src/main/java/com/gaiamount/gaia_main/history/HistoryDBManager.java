package com.gaiamount.gaia_main.history;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiyang-lu on 16-7-26.
 * 历史记录相关的业务方法的封装
 */
public class HistoryDBManager {
    private HistoryDatabaseHelper mHistoryDatabaseHelper;
    private SQLiteDatabase db;
    private String table = "history";

    public HistoryDBManager(Context context) {
        mHistoryDatabaseHelper = new HistoryDatabaseHelper(context);
        db = mHistoryDatabaseHelper.getReadableDatabase();
    }

    public void add(String screenshot, String cover, String name, long timeStamp,int id,int contentType) {
        ContentValues cv = new ContentValues();
        cv.put("screenshot", screenshot);
        cv.put("cover", cover);
        cv.put("name", name);
        cv.put("timeStamp", timeStamp);
        cv.put("id",id);
        cv.put("contentType",contentType);
        db.insert(table, null, cv);
    }

    public List<OnEventHistory> query() {
        Cursor cursor = db.query("history", new String[]{"screenshot", "cover", "name", "timeStamp","id","contentType"}, null, null, null, null, "timeStamp");

        List<OnEventHistory> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String screenShot = cursor.getString(0);
            String cover = cursor.getString(1);
            String name = cursor.getString(2);
            long timeStamp = cursor.getLong(3);
            int id = cursor.getInt(4);
            int contentType = cursor.getInt(5);
            list.add(new OnEventHistory(screenShot, cover, name, timeStamp,id,contentType));
        }
        return list;
    }

    /**
     * 清空表
     * @return
     */
    public boolean clear() {
        db.delete(table,null,null);
        return true;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
