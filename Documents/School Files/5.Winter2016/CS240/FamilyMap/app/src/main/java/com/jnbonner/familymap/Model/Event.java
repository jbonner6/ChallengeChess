package com.jnbonner.familymap.Model;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by James on 3/10/2016.
 *
 */
public class Event implements Comparable<Event>{
    String eventID;
    String personID;
    double latitude;
    double longitude;
    String country;
    String city;
    String description;
    int year;
    String descendant;
    Person person;
    Marker marker;



    public Event(String eventID, String personID, double latitude, double longitude, String country,
                 String city, String description, int year, String descendant){
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.description = description;
        this.year = year;
        this.descendant = descendant;
    }

    @Override
    /** Compare function overridden from Comparable for events used to sort*/
    public int compareTo(Event another) {

        if (this.year != -1) {
            if (this.year > another.getYear()) { // This year is future of another
                return 1;
            } else if (this.year < another.getYear()) { // this year is past of another
                return -1;
            } else if (this.year == another.getYear()) { // -1 = Another desc is greater than this desc
                return this.getDescription().compareToIgnoreCase(another.getDescription());
            } else {
                return 0;
            }
        }
        else {
            return this.getDescription().compareToIgnoreCase(another.getDescription());
        }
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public void setPerson(Person person){
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }
}
