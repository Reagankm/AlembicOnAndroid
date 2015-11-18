package com.reagankm.www.alembic.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by reagan on 11/17/15.
 */
public class LocalDBHelper extends SQLiteOpenHelper {

    //private static final String TABLE_NAME = "Scent";

    private static final String CREATE_SCENT_TABLE =
            "CREATE TABLE IF NOT EXISTS Scent (id TEXT PRIMARY KEY NOT NULL, name TEXT NOT NULL, " +
                    "rating INT NOT NULL)";

    private static final String DROP_USER_SQL = "DROP TABLE IF EXISTS Scent";

    public LocalDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                         int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SCENT_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        //Save existing column names
//        List<String> columns = getColumns(sqLiteDatabase, TABLE_NAME);
//
//        //Back up the table
//        sqLiteDatabase.execSQL("ALTER table " + TABLE_NAME + " RENAME TO 'temp_" + TABLE_NAME);
//
//
//
        //Create new table
        sqLiteDatabase.execSQL(DROP_USER_SQL);


        onCreate(sqLiteDatabase);
    }

//    public static List<String> getColumns(SQLiteDatabase db, String tableName) {
//        List<String> ar = null;
//        Cursor c = null;
//        try {
//            c = db.rawQuery("select * from " + tableName + " limit 1", null);
//            if (c != null) {
//                ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
//            }
//        } catch (Exception e) {
//            Log.v(tableName, e.getMessage(), e);
//            e.printStackTrace();
//        } finally {
//            if (c != null)
//                c.close();
//        }
//        return ar;
//    }
//
//    public static String join(List<String> list, String delim) {
//        StringBuilder buf = new StringBuilder();
//        int num = list.size();
//        for (int i = 0; i < num; i++) {
//            if (i != 0)
//                buf.append(delim);
//            buf.append((String) list.get(i));
//        }
//        return buf.toString();
//    }

}
