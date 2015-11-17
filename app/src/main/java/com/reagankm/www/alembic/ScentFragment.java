package com.reagankm.www.alembic;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScentFragment extends Fragment {

    private static final String TAG = "ScentFragmentTag";

    private String name;
    private String id;
    private TextView nameView;
    private TextView idView;

    private View thisView;

    private AppCompatRatingBar rating;

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

        Activity activity = (Activity) getContext();

        rating = (AppCompatRatingBar) activity.findViewById(R.id.detail_rating_bar);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //TODO: Save rating
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_scent, container, false);
        id = getArguments().getString(ScentActivity.getIdKey());
        name = getArguments().getString(ScentActivity.getNameKey());
        Log.d(TAG, "onCreateView, the scent info is " + id + " " + name);

        nameView = (TextView) thisView.findViewById(R.id.scent_detail_name);
        idView = (TextView) thisView.findViewById(R.id.scent_detail_id);

        nameView.setText(name);
        idView.setText(id);

        return thisView;
    }



}
