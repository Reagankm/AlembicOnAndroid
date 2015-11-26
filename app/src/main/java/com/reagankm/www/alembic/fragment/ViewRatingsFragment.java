package com.reagankm.www.alembic.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.activity.ScentListActivity;
import com.reagankm.www.alembic.model.LocalDB;
import com.reagankm.www.alembic.model.Scent;
import com.reagankm.www.alembic.model.ScentAdapter;
import com.reagankm.www.alembic.model.ScentInfo;
import com.reagankm.www.alembic.webtask.ScentQueryTask;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewRatingsFragment extends AbstractListFragment {

    private static final String TAG = "ViewRatingsFragmentTag";
    LocalDB db;
    TextView emptyText;
    Activity act;


    public ViewRatingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        act = (Activity) getContext();
        emptyText = (TextView) act.findViewById(R.id.empty_ratings_text_view);
        return v;

    }

    //Get the list of rated scents
    @Override
    public void onStart() {
        super.onStart();

        db = LocalDB.getInstance(getContext());
        int count = db.getCount();

        if (count == 0) {
            //display message telling user they haven't rated anything
            emptyText.setVisibility(View.VISIBLE);

            //Hide recycler view
            RecyclerView rv = (RecyclerView) act.findViewById(R.id.view_ratings_recycler_view);
            rv.setVisibility(View.GONE);

        } else {



            List<ScentInfo> allRatings = db.getRatings();
            if (allRatings != null && allRatings.size() > 0) {
                Scent.setAllItems(allRatings);
            } else {
                Log.e(TAG, "onStart, count = " + count + " but db.getRatings() returned null" +
                        " or empty list: " + allRatings);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public RecyclerView fetchScentListRecyclerView() {
        return (RecyclerView) thisView.findViewById(R.id.view_ratings_recycler_view);
    }

    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_view_ratings, container, false);
    }

    @Override
    public void updateUI() {

        if (db != null) {
            List<ScentInfo> allRatings = db.getRatings();
            if (allRatings != null && allRatings.size() > 0) {
                Scent.setAllItems(allRatings);
            }
        }



        super.updateUI();

    }


}
