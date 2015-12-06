package com.reagankm.www.alembic.fragment;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
 * A simple {@link Fragment} subclass.
 */
public class ScentFragment extends Fragment {

    private static final String TAG = "ScentFragmentTag";

    private String name;
    private String id;
    private TextView nameView;
    private TextView idView;
    private TextView ingredView;

    private View thisView;

    private AppCompatRatingBar rating;
    private LocalDB localDB;

    public ScentFragment() {
        // Required empty public constructor
        Log.d(TAG, "ScentFragment constructed");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            name = savedInstanceState.getString(ScentActivity.getNameKey());
            id = savedInstanceState.getString(ScentActivity.getIdKey());

        } else {
            Log.d(TAG, "onCreate has no savedInstanceState");
        }

        //localDB = LocalDB.getInstance(getContext());
        localDB = new LocalDB(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_scent, container, false);

        //Set name and id
        id = getArguments().getString(ScentActivity.getIdKey());
        name = getArguments().getString(ScentActivity.getNameKey());
        Log.d(TAG, "onCreateView, the scent info is " + id + " " + name);

        nameView = (TextView) thisView.findViewById(R.id.scent_detail_name);
        idView = (TextView) thisView.findViewById(R.id.scent_detail_id);
        ingredView = (TextView) thisView.findViewById(R.id.ingredient_textview);

        nameView.setText(name);
        idView.setText(id);

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
        //Activity activity = (Activity) getContext();

        rating = (AppCompatRatingBar) thisView.findViewById(R.id.detail_rating_bar);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Activity activity = getActivity();

                SharedPreferences sharedPrefs = getActivity().getSharedPreferences(getString(R.string.prefs_file), getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putLong(getString(R.string.ratings_last_updated), System.currentTimeMillis());

                //Save or update the rating
                if (rating == 0) {
                    //Clear item from DB if it exists
                    boolean result = localDB.removeScent(id);
                    Log.d(TAG, "onRatingChanged deleteScent result = " + result);

                } else {
                    //Update rating and store scent with rating in local DB
                    boolean result = localDB.insertScent(id, name, rating);
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

        return thisView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //localDB.closeDB();
    }



}
