package com.reagankm.www.alembic.model;

import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

/**
 * Gets the widgets from my item_list_scent view (the row view for each scent) and
 * assigns their values.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class ScentHolder extends AbstractScentHolder {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "ScentHolderTag";

    /**
     * Constructs a ScentHolder with the given view.
     *
     * @param itemView the item view
     */
    public ScentHolder(View itemView) {
        super(itemView);
    }

    /**
     * Assigns data for the given scent to the UI widgets.
     *
     * @param scent the current scent
     */
    @Override
    public void bindScent(ScentInfo scent) {

        //Assign scent name
        setScent(scent);
        Log.d(TAG, "bindScent with id " + scent.getId() + ", name " + scent.getName());
        getNameTextView().setText(scent.getName());

        //Assign rating or, if none exists, hide rating bar
        LocalDB db = new LocalDB(theContext);
        float rating = db.getRating(scent.getId());
        db.closeDB();

        RatingBar rb = getRatingBar();
        if (rating > 0) {

            rb.setRating(rating);
            rb.setVisibility(View.VISIBLE);
        } else {
            rb.setVisibility(View.GONE);
        }
    }


}
