package com.reagankm.www.alembic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.model.LocalDB;
import com.reagankm.www.alembic.model.ScentInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendationAgentActivity extends AppCompatActivity {

    TextView noRecommendations;
    RecyclerView rv;
    Map<String, Integer> goodPairs;
    Map<String, Integer> badPairs;
    LocalDB db;
    List<ScentInfo> allRecommendations;
    //Only want to show a small number of reccs at a time, but calculating these is
    //labor intensive so don't want to duplicate work/calls
    private static final int NUMBER_OF_SCENTS_TO_RECOMMEND = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_agent);

        db = LocalDB.getInstance(this);

        rv = (RecyclerView) findViewById(R.id.recommendations_recycler_view);

        //TODO: Display message if user has no ratings
        if (db.getCount() < 1) {
            noRecommendations = (TextView) findViewById(R.id.no_recommendations);
            rv.setVisibility(View.GONE);
            noRecommendations.setVisibility(View.VISIBLE);
        }

        List<ScentInfo> allScents = db.getAllRatedScents();

        //sort by rating from highest to lowest
        Collections.sort(allScents, new Comparator<ScentInfo>() {

            public int compare(ScentInfo a, ScentInfo b) {

                return (int) (b.getRating() - a.getRating());

            }

        });

        //ScentInfo current = allScents.get(0);

        goodPairs = new HashMap<>();  //>= 4
        badPairs = new HashMap<>(); //<= 2

        int i;
        for (i = 0; i < allScents.size() && allScents.get(i).getRating() >= 4; i++ ) {

            loadIngredientPairs(allScents.get(i), goodPairs);

        }

        int j;
        for (j = allScents.size() - 1; j >= 0 && allScents.get(j).getRating() <= 2; j--) {
            loadIngredientPairs(allScents.get(i), badPairs);
        }

        //TODO: load up a mediumPairs map if necessary using i and j as start/end indexes

        //List<String> goodScents; // >= 4
        //List<String> badScents; // <= 2
        //List<String> mediumScents; //> 2 and < 4







        //TODO: Choose recommendation if user only has 3s

    }

    private void loadIngredientPairs(ScentInfo scent, Map<String, Integer> pairMap) {

        List<String> ingredients = scent.getIngredientList();

        for (int i = 0; i < ingredients.size() - 1; i++) {

            String note1 = ingredients.get(i);

            for (int j = i + 1; j < ingredients.size(); j++) {
                String note2 = ingredients.get(j);
                String pair = note1.compareTo(note2) < 0 ? "" + note1 + note2 : "" + note2 + note1;
                Integer occurrences = pairMap.get(pair);

                if (occurrences == null) {
                    pairMap.put(pair, 1);
                } else {
                    pairMap.put(pair, occurrences + 1);
                }

            }
        }

    }

    //Load scent pairs for all scents rated 4 or 5
    private void loadGoodPairs() {

    }

    //Load scent pairs for all scents rated 1 or 2
    private void loadBadPairs() {

    }


}
