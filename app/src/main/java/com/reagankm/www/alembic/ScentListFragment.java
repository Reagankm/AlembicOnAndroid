package com.reagankm.www.alembic;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.reagankm.www.alembic.model.Scent;
import com.reagankm.www.alembic.model.ScentInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScentListFragment extends ListFragment {

    private static final String TAG = "ScentListFragmentTag";

    private List<ScentInfo> scentList;

    private ScentListAdapter<ScentInfo> mAdapter;

    private static final String
            url = "http://cssgate.insttech.washington.edu/~reagankm/queryScents.php";

    private ListView mListView;

    private String letter;

    private View thisView;



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
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_scent_list, container, false);
        letter = getArguments().getString(ScentListActivity.getLetterSelectedKey());
        Log.d(TAG, "onCreateView, the letter is " + letter);
        return thisView;
    }

    //Get the list of scents starting with the chosen letter
    @Override
    public void onStart() {
        super.onStart();
        Context c = thisView.getContext();
        ConnectivityManager connMgr = (ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            
            Scent.ITEMS.clear();
            new ScentWebTask().execute(url + "?letter=" + letter);
        } else {
            Toast.makeText(c
                    , "No network connection available.", Toast.LENGTH_SHORT)
                    .show();
        }

        mListView = (ListView) thisView.findViewById(android.R.id.list);
        scentList = Scent.ITEMS;

        mAdapter = new ScentListAdapter<>(c, scentList);

        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        ScentInfo scent = Scent.getItem(position);

        //View v = view.getTag();

        //TextView textView = (TextView) view.;
        String name = scent.name;
        String scentId = scent.id;


        //for debugging
        Toast.makeText(getContext(),
                "Scent chosen: " + name + " " + scentId, Toast.LENGTH_SHORT).show();


        Intent scentDetail = ScentActivity.createIntent(getContext(), scent);
        startActivity(scentDetail);

    }

    private class ScentListAdapter<T> extends ArrayAdapter<ScentInfo> {

        public ScentListAdapter(Context context, List<ScentInfo> scents) {
            super(context, 0, scents);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            // Get the data item for this position
            ScentInfo scent = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_scent, parent, false);

            }
            // Lookup view for data population
            TextView idView = (TextView) view.findViewById(R.id.scent_list_id);
            TextView nameView = (TextView) view.findViewById(R.id.scent_list_name);
            // Populate the data into the template view using the data object
            idView.setText(scent.id);
            nameView.setText(scent.name);
            // Return the completed view to render on screen

            return view;
        }
    }



}
