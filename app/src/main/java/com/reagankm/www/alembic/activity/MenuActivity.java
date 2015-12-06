package com.reagankm.www.alembic.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.reagankm.www.alembic.R;
import com.reagankm.www.alembic.fragment.ShareDialogFragment;
import com.reagankm.www.alembic.model.LocalDB;
import com.reagankm.www.alembic.model.ScentInfo;

import java.util.List;

/**
 * Created by reagan on 12/4/15.
 */
public class MenuActivity extends AppCompatActivity implements
        ShareDialogFragment.ShareDialogListener {

    /** The tag to use when logging from this activity. */
    private static final String TAG = "MenuActivityTag";

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
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


        menu.findItem(R.id.share_menu_item).setVisible(true);

        sharedPrefs = getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE);



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
        boolean superResult = super.onOptionsItemSelected(item);

        int id = item.getItemId();

        if (id == R.id.action_login_toggle) {
            //Log the user out and return them to the Login screen

            LoginManager.getInstance().logOut();
            Intent launchLogin = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(launchLogin);
            finish();

            return true;
        } else if (id == R.id.share_menu_item) {
            DialogFragment dialog = new ShareDialogFragment();
            dialog.show(getSupportFragmentManager(), "ShareDialogFragment");



        } else {

        }

        return superResult;


    }

    @Override
    public void onDialogShareRecommendationsClick(DialogFragment dialog) {
        Log.d(TAG, "onDialogShareRecommendationsClick");

        //Get recommendations from local db if they exist, otherwise give message
        //to go get recommendations

        //LocalDB db = LocalDB.getInstance(this);
        LocalDB db = new LocalDB(this);
        Log.d(TAG, "Got instance of localDB");
        if (db.getRecommendationCount() > 0) {
            Log.d(TAG, "Recommendations exist in db");
            List<ScentInfo> recommendations = db.getRecommendations();
            Log.d(TAG, "Got list of recommended scents");
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.recommendation_email_text) + "\n");

            for (ScentInfo scent : recommendations) {
                Log.d(TAG, "Adding scent " + scent);

                sb.append(scent.toString() + "\n");

            }

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            Log.d(TAG, "Created intent");
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            intent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.recommendation_subject));
            Log.d(TAG, "Added text and subject to intent");
            if (intent.resolveActivity(getPackageManager()) != null) {
                Log.d(TAG, "About to start intent");
                startActivity(intent);
            }





        } else {

            Toast.makeText(MenuActivity.this, getString(R.string.no_recommendations_share), Toast.LENGTH_LONG).show();




        }











    }

    @Override
    public void onDialogShareRatingsClick(DialogFragment dialog) {
        //Get ratings from local db if they exist, otherwise give message
        //to rate things

        //LocalDB db = LocalDB.getInstance(this);
        LocalDB db = new LocalDB(this);
        if (db.getRatedCount() > 0) {
            List<ScentInfo> ratings = db.getAllRatedScents();
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.rated_email_text) + "\n");

            for (ScentInfo scent : ratings) {

                sb.append(scent.getName() + ": " + scent.getRating() + "\nID: " + scent.getId() + "\n");

            }

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            intent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.ratings_subject));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }


        } else {

            Toast.makeText(MenuActivity.this, getString(R.string.no_ratings_share), Toast.LENGTH_LONG).show();




        }



    }

    @Override
    public void onDialogCancelShareClick(DialogFragment dialog) {
        //No action needed


    }


}
