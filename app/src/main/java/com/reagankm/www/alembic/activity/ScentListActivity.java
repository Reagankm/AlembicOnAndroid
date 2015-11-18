package com.reagankm.www.alembic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.ScentListFragment;

public class ScentListActivity extends FragmentActivity {

    private static final String EXTRA_LETTER_SELECTED
            = "com.reagankm.www.alembic.extra_letter_selected";

    private String letterSelected;

    public static String getLetterSelectedKey() {
        return EXTRA_LETTER_SELECTED;
    }

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

            bundle.putString(EXTRA_LETTER_SELECTED, letterSelected);
            theFragment.setArguments(bundle);

            fragManager.beginTransaction()
                    .add(R.id.fragment_list_container, theFragment)
                    .commit();
        }



    }

    public static Intent createIntent(Context packageContext, String letter) {
        Intent i = new Intent(packageContext, ScentListActivity.class);
        i.putExtra(EXTRA_LETTER_SELECTED, letter);
        return i;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(EXTRA_LETTER_SELECTED, letterSelected);
    }

}
