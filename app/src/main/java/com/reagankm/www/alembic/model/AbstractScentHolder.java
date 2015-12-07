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
 * Holds a scent for a recycler view, populates its UI and controls its behavior
 * when clicked.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public abstract class AbstractScentHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "AbstractScentHolderTag";

    /** The root view. */
    private RelativeLayout rootView;

    /** The context. */
    protected Context theContext;

    /** The textview that shows the scent name. */
    private TextView nameTextView;

    /** The rating bar that shows the scent's rating. */
    private AppCompatRatingBar ratingBar;

    /** The scent. */
    private ScentInfo scent;

    /**
     * Creates an abstract scent holder with the given view.
     * @param itemView the view for the scent
     */
    public AbstractScentHolder(View itemView) {
        super(itemView);

        nameTextView = (TextView) itemView.findViewById(R.id.item_list_scent_name);
        ratingBar = (AppCompatRatingBar) itemView.findViewById(R.id.item_list_rating_bar);
        rootView = (RelativeLayout) itemView.findViewById(R.id.rootView);
        theContext = rootView.getContext();

        itemView.setOnClickListener(this);
    }

    /**
     * Gets the root view.
     *
     * @return the root view.
     */
    public RelativeLayout getRootView() {
        return rootView;
    }

    /**
     * Bind a scent to its view.
     *
     * @param scent
     */
    abstract public void bindScent(ScentInfo scent);

    /**
     * When clicked, launch a Scent Activity with details of the
     * selected scent.
     *
     * @param v the view
     */
    @Override
    public void onClick(View v) {


        Log.d(TAG, "onClick creating intent for scent with id " + scent.getId()
                + ", name " + scent.getName());
        Intent i = ScentActivity.createIntent(theContext, scent);

        theContext.startActivity(i);
    }

    /**
     * Gets the textview for the scent's name.
     *
     * @return the textview
     */
    public TextView getNameTextView() {
        return nameTextView;
    }

    /**
     * Gets the rating bar for the scent's rating.
     *
     * @return the bar
     */
    public AppCompatRatingBar getRatingBar() {
        return ratingBar;
    }

    /**
     * Gets the scent.
     *
     * @return the scent
     */
    public ScentInfo getScent() {
        return scent;
    }

    /**
     * Sets the scent.
     * @param scent the scent to set
     */
    public void setScent(ScentInfo scent) {
        this.scent = scent;
    }



}
