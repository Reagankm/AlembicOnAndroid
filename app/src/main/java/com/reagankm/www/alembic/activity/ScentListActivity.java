package com.reagankm.www.alembic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.ScentListFragment;

/**
 * Displays a list of selectable scents and their ratings.
 */
public class ScentListActivity extends FragmentActivity {

    /** A key for storing the first letter of the scents to display. */
    private static final String EXTRA_LETTER_SELECTED
            = "com.reagankm.www.alembic.extra_letter_selected";

    /** The first letter of the scents to dispay. */
    private String letterSelected;

    /**
     * Get the key used to store/fetch the first letter of the scents to display.
     *
     * @return the key
     */
    public static String getLetterSelectedKey() {
        return EXTRA_LETTER_SELECTED;
    }

    /**
     * Creates the UI.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To restore letterSelected if user rotates screen
        if (savedInstanceState != null){
            letterSelected = savedInstanceState.getString(EXTRA_LETTER_SELECTED);
        } else {
            letterSelected = getIntent().getStringExtra(EXTRA_LETTER_SELECTED);
        }


        setContentView(R.layout.activity_scent_list);

        //Display ScentList fragment
        FragmentManager fragManager = getSupportFragmentManager();
        Fragment theFragment = fragManager.findFragmentById(R.id.fragment_list_container);

        if (theFragment == null) {
            theFragment = new ScentListFragment();
            Bundle bundle = new Bundle();

            //Pass the fragment the letter selected
            bundle.putString(EXTRA_LETTER_SELECTED, letterSelected);
            theFragment.setArguments(bundle);

            fragManager.beginTransaction()
                    .add(R.id.fragment_list_container, theFragment)
                    .commit();
        }



    }

    /**
     * Create an intent that can be sued to call this activity.
     *
     * @param packageContext the calling activity
     * @param letter the first letter of the scents to display in the list
     * @return the intent
     */
    public static Intent createIntent(Context packageContext, String letter) {
        Intent i = new Intent(packageContext, ScentListActivity.class);
        i.putExtra(EXTRA_LETTER_SELECTED, letter);
        return i;
    }

    /**
     * Save the current state of activity.
     *
     * @param savedInstanceState where the data can be saved
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(EXTRA_LETTER_SELECTED, letterSelected);
    }

}
