package com.reagankm.www.alembic;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;


/**
 * Displays a splash screen while the app determines whether the user
 * has previously logged in and then redirects to either the login activity
 * the main hub activity.
 *
 * @author Reagan Middlebrook
 * @version Phase 1
 */
public class SplashActivity extends AppCompatActivity {

    /** The tag to use when logging from this activity. */
    private static final String TAG="SplashActivityTag";

    /**
     * Creates the Splash Screen UI and calls a method to
     * redirect the user as needed to either the Login screen
     * or the main hub.
     *
     * @param savedInstanceState any saved instance data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d(TAG, "FB Sdk just initialized");

        setContentView(R.layout.activity_splash);

        //Verify whether user is already logged in
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                redirectPerToken(newAccessToken);
            }
        };

        redirectPerToken(AccessToken.getCurrentAccessToken());
    }

    /**
     * Checks whether the user is still logged in to Facebook with
     * this app and, if so, redirects them to the main hub; if not,
     * redirects them to the login screen.
     *
     * @param currentAccessToken the current access token provided
     *                           by Facebook's SDK
     */
    private void redirectPerToken(AccessToken currentAccessToken) {
        int timeout = 1000;

        //If an access token exists, then user is logged in
        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    //If user is already logged in, take them to the hub

                    startActivity(new Intent(SplashActivity.this, HubActivity.class));
                    finish();
                }
            }, timeout);
        } else {
            //user is not logged in
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, timeout);
        }
    }
}
