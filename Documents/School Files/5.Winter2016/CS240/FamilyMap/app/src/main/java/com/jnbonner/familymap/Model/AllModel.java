package com.jnbonner.familymap.Model;

import com.jnbonner.familymap.Filter.FilterListItem;
import com.jnbonner.familymap.LoginFragment;
import com.jnbonner.familymap.MainActivity;
import com.jnbonner.familymap.MapInfoFragment;
import com.jnbonner.familymap.Server.ServerAccess;
import com.jnbonner.familymap.Settings.SettingsActivity;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by James on 3/10/2016.
 */
public class AllModel {
    private static ServerAccess serverAccess;
    private static boolean extrasAdded;
    private static MainActivity mainActivity;
    private static List<Person> personList;
    private static List<Event> eventList;
    private static Person currentEventPerson;
    private static Event currentEvent;

    /** Maps and lists for the map to use and access*/
    private static Map<String, Set<String>> familyMap;
    private static List<FilterListItem> eventTypes;
    private static Map<String, Float> eventTypeColors;

    /** All information for the root person in the Family Tree */
    private static Person rootPerson;
    private static String loginUserName;
    private static String authToken;
    private static String loginPersonId;

    /** Used to store fragments from the Main Activity for later use*/
    private static LoginFragment loginFragment;
    private static MapInfoFragment mapInfoFragment;

    /** Information to correctly permeate settings activity settings*/
    private static SettingsActivity settingsActivity;
    private static int settingMapSpinner;
    private static int settingSpouseSpinner;
    private static int settingFamilyTreeSpinner;
    private static int settingLifeEventSpinner;
    private static boolean lifeSwitch;
    private static boolean familySwitch;
    private static boolean spouseSwitch;


    public AllModel(){
        authToken = null;
        serverAccess = new ServerAccess(false);
        extrasAdded = false;
        personList = new ArrayList<>();
        eventList = new ArrayList<>();
        eventTypes = new ArrayList<>();
        eventTypeColors = new HashMap<>();
        rootPerson = null;
        currentEvent = null;
        mapInfoFragment = null;
        settingsActivity = null;
        settingMapSpinner = 0;
        settingSpouseSpinner = 0;
        settingFamilyTreeSpinner = 1;
        settingLifeEventSpinner = 2;
        lifeSwitch = false;
        familySwitch = false;
        spouseSwitch = false;
    }

    /** All things that must be set upon logging out
     * On logout a new AllModel is created as well
     *      This resets all things in the constructor
     */
    public static void logout(){
        mapInfoFragment  = null;
        settingMapSpinner = 0;
        settingSpouseSpinner = 0;
        settingFamilyTreeSpinner = 1;
        settingLifeEventSpinner = 2;
        lifeSwitch = false;
        familySwitch = false;
        spouseSwitch = false;
    }

    /**
     * Called when reSync is called from the settings activity
     */
    public static void reSync(){
        personList = new ArrayList<>();
        eventList = new ArrayList<>();
        eventTypes = new ArrayList<>();
        eventTypeColors = new HashMap<>();
        rootPerson = null;
        currentEvent = null;
        extrasAdded = false;
    }

    /**
     * Used to capitalize the first letter in every word in a string
     * @param in    The string to be capitalized
     * @return      The capitalized string
     */
    public static String toUp(String in){
        try {
            return WordUtils.capitalizeFully(in);
        }
        catch (StringIndexOutOfBoundsException e){
            return in.toUpperCase();
        }
    }

    public static boolean isLifeSwitch() {
        return lifeSwitch;
    }

    public static void setLifeSwitch(boolean lifeSwitch) {
        AllModel.lifeSwitch = lifeSwitch;
    }

    public static boolean isFamilySwitch() {
        return familySwitch;
    }

    public static void setFamilySwitch(boolean familySwitch) {
        AllModel.familySwitch = familySwitch;
    }

    public static boolean isSpouseSwitch() {
        return spouseSwitch;
    }

    public static void setSpouseSwitch(boolean spouseSwitch) {
        AllModel.spouseSwitch = spouseSwitch;
    }

