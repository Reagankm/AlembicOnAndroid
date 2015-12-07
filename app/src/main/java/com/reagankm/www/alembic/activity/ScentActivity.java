package com.reagankm.www.alembic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.ScentFragment;
import com.reagankm.www.alembic.model.ScentInfo;

/**
 * Displays details of a particular scent.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class ScentActivity extends MenuActivity {

    /** A key for storing and fetching scent IDs. */
    private static final String SCENT_ID_SELECTED
            = "com.reagankm.www.alembic.scent_id_selected";

    /** A key for storing and fetching scent names. */
    private static final String SCENT_NAME_SELECTED
            = "com.reagankm.www.alembic.scent_name_selected";

    /** The tag to use when logging from this activity. */
    private static final String TAG = "ScentActivityTag";

    /** The ID of this scent. */
    private String scentId;

    /** The name of this scent. */
    private String scentName;

    /**
     * Creates the UI.
     *
     * @param savedInstanceState any saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Restore scent selected if user rotates screen
        if (savedInstanceState != null){
            scentId = savedInstanceState.getString(SCENT_ID_SELECTED);
            scentName = savedInstanceState.getString(SCENT_NAME_SELECTED);
            Log.d(TAG, "onCreate, restoring from savedInstanceState for scent " +
                    "with id " + scentId + ", name " + scentName);
        } else {
            scentId = getIntent().getStringExtra(SCENT_ID_SELECTED);
            scentName = getIntent().getStringExtra(SCENT_NAME_SELECTED);
        }

        setContentView(R.layout.activity_scent);

        //Display ScentList fragment
        FragmentManager fragManager = getSupportFragmentManager();
        Fragment theFragment = fragManager.findFragmentById(R.id.fragment_container);

        if (theFragment == null) {
            theFragment = new ScentFragment();
            Bundle bundle = new Bundle();

            bundle.putString(SCENT_ID_SELECTED, scentId);
            bundle.putString(SCENT_NAME_SELECTED, scentName);
            theFragment.setArguments(bundle);

            fragManager.beginTransaction()
                    .add(R.id.fragment_container, theFragment)
                    .commit();
        }

    }

    /**
     * Get the key for storing/fetching the scent name.
     *
     * @return the key for the scent name
     */
    public static String getNameKey() {
        return SCENT_NAME_SELECTED;
    }

    /**
     * Get the key for storing/fetching the scent ID.
     * @return the key for the scent id
     */
    public static String getIdKey() {
        return SCENT_ID_SELECTED;
    }

    /**
     * Creates an Intent for launching a ScentActivity which includes
     * details of the chosen scent.
     *
     * @param packageContext the calling Activity
     * @param scent the scent whose details should be displayed
     * @return the Intent
     */
    public static Intent createIntent(Context packageContext, ScentInfo scent) {
        Intent i = new Intent(packageContext, ScentActivity.class);
        i.putExtra(SCENT_ID_SELECTED, scent.getId());
        i.putExtra(SCENT_NAME_SELECTED, scent.getName());
        Log.d(TAG, "createIntent with ScentInfo object name " + scent.getName()
                + ", id " + scent.getId());
        return i;
    }

    /**
     * Saves the current state of the scent activity.
     *
     * @param savedInstanceState the savedInstanceState where data can be stored
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SCENT_ID_SELECTED, scentId);
        savedInstanceState.putString(SCENT_NAME_SELECTED, scentName);
    }

}
