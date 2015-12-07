package com.reagankm.www.alembic;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import com.reagankm.www.alembic.activity.HubActivity;
import com.reagankm.www.alembic.activity.ScentActivity;
import com.robotium.solo.Solo;

import java.util.ArrayList;

/**
 * Created by reagan on 12/6/15.
 */
public class ScentActivityTest extends ActivityInstrumentationTestCase2<ScentActivity> {

    private static final String TAG = "ScentActivityTestTag";
    private Solo solo;
    private String scentName = "Old Books";
    private String scentId = "oldbooks.id";

    public ScentActivityTest() {
        super(ScentActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent();
        intent.putExtra(ScentActivity.getNameKey(), scentName);
        intent.putExtra(ScentActivity.getIdKey(), scentId);
        setActivityIntent(intent);
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testNameShows() throws Exception {
        assertTrue(solo.searchText(scentName));
    }

    public void testIdShows() throws Exception {
        assertTrue(solo.searchText(scentId));
    }

    public void testRatingBar() throws Exception {
        ArrayList<RatingBar> rbList = solo.getCurrentViews(RatingBar.class);
        RatingBar theBar = rbList.get(0);

        //Set the rating bar to 5 (which is 10 steps, since there are half stars)
        solo.setProgressBar(0, 10);
        Log.d(TAG, "Progress bar should be 5, is " + theBar.getRating());
        assertTrue(theBar.getRating() == ((float) 5));

        //Set rating bar to 2.5 (which is 5 steps from 0)
        solo.setProgressBar(0, 5);

        Log.d(TAG, "Progress bar should be 2.5, is " + theBar.getRating());
        assertTrue(theBar.getRating() == ((float) 2.5));


    }


}
