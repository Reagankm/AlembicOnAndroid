package com.reagankm.www.alembic.webtask;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.reagankm.www.alembic.R;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Fetches and parses the product list from Black Phoenix Alchemy Lab,
 * adding new scents to the Scent table in the database and displaying
 * the total number of scents added in a Toast message when finsihed.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class ScentScraperTask extends AsyncTask<String, Integer, String> {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "ScentScraperTag";

    /** The URL of BPAL's product directory */
    private static final String DIRECTORY_URL = "http://blackphoenixalchemylab.com/product-directory/";

    /** The URL path of BPAL's shop */
    private static final String STORE_PATH = "http://blackphoenixalchemylab.com/shop/";

    /** The URL of the PHP script that accepts a scent name and id and adds it to the Scent table */
    private static final String
            addScentPhpUrl = "http://cssgate.insttech.washington.edu/~reagankm/addScent.php";

    /** The URL of the PHP script that accepts a scent id and ingredient name and id and adds it to the Ingredient table */
    private static final String
            addIngredientPhpUrl = "http://cssgate.insttech.washington.edu/~reagankm/addIngredient.php";

    /** Estimated maximum number of results to be parsed. */
    private static final int ESTIMATED_MAX = 6529;

    /** A count of how many new scents were added. */
    private int productCount;

    /** The calling Activity's context. */
    private final Context theContext;

    /** The progress bar. */
    private ProgressBar mProgressBar;

    /** The image replaced by the progress bar. */
    private ImageView originalImage;

    /** The button replaced by the progress bar. */
    private Button updateButton;


    /**
     * Creates a ScentScraperTask and sets its context.
     *
     * @param c the Context of the calling Activity
     */
    public ScentScraperTask(Context c){
        theContext = c;
        Activity hub = (Activity) theContext;
        updateButton = (Button) hub.findViewById(R.id.update_button);
        mProgressBar = (ProgressBar) hub.findViewById(R.id.progressBar);
        originalImage = (ImageView) hub.findViewById(R.id.get_new_image);
    }

    /**
     * Hides the update button and its image, replacing them with a progress bar.
     */
    protected void onPreExecute() {
        originalImage.setVisibility(View.GONE);
        updateButton.setVisibility(View.GONE);
        mProgressBar.setMax(ESTIMATED_MAX);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Parses the product list at BPAL and sends the name and unique id of each product
     * to the php script to be added to the database.
     *
     * @param params parameters passed by the calling Activity
     * @return A message explaining how many products were added
     */
    @Override
    protected String doInBackground(String... params){
        String result;

        try {
            productCount = 0;

            //Get a copy of the BPAL product directory web page and find the list
            //of products by name
            Document doc = Jsoup.connect(DIRECTORY_URL).timeout(0).maxBodySize(10*1024*1024).get();


            //Examine all scents in the product list
            Elements productHeader = doc.getElementsByClass("products");
            for (Element el : productHeader){
                //Log.d(TAG, "Parsing an element within productHeader");
                Element productList = el.nextElementSibling();
                Elements products = productList.getElementsByTag("a");

                //Send the name and unique id of each product to be passed into the
                //database
                for (Element p : products){
                    String name = p.attr("title");

                    //The id is the unique part of the product's URL
                    //(To differentiate between scents with the same name)
                    String id = p.attr("href");
                    id = id.substring(STORE_PATH.length());
                    Log.d(TAG, "Before Encoding: Title = " + name + ", id = " + id);

                    //Encode name and id so they're safe to pass as part of a web address
                    name = URLEncoder.encode(name, "UTF-8");
                    id = URLEncoder.encode(id, "UTF-8");

                    //Send the product to be added to the database via the PHP script
                    if (id.length() > 0 && name.length() > 0) {

                        String requestURL = addScentPhpUrl + "?id=" + id + "&name=" + name;
                        String phpResult = downloadUrl(requestURL);

                        //Update the progress dialog by 1 scent
                        publishProgress(1);
                        if (scentAddedSuccessfully(phpResult)) {
                            productCount++;
                        }
                    }
                }
            }
            //Examine all scents in the ingredients list

            //h2.topic + table selects the table under the topic header
            //thing ul selects all ul children of thing
            Elements ingredientList = doc.select("h2.topic + table ul");
            for (Element el : ingredientList){
                Elements ingredients = el.getElementsByTag("a");
                for (Element e : ingredients){

                    //Follow the path for each ingredient and store the scents found

                    String path = e.attr("href");
                    String ingredientName = e.text();

                    Log.d(TAG, "Path: " + path);
                    Log.d(TAG, "Ingredient Name: " + ingredientName);
                    ingredientName = URLEncoder.encode(ingredientName, "UTF-8");

                    Document ingredientDoc = Jsoup.connect(path).timeout(0).maxBodySize(10*1024*1024).get();
                    Elements productsFound = ingredientDoc.getElementsByClass("product_item");
                    //Log.d(TAG, "Extracted product_items from ingredientDoc");
                    for (Element prod : productsFound){
                        Elements productHead = prod.getElementsByTag("h2");
                        //Log.d(TAG, "Extracted h2s from product_items");
                        for (Element h2 : productHead){
                            Elements productList = h2.getElementsByTag("a");
                            //Log.d(TAG, "Extracted links from h2s");
                            for (Element p : productList){
                                //Add to scent list if it's not already there
                                String scent_name = p.text();

                                //The id is the unique part of the product's URL
                                //(To differentiate between scents with the same name)
                                String scent_id = p.attr("href");
                                scent_id = scent_id.substring(STORE_PATH.length());
                                //Log.d(TAG, "Before Encoding: Title = " + scent_name + ", id = " + scent_id);


                                //Log.d(TAG, "After Encoding: Title = " + name + ", id = " + scent_id);

                                //Send the product to be added to the database via the PHP script
                                if (scent_id.length() > 0 && scent_name.length() > 0) {
                                    //Encode name and id so they're safe to pass as part of a web address
                                    scent_name = URLEncoder.encode(scent_name, "UTF-8");
                                    scent_id = URLEncoder.encode(scent_id, "UTF-8");

                                    //Send data to Scent Name database
                                    String requestURL = addScentPhpUrl + "?id=" + scent_id + "&name=" + scent_name;
                                    String scentResult = downloadUrl(requestURL);
                                    Log.d(TAG, "Sending scent by ingredient " + ingredientName + ": " + scent_name + ", " + scent_id);

                                    //Send data to ingredient database
                                    requestURL = addIngredientPhpUrl + "?id=" + ingredientName + scent_id
                                            + "&name=" + ingredientName + "&scent_id=" + scent_id;
                                    String ingredientResult = downloadUrl(requestURL);


                                    //Update the progress dialog by 1 scent
                                    publishProgress(1);
                                    if (scentAddedSuccessfully(scentResult)
                                            || scentAddedSuccessfully(ingredientResult)) {
                                        productCount++;

                                    }

                                }
                            }
                        }
                    }

                }

                //stop processing (don't continue to the other ULs that were pulled)
                break;
            }
        } catch (IOException e){
            Log.e(TAG, "IOException in doInBackground " + e.toString());
        }

        //Create message String with details about the items processed
        if (productCount > 0){
            result = "Total Products Added: " + productCount;
        } else {
            result = "No new scents found at Black Phoenix Alchemy Lab";
        }

        return result;
    }

    /**
     * A helper method to check the result returned by the PHP file and
     * determine whether the product was or was not added to the database.
     *
     * @param phpResult the result from the PHP file that processed the item
     * @return true if the item was successfully added to the database, otherwise false
     */
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
                Log.d(TAG, "Not added");
            }

        } catch(Exception e) {
            Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
        }

        return result;
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
     * Update the progress bar by the amount at index 0.
     * @param values the amount to update the progress by
     */
    @Override
    protected void onProgressUpdate(Integer... values){
        mProgressBar.incrementProgressBy(values[0]);

    }

    /**
     * Displays a Toast message containing the result returned by doInBackground()
     * and returns the button and imageview to the UI.
     *
     * @param result the result message from doInBackground()
     */
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        mProgressBar.setVisibility(View.GONE);
        originalImage.setVisibility(View.VISIBLE);
        updateButton.setVisibility(View.VISIBLE);

        Toast.makeText(theContext, result,
                Toast.LENGTH_LONG).show();

    }
}
