package com.reagankm.www.alembic.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.activity.ScentActivity;

/**
 * Created by reagan on 11/17/15.
 */
public abstract class AbstractScentHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private RelativeLayout rootView;
    Context theContext;

    private static final String TAG = "AbstractScentHolderTag";

    private TextView nameTextView;
    private AppCompatRatingBar ratingBar;

    private ScentInfo scent;

    public AbstractScentHolder(View itemView) {
        super(itemView);
        nameTextView = (TextView) itemView.findViewById(R.id.item_list_scent_name);
        ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.item_list_rating_bar);
        rootView = (RelativeLayout) itemView.findViewById(R.id.rootView);
        theContext = rootView.getContext();
        itemView.setOnClickListener(this);
    }

    public RelativeLayout getRootView() {
        return rootView;
    }

    abstract public void bindScent(ScentInfo scent);

    @Override
    public void onClick(View v) {


        Log.d(TAG, "onClick creating intent for scent with id " + scent.getId()
                + ", name " + scent.getName());
        Intent i = ScentActivity.createIntent(theContext, scent);

        theContext.startActivity(i);
    }

    public TextView getNameTextView() {
        return nameTextView;
    }


    public AppCompatRatingBar getRatingBar() {
        return ratingBar;
    }

    public ScentInfo getScent() {
        return scent;
    }

    public void setScent(ScentInfo scent) {
        this.scent = scent;
    }



}
