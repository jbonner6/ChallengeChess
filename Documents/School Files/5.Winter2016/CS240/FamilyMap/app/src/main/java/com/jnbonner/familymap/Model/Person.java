package com.jnbonner.familymap.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by James on 3/10/2016.
 *
 */
public class Person {
    private String descendant;
    private String personId;
    private String firstName;
    private String lastName;
    private boolean gender; //T = Male .... F = Female
    private String father;
    private String mother;
    private String spouse;

    private Person fatherPoint;
    private Person motherPoint;
    private Person spousePoint;
    private List<Person> children;
    private boolean isFatherSide;
    private boolean isRoot;
    private boolean blankOnPurpose;

    private List<Event> persEvents;

    public Person(String descendant, String personId, String firstName, String lastName, boolean gender,
                  String father, String mother, String spouse){
        this.descendant = descendant;
        this.personId = personId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;

        persEvents = new ArrayList<>();
        children = new ArrayList<>();
        isRoot = false;
        spousePoint = null;
        blankOnPurpose = false;
    }

    /**
     * Special constructor used by the search class for empty queries
     */
    public Person(boolean isBlankOnPurpose){
        blankOnPurpose = true;
    }

    /**
     * Used to sort the Events for each person by Specificatoins given
     */
    public void organizeEvents(){
        Collections.sort(persEvents);

        if (!persEvents.get(0).getDescription().equals("birth") ||
                 !persEvents.get(persEvents.size() - 1).getDescription().equals("death")) {
            int index = 0;

            for (Event temp : persEvents) {
                if (temp.getDescription().equals("birth")) {
                    swapItems(0, index);
                }
                index++;
            }
            index = 0;
            for (Event temp : persEvents) {
                if (temp.getDescription().equals("death")) {
                    swapItems(persEvents.size() - 1, index);
                }
                index++;
            }
        }
    }

    /** Used only in the organize events to swap items at given indices*/
    private void swapItems(int a, int b){
        Event holder;
        holder = persEvents.get(a);
        persEvents.set(a, persEvents.get(b));
        persEvents.set(b, holder);
    }

    public boolean isBlankOnPurpose() {
        return blankOnPurpose;
    }

    public void setBlankOnPurpose(boolean blankOnPurpose) {
        this.blankOnPurpose = blankOnPurpose;
    }

    public Person getSpousePoint() {
        return spousePoint;
    }

    public void setSpousePoint(Person spousePoint) {
        this.spousePoint = spousePoint;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public boolean isFatherSide() {
        return isFatherSide;
    }

    public void setIsFatherSide(boolean isFatherSide) {
        this.isFatherSide = isFatherSide;
    }

    public Person getFatherPoint() {
        return fatherPoint;
    }

    public void setFatherPoint(Person fatherPoint) {
        this.fatherPoint = fatherPoint;
    }

    public Person getMotherPoint() {
        return motherPoint;
    }

    public void setMotherPoint(Person motherPoint) {
        this.motherPoint = motherPoint;
    }

    public List<Person> getChildren() {
        return children;
    }

    public void setChildren(List<Person> children) {
        this.children = children;
    }

    public List<Event> getPersEvents() {
        return persEvents;
    }

    public void setPersEvents(List<Event> persEvents) {
        this.persEvents = persEvents;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }
}
