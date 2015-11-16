package com.reagankm.www.alembic;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.reagankm.www.alembic.model.Scent;

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
 * A simple {@link Fragment} subclass.
 */
public class ScentListFragment extends Fragment {

    private static final String TAG = "ScentListFragmentTag";

    private List<Scent.ScentInfo> mList;

    private ArrayAdapter<Scent.ScentInfo> mAdapter;

    private static final String
            url = "http://cssgate.insttech.washington.edu/~reagankm/queryScents.php";
    private ListView mListView;

    private String letter;

    private View thisView;


    public ScentListFragment() {
        // Required empty public constructor
        Log.d(TAG, "ScentListFragment created");
        //final SharedPreferences sharedPrefs
        //        = getActivity().getSharedPreferences(getString(R.string.prefs_file), getActivity().MODE_PRIVATE);

        //letter = sharedPrefs.getString("letter", null);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            letter = savedInstanceState.getString(ScentListActivity.getLetterSelectedKey());

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_scent_list, container, false);
        letter = getArguments().getString(ScentListActivity.getLetterSelectedKey());
        Log.d(TAG, "onCreateView, the letter is " + letter);
        return thisView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Context c = thisView.getContext();
        ConnectivityManager connMgr = (ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new ScentWebTask().execute(url + "?letter=" + letter);
        } else {
            Toast.makeText(c
                    , "No network connection available.", Toast.LENGTH_SHORT)
                    .show();
        }

        mListView = (ListView) thisView.findViewById(R.id.scent_list);

        mList = Scent.ITEMS;

        mAdapter = new ArrayAdapter<>(c,
                android.R.layout.simple_list_item_1, android.R.id.text1, mList);
    }

    private class ScentWebTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "ScentListWebTaskTag";

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
                        Scent.ITEMS.add(new Scent.ScentInfo(id, name));

                    }



                    //sb.append(line);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //}
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private String downloadUrl(String myurl) throws IOException {
            InputStream inStream = null;
            Log.d(TAG, "downloadUrl");
            // Only display the first 500 characters of the retrieved
            // web page content.
            //int len = 500;

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

                BufferedReader in = new BufferedReader(
                        new InputStreamReader ( conn.getInputStream() )
                );

                //inStream = conn.getInputStream();

                //Try new method to get data
               // byte[] buffer = new byte[1024];
            //    int len1 = 0;
//                StringBuilder sb = new StringBuilder();
//                while ((len1 = inStream.read(buffer)) > 0) {
//                    getData(sb, inStream);
//                    Log.d(TAG, "downloadUrl, getting data");
//                }


                int len = 10000;
                // Convert the InputStream into a string
                //String contentAsString = readIt(inStream, len);

                //initiate strings to hold response data
                String inputLine;
                StringBuilder sb = new StringBuilder();;
                //read the InputStream with the BufferedReader line by line and add each line to responseData
                while ( ( inputLine = in.readLine() ) != null ){
                    sb.append(inputLine);
                }
                //String contentAsString = readIt(inStream);
                //String contentAsString = sb.toString();
                Log.d(TAG, "The fetched content is: " + sb.toString());
                return sb.toString();

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch(Exception e ) {
                Log.d(TAG, "Something happened" + e.getMessage());
            }
            finally {
                if (inStream != null) {
                    inStream.close();
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
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute");
            // Parse JSON
            try {
                mList.clear();
                Scent.ITEMS.clear();

                JSONArray jsonArray = new JSONArray(s);
                //JSONArray jsonArray = new JSONArray(line);
                Log.d(TAG, "onPostExecute, Converted line to JSON array");

                for (int i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String id = (String) jsonObject.get("id");
                    String name = (String) jsonObject.get("name");
                    Log.d(TAG, "onPostExecute, id: " + id + ", name: " + name);
                    Scent.addItem(new Scent.ScentInfo(id, name));

                }

                //Put them in alphabetical order
                Scent.sortItems();

                mList = Scent.ITEMS;
                mListView.setAdapter(mAdapter);
            }
            catch(Exception e) {
                Log.d(TAG, "Parsing JSON Exception " + e.getClass() + ": " + e.getCause()
                        + "\nMessage: " + e.getMessage());
            }
        }
    }


}
