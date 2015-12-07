package com.reagankm.www.alembic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.reagankm.www.alembic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Presents the user with all 26 letters plus a number symbol which, upon being
 * clicked, will display a list of scents beginning with that letter.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class DirectoryActivity extends FragmentActivity {



    /**
     * Creates the directory display.
     *
     * @param savedInstanceState any previously saved state data
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_directory);

        GridView directory = (GridView) findViewById(R.id.directory);
        int columns = 3;
        int padding = 1;
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int columnWidth = ((width - ((columns + 1) * padding)) / columns);
        directory.setColumnWidth(columnWidth);

        directory.setAdapter(new MyGridViewAdapter(this));

        directory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // When a GridView item is clicked, it's converted into
                // its ASCII representation
                String letter = "" + ((char) (position + 64));

                //Launch the ScentList with the chosen starting character
                Intent subDirectory = ScentListActivity.createIntent(DirectoryActivity.this, letter);
                startActivity(subDirectory);

            }
        });

    }

    /**
     * A helper class to assist in creating the directory's
     * images and provide easy access to their index numbers.
     */
    private class MyGridViewAdapter extends BaseAdapter
    {
        /** A list to store all the items in the GridView. */
        private List<GridItem> items;

        /** The LayoutInflater for the GridView. */
        private LayoutInflater inflater;

        public MyGridViewAdapter(Context context)
        {
            inflater = LayoutInflater.from(context);
            items = new ArrayList<>();

            //Add items/pictures for each directory letter
            items.add(new GridItem(R.drawable.hash_square));
            items.add(new GridItem(R.drawable.a_square));
            items.add(new GridItem(R.drawable.b_square));
            items.add(new GridItem(R.drawable.c_square));
            items.add(new GridItem(R.drawable.d_square));
            items.add(new GridItem(R.drawable.e_square));
            items.add(new GridItem(R.drawable.f_square));
            items.add(new GridItem(R.drawable.g_square));
            items.add(new GridItem(R.drawable.h_square));
            items.add(new GridItem(R.drawable.i_square));
            items.add(new GridItem(R.drawable.j_square));
            items.add(new GridItem(R.drawable.k_square));
            items.add(new GridItem(R.drawable.l_square));
            items.add(new GridItem(R.drawable.m_square));
            items.add(new GridItem(R.drawable.n_square));
            items.add(new GridItem(R.drawable.o_square));
            items.add(new GridItem(R.drawable.p_square));
            items.add(new GridItem(R.drawable.q_square));
            items.add(new GridItem(R.drawable.r_square));
            items.add(new GridItem(R.drawable.s_square));
            items.add(new GridItem(R.drawable.t_square));
            items.add(new GridItem(R.drawable.u_square));
            items.add(new GridItem(R.drawable.v_square));
            items.add(new GridItem(R.drawable.w_square));
            items.add(new GridItem(R.drawable.x_square));
            items.add(new GridItem(R.drawable.y_square));
            items.add(new GridItem(R.drawable.z_square));
        }

        /**
         * Returns a count of the number of items in the
         * GridView.
         *
         * @return the number of items in the gridview.
         */
        @Override
        public int getCount() {
            return items.size();
        }

        /**
         * Gets the item at the given index.
         *
         * @param i the index of the desired item
         * @return the item as an Object
         */
        @Override
        public Object getItem(int i)
        {
            return items.get(i);
        }

        /**
         * Gets the drawableId of the item at the given index.
         *
         * @param i the index of the desired item
         * @return the drawableId of the item
         */
        @Override
        public long getItemId(int i)
        {
            return items.get(i).drawableId;
        }

        /**
         * Creates a View for the GridItem at the given
         * index.
         *
         * @param i the index of the desired item
         * @param v the current state of the item's view
         * @param viewGroup the viewGroup needed by the layoutInflater
         * @return a view for the item
         */
        @Override
        public View getView(int i, View v, ViewGroup viewGroup)
        {

            ImageView picture;

            if(v == null)
            {
                v = inflater.inflate(R.layout.item_directory, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
            }

            picture = (ImageView) v.getTag(R.id.picture);

            GridItem item = (GridItem) getItem(i);

            picture.setImageResource(item.drawableId);

            return v;
        }

        /**
         * A class for items in the GridView.
         */
        private class GridItem
        {
            /** The id number of the item's picture. */
            final int drawableId;

            /**
             * Constructs a GridItem with the given drawableId.
             *
             * @param drawableId the drawableId location for this item
             */
            GridItem(int drawableId)
            {
                this.drawableId = drawableId;
            }
        }
    }

}

