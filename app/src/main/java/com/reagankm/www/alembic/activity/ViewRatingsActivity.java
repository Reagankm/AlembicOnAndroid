package com.reagankm.www.alembic.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.ViewRatingsFragment;


/**
 * Displays all rated scents.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class ViewRatingsActivity extends MenuActivity {

    /**
     * Creates the UI.
     *
     * @param savedInstanceState any saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ratings);

        //Display ScentList fragment
        FragmentManager fragManager = getSupportFragmentManager();
        Fragment theFragment = fragManager.findFragmentById(R.id.fragment_list_container);

        if (theFragment == null) {
            theFragment = new ViewRatingsFragment();

            fragManager.beginTransaction()
                    .add(R.id.fragment_list_container, theFragment)
                    .commit();
        }
    }
}
