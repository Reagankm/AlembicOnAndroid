package com.reagankm.www.alembic.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by reagan on 11/17/15.
 */
public class LocalDB {

    private static final String TAG = "LocalDBTag";

    private static final String TABLE_NAME = "Scent";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "LocalScentInfo.db";

    private LocalDBHelper mUserInfoDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public LocalDB(Context context) {
        mUserInfoDBHelper = new LocalDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mUserInfoDBHelper.getWritableDatabase();
    }

    public boolean insertScent(String id, String name, float rating) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("rating", rating);

        //This should insert the row if it doesn't exist or, if it already does, replace
        //it with the current values (ie, update the rating for that scent)
        long rowId = mSQLiteDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);
        return rowId != -1;
    }

    public float getRating(String id) {

        String[] columns = { "rating" };
        String where = "id = ?";
        String[] whereParams = { id };
        Cursor c = mSQLiteDatabase.query(
                TABLE_NAME,  // The table to query
                columns, // The columns to return
                where, // The WHERE clause
                whereParams, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null,  // The sort order
                null // The row limit
        );


        float result = -1;


        c.moveToFirst();
        int count = c.getCount();


        if (count > 1) {
            Log.e(TAG, "Multiple results (" + c.getCount() + ") returned when searching for id " + id);
            result = -99;
        } else if (count == 1){

            result = c.getFloat(0);
        }

//        //List<UserInfo> list = new ArrayList<UserInfo>();
//        for (int i=0; i<c.getCount(); i++) {
//            if (result != -1) {
//                Log.e(TAG, "Multiple results returned. Previous result = " + result);
//            }
//            result = c.getFloat(0);
//
//            c.moveToNext();
//        }

        return result;
    }

    public void closeDB() {
        mSQLiteDatabase.close();
    }




    private class LocalDBHelper extends SQLiteOpenHelper {

        //private static final String CREATE_USER_SQL =
        //"CREATE TABLE IF NOT EXISTS User (email TEXT PRIMARY KEY, pwd TEXT)";

        private static final String CREATE_SCENT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        "id TEXT PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "rating REAL NOT NULL)";

        private static final String DROP_USER_SQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

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

            sqLiteDatabase.execSQL(DROP_USER_SQL);

            onCreate(sqLiteDatabase);
        }


    }

}
