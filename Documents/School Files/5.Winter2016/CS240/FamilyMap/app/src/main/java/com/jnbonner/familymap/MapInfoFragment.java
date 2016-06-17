package com.jnbonner.familymap;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jnbonner.familymap.Filter.FilterActivity;
import com.jnbonner.familymap.Filter.FilterListItem;
import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.Model.Event;
import com.jnbonner.familymap.Model.Person;
import com.jnbonner.familymap.Person.PersonActivity;
import com.jnbonner.familymap.Search.SearchActivity;
import com.jnbonner.familymap.Settings.SettingsActivity;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by James on 3/20/2016.
 */
public class MapInfoFragment extends Fragment {

    private static final String ARG_TITLE = "Title";
    private String mParam1;
    private LinearLayout eventInfo;
    private GoogleMap mMap;
    private Map<Person, Marker> personToMarker;
    private Map<Marker, Person> markerToPerson;
    private Map<Event, Marker> eventMarkerMap;
    private List<Polyline> allPolyLines;
    private TextView topText;
    private TextView bottomText;
    private ImageView genderImage;
    private MenuItem search;
    private MenuItem filter;
    private MenuItem settings;
    private MenuItem toTop;
    private Person currentEventPerson;
    private Event currentEventFocus;


    public MapInfoFragment(){

    }

