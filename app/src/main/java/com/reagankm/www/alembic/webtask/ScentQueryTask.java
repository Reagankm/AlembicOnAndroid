package com.reagankm.www.alembic.webtask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.ScentListFragment;
import com.reagankm.www.alembic.model.Scent;
import com.reagankm.www.alembic.model.ScentInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Fetches all scents that begin with the given first letter.
 */
public class ScentQueryTask extends AsyncTask<String, Void, String> {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "ScentQueryTaskTag";

    /** The URL location for the PHP query. */
    private static final String
            url = "http://cssgate.insttech.washington.edu/~reagankm/queryScents.php";

    /** A buffered reader to read the data from the server. */
    private BufferedReader in;

    /** A dialog to display the query's progress. */
    private ProgressDialog dialog;

    /** The fragment where the results should be displayed. */
    private final ScentListFragment theFragment;

    /** The calling Activity's context. */
    private final Context theContext;

    /**
     * Creates a task with the given context and ScentListFragment.
     * @param c the context
     * @param f the fragment
     */
    public ScentQueryTask(Context c, ScentListFragment f) {
        super();
        theContext = c;
        theFragment = f;
        Activity activity = (Activity) c;
        dialog = new ProgressDialog(activity);
    }

    /**
     * Start the progress dialog.
     */
    @Override
    protected void onPreExecute() {
        dialog.setMessage(theContext.getResources().getString(R.string.scent_query_dialog));
        dialog.show();
    }

    /**
     * Sends the letter as a request to the PHP URL.
     *
     * @param params the letter as index 0
     * @return the resulting JSON as a String
     */
    @Override
    protected String doInBackground(String... params){

        String result = "";
        ConnectivityManager connMgr = (ConnectivityManager)
                theContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            Scent.ITEMS.clear();
            try {
                //params[0] is the letter selected
                return downloadUrl(url + "?letter=" + params[0]);
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


            // Convert the InputStream into a string

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


            JSONArray jsonArray = new JSONArray(s);
            Log.d(TAG, "onPostExecute, Converted line to JSON array");

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String id = (String) jsonObject.get("id");
                String name = (String) jsonObject.get("name");
                Log.d(TAG, "onPostExecute, id: " + id + ", name: " + name);
                Scent.addItem(new ScentInfo(id, name));

            }

            //Put them in alphabetical order
            Scent.sortItems();

            theFragment.updateUI();


        } catch(Exception e) {
            Log.d(TAG, "Parsing JSON Exception " + e.getClass() + ": " + e.getCause()
                    + "\nMessage: " + e.getMessage());
        }

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
