package com.reagankm.www.alembic.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.activity.ScentActivity;
import com.reagankm.www.alembic.model.LocalDB;
import com.reagankm.www.alembic.webtask.GetIngredientsTask;

import java.util.List;


/**
 * Displays details of a given scent.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class ScentFragment extends Fragment {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "ScentFragmentTag";

    /** The name of the scent. */
    private String name;

    /** The ID of the scent. */
    private String id;

    /**
     * Constructs a scent fragment.
     */
    public ScentFragment() {
        // Required empty public constructor
        Log.d(TAG, "ScentFragment constructed");
    }

    /**
     * Initializes the fragment.
     *
     * @param savedInstanceState Any previously saved data like the scent name and id
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            name = savedInstanceState.getString(ScentActivity.getNameKey());
            id = savedInstanceState.getString(ScentActivity.getIdKey());

        } else {
            Log.d(TAG, "onCreate has no savedInstanceState");
        }

    }

    /**
     * Creates the UI.
     *
     * @param inflater the layout inflater
     * @param container the view group container
     * @param savedInstanceState any saved instance data
     * @return the view for the scent
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View thisView = inflater.inflate(R.layout.fragment_scent, container, false);

        //Set name and id
        id = getArguments().getString(ScentActivity.getIdKey());
        name = getArguments().getString(ScentActivity.getNameKey());
        Log.d(TAG, "onCreateView, the scent info is " + id + " " + name);

        TextView nameView = (TextView) thisView.findViewById(R.id.scent_detail_name);
        TextView idView = (TextView) thisView.findViewById(R.id.scent_detail_id);
        TextView ingredView = (TextView) thisView.findViewById(R.id.ingredient_textview);

        nameView.setText(name);
        idView.setText(id);

        //Set the ingredient list if it's known in the local database
        final LocalDB localDB = new LocalDB(getContext());

        List<String> ingredList = localDB.getIngredients(id);
        if (ingredList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : ingredList) {
                sb.append(s + ", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            ingredView.setText(sb.toString());
            ingredView.setVisibility(View.VISIBLE);
        }

        //Set rating bar value and listener
        AppCompatRatingBar rating = (AppCompatRatingBar) thisView.findViewById(R.id.detail_rating_bar);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                SharedPreferences sharedPrefs = getActivity().getSharedPreferences(getString(R.string.prefs_file), getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putLong(getString(R.string.ratings_last_updated), System.currentTimeMillis());
                editor.apply();
                Log.d(TAG, "Storing time last rated");

                //Save or update the rating
                if (rating == 0) {
                    //Clear item from DB if it exists
                    LocalDB localDB = new LocalDB(getContext());
                    boolean result = localDB.removeScent(id);
                    localDB.closeDB();
                    Log.d(TAG, "onRatingChanged deleteScent result = " + result);

                } else {
                    //Update rating and store scent with rating in local DB
                    LocalDB localDB = new LocalDB(getContext());
                    boolean result = localDB.insertScent(id, name, rating);
                    localDB.closeDB();
                    Log.d(TAG, "onRatingChanged insertScent result = " + result);

                    //Store ingredients of scent in local DB
                    new GetIngredientsTask(getContext()).execute(id);

                }
            }
        });

        float prevRating = localDB.getRating(id);
        if (prevRating > 0) {
            rating.setRating(prevRating);
        }

        localDB.closeDB();

        return thisView;
    }


}
