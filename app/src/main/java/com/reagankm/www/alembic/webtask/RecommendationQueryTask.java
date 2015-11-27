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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by reagan on 11/26/15.
 */
public class RecommendationQueryTask extends AsyncTask<String, Void, String>{

    private static final String TAG = "RecommendQueryTaskTag";

    private static final String
            url = "http://cssgate.insttech.washington.edu/~reagankm/recommendationQuery.php";

    private BufferedReader in;

    private RecommendationQueryListener listener;

    /** The calling Activity's context. */
    private final Context theContext;

    public RecommendationQueryTask(Context c) {
        super();
        theContext = c;
    }

    public void setQueryListener(RecommendationQueryListener listener) {
        this.listener = listener;
    }

    //params[0] is the query to pass
    @Override
    protected String doInBackground(String... params){
        Log.d(TAG, "doInBackground()");

        String result = "";
        ConnectivityManager connMgr = (ConnectivityManager)
                theContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            try {
                Log.d(TAG, "Trying to process URL");

                return downloadUrl(url + "?good1=" + params[0] + "&good2=" + params[1]
                       // + "&bad1=" + params[2] + "&bad2=" + params[3]
                       );
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        } else {
            Log.e(TAG, "No network connection available.");
        }

        return result;
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
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

    //Parse the result and load the scents as items in the scent list
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "onPostExecute");
        // Parse JSON
        try {
            List<ScentInfo> result = new ArrayList<>();


            JSONArray jsonArray = new JSONArray(s);
            //JSONArray jsonArray = new JSONArray(line);
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

    public interface RecommendationQueryListener {

        public void onCompletion(List<ScentInfo> results);


    }


}




