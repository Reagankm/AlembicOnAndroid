package com.reagankm.www.alembic;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;

//Displays a splash screen while app is loading and determining whether
//user is already logged in
public class SplashActivity extends AppCompatActivity {
    private static final String TAG="SplashActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d(TAG, "FB Sdk just initialized");


        setContentView(R.layout.activity_splash);

        //Verify if user is already logged in
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

        updateWithToken(AccessToken.getCurrentAccessToken());
    }

    private void updateWithToken(AccessToken currentAccessToken) {
        int timeout = 1000;

        if (currentAccessToken != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    //If user is already logged in, take them to the hub
                    Intent i = new Intent(SplashActivity.this, HubActivity.class);
                    startActivity(i);

                    finish();
                }
            }, timeout);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);

                    finish();
                }
            }, timeout);
        }
    }
}
