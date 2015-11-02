package com.reagankm.www.alembic;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        setContentView(R.layout.activity_login);
        Log.d(TAG, "setContentView just occurred");

        callbackManager = CallbackManager.Factory.create();
        Log.d(TAG, "callbackManager just created");

        /*LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {


                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "FB Callback onSuccess called with Login Result " + loginResult);
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                        Intent launchHub = new Intent(LoginActivity.this, HubActivity.class);
                        startActivity(launchHub);
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "FB Callback onCancel called with Login Result ");

                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "FB Callback onError called with exception " + exception);

                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
*/



        fbButton = (LoginButton) findViewById(R.id.login_button);
        fbButton.setReadPermissions("public_profile");
        Log.d(TAG, "created FB button and set read permissions");

        fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "FB Button callback registered as SUCCESS: " + loginResult);
                Intent launchHub = new Intent(LoginActivity.this, HubActivity.class);
                startActivity(launchHub);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "FB Button callback returned to onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "FB Button callback registered as Error: " + exception);
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
}
