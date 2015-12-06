package com.reagankm.www.alembic.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.ViewRatingsFragment;

public class ViewRatingsActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ratings);

        //Display ScentList fragment
        FragmentManager fragManager = getSupportFragmentManager();
        Fragment theFragment = fragManager.findFragmentById(R.id.fragment_list_container);

        if (theFragment == null) {
            theFragment = new ViewRatingsFragment();
            //Bundle bundle = new Bundle();

            //bundle.putString(EXTRA_LETTER_SELECTED, letterSelected);
            //theFragment.setArguments(bundle);

            fragManager.beginTransaction()
                    .add(R.id.fragment_list_container, theFragment)
                    .commit();
        }
    }






}
