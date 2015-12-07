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
 * Local storage for scents the user has rated, recommendations that
 * have been calculated, and ingredients for rated scents.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class LocalDB {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "LocalDBTag";

    /** The name of the scent table. */
    private static final String SCENT_TABLE_NAME = "Scent";

    /** The name of the ingredient table. */
    private static final String INGREDIENT_TABLE_NAME = "Ingredient";

    /** The name of the recommendation table. */
    private static final String RECOMMENDATION_TABLE_NAME = "Recommendation";

    /** The version number. */
    public static final int DB_VERSION = 1;

    /** The database name. */
    public static final String DB_NAME = "LocalScentInfo.db";

    /** The SQLite database. */
    private static SQLiteDatabase mSQLiteDatabase;

    /** The local db helper. */
    private  LocalDBHelper dbHelper;

    /**
     * Creates a local DB object with the given context.
     *
     * @param context the context
     */
    public LocalDB(Context context) {
        dbHelper = new LocalDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = dbHelper.getWritableDatabase();

        //Allow concurrent reads/writes from multiple threads
        mSQLiteDatabase.enableWriteAheadLogging();
    }

    /**
     * Insert an ingredient into the ingredient table.
     *
     * @param ingredName the ingredient name
     * @param scentId the id of the scent with that ingredient
     */
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

    /**
     * Insert a recommended scent into the recommendation table.
     *
     * @param id the scent ID
     * @param name the scent name
     */
    public void insertRecommendation(String id, String name) {
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("name", name);

        //This should insert the row if it doesn't exist or, if it already does,
        //do nothing
        mSQLiteDatabase.insertWithOnConflict(RECOMMENDATION_TABLE_NAME, null, cv,
                SQLiteDatabase.CONFLICT_IGNORE);


    }

    /**
     * Insert a scent into rated scents table.
     *
     * @param id the scent ID
     * @param name the scent name
     * @param rating the scent rating
     * @return true if inserted correctly, otherwise false
     */
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

    /**
     * Remove a scent from the rated scents table.
     *
     * @param id the id to be removed
     * @return true if operation successful
     */
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

    /**
     * Return the number of rated scents.
     *
     * @return the count
     */
    public int getRatedCount() {
        Long result = DatabaseUtils.queryNumEntries(mSQLiteDatabase, SCENT_TABLE_NAME);
        return result.intValue();
    }

    /**
     * Returns the number of recommended scents.
     *
     * @return the count
     */
    public int getRecommendationCount() {
        Long result = DatabaseUtils.queryNumEntries(mSQLiteDatabase, RECOMMENDATION_TABLE_NAME);
        return result.intValue();
    }

    /**
     * Get the ingredients of the scent with the given scent id.
     *
     * @param scentId the ID
     * @return the ingredient list
     */
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

    /**
     * Get all recommendations.
     *
     * @return the list of recommended scents
     */
    public List<ScentInfo> getRecommendations() {
        List<ScentInfo> result = new ArrayList<>();

        String[] columns = { "id", "name" };

        Cursor c = mSQLiteDatabase.query(
                RECOMMENDATION_TABLE_NAME,  // The table to query
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
            ScentInfo scent = new ScentInfo(id, name);

            result.add(scent);

            c.moveToNext();
        }

        return result;

    }

    /**
     * Get details of all rated scents.
     *
     * @return the list of rated scents
     */
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
            List<String> ingredientList = getIngredients(id);

            ScentInfo scent = new ScentInfo(id, name, rating, ingredientList);
            result.add(scent);
            c.moveToNext();
        }


        return result;

    }

    /**
     * Clear all recommendations
     */
    public void clearRecommendations() {

        dbHelper.dropRecommendations(mSQLiteDatabase);

    }

    /**
     * Get the rating of a particular scent.
     *
     * @param id the id of the scent whose rating should be fetched
     * @return the rating
     */
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

    /**
     * Close an instance of the database.
     */
    public void closeDB() {
        mSQLiteDatabase.close();
    }


    /**
     * A helper class to handle table creation and maintenance.
     */
    private class LocalDBHelper extends SQLiteOpenHelper {

        /** The command to create the scent table. */
        private static final String CREATE_SCENT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + SCENT_TABLE_NAME + " (" +
                        "id TEXT PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "rating REAL NOT NULL)";

        /** The command to create the ingredient table. */
        private static final String CREATE_INGREDIENT_TABLE =
                "CREATE TABLE IF NOT EXISTS " + INGREDIENT_TABLE_NAME + " (" +
                        "id TEXT PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL, " +
                        "scent_id TEXT NOT NULL)";

        /** The command to create the recommendation table. */
        private static final String CREATE_RECOMMENDATION_TABLE =
                "CREATE TABLE IF NOT EXISTS " + RECOMMENDATION_TABLE_NAME + " (" +
                        "id TEXT PRIMARY KEY NOT NULL, " +
                        "name TEXT NOT NULL)";

        /** Command to drop the scent table. */
        private static final String DROP_SCENT = "DROP TABLE IF EXISTS " + SCENT_TABLE_NAME;

        /** Command to drop the ingredient table. */
        private static final String DROP_INGREDIENT = "DROP TABLE IF EXISTS " + INGREDIENT_TABLE_NAME;

        /** Command to drop the recommendation table. */
        private static final String DROP_RECOMMENDATION = "DROP TABLE IF EXISTS " + RECOMMENDATION_TABLE_NAME;

        /**
         * Constructs a LocalDBHelper object with the given context, name, factory, and version.
         *
         * @param context the context
         * @param name the name
         * @param factory the factory
         * @param version the version number
         */
        public LocalDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                             int version) {
            super(context, name, factory, version);
        }

        /**
         * Instantiates the database tables if they don't already exist.
         *
         * @param sqLiteDatabase a SQLiteDatabase for the tables
         */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.d(TAG, "sqlitehelper onCreate");
            sqLiteDatabase.execSQL(CREATE_SCENT_TABLE);
            sqLiteDatabase.execSQL(CREATE_INGREDIENT_TABLE);
            sqLiteDatabase.execSQL(CREATE_RECOMMENDATION_TABLE);
        }

        /**
         * Drops the current tables and creates them anew.
         *
         * @param sqLiteDatabase the database for the tables
         * @param i the int
         * @param i1 the other int
         */
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Log.d(TAG, "sqlitehelper onUpgrade");

            sqLiteDatabase.execSQL(DROP_SCENT);
            sqLiteDatabase.execSQL(DROP_INGREDIENT);
            sqLiteDatabase.execSQL(DROP_RECOMMENDATION);

            onCreate(sqLiteDatabase);
        }

        /**
         * Drops the recommendation table and creates a blank one.
         *
         * @param sqLiteDatabase the database for the table
         */
        public void dropRecommendations(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DROP_RECOMMENDATION);
            onCreate(sqLiteDatabase);
        }


    }

}
