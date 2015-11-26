package com.reagankm.www.alembic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.reagankm.www.alembic.R;

import java.util.HashMap;
import java.util.Map;

public class RecommendationAgentActivity extends AppCompatActivity {

    TextView noRecommendations;
    RecyclerView rv;
    Map<String, Integer> goodPairs;
    Map<String, Integer> badPairs;
    //Only want to show a small number of reccs at a time, but calculating these is
    //labor intensive so don't want to duplicate work/calls
    private static final int NUMBER_OF_SCENTS_TO_RECOMMEND = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_agent);

        noRecommendations = (TextView) findViewById(R.id.no_recommendations);
        rv = (RecyclerView) findViewById(R.id.recommendations_recycler_view);

        goodPairs = new HashMap<>();
        badPairs = new HashMap<>();

        //TODO: Display message if user has no ratings

        //TODO: Choose recommendation if user only has 3s

    }

    //Load scent pairs for all scents rated 4 or 5
    private void loadGoodPairs() {

    }

    //Load scent pairs for all scents rated 1 or 2
    private void loadBadPairs() {

    }
}
