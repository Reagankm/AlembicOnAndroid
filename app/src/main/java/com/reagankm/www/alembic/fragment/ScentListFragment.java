package com.reagankm.www.alembic.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.model.LocalDB;
import com.reagankm.www.alembic.webtask.ScentQueryTask;
import com.reagankm.www.alembic.activity.ScentActivity;
import com.reagankm.www.alembic.activity.ScentListActivity;
import com.reagankm.www.alembic.model.Scent;
import com.reagankm.www.alembic.model.ScentInfo;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScentListFragment extends AbstractListFragment {

    private static final String TAG = "ScentListFragmentTag";

    private static final String LAST_POSITION
            = "com.reagankm.www.alembic.last_position";

    private String letter;


    private int lastFirstVisiblePosition;



    public ScentListFragment() {
        // Required empty public constructor
        Log.d(TAG, "ScentListFragment created");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            letter = savedInstanceState.getString(ScentListActivity.getLetterSelectedKey());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        letter = getArguments().getString(ScentListActivity.getLetterSelectedKey());
        Log.d(TAG, "onCreateView, the letter is " + letter);

        return thisView;
    }

    //Get the list of scents starting with the chosen letter
    @Override
    public void onStart() {
        super.onStart();
        //Run scent query to populate list based on letter selected
        new ScentQueryTask(getContext(), this).execute(letter);
    }



}
