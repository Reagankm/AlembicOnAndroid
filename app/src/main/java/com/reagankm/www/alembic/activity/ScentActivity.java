package com.reagankm.www.alembic.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.ScentFragment;
import com.reagankm.www.alembic.model.ScentInfo;

public class ScentActivity extends AppCompatActivity {

    private static final String SCENT_ID_SELECTED
            = "com.reagankm.www.alembic.scent_id_selected";
    private static final String SCENT_NAME_SELECTED
            = "com.reagankm.www.alembic.scent_name_selected";
    private static final String TAG = "ScentActivityTag";

    private String scentId;
    private String scentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //To restore scent selected if user rotates screen
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


    public static String getNameKey() {
        return SCENT_NAME_SELECTED;
    }

    public static String getIdKey() {
        return SCENT_ID_SELECTED;
    }

    public static Intent createIntent(Context packageContext, ScentInfo scent) {
        Intent i = new Intent(packageContext, ScentActivity.class);
        i.putExtra(SCENT_ID_SELECTED, scent.getId());
        i.putExtra(SCENT_NAME_SELECTED, scent.getName());
        Log.d(TAG, "createIntent with ScentInfo object name " + scent.getName()
                + ", id " + scent.getId());
        return i;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SCENT_ID_SELECTED, scentId);
        savedInstanceState.putString(SCENT_NAME_SELECTED, scentName);
    }

}
