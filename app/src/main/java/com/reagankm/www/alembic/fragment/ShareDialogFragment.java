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
 * Created by reagan on 12/2/15.
 */
public class ShareDialogFragment extends DialogFragment {

    private static final String TAG="ShareDialogFragmentTag";


    // Use this instance of the interface to deliver action events
    ShareDialogListener mListener;

    public ShareDialogFragment() {
        // Required empty public constructor
    }



    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ShareDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ShareDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String dialogString = getString(R.string.share_dialog_text);

        builder.setMessage(dialogString).setTitle(R.string.share_dialog_title)
                .setPositiveButton(R.string.recommendations, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        // Send the share recommendations button event back to the host activity
                        mListener.onDialogShareRecommendationsClick(ShareDialogFragment.this);


                    }
                })
                .setNeutralButton(R.string.ratings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // User cancelled the dialog. No further action needed.
                        // Send the negative button event back to the host activity
                        mListener.onDialogShareRatingsClick(ShareDialogFragment.this);

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                // User cancelled the dialog. No further action needed.
                // Send the negative button event back to the host activity


                mListener.onDialogCancelShareClick(ShareDialogFragment.this);

            }
        });

        builder.setCancelable(true);

        // Create the AlertDialog object and return it
        return builder.create();
    }



    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ShareDialogListener {
        public void onDialogShareRecommendationsClick(DialogFragment dialog);
        public void onDialogShareRatingsClick(DialogFragment dialog);
        public void onDialogCancelShareClick(DialogFragment dialog);
    }


}
