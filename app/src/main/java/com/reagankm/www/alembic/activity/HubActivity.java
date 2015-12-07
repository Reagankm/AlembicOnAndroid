package com.reagankm.www.alembic.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.UpdateDialogFragment;
import com.reagankm.www.alembic.webtask.ScentScraperTask;

/**
 * Displays the main hub of the app with navigation buttons to
 * all the actions the app allows.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class HubActivity extends MenuActivity
        implements UpdateDialogFragment.UpdateDialogListener {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "HubActivityTag";

    /**
     * Creates the Hub UI and calls a method to
     * redirect the user as needed to either the Login screen
     * or the main hub.
     *
     * @param savedInstanceState any saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_hub);

        CallbackManager callbackManager = CallbackManager.Factory.create();

        //Get user's name and set welcome message
        final SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);

        String name = sharedPrefs.getString("name", null);


        final TextView welcomeMessage = (TextView) findViewById(R.id.welcome_text);

        if (name != null) {
            welcomeMessage.setText("Welcome, " + name + ", to");

        }

        //Set up Facebook Login Manager for later use with the menu
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                //Record success
                Log.d(TAG, "FB Button callback registered as SUCCESS: " + loginResult);
                Toast.makeText(HubActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                //Save user's profile name and id for user in other Activities
                com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("name", profile.getName());

                editor.apply();

                //Update user welcome name
                welcomeMessage.setText("Welcome, " + profile.getName() + ", to");

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "FB Button callback returned to onCancel");
                Toast.makeText(HubActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "FB Button callback registered as Error: " + exception);
                Toast.makeText(HubActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        //Create buttons and set onClickListeners
        Button updateButton = (Button) findViewById(R.id.update_button);
        Button rateButton = (Button) findViewById(R.id.rate_button);
        Button recommendButton = (Button) findViewById(R.id.suggest_button);
        Button reviewButton = (Button) findViewById(R.id.review_button);

        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                // Make sure the user wants to update
                DialogFragment dialog = new UpdateDialogFragment();
                dialog.show(getSupportFragmentManager(), "UpdateDialogFragment");

                //If they choose yes, dialog will call onDialogPositiveClick to handle
                //the update

            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                //Launch directory where user can rate scents
                Intent launchDirectory = new Intent(HubActivity.this, DirectoryActivity.class);
                startActivity(launchDirectory);


            }


        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch ViewRatings so user can see scents they've rated
                Intent launchRated = new Intent(HubActivity.this, ViewRatingsActivity.class);
                startActivity(launchRated);
            }
        });

        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launch RecommendationAgent to recommend scents to user
                Intent launchAgent = new Intent(HubActivity.this, RecommendationAgentActivity.class);
                startActivity(launchAgent);
            }
        });

    }

    /**
     * Called in response to a positive click on the UpdateDialog, this
     * method begins the long task of updating scents.
     *
     * @param dialog the UpdateDialog whose button was clicked
     */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog){
        //Scrape and parse BPAL data and update database with it, then
        //display message to let the user know how much new data was found

        ScentScraperTask scrapes = new ScentScraperTask(HubActivity.this);
        scrapes.execute();
    }

    /**
     * Called in response to a negative click on the UpdateDialog, this
     * method cancels the update task.
     *
     * @param dialog the UpdateDialog whose button was clicked
     */
    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        //No action needed as this click indicates the user does not want
        //to update
    }





}
