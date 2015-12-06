package com.reagankm.www.alembic.webtask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.ScentListFragment;
import com.reagankm.www.alembic.model.LocalDB;
import com.reagankm.www.alembic.model.Scent;
import com.reagankm.www.alembic.model.ScentInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by reagan on 11/26/15.
 */
public class GetIngredientsTask extends AsyncTask<String, Void, String> {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "GetIngredientsTaskTag";

    private static final String
            phpUrl = "http://cssgate.insttech.washington.edu/~reagankm/queryIngredients.php";

    private Context theContext;

    private BufferedReader in;

    private String scentId;

    public GetIngredientsTask(Context c) {
        super();
        theContext = c;

    }

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
            //new ScentWebTask().execute(url + "?letter=" + letter);
        } else {
            Log.e(TAG, "No network connection available.");
        }

        return result;
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
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


    //Parse the result and load the ingredients in the LocalDB
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "onPostExecute");
        // Parse JSON
        try {


            JSONArray jsonArray = new JSONArray(s);
            //JSONArray jsonArray = new JSONArray(line);
            Log.d(TAG, "onPostExecute, Converted line to JSON array");
            //LocalDB db = LocalDB.getInstance();
            LocalDB db = new LocalDB(theContext);
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String ingredientName = (String) jsonObject.get("name");
              //  String name = (String) jsonObject.get("name");
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
