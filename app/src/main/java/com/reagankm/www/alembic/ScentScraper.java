package com.reagankm.www.alembic;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by reagan on 11/3/15.
 */
public class ScentScraper extends AsyncTask<String, Void, String>{

    private static final String TAG = "ScentScraperTag";
    private static final String DIRECTORY_URL = "http://blackphoenixalchemylab.com/product-directory/";
    private static final String STORE_PATH = "http://blackphoenixalchemylab.com/shop/";
    private static final String
            phpUrl = "http://cssgate.insttech.washington.edu/~reagankm/addScent.php";
    protected static int productCount;

    @Override
    protected String doInBackground(String... urls){
        return "Your face";
    }

    public void getScents(){
        try {
            //track products added, for debugging
            //Map<String, String> productList = new HashMap<String, String>();
            productCount = 0;

            Document doc = Jsoup.connect(DIRECTORY_URL).get();
            Elements productHeader = doc.getElementsByClass("products");
            for (Element el : productHeader){
                Log.d(TAG, "Parsing an element within productHeader");
                Element productList = el.nextElementSibling();
                Elements products = productList.getElementsByTag("a");
                for (Element p : products){
                    String name = p.attr("title");

                    //The id is the unique part of the product's URL
                    //(To differentiate scents with the same name)
                    String id = p.attr("href");
                    id = id.substring(STORE_PATH.length());

                    if (id.length() > 0 && name.length() > 0){
                        String requestURL = phpUrl + "?id=" + id + "&name=" + name;
                        AddScentWebTask task = new AddScentWebTask();
                        task.execute(requestURL);
                        productCount++;

                    }

                    //addScent(id, title);

                }
            }



        } catch (IOException e){
            Log.e(TAG, "IOException in getScents " + e.toString());
        }
    }


}
