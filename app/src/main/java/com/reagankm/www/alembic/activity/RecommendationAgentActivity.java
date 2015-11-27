package com.reagankm.www.alembic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.model.LocalDB;
import com.reagankm.www.alembic.model.ScentInfo;
import com.reagankm.www.alembic.webtask.RecommendationQueryTask;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;


public class RecommendationAgentActivity extends AppCompatActivity
        implements RecommendationQueryTask.RecommendationQueryListener {

    private static final String TAG = "RecommendAgentActvtyTag";

    private TextView noRecommendations;
    private RecyclerView rv;
    private Map<String, Integer> goodPairs;
    private Map<String, Integer> badPairs;
    private LocalDB db;
    private ArrayBlockingQueue<ScentInfo> allRecommendations;
    private List<ScentInfo> allRated;
    //Only want to show a small number of reccs at a time, but calculating these is
    //labor intensive so don't want to duplicate work/calls
    private static final int NUMBER_OF_SCENTS_TO_RECOMMEND = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_agent);

        db = LocalDB.getInstance(this);

        allRecommendations = new ArrayBlockingQueue<>(NUMBER_OF_SCENTS_TO_RECOMMEND);
        //rv = (RecyclerView) findViewById(R.id.recommendations_recycler_view);

        //TODO: Display message if user has no ratings
        if (db.getCount() < 1) {
            noRecommendations = (TextView) findViewById(R.id.no_recommendations);
//            rv.setVisibility(View.GONE);
            noRecommendations.setVisibility(View.VISIBLE);
        }

        allRated = db.getAllRatedScents();
        Log.d(TAG, "Loaded all rated with size " + allRated.size());

        //sort by rating from highest to lowest
        Collections.sort(allRated, new Comparator<ScentInfo>() {

            public int compare(ScentInfo a, ScentInfo b) {

                return (int) (b.getRating() - a.getRating());

            }

        });

        //ScentInfo current = allScents.get(0);

        goodPairs = new HashMap<>();  //>= 4
        badPairs = new HashMap<>(); //<= 2

        int i;
        for (i = 0; i < allRated.size() && allRated.get(i).getRating() >= 4; i++ ) {
            ScentInfo current = allRated.get(i);
            Log.d(TAG, "onCreate finding scentInfo with high ratings. Current "
                    + current.getName() + " rated " + current.getRating());

            loadIngredientPairs(current, goodPairs);
            Log.d(TAG, "onCreate after returning from loadIngredPairs, goodPairs has size "
                    + goodPairs.size());

        }
        Log.d(TAG, "Loaded good pairs, goodPairs now has size " + goodPairs.size());
        int j;
        for (j = allRated.size() - 1; j >= 0 && allRated.get(j).getRating() <= 2; j--) {
            ScentInfo current = allRated.get(j);
            Log.d(TAG, "onCreate finding scentInfo with low ratings. Current "
                    + current.getName() + " rated " + current.getRating());

            loadIngredientPairs(current, badPairs);
            Log.d(TAG, "onCreate after returning from loadIngredPairs, badPairs has size "
                    + badPairs.size());
        }
        Log.d(TAG, "Loaded bad pairs, badPairs now has size " + badPairs.size());
        //TODO: load up a mediumPairs map if necessary using i and j as start/end indexes

        loadRecommendations();

        //TODO: Choose recommendation if user only has 3s

    }

    //TODO: Modify so this works without pairs (considering only single values)
    private void loadRecommendations() {
        Log.d(TAG, "loadRecommendations()");

        Set<Map.Entry<String,Integer>> goodEntries = new TreeSet<>(new Comparator<Map.Entry<String, Integer>>() {

            public int compare(Map.Entry<String, Integer> objA,
                               Map.Entry<String, Integer> objB) {

                int result = objB.getValue() - objA.getValue();
                if (result == 0) {
                    result = objA.getKey().compareTo(objB.getKey());
                }
                return result;
            }


        });

        Set<Map.Entry<String, Integer>> badEntries = new TreeSet<>(new Comparator<Map.Entry<String, Integer>>() {

            public int compare(Map.Entry<String, Integer> objA,
                               Map.Entry<String, Integer> objB) {

                int result = objB.getValue() - objA.getValue();
                if (result == 0) {
                    result = objA.getKey().compareTo(objB.getKey());
                }
                return result;
            }


        });


        if (goodPairs.size() > 0) {
            for(Map.Entry<String, Integer> entry : goodPairs.entrySet()) {
                goodEntries.add(entry);

            }
//            goodValues.addAll(goodPairs.entrySet());
            Log.d(TAG, "Got sorted good values into treeset with size " + goodEntries.size());


        }

        if (badPairs.size() > 0) {
            for (Map.Entry<String, Integer> entry : badPairs.entrySet()) {
                badEntries.add(entry);

            }
//            badEntries.addAll(badPairs.entrySet());
            Log.d(TAG, "Got sorted bad values into treeset with size " + badEntries.size());

        }

        if (goodEntries.size() > 0 && badEntries.size() > 0) {

            Iterator<Map.Entry<String, Integer>> goodIterator = goodEntries.iterator();
            while (goodIterator.hasNext()
                    && allRecommendations.size() < NUMBER_OF_SCENTS_TO_RECOMMEND) {
                Map.Entry<String, Integer> goodEntry = goodIterator.next();
                String goodKey = goodEntry.getKey();
                Log.d(TAG, "goodEntry key: " + goodKey + ", value: " + goodEntry.getValue());
                int index = goodKey.indexOf('\'');
                String goodOne = goodKey.substring(0, index);
                String goodTwo = goodKey.substring(index + 1);
                Log.d(TAG, "Good one: " + goodOne + ", goodTwo: " + goodTwo);

                Iterator<Map.Entry<String, Integer>> badIterator = badEntries.iterator();

                while (badIterator.hasNext()
                        && allRecommendations.size() < NUMBER_OF_SCENTS_TO_RECOMMEND) {

                    Map.Entry<String, Integer> badEntry = badIterator.next();
                    String badKey = badEntry.getKey();
                    Log.d(TAG, "badEntry key: " + badKey + ", value: " + badEntry.getValue());
                    index = badKey.indexOf('\'');
                    String badOne = badKey.substring(0, index);
                    String badTwo = badKey.substring(index + 1);
                    Log.d(TAG, "Bad one: " + badOne + ", badTwo: " + badTwo);

                    String[] params = new String[4];
                    try {
                        params[0] = URLEncoder.encode(goodOne, "UTF-8");
                        params[1] = URLEncoder.encode(goodTwo, "UTF-8");
                        params[2] = URLEncoder.encode(badOne, "UTF-8");
                        params[3] = URLEncoder.encode(badTwo, "UTF-8");

                        RecommendationQueryTask webtask = new RecommendationQueryTask(this);
                        webtask.setQueryListener(this);
                        webtask.execute(params);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception in encoding: " + e);
                    }
                }

            }

            Log.d(TAG, "Finished searching for recommendations");



        } else {

            //TODO: How to behave if there are not good and bad pairs to compare

        }
    }

    private void loadIngredientPairs(ScentInfo scent, Map<String, Integer> pairMap) {

        List<String> ingredients = scent.getIngredientList();
        Log.d(TAG, "loadIngredientPairs retrieved an ingredient list of size "
                + ingredients.size() + " from scent " + scent.getName());

        for (int i = 0; i < ingredients.size() - 1; i++) {

            String note1 = ingredients.get(i);

            for (int j = i + 1; j < ingredients.size(); j++) {
                String note2 = ingredients.get(j);
                String pair = note1.compareTo(note2) < 0 ? "" + note1 + "'" + note2 : "" + note2 + "'" + note1;
                Integer occurrences = pairMap.get(pair);

                if (occurrences == null) {
                    pairMap.put(pair, 1);
                } else {
                    pairMap.put(pair, occurrences + 1);
                }

            }
        }

        Log.d(TAG, "loadIngredientPairs exiting with pairMap size " + pairMap.size());

    }


    //only add if scent is not rated
    @Override
    public void onCompletion(List<ScentInfo> results) {

        if (allRecommendations.size() < NUMBER_OF_SCENTS_TO_RECOMMEND) {


            for (ScentInfo recommendation : results) {
                Log.d(TAG, "Recommended scent: " + recommendation.getName());

                boolean alreadyRated = false;
                for (int i = 0; i < allRated.size() && !alreadyRated; i++) {
                    if (allRated.get(i).equals(recommendation)) {
                        alreadyRated = true;
                    }
                }

                if (!alreadyRated) {

                    Log.d(TAG, "Adding recommended scent " + recommendation.getName());
                    allRecommendations.add(recommendation);
                }
            }
        }

    }


}