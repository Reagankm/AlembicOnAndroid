package com.reagankm.www.alembic.webtask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.reagankm.www.alembic.model.LocalDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * From the MySQL database on the server, fetches the ingredient
 * list corresponding to a given scent id.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class GetIngredientsTask extends AsyncTask<String, Void, String> {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "GetIngredientsTaskTag";

    /** The URL location for the PHP query. */
    private static final String
            phpUrl = "http://cssgate.insttech.washington.edu/~reagankm/queryIngredients.php";

    /** The context. */
    private Context theContext;

    /** A buffered reader to read the data from the server. */
    private BufferedReader in;

    /** The scent ID whose ingredients are sought. */
    private String scentId;

    /**
     * Creates a task with the given context.
     *
     * @param c the context
     */
    public GetIngredientsTask(Context c) {
        super();
        theContext = c;

    }

    /**
     * Sends the scent ID as a request to the PHP URL.
     *
     * @param params the Scent ID as index 0
     * @return the resulting JSON as a String
     */
    @Override
    protected String doInBackground(String... params) {
        String result = "";
        ConnectivityManager connMgr = (ConnectivityManager)
                theContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {


            try {
                //params[0] is the scent id
                scentId = params[0];
                return downloadUrl(phpUrl + "?scent_id=" + scentId);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }

        } else {
            Log.e(TAG, "No network connection available.");
        }

        return result;
    }


    /**
     * Given a URL, establishes an HttpUrlConnection and retrieves
     * the web page content as a InputStream, which it returns as
     * a string.
     * @param myurl the URL
     * @return the String result
     * @throws IOException in case of error
     */
    private String downloadUrl(String myurl) throws IOException {

        Log.d(TAG, "downloadUrl");


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
            Log.d(TAG, "The connection response is: " + response);

            in = new BufferedReader(
                    new InputStreamReader( conn.getInputStream() )
            );


            //initiate strings to hold response data
            String inputLine;
            StringBuilder sb = new StringBuilder();;
            //read the InputStream with the BufferedReader line by line and add each line to responseData
            while ( ( inputLine = in.readLine() ) != null ){
                sb.append(inputLine);
            }
            Log.d(TAG, "The fetched content is: " + sb.toString());
            return sb.toString();

            // Makes sure that the InputStream is closed after the app is
            // finished using it.

        } catch(Exception e ) {
            Log.d(TAG, "Something happened" + e.getMessage());
        }
        finally {
            if (in != null) {
                in.close();
            }
        }
        return null;
    }


    /**
     * Parse the result and load the ingredients in the LocalDB.
     *
     * @param s the resulting JSON as a String
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "onPostExecute");
        // Parse JSON
        try {


            JSONArray jsonArray = new JSONArray(s);
            Log.d(TAG, "onPostExecute, Converted line to JSON array");

            LocalDB db = new LocalDB(theContext);
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String ingredientName = (String) jsonObject.get("name");

                Log.d(TAG, "onPostExecute, storing in local DB ingredientName: " + ingredientName
                        + ", scentId: " + scentId);
                db.insertIngredient(ingredientName, scentId);

            }

            db.closeDB();


        } catch(Exception e) {
            Log.d(TAG, "Parsing JSON Exception " + e.getClass() + ": " + e.getCause()
                    + "\nMessage: " + e.getMessage());
        }
    }


}
