package com.reagankm.www.alembic.fragment;


import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.activity.ScentActivity;
import com.reagankm.www.alembic.activity.ScentListActivity;
import com.reagankm.www.alembic.model.LocalDB;
import com.reagankm.www.alembic.model.Scent;
import com.reagankm.www.alembic.model.ScentAdapter;
import com.reagankm.www.alembic.model.ScentHolder;
import com.reagankm.www.alembic.model.ScentInfo;
import com.reagankm.www.alembic.webtask.ScentQueryTask;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AbstractListFragment extends Fragment {

    View thisView;

    private RecyclerView scentListRecyclerView;

    private static final String TAG = "AbstractListFragmentTag";


    public AbstractListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        thisView = inflateView(inflater, container);

        scentListRecyclerView = fetchScentListRecyclerView();
               // = (RecyclerView) thisView.findViewById(R.id.abstract_recycler_view);
        scentListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();


        return thisView;
    }

    public abstract View inflateView(LayoutInflater inflater, ViewGroup container);

    public void updateUI() {
        List<ScentInfo> scents = Scent.getAllItems();

        ScentAdapter scentAdapter = new ScentAdapter(scents);
        scentListRecyclerView.setAdapter(scentAdapter);

    }

    public abstract RecyclerView fetchScentListRecyclerView();



}




