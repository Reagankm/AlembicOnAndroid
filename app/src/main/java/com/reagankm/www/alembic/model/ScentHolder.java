package com.reagankm.www.alembic.model;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.activity.ScentActivity;

/**
 * Created by reagan on 11/17/15.
 */
//Gets the widgets from my item_list_scent view (the row view for each scent)
//and assigns their values
public class ScentHolder extends AbstractScentHolder {

    private static final String TAG = "ScentHolderTag";

    public ScentHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindScent(ScentInfo scent) {
        setScent(scent);
        Log.d(TAG, "bindScent with id " + scent.getId() + ", name " + scent.getName());
        getNameTextView().setText(scent.getName());

        LocalDB db = new LocalDB(theContext);

        float rating = db.getRating(scent.getId());
        RatingBar rb = getRatingBar();
        if (rating > 0) {

            rb.setRating(rating);
            rb.setVisibility(View.VISIBLE);
        } else {
            rb.setVisibility(View.GONE);
        }
    }


}
