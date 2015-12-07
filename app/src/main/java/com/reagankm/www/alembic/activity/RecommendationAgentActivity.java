package com.reagankm.www.alembic.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.ScentFragment;
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

/**
 * Fetches and displays up to 10 scent recommendations based on
 * user's ratings.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class RecommendationAgentActivity extends MenuActivity
        implements RecommendationQueryTask.RecommendationQueryListener {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "RecommendAgentActvtyTag";


    /** The number of scents to recommend. */
    private static final int NUMBER_OF_SCENTS_TO_RECOMMEND = 10;


    /** Whether the current fresh recommendations have been saved locally.  */
    private boolean storedFresh;

    /** Stores scentPairs with their number of occurrences. */
    private Map<String, Integer> goodPairs;

    /** Counter for many RecommendationQueryTasks have been started. */
    private int webTaskCounter;

    /** A list to store the scent recommendations. */
    private ArrayBlockingQueue<ScentInfo> allRecommendations;

    /** A list of all scents the user has rated. */
    private List<ScentInfo> allRated;

    /** The "Reveal Recommendation" button. */
    private Button revealButton;

    /** An iterator of recommended scents. */
    private Iterator<ScentInfo> recIterator;

    /** The frame layout. */
    private FrameLayout frame;

    /** The FragmentManager. */
    private FragmentManager fragManager;

    /** The ProgressDialog to display while the recommendations are being fetched. */
    private ProgressDialog dialog;

    /** The SharedPreferences. */
    private SharedPreferences sharedPrefs;


    /**
     * Creates the UI and loads recommendations.
     *
     * @param savedInstanceState any saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_agent);

        allRecommendations = new ArrayBlockingQueue<>(NUMBER_OF_SCENTS_TO_RECOMMEND);

        revealButton = (Button) findViewById(R.id.reveal_button);
        revealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealButton.setText(getResources().getString(R.string.reveal_next));
                displayRecommendations();

            }
        });

        LocalDB db = new LocalDB(this);

        if (db.getRatedCount() < 1) {
            db.closeDB();
            //If the user hasn't rated anything, display a message
            TextView noRecommendations = (TextView) findViewById(R.id.no_recommendations);

            noRecommendations.setVisibility(View.VISIBLE);
        } else {
            //Begin the progress dialog and launch the recommendation loader.
            dialog = ProgressDialog.show(RecommendationAgentActivity.this, "", getString(R.string.recommendation_query_dialog));
            Log.d(TAG, "Told dialog to show");

            if (noRecentChanges()) {

                loadRecommendationsFromDb();

            } else {
                loadFreshRecommendations();


            }


        }
    }


    /**
     *
     * Displays recommended scents.
     *
     */
    private void displayRecommendations() {
        Log.d(TAG, "displayRecommendations");

        if (frame == null) {
            frame = (FrameLayout) findViewById(R.id.fragment_recommendation_container);
            frame.setVisibility(View.VISIBLE);
        }

        //Loops through the recommended scents again once it reaches
        //the end
        if (recIterator == null || !recIterator.hasNext()) {
            recIterator = allRecommendations.iterator();
        }

        if (recIterator.hasNext()) {

            ScentInfo current = recIterator.next();
            String scentId = current.getId();
            String scentName = current.getName();
            Log.d(TAG, "Displaying scent " + scentName);

            if (fragManager == null) {
                fragManager = getSupportFragmentManager();
            }

            boolean firstRecommendation = fragManager.findFragmentById(R.id.fragment_recommendation_container) == null;


            Fragment theFragment = new ScentFragment();
            Bundle bundle = new Bundle();

            bundle.putString(ScentActivity.getIdKey(), scentId);
            bundle.putString(ScentActivity.getNameKey(), scentName);
            theFragment.setArguments(bundle);

            FragmentTransaction transaction = fragManager.beginTransaction();

            if (firstRecommendation) {
                //If this is the first recommendation being viewed, add the transaction
                transaction.add(R.id.fragment_recommendation_container, theFragment)
                        .commit();
            } else {
                //If it's not the first, replace the previous transaction
                transaction.replace(R.id.fragment_recommendation_container, theFragment)
                        .commit();
            }

        }
    }


    /**
     * Determines whether any scents have been rated since the last time
     * fresh recommendations were calculated.
     *
     * @return true if no scent ratings have changed since last calculation,
     * otherwise false
     */
    private boolean noRecentChanges() {
        sharedPrefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);

        Long timeLastRated = sharedPrefs.getLong(getString(R.string.ratings_last_updated), -1);
        Long timeLastRatedWhenLastRecommendations = sharedPrefs.getLong(getString(R.string.ratings_timestamp_at_last_recommandation), -1);

        Log.d(TAG, "TimeLastRated: " + timeLastRated + ", Recommendations TimeLastRated: " + timeLastRatedWhenLastRecommendations);

        return timeLastRated != -1 && timeLastRatedWhenLastRecommendations != -1
                && timeLastRated.equals(timeLastRatedWhenLastRecommendations);

    }

    /**
     * Fetches recommendation data stored in the local SQLite database.
     */
    private void loadRecommendationsFromDb() {
        Log.d(TAG, "loadRecommendationsFromDb");

        if (allRecommendations.size() < NUMBER_OF_SCENTS_TO_RECOMMEND) {
            Log.d(TAG, "loadRecommendationsFromDb: fetching recommendations from DB");

            LocalDB db = new LocalDB(this);
            List<ScentInfo> recs = db.getRecommendations();
            db.closeDB();

            for (int i = 0; i < recs.size()
                    && allRecommendations.size() < NUMBER_OF_SCENTS_TO_RECOMMEND; i++) {
                Log.d(TAG, "loadRecommendationsFromDB: adding scent " + recs.get(i));

                allRecommendations.add(recs.get(i));
            }

        } else {
            Log.d(TAG, "loadRecommendationsFromDb: Recommendations still saved in class");
        }

        //Dismisses progress dialog once recommendations are loaded
        if (dialog.isShowing()) {
            dialog.dismiss();
            Log.d(TAG, "loadRecommendationsFromDb: Told dialog to stop showing");
        }

    }

    /**
     * Stores the current recommendations into the local SQLite database.
     */
    private void storeRecommendationsInDb() {
        Log.d(TAG, "storeRecommendationsInDb");
        LocalDB db = new LocalDB(this);

        //Remove any previous recommendations
        db.clearRecommendations();

        for (ScentInfo scent : allRecommendations) {
            String name = scent.getName();
            String id = scent.getId();
            Log.d(TAG, "storeRecommendationsInDB: storing scent " + name);
            db.insertRecommendation(id, name);
        }
        db.closeDB();

    }

    /**
     * Stores pairs of ingredients for all scents the user rated
     * at least 4 stars.
     */
    private void loadPairs() {

        //Get all rated scents and load scent pairs for those rated highly
        LocalDB db = new LocalDB(this);
        allRated = db.getAllRatedScents();
        db.closeDB();

        Log.d(TAG, "Loaded all rated with size " + allRated.size());

        //sort by rating from highest to lowest
        Collections.sort(allRated, new Comparator<ScentInfo>() {

            public int compare(ScentInfo a, ScentInfo b) {

                return (int) (b.getRating() - a.getRating());

            }

        });


        goodPairs = new HashMap<>();

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

    }

    /**
     * Sort scent pairs by frequency so pairs that appear multiple times in user's
     * favorite scents will be prioritized when calculating recommendations.
     *
     * @return a Set of Map.Entries representing the pairs and their frequencies
     */
    private Set<Map.Entry<String, Integer>> sortPairsByFrequency() {
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


        if (goodPairs.size() > 0) {
            for(Map.Entry<String, Integer> entry : goodPairs.entrySet()) {
                goodEntries.add(entry);

            }

            Log.d(TAG, "Got sorted good values into treeset with size " + goodEntries.size());


        }

        return goodEntries;

    }


    /**
     * Calculates new recommendations by finding all pairs of ingredients in
     * scents the user liked and returning recommendations with some of the
     * same scent pairs.
     *
     * @return true if recommendations were found
     */
    private boolean loadFreshRecommendations() {
        Log.d(TAG, "loadFreshRecommendations");

        storedFresh = false;
        allRecommendations.clear();

        //Save the new recommendations timestamp as the current timestamp for ratings_last_updated
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong(getString(R.string.ratings_timestamp_at_last_recommandation),
                sharedPrefs.getLong(getString(R.string.ratings_last_updated), -1));
        editor.apply();


        loadPairs();

        Set<Map.Entry<String,Integer>> goodEntries = sortPairsByFrequency();

        if (goodEntries.size() > 0) {

            Iterator<Map.Entry<String, Integer>> goodIterator = goodEntries.iterator();
            webTaskCounter = 0;

            //Search for recommendations until out of scent pairs or until required
            //number has been found
            while (goodIterator.hasNext()
                    && allRecommendations.size() < NUMBER_OF_SCENTS_TO_RECOMMEND) {

                Map.Entry<String, Integer> goodEntry = goodIterator.next();

                String goodKey = goodEntry.getKey();

                Log.d(TAG, "goodEntry key: " + goodKey + ", value: " + goodEntry.getValue());

                //Divide the pair into separate ingredients
                int index = goodKey.indexOf('\'');

                String firstGoodIngredient = goodKey.substring(0, index);
                String secondGoodIngredient = goodKey.substring(index + 1);

                Log.d(TAG, "Good one: " + firstGoodIngredient + ", goodTwo: " + secondGoodIngredient);

                String[] params = new String[2];
                try {
                    //Send a query to the mySQL database looking for scents with this
                    //pair

                    params[0] = URLEncoder.encode(firstGoodIngredient, "UTF-8");
                    params[1] = URLEncoder.encode(secondGoodIngredient, "UTF-8");

                    RecommendationQueryTask webtask = new RecommendationQueryTask(this);
                    webTaskCounter++;
                    webtask.setQueryListener(this);
                    webtask.execute(params);

                } catch (Exception e) {
                    Log.e(TAG, "Exception in encoding: " + e);
                }
            }

            Log.d(TAG, "Done creating webTasks");
        }

        return true;


    }

    /**
     * A helper method that fetches the ingredients of a scent and loads
     * all possible ingredient pairs that can be created from its ingredients.
     *
     * @param scent the scent whose ingredients should be turned into pairs
     * @param pairMap the pairMap where these pairs should be stored
     */
    private void loadIngredientPairs(ScentInfo scent, Map<String, Integer> pairMap) {

        List<String> ingredients = scent.getIngredientList();

        Log.d(TAG, "loadIngredientPairs retrieved an ingredient list of size "
                + ingredients.size() + " from scent " + scent.getName());


        for (int i = 0; i < ingredients.size() - 1; i++) {

            String note1 = ingredients.get(i);

            for (int j = i + 1; j < ingredients.size(); j++) {
                String note2 = ingredients.get(j);

                //Create pairs in alphabetical order
                String pair = note1.compareTo(note2) < 0 ? "" + note1 + "'" + note2 : "" + note2 + "'" + note1;
                Integer occurrences = pairMap.get(pair);

                if (occurrences == null) {
                    pairMap.put(pair, 1);
                } else {
                    //If this pair already exists in the map, increment its occurrence number
                    pairMap.put(pair, occurrences + 1);
                }

            }
        }

        Log.d(TAG, "loadIngredientPairs exiting with pairMap size " + pairMap.size());

    }

    /**
     * Checks the results returned by a web task and adds valid recommendations
     * to the recommendation queue.  To be valid, the queue must not be full
     * and the scent must be unrated.
     *
     * @param results the possible recommendations found by a web task
     */
    @Override
    public void onCompletion(List<ScentInfo> results) {
        Log.d(TAG, "onCompletion");
        webTaskCounter--;

        if (allRecommendations.size() < NUMBER_OF_SCENTS_TO_RECOMMEND) {

            //If the queue is not yet full, add any unrated scents to the queue

            for (ScentInfo recommendation : results) {
                Log.d(TAG, "Recommended scent: " + recommendation.getName());

                boolean alreadyRated = false;

                //Check the list of rated scents to make sure possible recommendation
                //hasn't been rated
                for (int i = 0; i < allRated.size() && !alreadyRated; i++) {
                    if (allRated.get(i).equals(recommendation)) {
                        alreadyRated = true;
                    }
                }

                //Check any existing recommended scents in the queue to make sure
                //possible recommendation is not a repeat
                Iterator<ScentInfo> itr = allRecommendations.iterator();

                while (itr.hasNext()) {
                    if (itr.next().equals(recommendation)) {
                        alreadyRated = true;
                    }
                }

                //If it's still valid, add recommendation to the queue
                if (!alreadyRated) {

                    Log.d(TAG, "Adding recommended scent " + recommendation.getName());
                    allRecommendations.add(recommendation);
                }
            }
        }

        if (!storedFresh && (webTaskCounter < 1 || allRecommendations.size() >= NUMBER_OF_SCENTS_TO_RECOMMEND)) {
            //If all the web tasks have finished (no further recommendations are coming)
            //or if the queue is full, store the new recommendations in the local database
            //and dismiss the progress dialog.

            storedFresh = true;
            if (dialog.isShowing()) {
                dialog.dismiss();
                Log.d(TAG, "Told dialog to stop showing");
            }

            storeRecommendationsInDb();
        }

    }
}
