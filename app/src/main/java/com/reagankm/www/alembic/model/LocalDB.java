package com.reagankm.www.alembic.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reagan on 11/17/15.
 */
public class LocalDB {

    private static final String TAG = "LocalDBTag";

    private static final String SCENT_TABLE_NAME = "Scent";
    private static final String INGREDIENT_TABLE_NAME = "Ingredient";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "LocalScentInfo.db";


    private static SQLiteDatabase mSQLiteDatabase;

    private static LocalDB theLocalDB;



    private LocalDB(Context context) {
        LocalDBHelper dbHelper = new LocalDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = dbHelper.getWritableDatabase();

        //Allow concurrent reads/writes from multiple threads
        mSQLiteDatabase.enableWriteAheadLogging();
    }

    public static LocalDB getInstance(Context c) {

        if (theLocalDB == null) {
            theLocalDB = new LocalDB(c);
        }

        return theLocalDB;

    }

    public static LocalDB getInstance() {
        if (theLocalDB != null) {
            return theLocalDB;
        } else {
            throw new IllegalStateException("theLocalDB has not been initialized with a context");
        }
    }

    public void insertIngredient(String ingredName, String scentId) {

        ContentValues cv = new ContentValues();
        cv.put("id", ingredName + scentId);
        cv.put("name", ingredName);
        cv.put("scent_id", scentId);


        //This should insert the row if it doesn't exist or, if it already does,
        //do nothing
        mSQLiteDatabase.insertWithOnConflict(INGREDIENT_TABLE_NAME, null, cv,
                SQLiteDatabase.CONFLICT_IGNORE);



    }




    public boolean insertScent(String id, String name, float rating) {


        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("rating", rating);

        //This should insert the row if it doesn't exist or, if it already does, replace
        //it with the current values (ie, update the rating for that scent)
        long rowId = mSQLiteDatabase.insertWithOnConflict(SCENT_TABLE_NAME, null, contentValues,
                SQLiteDatabase.CONFLICT_REPLACE);



        return rowId != -1;
    }

    public boolean removeScent(String id) {
        boolean result = false;

        String where = "id = ?";
        String[] whereParams = { id };

        int scentRowsDeleted = mSQLiteDatabase.delete(SCENT_TABLE_NAME, where, whereParams);

        where = "scent_id = ?";
        int ingredRowsDeleted = mSQLiteDatabase.delete(INGREDIENT_TABLE_NAME, where, whereParams);

        if (scentRowsDeleted == 1) {
            result = true;
            Log.d(TAG, "removeScent removed one scent with id " + id + ", " + ingredRowsDeleted
                    + " ingredient rows");
        } else if (scentRowsDeleted > 1) {
            result = true;
            Log.e(TAG, "removeScent with id " + id + " removed " + scentRowsDeleted + " rows!");
        } else {
            Log.d(TAG, "removeScent removed zero rows with id " + id);
        }

        return result;
    }

    //Returns the number of rated scents
    public int getCount() {
        Long result = DatabaseUtils.queryNumEntries(mSQLiteDatabase, SCENT_TABLE_NAME);
        return result.intValue();
    }

    public List<String> getIngredients(String scentId) {
        List<String> result = new ArrayList<>();

        String[] columns = { "name" };
        String where = "scent_id = ?";
        String[] whereParams = { scentId };
        Cursor c = mSQLiteDatabase.query(
                INGREDIENT_TABLE_NAME,  // The table to query
                columns, // The columns to return
                where, // The WHERE clause
                whereParams, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null,  // The sort order
                null // The row limit
        );

        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            result.add(c.getString(0));
            c.moveToNext();
        }

        return result;

    }

    //Gets details of all rated scents
    public List<ScentInfo> getAllRatedScents() {

        List<ScentInfo> result = new ArrayList<>();

        String[] columns = { "id", "name", "rating" };


        Cursor c = mSQLiteDatabase.query(
                SCENT_TABLE_NAME,  // The table to query
                columns, // The columns to return
                null, // The WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null,  // The sort order
                null // The row limit
        );


        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            String id = c.getString(0);
            String name = c.getString(1);
            float rating = Float.valueOf(c.getString(2));

            ScentInfo scent = new ScentInfo(id, name, rating);
            result.add(scent);
            c.moveToNext();
        }


        return result;

    }

    //Gets the rating of a particular scent
    public float getRating(String id) {

        String[] columns = { "rating" };
        String where = "id = ?";
        String[] whereParams = { id };
        Cursor c = mSQLiteDatabase.query(
                SCENT_TABLE_NAME,  // The table to query
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


        return result;
    }

    public void closeDB() {
        mSQLiteDatabase.close();
    }




    private class LocalDBHelper extends SQLiteOpenHelper {


        private static final String CREATE_SCENT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + SCENT_TABLE_NAME + " (" +
                        "id TEXT PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "rating REAL NOT NULL)";

        private static final String CREATE_INGREDIENT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + INGREDIENT_TABLE_NAME + " (" +
                        "id TEXT PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "scent_id TEXT NOT NULL)";

        private static final String DROP_SCENT = "DROP TABLE IF EXISTS " + SCENT_TABLE_NAME;
        private static final String DROP_INGREDIENT = "DROP TABLE IF EXISTS " + INGREDIENT_TABLE_NAME;

        public LocalDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                             int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_SCENT_TABLE);
            sqLiteDatabase.execSQL(CREATE_INGREDIENT_TABLE);
        }


        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            sqLiteDatabase.execSQL(DROP_SCENT);
            sqLiteDatabase.execSQL(DROP_INGREDIENT);

            onCreate(sqLiteDatabase);
        }


    }

}
