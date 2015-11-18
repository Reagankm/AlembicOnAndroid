package com.reagankm.www.alembic.model;

import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

/**
 * Created by reagan on 11/17/15.
 */
public class RatedScentHolder extends AbstractScentHolder {

    private static final String TAG = "RatedScentHolderTag";

    public RatedScentHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindScent(ScentInfo scent) {
        setScent(scent);
        Log.d(TAG, "bindScent with id " + scent.getId() + ", name " + scent.getName());
        getNameTextView().setText(scent.getName());
        RatingBar rb = getRatingBar();
        rb.setRating(scent.getRating());
    }

}