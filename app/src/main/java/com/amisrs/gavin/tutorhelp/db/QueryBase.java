package com.amisrs.gavin.tutorhelp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Gavin on 15/10/2016.
 */
public class QueryBase {
    public SQLiteDatabase db;
    public DBHelper dh;
    public Context context;


    public QueryBase(Context context) {
        dh = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        db = dh.getWritableDatabase();
    }

    public void close() {
        dh.close();
        db.close();
    }

    public Context getContext() {
        return context;
    }
}
