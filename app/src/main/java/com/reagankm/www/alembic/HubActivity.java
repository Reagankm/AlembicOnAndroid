package com.reagankm.www.alembic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

public class HubActivity extends AppCompatActivity {

    private Button updateButton;
    private Button rateButton;
    private Button recommendButton;
    private Button reviewButton;

    private static final String TAG = "HubActivityTag";
    protected static int newProductCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        newProductCount = 0;
        //Get user's name and set welcome message
        SharedPreferences sharedPrefs = getSharedPreferences("details", MODE_PRIVATE);

        String nameStr = sharedPrefs.getString("name", null);
        String idStr = sharedPrefs.getString("id", null);
        //AccessToken token = AccessToken.getCurrentAccessToken();

        TextView welcomeMessage = (TextView) findViewById(R.id.welcome_text);


//        if(token != null)
  //      {
        if(nameStr != null)
        {
            welcomeMessage.setText("Welcome, " + nameStr + ", to");

        }
        //}


        updateButton = (Button) findViewById(R.id.update_button);
        rateButton = (Button) findViewById(R.id.rate_button);
        recommendButton = (Button) findViewById(R.id.suggest_button);
        reviewButton = (Button) findViewById(R.id.review_button);

        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                //Scrape and parse BPAL data and update database with it, then
                //display message to let the user know how much new data was found
                //(So they know button did something)
                ScentScraperTask scrapes = new ScentScraperTask(HubActivity.this);
                scrapes.execute();

                //Intent launchHub = new Intent(HubActivity.this, HubActivity.class);
                //startActivity(launchHub);

            }
        });

    }



    @Override protected void onDestroy() {
    // TODO Auto-generated method stub
        super.onDestroy();
        Session.getActiveSession().closeAndClearTokenInformation();
    }


}
