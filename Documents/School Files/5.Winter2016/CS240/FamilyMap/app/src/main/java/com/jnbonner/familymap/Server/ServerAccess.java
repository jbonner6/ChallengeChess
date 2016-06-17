package com.jnbonner.familymap.Server;

import android.content.Context;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.Toast;

import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.Model.Event;
import com.jnbonner.familymap.Model.Person;
import com.jnbonner.familymap.R;

import org.json.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * Created by James on 3/10/2016.
 */
public class ServerAccess {

    private String accessString;
    private String urlResponseValue;
    Context mContext;
    String urlBase;
    boolean test;
    CountDownLatch signal;

    /**
     * General constructor for ServerAccess
     * @param test  Whether or not this is being used by a JUnit test file
     */
    public ServerAccess(boolean test){
        this.test = test;
    }

    /**
     * Constructor used only by JUnit test files.
     * @param test      Whether or not this is being used by a JUnit test file
     * @param signal    A countdown for the on post execute to wait for returned information from
     *                  the server before returning info to the JUnit test
     *
     */
    public ServerAccess(boolean test, CountDownLatch signal){
        this.test = test;
        this.signal = signal;
    }

    /** Called upon logging in to app*/
    public void login(String urlBase, String username, String password, Context context) throws MalformedURLException {
        this.urlBase = urlBase;
        String urlLogin = urlBase + "/user/login";
        URL url = new URL(urlLogin);
        accessString = createLoginString(username, password);
        ServerTask serverTask = new ServerTask(context, "login");
        urlResponseValue = "";
        serverTask.execute(url);
    }

    /** Syncs both the people and event data sequentially*/
    public void syncData(Context context){
        getPeople(context);
        getEvents(context);
    }

    public void getPeople(Context context){
        String urlGetPeople = urlBase + "/person/";
        URL url = null;
        try {
            url = new URL(urlGetPeople);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ServerTask serverTask = new ServerTask(context, "getPeople");
        urlResponseValue = "";
        serverTask.execute(url);
    }
    public void getEvents(Context context){
        String urlGetEvents = urlBase + "/event/";
        URL url = null;
        try {
            url = new URL(urlGetEvents);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ServerTask serverTask = new ServerTask(context, "getEvents");
        urlResponseValue = "";
        serverTask.execute(url);
    }

    private String createLoginString(String username, String password){
        String toReturn = "{" +
                "username:\"" +
                username +
                "\", password:\"" +
                password +
                "\"}";
        return toReturn;

    }

    /** Asynchronus task that performs requests to the server and processes returned values*/
    public class ServerTask extends AsyncTask<URL, Integer, Long> {
        String urlContent;
        String operation;

        public ServerTask(Context context, String operation){
            mContext = context;
            this.operation = operation;
        }

        @Override
        protected Long doInBackground(URL... urls) {
            HttpClient httpClient = new HttpClient();
            long totalSize = 0;
            if (operation.equals("login")) {
                urlContent = httpClient.postURL(urls[0], accessString);
            }
            else if (operation.equals("getPeople") || operation.equals("getEvents")){
                httpClient.setAuthToken(AllModel.getAuthToken());
                urlContent = httpClient.getURL(urls[0]);
            }

            return totalSize;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){

        }

        @Override
        protected void onPostExecute(Long result){
//            Toast.makeText(mContext, urlContent, Toast.LENGTH_LONG).show();

            urlResponseValue = urlContent;
            switch(operation){
                case "login": parseServerResponseLogin(urlResponseValue);
                    if (AllModel.getAuthToken() != null && !AllModel.getAuthToken().isEmpty()){
                        if (!test){syncData(mContext);}
                        else {
                            //signal.countDown();
                            syncData(mContext);
                        }
                    }
                    break;
                case "getPeople":parseServerResponsePost(urlResponseValue, "person");
                    break;
                case "getEvents":parseServerResponsePost(urlResponseValue, "event");
                    if (!test) {
                        mapEvents();
                        AllModel.getMainActivity().transToMap();
                        if (AllModel.getSettingsActivity() != null) {
                            if (AllModel.getEventList().size() > 0 &&
                                    AllModel.getPersonList().size() > 0) {
                                AllModel.setCurrentEventPerson(null);
                                AllModel.setCurrentEvent(null);
                                AllModel.getSettingsActivity().returnAfterReSync();
                            } else {
                                Toast.makeText(mContext, "Re-sync failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    else{
                        signal.countDown();
                    }
                    break;
            }
        }
    }

    private void mapEvents(){
        for(Event temp: AllModel.getEventList()){
            for(Person pers: AllModel.getPersonList()){
                if (pers.getPersonId().equals(temp.getPersonID())){
                    temp.setPerson(pers);
                    pers.getPersEvents().add(temp);
                }
            }
        }
    }

    private void parseServerResponseLogin(String response){

        try {
            JSONObject obj = new JSONObject(response);
            if (obj.getString("Authorization") != null){
                Toast.makeText(mContext, "Login Successful", Toast.LENGTH_LONG).show();
                String authToken = obj.getString("Authorization");
                String username = obj.getString("userName");
                String personId = obj.getString("personId");
                AllModel.setAuthToken(authToken);
                AllModel.setLoginUserName(username);
                AllModel.setLoginPersonId(personId);
            }

        } catch (JSONException e) {
            Toast.makeText(mContext, "Login Failed: Invalid Username or Password", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void  parseServerResponsePost(String response, String category){
        try {
            JSONObject baseObj = new JSONObject(response);
            JSONArray objArray = baseObj.getJSONArray("data");
            for (int i = 0; i < objArray.length(); i++){
                JSONObject current = objArray.getJSONObject(i);
                if (category.equals("person")){
                    addPerson(current);
                }
                else if (category.equals("event")){
                    addEvent(current);
                }
            }
//            Toast.makeText(mContext, String.valueOf(AllModel.getPersonList().size()), Toast.LENGTH_LONG).show();
//            Toast.makeText(mContext, String.valueOf(AllModel.getEventList().size()), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addPerson(JSONObject person){
        try {
            String descendant = person.getString("descendant");
            String personId = person.getString("personID");
            String firstName = person.getString("firstName").toLowerCase();
            String lastName = person.getString("lastName").toLowerCase();
            boolean gender = true; //T = Male .... F = Female
            switch(person.getString("gender").toLowerCase()){
                case "m": gender = true;
                    break;
                case "f": gender = false;
                    break;
            }
            String father = null;
            if (person.has("father")) {
                father = person.getString("father");
            }
            String mother = null;
            if(person.has("mother")) {
                mother = person.getString("mother");
            }
            String spouse = null;
            if(person.has("spouse")) {
                spouse = person.getString("spouse");
            }

            Person toAdd = new Person(descendant, personId, firstName, lastName, gender, father,
                    mother, spouse);
            AllModel.getPersonList().add(toAdd);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void addEvent(JSONObject event){
        try{
            String eventID = event.getString("eventID");
            String personID = event.getString("personID");
            double latitude = event.getDouble("latitude");
            double longitude = event.getDouble("longitude");
            String country = event.getString("country").toLowerCase();
            String city = event.getString("city").toLowerCase();
            String description = event.getString("description").toLowerCase();
            int year = -1;
            if (event.has("year")) {
                year = event.getInt("year");
            }
            String descendant = event.getString("descendant").toLowerCase();

            Event toAdd = new Event(eventID, personID, latitude, longitude, country, city,
                    description, year, descendant);
            AllModel.getEventList().add(toAdd);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }
}
