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
 * Created by reagan on 11/17/15.
 */
public class ScentAdapter extends RecyclerView.Adapter<ScentHolder> {
    private static final String TAG = "ScentAdapterTag";
    private List<ScentInfo> scentList;
    Context theContext;

    public ScentAdapter(List<ScentInfo> scents) {
        scentList = scents;
    }

    //Create a view and wrap it in a holder
    @Override
    public ScentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        theContext = parent.getContext();
        Activity activity = (Activity) theContext;
        LayoutInflater inflater = LayoutInflater.from(activity);
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

            holder.getRootView().setBackgroundResource(R.color.accent);
        }
        else
        {

            holder.getRootView().setBackgroundResource(R.color.icons);
        }
    }


    @Override
    public int getItemCount() {
        return scentList.size();
    }
}