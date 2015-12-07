package com.reagankm.www.alembic.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.reagankm.www.alembic.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Displays a dialog that asks the suer if they want to update scent data.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class UpdateDialogFragment extends DialogFragment {

    /** The tag to use when logging from this fragment. */
    private static final String TAG="UpdateDialogFragmentTag";

    /** A listener for the dialog buttons. */
    private UpdateDialogListener listener;

    /**
     * Constructs an update dialog fragment.
     */
    public UpdateDialogFragment() {
        // Required empty public constructor
    }



    /**
     * Saves the listener when one is attached.
     *
     * @param activity the calling activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (UpdateDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement UpdateDialogListener");
        }
    }




    //gets the last updated dates and returns the newest one as a string

    /**
     * Gets the date of the most recent scent or ingredient update.
     *
     * @return A string representation of that date
     */
    private String newestDate() {
        String result = "";
        Activity theActivity = getActivity();
        final SharedPreferences sharedPrefs
                = theActivity.getSharedPreferences(getString(R.string.prefs_file),
                theActivity.MODE_PRIVATE);

        //Get date for the last time scents were updated
        String scent_date = sharedPrefs.getString("scent_date", null);

        //Get date for the last time ingredients were updated
        String ingredient_date = sharedPrefs.getString("ingredient_date", null);

        //Compare them and find the most recent
        String expectedPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        try {

            Date scent = formatter.parse(scent_date);
            Log.d(TAG, "Scent date = " + scent.toString());
            Date ingredient = formatter.parse(ingredient_date);
            Log.d(TAG, "Ingredient date = " + ingredient.toString());
            result = (scent.compareTo(ingredient) > 0) ? scent.toString() : ingredient.toString();
            Log.d(TAG, "Scent.compareTo(ingredient) = " + scent.compareTo(ingredient)
                    + ", so result = " + result);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return result;
    }

    /**
     * Creates the UI and listens for user's choice.
     *
     * @param savedInstanceState any saved instance data
     * @return the dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String dialogString = getString(R.string.dialog_message_pt_1) + newestDate()
                + getString(R.string.dialog_message_pt_2);

        builder.setMessage(dialogString).setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.yes_update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(UpdateDialogFragment.this);


                    }
                })
                .setNegativeButton(R.string.no_update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog. No further action needed.
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(UpdateDialogFragment.this);

                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }



    /**
     * A listener interface to go along with this dialog fragment.
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     */
    public interface UpdateDialogListener {
        /**
         * If the user clicks yes, begin update.
         *
         * @param dialog the dialog
         */
        void onDialogPositiveClick(DialogFragment dialog);

        /**
         * If the user clicks no, do not begin update.
         *
         * @param dialog the dialog
         */
        void onDialogNegativeClick(DialogFragment dialog);
    }


}
