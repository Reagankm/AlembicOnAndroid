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
public class ScentListFragment extends Fragment {

    private static final String TAG = "ScentListFragmentTag";

    private static final String LAST_POSITION
            = "com.reagankm.www.alembic.last_position";

    private String letter;

    private View thisView;

    private RecyclerView scentListRecyclerView;

    private ScentAdapter scentAdapter;

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
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_scent_list, container, false);
        letter = getArguments().getString(ScentListActivity.getLetterSelectedKey());
        Log.d(TAG, "onCreateView, the letter is " + letter);

        scentListRecyclerView
                = (RecyclerView) thisView.findViewById(R.id.scent_recycler_view);
        scentListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();


        return thisView;
    }

    public void updateUI() {
        List<ScentInfo> scents = Scent.getAllItems();
        scentAdapter = new ScentAdapter(scents);
        scentListRecyclerView.setAdapter(scentAdapter);

    }

    //Get the list of scents starting with the chosen letter
    @Override
    public void onStart() {
        super.onStart();
        //Run scent query to populate list based on letter selected
        new ScentQueryTask(getContext(), this).execute(letter);
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }



    //

    //@Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        lastFirstVisiblePosition = ((LinearLayoutManager) scentListRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
//        //outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, scentListRecyclerView.getLayoutManager().onSaveInstanceState());
//    }

    //Gets the widgets from my item_list_scent view (the row view for each scent)
    //and assigns their values
    private class ScentHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private RelativeLayout rootView;

        private static final String TAG = "ScentHolderTag";

        private TextView nameTextView;
        private AppCompatRatingBar ratingBar;

        private ScentInfo scent;

        public ScentHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.item_list_scent_name);
            ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.item_list_rating_bar);
            rootView=(RelativeLayout) itemView.findViewById(R.id.rootView);
            itemView.setOnClickListener(this);
        }


        public void bindScent(ScentInfo scent) {
            this.scent = scent;
            Log.d(TAG, "bindScent with id " + scent.getId() + ", name " + scent.getName());
            nameTextView.setText(scent.getName());
            LocalDB db = new LocalDB(getContext());
            float rating = db.getRating(scent.getId());
            if (rating > 0) {
                ratingBar.setRating(rating);
                ratingBar.setVisibility(View.VISIBLE);
            } else {
                ratingBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "oncClick creating intent for scent with id " + scent.getId()
                    + ", name " + scent.getName());
            Intent i = ScentActivity.createIntent(getContext(), scent);

            startActivity(i);
        }
    }

    private class ScentAdapter extends RecyclerView.Adapter<ScentHolder> {
        private static final String TAG = "ScentAdapterTag";
        private List<ScentInfo> scentList;

        public ScentAdapter(List<ScentInfo> scents) {
            scentList = scents;
        }

        //Create a view and wrap it in a holder
        @Override
        public ScentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.item_list_scent, parent, false);

            return new ScentHolder(v);
        }

        //Bind a view to my model for this position
        @Override
        public void onBindViewHolder(ScentHolder holder, int position) {
            ScentInfo scent = scentList.get(position);
            //holder.nameTextView.setText(scent.name);
            holder.bindScent(scent);

            //Change color every other row

            if(position % 2 == 0)
            {

                holder.rootView.setBackgroundResource(R.color.accent);
            }
            else
            {

                holder.rootView.setBackgroundResource(R.color.icons);
            }
        }


        @Override
        public int getItemCount() {
            return scentList.size();
        }
    }


}
