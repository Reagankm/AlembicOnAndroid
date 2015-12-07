package com.reagankm.www.alembic.webtask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.reagankm.www.alembic.model.ScentInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * From the MySQL database on the server, fetches scents who have the same
 * pair of ingredients as those given.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class RecommendationQueryTask extends AsyncTask<String, Void, String>{

    /** The tag to use when logging from this activity. */
    private static final String TAG = "RecommendQueryTaskTag";

    /** The URL location for the PHP query. */
    private static final String
            url = "http://cssgate.insttech.washington.edu/~reagankm/recommendationQuery.php";

    /** A buffered reader to read the data from the server. */
    private BufferedReader in;

    /** A listener to be updated when the query is complete. */
    private RecommendationQueryListener listener;

    /** The calling Activity's context. */
    private final Context theContext;

    /**
     * Creates a task with the given context.
     *
     * @param c the context
     */
    public RecommendationQueryTask(Context c) {
        super();
        theContext = c;

    }

    /**
     * Sets the listener to the one provided.
     *
     * @param listener the listener
     */
    public void setQueryListener(RecommendationQueryListener listener) {
        this.listener = listener;
    }


    /**
     * Sends the ingredient pair as a request to the PHP URL.
     *
     * @param params the ingredients at index 0 and 1
     * @return the resulting JSON as a String
     */
    @Override
    protected String doInBackground(String... params){
        Log.d(TAG, "doInBackground()");

        String result = "";
        ConnectivityManager connMgr = (ConnectivityManager)
                theContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            try {
                // params[0] is the first ingredient in the pair,
                // params[1] is the second
                Log.d(TAG, "Trying to process URL");

                return downloadUrl(url + "?good1=" + params[0] + "&good2=" + params[1]);
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

        Log.d(TAG, "downloadUrl: " + myurl);


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
     * Parse the result and load the scents as items in the scent list.
     *
     * @param s the resulting JSON as a String
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "onPostExecute");
        // Parse JSON
        try {
            List<ScentInfo> result = new ArrayList<>();


            JSONArray jsonArray = new JSONArray(s);
            Log.d(TAG, "onPostExecute, Converted line to JSON array");

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String id = (String) jsonObject.get("id");
                String name = (String) jsonObject.get("name");
                Log.d(TAG, "onPostExecute, id: " + id + ", name: " + name);
                result.add(new ScentInfo(id, name));

            }

            //Send results to listener activity
            if (listener != null) {
                Log.d(TAG, "Sending results to the listener");
                listener.onCompletion(result);
            }

        } catch(Exception e) {
            Log.d(TAG, "Parsing JSON Exception " + e.getClass() + ": " + e.getCause()
                    + "\nMessage: " + e.getMessage());
        }



    }

    /**
     * An interface for listening to this query.
     */
    public interface RecommendationQueryListener {

        /**
         * Sends the results upon completion.
         *
         * @param results the resulting ScentInfo list
         */
        void onCompletion(List<ScentInfo> results);


    }


}




