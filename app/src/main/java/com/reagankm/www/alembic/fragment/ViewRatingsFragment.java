package com.reagankm.www.alembic.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 */
public class ViewRatingsFragment extends AbstractListFragment {

    private static final String TAG = "ViewRatingsFragmentTag";
    LocalDB db;
    TextView emptyText;
  //  Activity act;


    public ViewRatingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View v = super.onCreateView(inflater, container, savedInstanceState);
        //act = (Activity) getContext();
        return v;

    }

    //Get the list of rated scents
    @Override
    public void onStart() {
        super.onStart();

        //db = LocalDB.getInstance(getContext());

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emptyText = (TextView) view.findViewById(R.id.empty_ratings_text_view);
        db = new LocalDB(getContext());
        int count = db.getRatedCount();
        Log.d(TAG, "onCreateView: got local rated count");
        if (count == 0) {
            //display message telling user they haven't rated anything
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


    }

    @Override
    public RecyclerView fetchScentListRecyclerView() {
        return (RecyclerView) thisView.findViewById(R.id.view_ratings_recycler_view);
    }



    @Override
    public View inflateView(LayoutInflater inflater, ViewGroup container) {
        Log.d(TAG, "inflateView");
        View v = inflater.inflate(R.layout.fragment_view_ratings, container, false);

        Log.d(TAG, "inflateView: Assigned emptyText");

        return v;
    }

    @Override
    public void updateUI() {

        if (db != null) {
            List<ScentInfo> allRatings = db.getAllRatedScents();
            if (allRatings != null && allRatings.size() > 0) {
                Scent.setAllItems(allRatings);
            }
        }



        super.updateUI();

    }


}
