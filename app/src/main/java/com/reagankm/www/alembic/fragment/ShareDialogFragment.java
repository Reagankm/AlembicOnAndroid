package com.reagankm.www.alembic.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.reagankm.www.alembic.R;

/**
 * Displays a dialog that lets the user share their ratings or recommendations over
 * email.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class ShareDialogFragment extends DialogFragment {

    /** The tag to use when logging from this fragment. */
    private static final String TAG="ShareDialogFragmentTag";

    /** A listener for the dialog buttons. */
    private ShareDialogListener listener;

    /**
     * Constructs a share dialog fragment.
     */
    public ShareDialogFragment() {
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
            // Instantiate the listener so we can send details to the host activity
            listener = (ShareDialogListener) activity;
        } catch (ClassCastException e) {
            // If the activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ShareDialogListener");
        }
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

        String dialogString = getString(R.string.share_dialog_text);

        builder.setMessage(dialogString).setTitle(R.string.share_dialog_title)
                .setPositiveButton(R.string.recommendations, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        // Send the share recommendations button event back to the host activity
                        listener.onDialogShareRecommendationsClick(ShareDialogFragment.this);


                    }
                })
                .setNegativeButton(R.string.ratings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Send the share ratings event back to the host
                        listener.onDialogShareRatingsClick(ShareDialogFragment.this);

                    }
                })
                .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                // User cancelled the dialog.Send the negative button event back to the host
                listener.onDialogCancelShareClick(ShareDialogFragment.this);

            }
        });

        builder.setCancelable(true);

        // Create the AlertDialog object and return it
        return builder.create();
    }



    /**
     * A listener interface to go along with this dialog fragment.
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     */
    public interface ShareDialogListener {

        /**
         * The action to take when the user chooses to share recommendations.
         * @param dialog the dialog
         */
        void onDialogShareRecommendationsClick(DialogFragment dialog);

        /**
         * The action to take when the user chooses to share ratings.
         * @param dialog the dialog
         */
        void onDialogShareRatingsClick(DialogFragment dialog);

        /**
         * The action to take when the user chooses to cancel.
         * @param dialog the dialog
         */
        void onDialogCancelShareClick(DialogFragment dialog);
    }


}
