package com.reagankm.www.alembic.model;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reagankm.www.alembic.R;

import java.util.List;

/**
 * Holds the list of scents to be displayed in a RecylerView, creates ScentHolders
 * for them, and binds them together.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class ScentAdapter extends RecyclerView.Adapter<ScentHolder> {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "ScentAdapterTag";

    /** The list of ScentInfo to display.*/
    private List<ScentInfo> scentList;

    /**
     * Creates a ScentAdapter with the given list of ScentInfo.
     *
     * @param scents the list
     */
    public ScentAdapter(List<ScentInfo> scents) {
        scentList = scents;
    }

    /**
     * Create a view and wrap it in a ScentHolder.
     * @param parent the parent view group
     * @param viewType the view type
     * @return the new ScentHolder
     */
    @Override
    public ScentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context theContext = parent.getContext();
        Activity activity = (Activity) theContext;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.item_list_scent, parent, false);

        return new ScentHolder(v);
    }

    /**
     * Bind a view to the ScentInfo at the selected position index.
     *
     * @param holder the ScentHolder to be updated
     * @param position the position of the ScentInfo to populate the holder
     */
    @Override
    public void onBindViewHolder(ScentHolder holder, int position) {

        //Bind the scent holder to the scent
        ScentInfo scent = scentList.get(position);
        holder.bindScent(scent);

        //Change color every other row
        if(position % 2 == 0)
        {

            holder.getRootView().setBackgroundResource(R.color.accent);
            holder.getRatingBar().setBackgroundColor(R.color.primary_light);

        }
        else
        {

            holder.getRootView().setBackgroundResource(R.color.icons);
        }
    }

    /**
     * Gets the total number of ScentInfo items.
     *
     * @return the total count
     */
    @Override
    public int getItemCount() {
        return scentList.size();
    }
}