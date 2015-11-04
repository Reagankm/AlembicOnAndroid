package com.reagankm.www.alembic;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by reagan on 11/3/15.
 */
public class ScentScraperTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "ScentScraperTag";
    private static final String DIRECTORY_URL = "http://blackphoenixalchemylab.com/product-directory/";
    private static final String STORE_PATH = "http://blackphoenixalchemylab.com/shop/";
    private static final String
            phpUrl = "http://cssgate.insttech.washington.edu/~reagankm/addScent.php";
    private int productCount;

    private final Context theContext;

    public ScentScraperTask(Context c){
        theContext = c;
    }

    @Override
    protected String doInBackground(String... params){
        String result;

        try {
            //track products added, for debugging
            //Map<String, String> productList = new HashMap<String, String>();
            productCount = 0;

            Document doc = Jsoup.connect(DIRECTORY_URL).timeout(0).maxBodySize(10*1024*1024).get();
            Elements productHeader = doc.getElementsByClass("products");
            for (Element el : productHeader){
                Log.d(TAG, "Parsing an element within productHeader");
                Element productList = el.nextElementSibling();
                Elements products = productList.getElementsByTag("a");
                //int counter = 0;
                for (Element p : products){
                    //if (counter < 10) {
                    String name = p.attr("title");

                    //The id is the unique part of the product's URL
                    //(To differentiate scents with the same name)
                    String id = p.attr("href");
                    id = id.substring(STORE_PATH.length());
                    Log.d(TAG, "Before Encoding: Title = " + name + ", id = " + id);

                    name = URLEncoder.encode(name, "UTF-8");
                    id = URLEncoder.encode(id, "UTF-8");
                    Log.d(TAG, "After Encoding: Title = " + name + ", id = " + id);

                    if (id.length() > 0 && name.length() > 0) {
                        String requestURL = phpUrl + "?id=" + id + "&name=" + name;
                        //AddScentWebTask task = new AddScentWebTask();
                        //task.execute(requestURL);
                        String phpResult = downloadUrl(requestURL);
                        if (scentAddedSuccessfully(phpResult)) {
                            productCount++;
                        }


                    }

                        //counter++;
                   // } else {
                     //   break;
                    //}

                    //addScent(id, title);

                }
            }



        } catch (IOException e){
            Log.e(TAG, "IOException in getScents " + e.toString());
        }


        if (productCount > 0){
            result = "Total Products Added: " + productCount;
        } else {
            result = "No new scents found at Black Phoenix Alchemy Lab";
        }

        return result;
    }

    private boolean scentAddedSuccessfully(String phpResult){
        boolean result = false;

        // Parse JSON
        try {
            JSONObject jsonObject = new JSONObject(phpResult);
            String status = jsonObject.getString("result");

            if (status.equalsIgnoreCase("added")) {
                Log.d(TAG, "Successfully added scent");
                result = true;
            } else {
                //String reason = jsonObject.getString("error");
                Log.d(TAG, "Not added");

            }

        } catch(Exception e) {
            Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
        }

        return result;
    }


    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
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

    // Reads an InputStream and converts it to a String.
    public String inputStreamToString(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Toast.makeText(theContext, result,
                Toast.LENGTH_LONG).show();

        /*super.onPostExecute(s);

        // Parse JSON
        try {
            JSONObject jsonObject = new JSONObject(s);
            String status = jsonObject.getString("result");
            if (status.equalsIgnoreCase("success")) {

                productCount++;
                Log.d(TAG, "successfully added scent");
                //Toast.makeText("User successfully inserted",
                //        Toast.LENGTH_SHORT)
                //        .show();
            } else {
                String reason = jsonObject.getString("error");
                Log.d(TAG, "Failed: " + reason);
                //Toast.makeText(getActivity(), "Failed :" + reason,
                //      Toast.LENGTH_SHORT)
                //    .show();
            }

//            getFragmentManager().popBackStackImmediate();
        } catch(Exception e) {
            Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
        }*/
    }
//}
}
