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

//    static final String[] numbers = new String[]{
//            "#", "A", "B", "C", "D", "E",
//            "F", "G", "H", "I", "J", "K",
//            "L", "M", "N", "O", "P", "Q",
//            "R", "S", "T", "U", "V", "W",
//            "X", "Y", "Z"};

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

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_list_item_1, numbers);

        //directory.setAdapter(adapter);
        directory.setAdapter(new MyAdapter(this));

        directory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //for debugging
                Toast.makeText(getApplicationContext(),
                        "Position: " + position, Toast.LENGTH_SHORT).show();


//                final SharedPreferences sharedPrefs
//                        = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);
//
//                SharedPreferences.Editor editor = sharedPrefs.edit();
                String letter = "" + ((char) (position + 64));
//                editor.putString("letter", letter);

                Intent subDirectory = ScentListActivity.createIntent(DirectoryActivity.this, letter);
                startActivity(subDirectory);

            }
        });

    }

    private class MyAdapter extends BaseAdapter
    {
        private List<Item> items = new ArrayList<>();
        private LayoutInflater inflater;

        public MyAdapter(Context context)
        {
            inflater = LayoutInflater.from(context);

            items.add(new Item(R.drawable.reading_lady_96, "A"));
            items.add(new Item(R.drawable.a, "B"));
            items.add(new Item(R.drawable.reading_lady_96, "C"));
            items.add(new Item(R.drawable.reading_lady_96, "D"));
            items.add(new Item(R.drawable.a, "E"));
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
                //v.setTag(R.id.text, v.findViewById(R.id.text));
            }

            picture = (ImageView)v.getTag(R.id.picture);
            picture.setPadding(5, 5, 5, 5);
            //name = (TextView)v.getTag(R.id.text);

            Item item = (Item)getItem(i);

            picture.setImageResource(item.drawableId);
            //name.setText(item.name);

            return v;
        }

        private class Item
        {
            //final String name;
            final int drawableId;
            final String letter;

            Item(int drawableId, String letter)
            {   this.letter = letter;
                //this.name = name;
                this.drawableId = drawableId;
            }
        }
    }

}

