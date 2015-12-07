package com.reagankm.www.alembic.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reagankm.www.alembic.model.Scent;
import com.reagankm.www.alembic.model.ScentAdapter;
import com.reagankm.www.alembic.model.ScentInfo;

import java.util.List;

/**
 * Displays a list of scents.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public abstract class AbstractListFragment extends Fragment {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "AbstractListFragmentTag";

    /** A recycler view to hold the visible items in the list. */
    private RecyclerView scentListRecyclerView;

    /** The view for this list. */
    protected View thisView;

    public AbstractListFragment() {
        // Required empty public constructor
    }


    /**
     * Creates the UI.
     *
     * @param savedInstanceState any saved instance data
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    /**
     * Creates the View for this list.
     *
     * @param inflater the layout inflater
     * @param container the view group container
     * @param savedInstanceState any saved instance data
     * @return the view for this lsit
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView");
        thisView = inflateView(inflater, container);

        scentListRecyclerView = fetchScentListRecyclerView();
        scentListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();


        return thisView;
    }

    /**
     * Inflates the view with the given layout inflater and view group.
     *
     * @param inflater the inflater for the layout
     * @param container the container for the view
     * @return the view
     */
    public abstract View inflateView(LayoutInflater inflater, ViewGroup container);

    /**
     * Updates the current state of the list UI.
     */
    public void updateUI() {
        List<ScentInfo> scents = Scent.getAllItems();

        ScentAdapter scentAdapter = new ScentAdapter(scents);
        scentListRecyclerView.setAdapter(scentAdapter);

    }

    /**
     * Fetch the RecyclerView (the container for the visible scents).
     * @return the recycler view
     */
    public abstract RecyclerView fetchScentListRecyclerView();



}




