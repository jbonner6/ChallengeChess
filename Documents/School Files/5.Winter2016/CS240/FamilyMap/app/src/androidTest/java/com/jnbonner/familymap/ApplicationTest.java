package com.jnbonner.familymap;

import android.app.Application;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.ApplicationTestCase;

import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.Model.Event;
import com.jnbonner.familymap.Model.Person;
import com.jnbonner.familymap.Server.ServerAccess;

import java.net.MalformedURLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends InstrumentationTestCase {

    ServerAccess serverAccess;
    String badUsername;
    String badPassword;
    String goodUsername;
    String goodPassword;
    String serverHostString;
    String descendent;
    int serverPortInt;
    String testBaseURL;
    final CountDownLatch signal = new CountDownLatch(1);

    public void setUp() throws Exception{
        super.setUp();
        new AllModel();
        serverAccess = new ServerAccess(true, signal);
        AllModel.setServerAccess(serverAccess);
        badUsername = "bad";
        badPassword = "bad";
        goodUsername = "test";
        goodPassword = "test";
        serverHostString = "192.168.1.130";
        serverPortInt = 3636;
        testBaseURL = "http://" + serverHostString + ":" + String.valueOf(serverPortInt);
        descendent = "test";
    }

    public void tearDown() throws Exception{
        new AllModel();
        AllModel.logout();
    }

    /**
     * Tests that a login to the server and population of subsequent model objects should fail
     * if bad login credentials are provided
     */
    public void testBadLogin() throws Throwable {
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverAccess.login(testBaseURL, badUsername, badPassword, getInstrumentation().getContext());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        signal.await(5, TimeUnit.SECONDS);
        assertTrue("Test Bad Login", AllModel.getAuthToken() == null);
    }

    /**
     * Tests that a login to the server and population of subsequent model objects should succeed
     * if good login credentials are provided
     */
    public void testGoodLogin() throws Throwable{
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverAccess.login(testBaseURL, goodUsername, goodPassword, getInstrumentation().getContext());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        signal.await(5, TimeUnit.SECONDS);
        assertTrue("Test Good Login", AllModel.getAuthToken() != null);
    }

    /**
     * Tests that Person objects are correctly created and the Model Person List is correctly
     * populated upon login to the server with good credentials
     */
    public void testSyncPerson() throws Throwable{
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverAccess.login(testBaseURL, goodUsername, goodPassword, getInstrumentation().getContext());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        signal.await(10, TimeUnit.SECONDS);
        boolean badLoad = false;
        boolean allGood = true;
        if (AllModel.getPersonList().size() > 0){
            for (Person temp: AllModel.getPersonList()){
                if (!temp.getDescendant().equals(descendent)){
                    badLoad = true;
                }
            }
        }
        else {
            badLoad = true;
        }
        if (badLoad){
            allGood = false;
        }
        assertTrue("Load Person Information", allGood);
    }

    /**
     * Tests that Event objects are correctly created and the Model Event List is correctly
     * populated upon login to the server with good credentials
     */
    public void testSyncEvents() throws Throwable{
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    serverAccess.login(testBaseURL, goodUsername, goodPassword, getInstrumentation().getContext());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        signal.await(10, TimeUnit.SECONDS);
        boolean badLoad = false;
        boolean allGood = true;
        if (AllModel.getEventList().size() > 0){
            for (Event temp: AllModel.getEventList()){
                if (!temp.getDescendant().equals(descendent)){
                    badLoad = true;
                }
            }
        }
        else {
            badLoad = true;
        }
        if (badLoad){allGood = false;}
        assertTrue("Load Event Information", allGood);
    }
}