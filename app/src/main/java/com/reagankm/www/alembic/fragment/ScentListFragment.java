package com.reagankm.www.alembic.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.activity.ScentListActivity;
import com.reagankm.www.alembic.webtask.ScentQueryTask;


/**
 * Displays a list of scents that start with the same letter.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class ScentListFragment extends AbstractListFragment {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "ScentListFragmentTag";

    /** The first letter of the scents to list. */
    private String letter;


    /**
     * Constructs a scent list fragment.
     */
    public ScentListFragment() {
        // Required empty public constructor

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            //Fetches the previous letter if one is available
            letter = savedInstanceState.getString(ScentListActivity.getLetterSelectedKey());

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //Save the first letter of the scents to display
        letter = getArguments().getString(ScentListActivity.getLetterSelectedKey());
        Log.d(TAG, "onCreateView, the letter is " + letter);

        return thisView;
    }


    /**
     * When the Fragment is started, get the list of scents beginning
     * with the chosen letter.
     */
    @Override
    public void onStart() {
        super.onStart();
        //Run scent query to populate list based on letter selected
        new ScentQueryTask(getContext(), this).execute(letter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecyclerView fetchScentListRecyclerView() {
        return (RecyclerView) thisView.findViewById(R.id.scent_recycler_view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_scent_list, container, false);
    }


}
