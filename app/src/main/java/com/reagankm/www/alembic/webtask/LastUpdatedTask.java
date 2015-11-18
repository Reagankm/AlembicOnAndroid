package com.reagankm.www.alembic.webtask;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.reagankm.www.alembic.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by reagan on 11/10/15.
 */
public class LastUpdatedTask extends AsyncTask<Void, Void, String> {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "LastUpdatedTaskTag";

    private static final String
            phpUrl = "http://cssgate.insttech.washington.edu/~reagankm/lastUpdated.php";

    /** The calling Activity's context. */
    private final Context theContext;

    public LastUpdatedTask(Context c){
        theContext = c;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {

            return downloadUrl(phpUrl);
        } catch (IOException e){
            Log.e(TAG, "IOException in doInBackground " + e.toString());
        }

        return "";
    }

    /**
     * Given a URL, establishes an HttpUrlConnection and retrieves
     * the webpage content as an InputStream which it returns as a
     * String.
     *
     * @param myurl the URL to which to be connected
     * @return a String of the webpage content
     * @throws IOException
     */
    private String downloadUrl(String myurl) throws IOException {

        InputStream inputStream = null;

        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            inputStream = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = inputStreamToString(inputStream, len);
            Log.d(TAG, "The string returned from PHP is: " + contentAsString);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch(Exception e ) {
            Log.d(TAG, "Something happened" + e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return null;
    }


    /**
     * Converts the given InputStream to a String.
     *
     * @param stream the InputStream to be converted.
     * @param len the length
     * @return the String version of the InputStream
     * @throws IOException
     */
    public String inputStreamToString(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


    /**
     * Saves result to shared preferences
     *
     * @param result the result message from doInBackground()
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        //Parse JSON and save result in shared preferences
        try {
            JSONObject jsonObject = new JSONObject(result);
            String scent_date = (String) jsonObject.get("scent");
            String ingredient_date = (String) jsonObject.get("ingredient");

            final SharedPreferences sharedPrefs =
                    theContext.getSharedPreferences(theContext.getString(R.string.prefs_file),
                            theContext.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.putString("scent_date", scent_date);
            editor.putString("ingredient_date", ingredient_date);
            editor.apply();

            String toastText = "Stored last update dates: " + scent_date + ", "
                    + ingredient_date;
            Toast.makeText(theContext, toastText,
                    Toast.LENGTH_LONG).show();

        } catch(Exception e) {
            Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
        }



    }




}
