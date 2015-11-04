package com.reagankm.www.alembic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
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

import java.util.Arrays;

public class HubActivity extends AppCompatActivity {

    private Button updateButton;
    private Button rateButton;
    private Button recommendButton;
    private Button reviewButton;

    private static final String TAG = "HubActivityTag";
    protected static int newProductCount;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_hub);

        //Set text of menu item
//        MenuItem toggle = (MenuItem) findViewById(R.id.action_login_toggle);
//        if (isLoggedIn()){
//            toggle.setTitle("Log Out");
//        } else {
//            toggle.setTitle("Log In");
//        }

        callbackManager = CallbackManager.Factory.create();


        //Get user's name and set welcome message
        final SharedPreferences sharedPrefs = getSharedPreferences("details", MODE_PRIVATE);

        String nameStr = sharedPrefs.getString("name", null);
        String idStr = sharedPrefs.getString("id", null);
        //AccessToken token = AccessToken.getCurrentAccessToken();

        final TextView welcomeMessage = (TextView) findViewById(R.id.welcome_text);


//        if(token != null)
  //      {
        if(nameStr != null)
        {
            welcomeMessage.setText("Welcome, " + nameStr + ", to");

        }
        //}

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

    private boolean isLoggedIn() {
        //If there is an access token, user is logged in
        return AccessToken.getCurrentAccessToken() != null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login_toggle) {

            //if (isLoggedIn()){

                //Log user out and return to login screen
                LoginManager.getInstance().logOut();
                Intent launchHub = new Intent(HubActivity.this, LoginActivity.class);
                startActivity(launchHub);
                finish();

            /*} else {

                //Let user login with Facebook
                LoginManager.getInstance().logInWithReadPermissions(HubActivity.this, Arrays.asList("public_profile"));

            }*/


            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }


    }





}
