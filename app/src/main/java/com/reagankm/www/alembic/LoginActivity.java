package com.reagankm.www.alembic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private LoginButton fbButton;
    private Button skipLoginButton;
    private CallbackManager callbackManager;
    private static final String TAG="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate super() just called.");

        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d(TAG, "FB Sdk just initialized");

        //Verify if user is already logged in
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

        updateWithToken(AccessToken.getCurrentAccessToken());

        setContentView(R.layout.activity_login);
        Log.d(TAG, "setContentView just occurred");

        callbackManager = CallbackManager.Factory.create();
        Log.d(TAG, "callbackManager just created");

        final SharedPreferences sharedPrefs = getSharedPreferences("details", MODE_PRIVATE);

        //Check if user is already logged in
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

        /*AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };*/

        // If the access token is available already assign it.
        AccessToken accessToken = AccessToken.getCurrentAccessToken();


        fbButton = (LoginButton) findViewById(R.id.login_button);
        fbButton.setReadPermissions("public_profile");
        Log.d(TAG, "created FB button and set read permissions");

        fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                //Record success
                Log.d(TAG, "FB Button callback registered as SUCCESS: " + loginResult);
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                //Save user's profile name and id for user in other Activities
                com.facebook.Profile profile = com.facebook.Profile.getCurrentProfile();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("name", profile.getName());
                editor.putString("id", profile.getId());
                editor.commit();

                //Launch the next Activity
                Intent launchHub = new Intent(LoginActivity.this, HubActivity.class);
                startActivity(launchHub);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "FB Button callback returned to onCancel");
                Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "FB Button callback registered as Error: " + exception);
                Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "FB Button clicked");
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
                Log.d(TAG, "FB Button Login Manager instance called logInWithReadPermissions");


            }
        });
        Log.d(TAG, "FB Button onClickListener just created");


        skipLoginButton = (Button) findViewById(R.id.skip_login);
        skipLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("name", "Guest");
                editor.putString("id", null);

                Intent launchHub = new Intent(LoginActivity.this, HubActivity.class);
                startActivity(launchHub);

            }
        });
        Log.d(TAG, "Skip Login Button onClickListener just created");


    }


    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /*private void updateWithToken(AccessToken currentAccessToken) {
        if (currentAccessToken != null) {

            LOAD ACTIVITY A!

        } else {

            LOAD ACTIVITY B!
        }
    }*/

    private void updateWithToken(AccessToken currentAccessToken) {
        int timeout = 500;
        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    //If user is already logged in, take them to the hub
                    Intent i = new Intent(LoginActivity.this, HubActivity.class);
                    startActivity(i);

                    finish();
                }
            }, timeout);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);

                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}
