package com.reagankm.www.alembic;

import android.test.ActivityInstrumentationTestCase2;

import com.reagankm.www.alembic.activity.DirectoryActivity;
import com.reagankm.www.alembic.activity.HubActivity;
import com.reagankm.www.alembic.activity.LoginActivity;
import com.reagankm.www.alembic.activity.RecommendationAgentActivity;
import com.reagankm.www.alembic.activity.ViewRatingsActivity;
import com.robotium.solo.Solo;

import junit.framework.Assert;

/**
 * Created by reagan on 12/6/15.
 */
public class HubActivityTest
        extends ActivityInstrumentationTestCase2<HubActivity> {


    private Solo solo;

    public HubActivityTest() {
        super(HubActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    public void testWelcomeByName() throws Exception {

        Assert.assertTrue(solo.searchText("Theodosia Testalot"));
    }


    public void testSetUp() throws Exception {
        solo.assertCurrentActivity("Wrong activity created.", HubActivity.class);


    }

    public void testClickUpdateButtonNo() throws Exception {
        // Click a button which will start a new Activity
        // Here we use the ID of the string to find the right button
        solo.clickOnButton(solo.getString(R.string.get_scent_updates));
        solo.waitForDialogToOpen();
        //Click on negative button
        solo.clickOnView(solo.getView(android.R.id.button2));
    }

    public void testClickUpdateButtonYes() throws Exception {
        solo.clickOnButton(solo.getString(R.string.get_scent_updates));
        solo.waitForDialogToOpen();
        //Click on negative button
        solo.clickOnView(solo.getView(android.R.id.button1));

    }

    public void testClickRateScentsButton() throws Exception {
        solo.clickOnButton(solo.getString(R.string.rate_scents_text));
        // Validate that the Activity is the correct one
        solo.assertCurrentActivity("Wrong activity launched", DirectoryActivity.class);
    }

    public void testClickGetRecommendationButton() throws Exception {
        solo.clickOnButton(solo.getString(R.string.get_recommendation_text));
        // Validate that the Activity is the correct one
        solo.assertCurrentActivity("Wrong activity launched", RecommendationAgentActivity.class);
    }



    public void testClickViewRatingsButton() throws Exception {
        solo.clickOnButton(solo.getString(R.string.view_ratings_text));
        // Validate that the Activity is the correct one
        solo.assertCurrentActivity("Wrong activity launched", ViewRatingsActivity.class);
    }






}
