package com.reagankm.www.alembic;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DirectoryActivity extends FragmentActivity {

    GridView directory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_directory);

        directory = (GridView) findViewById(R.id.directory);
        int columns = 3;
        int padding = 1;
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int columnWidth = (int) ((width - ((columns + 1) * padding)) / columns);
        directory.setColumnWidth(columnWidth);

        directory.setAdapter(new MyAdapter(this));

        directory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //for debugging
                Toast.makeText(getApplicationContext(),
                        "Position: " + position, Toast.LENGTH_SHORT).show();

                String letter = "" + ((char) (position + 64));

                Intent subDirectory = ScentListActivity.createIntent(DirectoryActivity.this, letter);
                startActivity(subDirectory);

            }
        });

    }

    private class MyAdapter extends BaseAdapter
    {
        private List<GridItem> items = new ArrayList<>();
        private LayoutInflater inflater;

        public MyAdapter(Context context)
        {
            inflater = LayoutInflater.from(context);

            //Create pictures for each directory letter
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

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i)
        {
            return items.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return items.get(i).drawableId;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            View v = view;
            ImageView picture;
            //TextView name;

            if(v == null)
            {
                v = inflater.inflate(R.layout.directory_item, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
            }

            picture = (ImageView)v.getTag(R.id.picture);

            GridItem item = (GridItem)getItem(i);

            picture.setImageResource(item.drawableId);

            return v;
        }

        private class GridItem
        {
            //final String name;
            final int drawableId;
            //final String letter;

            GridItem(int drawableId)
            {  // this.letter = letter;
                //this.name = name;
                this.drawableId = drawableId;
            }
        }
    }

}

