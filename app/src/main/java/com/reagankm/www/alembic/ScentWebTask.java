package com.reagankm.www.alembic;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.reagankm.www.alembic.model.Scent;
import com.reagankm.www.alembic.model.ScentInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by reagan on 11/16/15.
 */
//Connect to PHP url and pull down list of scents
public class ScentWebTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "ScentWebTaskTag";
    BufferedReader in;


    @Override
    protected String doInBackground(String...urls) {
        Log.d(TAG, "doInBackground");
        // params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    void getData(StringBuilder sb, InputStream inStream) {
        Log.d(TAG, "getData");
        try {

            BufferedReader buff = new BufferedReader(new InputStreamReader(inStream));

            String line = buff.readLine();
            Log.d(TAG, "getData, Got line: " + line);
            //while (line != null) {
            try {
                JSONArray jsonArray = new JSONArray(line);
                Log.d(TAG, "getData, Converted line to JSON array");

                for (int i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String id = (String) jsonObject.get("id");

                    String name = (String) jsonObject.get("name");
                    Log.d(TAG, "getData, id: " + id + ", name: " + name);
                    Scent.ITEMS.add(new ScentInfo(id, name));

                }



                //sb.append(line);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //}
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    new InputStreamReader ( conn.getInputStream() )
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

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        //public String readIt(InputStream stream) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    //Parse the result and load the scents as items in the scent list
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "onPostExecute");
        // Parse JSON
        try {


            JSONArray jsonArray = new JSONArray(s);
            //JSONArray jsonArray = new JSONArray(line);
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


        }
        catch(Exception e) {
            Log.d(TAG, "Parsing JSON Exception " + e.getClass() + ": " + e.getCause()
                    + "\nMessage: " + e.getMessage());
        }
    }
}