    public static int getSettingSpouseSpinner() {
        return settingSpouseSpinner;
    }

    public static void setSettingSpouseSpinner(int settingSpouseSpinner) {
        AllModel.settingSpouseSpinner = settingSpouseSpinner;
    }

    public static int getSettingFamilyTreeSpinner() {
        return settingFamilyTreeSpinner;
    }

    public static void setSettingFamilyTreeSpinner(int settingFamilyTreeSpinner) {
        AllModel.settingFamilyTreeSpinner = settingFamilyTreeSpinner;
    }

    public static int getSettingLifeEventSpinner() {
        return settingLifeEventSpinner;
    }

    public static void setSettingLifeEventSpinner(int settingLifeEventSpinner) {
        AllModel.settingLifeEventSpinner = settingLifeEventSpinner;
    }

    public static int getSettingMapSpinner() {
        return settingMapSpinner;
    }

    public static void setSettingMapSpinner(int settingMapSpinner) {
        AllModel.settingMapSpinner = settingMapSpinner;
    }

    public static SettingsActivity getSettingsActivity() {
        return settingsActivity;
    }

    public static void setSettingsActivity(SettingsActivity settingsActivity) {
        AllModel.settingsActivity = settingsActivity;
    }

    public static Map<String, Float> getEventTypeColors() {
        return eventTypeColors;
    }

    public static void setEventTypeColors(Map<String, Float> eventTypeColors) {
        AllModel.eventTypeColors = eventTypeColors;
    }

    public static Event getCurrentEvent() {
        return currentEvent;
    }

    public static void setCurrentEvent(Event currentEvent) {
        AllModel.currentEvent = currentEvent;
    }

    public static Person getCurrentEventPerson() {
        return currentEventPerson;
    }

    public static void setCurrentEventPerson(Person currentEventPerson) {
        AllModel.currentEventPerson = currentEventPerson;
    }

    public static Person getRootPerson() {
        return rootPerson;
    }

    public static void setRootPerson(Person rootPerson) {
        AllModel.rootPerson = rootPerson;
    }

    public static LoginFragment getLoginFragment() {
        return loginFragment;
    }

    public static void setLoginFragment(LoginFragment loginFragment) {
        AllModel.loginFragment = loginFragment;
    }

    public static MapInfoFragment getMapInfoFragment() {
        return mapInfoFragment;
    }

    public static void setMapInfoFragment(MapInfoFragment mapInfoFragment) {
        AllModel.mapInfoFragment = mapInfoFragment;
    }

    public static Map<String, Set<String>> getFamilyMap() {
        return familyMap;
    }

    public static void setFamilyMap(Map<String, Set<String>> familyMap) {
        AllModel.familyMap = familyMap;
    }

    public static List<FilterListItem> getEventTypes() {
        return eventTypes;
    }

    public static void setEventTypes(List<FilterListItem> eventTypes) {
        AllModel.eventTypes = eventTypes;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        AllModel.mainActivity = mainActivity;
    }

    public static ServerAccess getServerAccess() {
        return serverAccess;
    }

    public static void setServerAccess(ServerAccess serverAccess) {
        AllModel.serverAccess = serverAccess;
    }

    public static boolean isExtrasAdded() {
        return extrasAdded;
    }

    public static void setExtrasAdded(boolean loginContinue) {
        AllModel.extrasAdded = loginContinue;
    }

    public static List<Person> getPersonList() {
        return personList;
    }

    public static void setPersonList(List<Person> personList) {
        AllModel.personList = personList;
    }

    public static List<Event> getEventList() {
        return eventList;
    }

    public static void setEventList(List<Event> eventList) {
        AllModel.eventList = eventList;
    }

    public static String getLoginUserName() {
        return loginUserName;
    }

    public static void setLoginUserName(String loginUserName) {
        AllModel.loginUserName = loginUserName;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        AllModel.authToken = authToken;
    }

    public static String getLoginPersonId() {
        return loginPersonId;
    }

    public static void setLoginPersonId(String loginPersonId) {
        AllModel.loginPersonId = loginPersonId;
    }
}