    public static MapInfoFragment newInstance(String title){
        MapInfoFragment fragment = new MapInfoFragment();
        Bundle args = new Bundle();

        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        personToMarker = new HashMap<>();
        markerToPerson = new HashMap<>();
        eventMarkerMap = new HashMap<>();
        currentEventPerson = null;
        currentEventFocus = AllModel.getCurrentEvent();
        allPolyLines = new ArrayList<>();

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_TITLE);
        }

        // Creates a new map fragment
        SupportMapFragment sMap = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.mainMapFragment);
        if (sMap == null){
            sMap = SupportMapFragment.newInstance();
        }

        // Gets the actual google map and saves it off in to mMap upon completion
        sMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // If being called from a person activity
                // zooms in on a specific event on the map
                if (currentEventFocus != null) {
                    LatLng eventFocus = new LatLng(AllModel.getCurrentEvent().getLatitude(),
                            AllModel.getCurrentEvent().getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventFocus, 5));
                }

                // Sets the click listener for markers on the map
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        handleMarkerClick(marker);
                        return true;
                    }
                });
                placeEventMarkers();
                if (currentEventFocus != null) {
                    handleMarkerClick(currentEventFocus.getMarker());
                }
                setMapType();

            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainMapFragment, sMap).commit();
    }

    // Sets the map type upon creation based on selection from Settins activity
    private void setMapType(){
        String type = getResources().getStringArray(R.array.map_types_array)[AllModel.getSettingMapSpinner()];
        switch (type){
            case "Normal":mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case "Hybrid":mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case "Satellite":mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case "Terrain":mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (currentEventFocus == null) {
            createMenuNoEvent(menu, inflater);
        }
        else {
            createMenuWithEvent(menu, inflater);
        }
    }

    /** Creates the options menu at top of activity when there is an event focused
            Coming from a person activity */
    private void createMenuWithEvent(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_activity_menu, menu);

        toTop = menu.findItem(R.id.map_go_to_top);
        Drawable toSetToTop = new IconDrawable(getActivity(), Iconify.IconValue.fa_angle_double_up)
                .colorRes(R.color.menuIcon)
                .sizeDp(40);
        toTop.setIcon(toSetToTop);
    }

    /**
     * Creates the options menu at the top of the activity when there is no event to focus
     *      This is the map options setting for a new map after login
     */
    private void createMenuNoEvent(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.map_menu_options, menu);

        search = menu.findItem(R.id.menu_item_search);
        Drawable toSetSearch = new IconDrawable(getActivity(), Iconify.IconValue.fa_search)
                .colorRes(R.color.menuIcon)
                .sizeDp(40);
        search.setIcon(toSetSearch);

        filter = menu.findItem(R.id.menu_item_filter);
        Drawable toSetFilter = new IconDrawable(getActivity(), Iconify.IconValue.fa_filter)
                .colorRes(R.color.menuIcon)
                .sizeDp(40);
        filter.setIcon(toSetFilter);

        settings = menu.findItem(R.id.menu_item_settings);
        Drawable toSetSettings = new IconDrawable(getActivity(), Iconify.IconValue.fa_gear)
                .colorRes(R.color.menuIcon)
                .sizeDp(40);
        settings.setIcon(toSetSettings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                startSearchActivity();
                return true;
            case R.id.menu_item_filter:
                startFilterActivity();
                return true;
            case R.id.menu_item_settings:
                startSettingsActivity();
                return true;
            case R.id.map_go_to_top:
                goToTop();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToTop(){
        AllModel.setCurrentEvent(null);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startSearchActivity(){
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void startFilterActivity(){
        Intent intent = new Intent(getActivity(), FilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // not sure what this does...
        startActivity(intent);
    }

    private void startSettingsActivity(){
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void handleMarkerClick(Marker marker){
        // removes all previously made polyLines
        for (Polyline temp: allPolyLines){
            temp.remove();
        }
        allPolyLines.clear();

        // Sets the information at the bottom of the screen for the specific marker
        Event event = null;
        Person toDisplay = markerToPerson.get(marker);
        currentEventPerson = toDisplay;
        for (Event temp: toDisplay.getPersEvents()) {
            if (temp.getDescription().equals(marker.getTitle())){
                event = temp;
                break;
            }
        }
        if (toDisplay != null) {
            topText.setText(AllModel.toUp(toDisplay.getFirstName()) + " "
                    + AllModel.toUp(toDisplay.getLastName()));
            bottomText.setText(AllModel.toUp(event.getDescription()) +
                    ": " +
                    AllModel.toUp(event.getCity()) +
                    ", " +
                    AllModel.toUp(event.getCountry()) +
                    " (" +
                    event.getYear() +
                    ")");
        }
        if (toDisplay.isGender()) {
            genderImage.setImageDrawable(
                    new IconDrawable(getActivity(), Iconify.IconValue.fa_male)
                            .colorRes(R.color.colorMale)
                            .sizeDp(100));
        }
        else {
            genderImage.setImageDrawable(
                    new IconDrawable(getActivity(), Iconify.IconValue.fa_female)
                            .colorRes(R.color.colorAccent)
                            .sizeDp(100));
        }

        setLines(marker, toDisplay, event);

    }

    /** Sets the polyLines based upon current settings in the Settings activity */
    private void setLines(Marker marker, Person person, Event event){
        if (AllModel.isSpouseSwitch()){
            if (person.getSpousePoint() != null){
                Event spouseFirst = person.getSpousePoint().getPersEvents().get(0);
                LatLng toSpouse = new LatLng(spouseFirst.getLatitude(), spouseFirst.getLongitude());
                PolylineOptions toDraw = new PolylineOptions()
                        .add(marker.getPosition(), toSpouse)
                        .width(10)
                        .color(getLineColor(AllModel.getSettingSpouseSpinner()));
                Polyline toList = mMap.addPolyline(toDraw);
                allPolyLines.add(toList);
            }
        }
        if (AllModel.isLifeSwitch()){
            for (int i = 0; i < person.getPersEvents().size() - 1; i++){
                Event first = person.getPersEvents().get(i);
                Event second = person.getPersEvents().get(i + 1);

                LatLng llFirst = new LatLng(first.getLatitude(), first.getLongitude());
                LatLng llSecond = new LatLng(second.getLatitude(), second.getLongitude());
                PolylineOptions toDraw = new PolylineOptions()
                        .add(llFirst, llSecond)
                        .width(10)
                        .color(getLineColor(AllModel.getSettingLifeEventSpinner()));
                Polyline toList = mMap.addPolyline(toDraw);
                allPolyLines.add(toList);
            }
        }
        if (AllModel.isFamilySwitch()){
            List<Polyline> lines = new ArrayList<>();
            if (person.getFatherPoint() != null) {
                Event sEvent = person.getFatherPoint().getPersEvents().get(0);
                LatLng llSecond = new LatLng(sEvent.getLatitude(), sEvent.getLongitude());
                PolylineOptions toDraw = new PolylineOptions()
                        .add(marker.getPosition(), llSecond)
                        .width(30)
                        .color(getLineColor(AllModel.getSettingFamilyTreeSpinner()));
                Polyline toList = mMap.addPolyline(toDraw);
                lines.add(toList);
                familyLineRecurse(person.getFatherPoint(), lines, 30);
            }
            if (person.getMotherPoint() != null){
                Event sEvent = person.getMotherPoint().getPersEvents().get(0);
                LatLng llSecond = new LatLng(sEvent.getLatitude(), sEvent.getLongitude());
                PolylineOptions toDraw = new PolylineOptions()
                        .add(marker.getPosition(), llSecond)
                        .width(30)
                        .color(getLineColor(AllModel.getSettingFamilyTreeSpinner()));
                Polyline toList = mMap.addPolyline(toDraw);
                lines.add(toList);
                familyLineRecurse(person.getMotherPoint(), lines, 30);
            }
            allPolyLines.addAll(lines);
            lines.clear();
        }
    }

    /** Recursively places polyLines up the family tree to all ancestors*/
    private void familyLineRecurse(Person person, List<Polyline> lines, int width){
        width -= 3;
        if (width < 1){width = 1;}
        if (person.getFatherPoint() != null){
            Event fEvent = person.getPersEvents().get(0);
            Event sEvent = person.getFatherPoint().getPersEvents().get(0);
            LatLng llFirst = new LatLng(fEvent.getLatitude(), fEvent.getLongitude());
            LatLng llSecond = new LatLng(sEvent.getLatitude(), sEvent.getLongitude());
            PolylineOptions toDraw = new PolylineOptions()
                    .add(llFirst, llSecond)
                    .width(width)
                    .color(getLineColor(AllModel.getSettingFamilyTreeSpinner()));
            Polyline toList = mMap.addPolyline(toDraw);
            lines.add(toList);
            familyLineRecurse(person.getFatherPoint(), lines, width);
        }
        if (person.getMotherPoint() != null){
            Event fEvent = person.getPersEvents().get(0);
            Event sEvent = person.getMotherPoint().getPersEvents().get(0);
            LatLng llFirst = new LatLng(fEvent.getLatitude(), fEvent.getLongitude());
            LatLng llSecond = new LatLng(sEvent.getLatitude(), sEvent.getLongitude());
            PolylineOptions toDraw = new PolylineOptions()
                    .add(llFirst, llSecond)
                    .width(width)
                    .color(getLineColor(AllModel.getSettingFamilyTreeSpinner()));
            Polyline toList = mMap.addPolyline(toDraw);
            lines.add(toList);
            familyLineRecurse(person.getMotherPoint(), lines, width);
        }

    }

    private int getLineColor(int index){
        String type = getResources().getStringArray(R.array.line_color_options)[index];
        int color;
        switch (type){
            case "Red":color = ContextCompat.getColor(getContext(),R.color.lineRed);
                break;
            case "Blue":color = ContextCompat.getColor(getContext(),R.color.lineBlue);
                break;
            case "Green":color = ContextCompat.getColor(getContext(),R.color.lineGreen);
                break;
            default:color = ContextCompat.getColor(getContext(),R.color.lineRed);
        }
        return color;
    }

    /** Places all event markers based on current filter settings */
    public void placeEventMarkers(){
        for(Event temp: AllModel.getEventList()) {
            LatLng toPlace = new LatLng(temp.getLatitude(), temp.getLongitude());
            BitmapDescriptor icon = getMarkerColor(temp.getDescription());

            MarkerOptions toAdd = new MarkerOptions()
                    .title(temp.getDescription())
                    .snippet(temp.getPerson().getFirstName() + " " + temp.getPerson().getLastName())
                    .position(toPlace)
                    .icon(icon);
            if (currentEventFocus == null){
                setMarkersShown(toAdd, temp.getPerson());
            }

            Marker m = mMap.addMarker(toAdd);
            temp.setMarker(m);
            eventMarkerMap.put(temp, m);
            personToMarker.put(temp.getPerson(), m);
            markerToPerson.put(m, temp.getPerson());
            if (!AllModel.isExtrasAdded()) {
                boolean wasFound = false;
                for (FilterListItem eTemp : AllModel.getEventTypes()) {
                    if (eTemp.getFilterTitle().equals(temp.getDescription())) {
                        wasFound = true;
                    }
                }
                if (!wasFound) {
                    String description = "FILTER BY " + temp.getDescription() + " EVENTS";
                    FilterListItem filterListItem =
                            new FilterListItem(temp.getDescription(), description.toUpperCase(), true);
                    AllModel.getEventTypes().add(filterListItem);
                }
            }
        }
        if (!AllModel.isExtrasAdded()) {
            addExtraFilters();
        }

    }

    /** Used to set the visibility of a marker based on Filter activity settings*/
    private void setMarkersShown(MarkerOptions marker, Person person){
        for (FilterListItem temp: AllModel.getEventTypes()){
            if (temp.getFilterTitle().equals(marker.getTitle())){
                if (!temp.isFilterShown()){
                    marker.visible(false);
                }
            }
            else if (temp.getFilterTitle().equals("Male Events")){
                if (person.isGender() && !temp.isFilterShown()){
                    marker.visible(false);
                }
            }
            else if (temp.getFilterTitle().equals("Female Events")){
                if (!person.isGender() && !temp.isFilterShown()){
                    marker.visible(false);
                }
            }
            else if (!person.isRoot()) {
                if (temp.getFilterTitle().equals("Father's Side")) {
                    if (person.isFatherSide() && !temp.isFilterShown()) {
                        marker.visible(false);
                    }
                } else if (temp.getFilterTitle().equals("Mother's Side")) {
                    if (!person.isFatherSide() && !temp.isFilterShown()) {
                        marker.visible(false);
                    }
                }
            }
        }
    }

    /** Generates filter types after entering the dynamic filters*/
    private void addExtraFilters(){
        String title = "Father's Side";
        String desc = "FILTER BY " + title + " OF FAMILY";
        desc = desc.toUpperCase();
        AllModel.getEventTypes().add(new FilterListItem(title, desc, true));

        title = "Mother's Side";
        desc = "FILTER BY " + title + " OF FAMILY";
        desc = desc.toUpperCase();
        AllModel.getEventTypes().add(new FilterListItem(title, desc, true));

        title = "Male Events";
        desc = "FILTER EVENTS BASED ON GENDER";
        AllModel.getEventTypes().add(new FilterListItem(title, desc, true));

        title = "Female Events";
        AllModel.getEventTypes().add(new FilterListItem(title, desc, true));

        AllModel.setExtrasAdded(true);
    }

    /** Gets the color of a marker or generates a new one if no marker of its type has been created*/
    private BitmapDescriptor getMarkerColor(String description){
        float color;
        if (AllModel.getEventTypeColors().containsKey(description)){
            color = AllModel.getEventTypeColors().get(description);
        }
        else {
            color = new Random().nextInt(360);
            AllModel.getEventTypeColors().put(description, color);
        }

        return BitmapDescriptorFactory.defaultMarker(color);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_maps, container, false);
        eventInfo = (LinearLayout)v.findViewById(R.id.event_info);
        topText = (TextView)v.findViewById(R.id.eventInfoTop);
        bottomText = (TextView)v.findViewById(R.id.eventInfoBottom);
        genderImage = (ImageView)v.findViewById(R.id.gender_image);
        genderImage.setImageDrawable(new IconDrawable(getActivity(), Iconify.IconValue.fa_genderless)
                .colorRes(R.color.genderless)
                .sizeDp(100));

        eventInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentEventPerson != null){
                    AllModel.setCurrentEventPerson(currentEventPerson);
                    startPersonActivity(v);
                }
                else {
                    Toast.makeText(v.getContext(), "Please Select a Marker to See More Information",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    private void startPersonActivity(View v){
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        startActivity(intent);
    }
}
