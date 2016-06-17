package com.jnbonner.familymap.Person;

import android.media.Image;

import com.jnbonner.familymap.Model.Event;
import com.jnbonner.familymap.Model.Person;
import com.jnbonner.familymap.R;
import com.joanzapata.android.iconify.Iconify;

import java.util.Comparator;

/**
 * Created by James on 3/28/2016.
 */
public class PersonChildObject {
    private Person personPoint;
    private String title;
    private String description;
    public Iconify.IconValue image;
    private Event personEvent;
    public int color;

    public PersonChildObject(String title, String description,
                             Iconify.IconValue image, Person person, Event event){
        this.title = title;
        this.description = description;
        this.image = image;
        personPoint = person;
        personEvent = event;
        setImageColor(image);
    }

    public PersonChildObject(String title, String description,
                             Iconify.IconValue image, Person person){
        this.title = title;
        this.description = description;
        this.image = image;
        personEvent = null;
        personPoint = person;
        setImageColor(image);
    }

    private void setImageColor(Iconify.IconValue image){

        switch (image.formattedName()){
            case "{fa_male}": color = R.color.colorMale;
                break;
            case "{fa_female}": color = R.color.colorAccent;
                break;
            case "{fa_map_marker}": color = R.color.mapMarker;
                break;
            default: color = R.color.genderless;
        }
    }

    public Person getPersonPoint() {
        return personPoint;
    }

    public void setPersonPoint(Person personPoint) {
        this.personPoint = personPoint;
    }

    public Event getPersonEvent() {
        return personEvent;
    }

    public void setPersonEvent(Event personEvent) {
        this.personEvent = personEvent;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Iconify.IconValue getImage() {
        return image;
    }

    public void setImage(Iconify.IconValue image) {
        this.image = image;
    }
}
