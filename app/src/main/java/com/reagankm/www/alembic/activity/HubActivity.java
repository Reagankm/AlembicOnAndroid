package com.reagankm.www.alembic.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.webtask.ScentScraperTask;
import com.reagankm.www.alembic.fragment.UpdateDialogFragment;

/**
 * Displays the main hub of the app with navigation buttons to
 * all the actions the app allows.
 *
 * @author Reagan Middlebrook
 * @version Phase 1
 */
public class HubActivity extends AppCompatActivity implements UpdateDialogFragment.UpdateDialogListener {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "HubActivityTag";

    /** The button that launches an update of the scent database. */
    private Button updateButton;

    /** The button that launches the rate scents activity. */
    private Button rateButton;

    /** The button that launches the recommendation activity. */
    private Button recommendButton;

    /** The button that launches the review ratings activity. */
    private Button reviewButton;

    /** The callback manager to handle calls from
     * onActivityResult to Facebook's SDK */
    private CallbackManager callbackManager;

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

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_hub);

        callbackManager = CallbackManager.Factory.create();

        //Get user's name and set welcome message
        final SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);

        String name = sharedPrefs.getString("name", null);
        String id = sharedPrefs.getString("id", null);

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
                editor.putString("id", profile.getId());
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
        updateButton = (Button) findViewById(R.id.update_button);
        rateButton = (Button) findViewById(R.id.rate_button);
        recommendButton = (Button) findViewById(R.id.suggest_button);
        reviewButton = (Button) findViewById(R.id.review_button);

        updateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                // Make sure the user wants to update
                DialogFragment dialog = new UpdateDialogFragment();
                dialog.show(getSupportFragmentManager(), "UpdateDialogFragment");

                //If they choose yes, dialog will call onDialogPositiveClick to handle
                //update

            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                Intent launchDirectory = new Intent(HubActivity.this, DirectoryActivity.class);
                startActivity(launchDirectory);


            }


        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchRated = new Intent(HubActivity.this, ViewRatingsActivity.class);
                startActivity(launchRated);
            }
        });

//        recommendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent launch = new
//            }
//        });

    }


    /**
     * Inflate the menu and display LogOut menu item only if
     * user is currently logged in.
     *
     * @param menu the menu to be inflated
     * @return whether the menu inflation was successful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_hub, menu);

        if (isLoggedIn()){
            menu.findItem(R.id.action_login_toggle).setVisible(true);
        } else {
            menu.findItem(R.id.action_login_toggle).setVisible(false);
        }


        Log.d(TAG, "Create menu");

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A helper method that returns whether or not the user is
     * logged in.
     *
     * @return true if the user is logged in with Facebook, otherwise
     * false
     */
    private boolean isLoggedIn() {
        //If there is an access token, user is logged in
        return AccessToken.getCurrentAccessToken() != null;
    }

    /**
     * Defines how to behave when a menu option is selected.
     *
     * @param item the menu item selected
     * @return whether the menu item processed successfully
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_login_toggle) {
                //Log the user out and return them to the Login screen

                LoginManager.getInstance().logOut();
                Intent launchHub = new Intent(HubActivity.this, LoginActivity.class);
                startActivity(launchHub);
                finish();

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog){
        //Scrape and parse BPAL data and update database with it, then
        //display message to let the user know how much new data was found
        //(So they know button did something)

        ScentScraperTask scrapes = new ScentScraperTask(HubActivity.this);
        scrapes.execute();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        //No action needed
    }



}
