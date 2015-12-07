package com.reagankm.www.alembic.fragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.model.LocalDB;
import com.reagankm.www.alembic.model.Scent;
import com.reagankm.www.alembic.model.ScentInfo;

import java.util.List;

/**
 * Displays a list of rated scents.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class ViewRatingsFragment extends AbstractListFragment {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "ViewRatingsFragmentTag";

    /**
     * Constructs a fragment.
     */
    public ViewRatingsFragment() {
        // Required empty public constructor
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    /**
     * On Resume, update the UI.
     */
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    /**
     * When the view is created, populate it with rated scents.
     *
     * @param view the view
     * @param savedInstanceState any saved instance data
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //Check whether user has rated any scents
        LocalDB db = new LocalDB(getContext());
        int count = db.getRatedCount();
        Log.d(TAG, "onCreateView: got local rated count");
        if (count == 0) {

            //display message telling user they haven't rated anything
            TextView emptyText = (TextView) view.findViewById(R.id.empty_ratings_text_view);

            emptyText.setVisibility(View.VISIBLE);

            //Hide recycler view
            RecyclerView rv = (RecyclerView) view.findViewById(R.id.view_ratings_recycler_view);
            rv.setVisibility(View.GONE);

        } else {

            List<ScentInfo> allRatings = db.getAllRatedScents();
            if (allRatings != null && allRatings.size() > 0) {
                Scent.setAllItems(allRatings);
            } else {
                Log.e(TAG, "onStart, count = " + count + " but db.getRatings() returned null" +
                        " or empty list: " + allRatings);
            }
        }

        db.closeDB();


    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RecyclerView fetchScentListRecyclerView() {
        return (RecyclerView) thisView.findViewById(R.id.view_ratings_recycler_view);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        Log.d(TAG, "inflateView");
        View v = inflater.inflate(R.layout.fragment_view_ratings, container, false);

        Log.d(TAG, "inflateView: Assigned emptyText");

        return v;
    }

    /**
     * Populate the list of scents with rated scents.
     */
    @Override
    public void updateUI() {
        LocalDB db = new LocalDB(getContext());


        List<ScentInfo> allRatings = db.getAllRatedScents();
        db.closeDB();
        if (allRatings != null && allRatings.size() > 0) {
            Scent.setAllItems(allRatings);
        }

        super.updateUI();

    }


}
