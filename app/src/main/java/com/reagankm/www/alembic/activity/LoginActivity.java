package com.reagankm.www.alembic.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.reagankm.www.alembic.R;

import java.util.Arrays;

/**
 * Displays a login screen which lets the user log in
 * using their Facebook account or bypass the login and
 * continue as a guest.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class LoginActivity extends AppCompatActivity {

    /** The tag to use when logging from this activity. */
    private static final String TAG="LoginActivityTag";

    /** A ProfileTracker to track Facebook profile changes. */
    private static ProfileTracker mProfileTracker;

    /** The profile of the current Facebook user. */
    private Profile userProfile;

    /** The callback manager to handle calls from
     * onActivityResult to Facebook's SDK */
    private CallbackManager callbackManager;


    /**
     * Creates the Login UI with buttons that allow the user to
     * login with Facebook or skip logging in.
     *
     * @param savedInstanceState any saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate super() just called.");
        mProfileTracker = null;

        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d(TAG, "FB Sdk just initialized");

        setContentView(R.layout.activity_login);
        Log.d(TAG, "setContentView just occurred");

        if (callbackManager == null) {

            callbackManager = CallbackManager.Factory.create();
            Log.d(TAG, "callbackManager just created");

        }


        //Get the shared preferences which can store data for later activities
        final SharedPreferences sharedPrefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);


        //Set up the Facebook login button
        LoginButton fbButton = (LoginButton) findViewById(R.id.login_button);
        fbButton.setReadPermissions("public_profile");
        Log.d(TAG, "created FB button and set read permissions");

        //Associate the FB button with the callback manager and tell it how to behave
        //after a user's login attempt
        fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            /**
             * When a user logs in successfully, inform them with a Toast message,
             * save their name and id for later use, and then launch the hub activity.
             *
             * @param loginResult the result of their Facebook login attempt
             */
            @Override
            public void onSuccess(LoginResult loginResult) {


                //Record success
                Log.d(TAG, "FB Button callback registered as SUCCESS: " + loginResult);
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                //Save user's profile name and id for user in other Activities
                userProfile = Profile.getCurrentProfile();

                if(userProfile == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            userProfile = profile2;
                            Log.v(TAG, "With tracker, profile2 " + profile2.getName()
                                    + ", userProfile " + userProfile.getName());
                            SharedPreferences.Editor editor = sharedPrefs.edit();

                            editor.putString("name", userProfile.getName());
                            editor.apply();
                            LoginActivity.mProfileTracker.stopTracking();
                        }
                    };
                    mProfileTracker.startTracking();
                }
                else {

                    Log.v(TAG, "No tracker profile: " + userProfile.getFirstName());
                }


                //Launch the next Activity
                Intent launchHub = new Intent(LoginActivity.this, HubActivity.class);
                startActivity(launchHub);
                finish();
            }

            /**
             * When a user cancels their login attempt, send them a Toast notification
             * with that information;
             */
            @Override
            public void onCancel() {
                Log.d(TAG, "FB Button callback returned to onCancel");
                Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();

            }

            /**
             * When Facebook returns an error after a user's login attempt, pop up a
             * Toast notification with details.
             *
             * @param exception the FacebookException that resulted
             */
            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "FB Button callback registered as Error: " + exception);
                Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        //When FB button is clicked, attempt to login
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "FB Button clicked");
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                Log.d(TAG, "FB Button Login Manager instance called logInWithReadPermissions");


            }
        });
        Log.d(TAG, "FB Button onClickListener just created");

        //When skipLogin button is clicked, save the user name as "Guest" and launch
        //the main hub activity
        Button skipLoginButton = (Button) findViewById(R.id.skip_login);
        skipLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                Log.d(TAG, "Login as guest (skip login button clicked)");
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("name", "Guest");
                Log.d(TAG, "Put Guest as name");
                editor.apply();

                Intent launchHub = new Intent(LoginActivity.this, HubActivity.class);
                startActivity(launchHub);
                finish();

            }
        });
        Log.d(TAG, "Skip Login Button onClickListener just created");



    }

    /**
     * Sets the activity running again when it is resumed after
     * being paused.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    /**
     * Pauses the activity when it is running.
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    /**
     * Called when an activity I launched exits.
     *
     * @param requestCode the requestCode the other activity started with
     * @param resultCode the resultCode returned by the other activity
     * @param data any additional data from the exited activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
