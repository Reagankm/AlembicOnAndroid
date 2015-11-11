package com.reagankm.www.alembic;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link DialogFragment} subclass.
 */
public class UpdateDialogFragment extends DialogFragment {

    private static final String TAG="UpdateDialogFragmentTag";


    // Use this instance of the interface to deliver action events
    UpdateDialogListener mListener;

    public UpdateDialogFragment() {
        // Required empty public constructor
    }



    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (UpdateDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement UpdateDialogListener");
        }
    }




    //gets the last updated dates and returns the newest one as a string
    private String newestDate() {
        String result = "";
        Activity theActivity = getActivity();
        final SharedPreferences sharedPrefs
                = theActivity.getSharedPreferences(getString(R.string.prefs_file),
                theActivity.MODE_PRIVATE);

        String scent_date = sharedPrefs.getString("scent_date", null);
        String ingredient_date = sharedPrefs.getString("ingredient_date", null);

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String dialogString = getString(R.string.dialog_message_pt_1) + newestDate()
                + getString(R.string.dialog_message_pt_2);

        builder.setMessage(dialogString).setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.yes_update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(UpdateDialogFragment.this);


                    }
                })
                .setNegativeButton(R.string.no_update, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog. No further action needed.
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(UpdateDialogFragment.this);

                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }



    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface UpdateDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }


}
