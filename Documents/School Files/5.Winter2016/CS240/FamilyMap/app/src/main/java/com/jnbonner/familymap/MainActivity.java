package com.jnbonner.familymap;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.Model.Person;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fm;
    private LoginFragment loginFragment;
    private MapInfoFragment myMapFragment;
    AllModel allModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = this.getSupportFragmentManager();
        AllModel.setMainActivity(this);

        // Initial Start of app or on Logout
        if (savedInstanceState == null &&
                AllModel.getMapInfoFragment() == null) {
            allModel = new AllModel();
            createLogin();
            myMapFragment = null;
        }
        // Returning to the main fragment but should display the map
        else {
            loginFragment = AllModel.getLoginFragment();
            if (AllModel.getMapInfoFragment() != null) {
                myMapFragment = AllModel.getMapInfoFragment();
                fm.beginTransaction().
                        add(R.id.loginFrame, myMapFragment).commit();
            }
        }
    }

    // Called for a new login
    public void createLogin(){
        loginFragment = (LoginFragment)fm.findFragmentById(R.id.loginFrame);
        if (loginFragment == null){
            loginFragment = LoginFragment.newInstance("LOGIN PAGE");
        }
        fm.beginTransaction().
                add(R.id.loginFrame, loginFragment).commit();
    }

    // Called from the async task after logging in to switch the Login fragment
    //   out for the map fragment
    public void transToMap(){
        if(AllModel.getMapInfoFragment() == null) {
            myMapFragment = (MapInfoFragment) fm.findFragmentById(R.id.mapFragLayout);
            if (myMapFragment == null) {
                myMapFragment = MapInfoFragment.newInstance("MainMapFragment");

                fm.beginTransaction().replace(R.id.loginFrame, myMapFragment).commit();
            }
            myMapFragment.setHasOptionsMenu(true);
            AllModel.setMapInfoFragment(myMapFragment);
        }

        if (AllModel.getRootPerson() == null) {
            genFamilyTree();
        }

        organizeEvents();
    }

    // This will organize the events in to the correct order for each person
    private void organizeEvents(){
        for (Person temp: AllModel.getPersonList()){
            temp.organizeEvents();
        }
    }

    // will generate the family tree after logging in successfully
    private void genFamilyTree(){
        for (Person temp : AllModel.getPersonList()){ // the current person
            if (temp.getPersonId().equals(AllModel.getLoginPersonId())){
                AllModel.setRootPerson(temp);
            }
            if (temp.getFather() != null){
                for (Person tempF : AllModel.getPersonList()){ // the current person's father
                    if (temp.getFather().equals(tempF.getPersonId())){
                        temp.setFatherPoint(tempF);
                        tempF.getChildren().add(temp);
                    }
                }
            }
            if (temp.getMother() != null){
                for (Person tempM : AllModel.getPersonList()){ // the current person's mother
                    if (temp.getMother().equals(tempM.getPersonId())){
                        temp.setMotherPoint(tempM);
                        tempM.getChildren().add(temp);
                    }
                }
            }
            if (temp.getSpouse() != null && temp.getSpousePoint() == null){
                for (Person tempS : AllModel.getPersonList()){ // the current person's spouse
                    if (temp.getSpouse().equals(tempS.getPersonId())){
                        temp.setSpousePoint(tempS);
                        tempS.setSpousePoint(temp);
                    }
                }
            }
        }
        detSideOfFamily();
    }

    // sets which side of the family a person belongs to
    private void detSideOfFamily(){
        for (Person temp: AllModel.getPersonList()){
            if (temp.equals(AllModel.getRootPerson())){
                temp.setIsRoot(true);                   // set the root person to know it is root
            }
            else if(findSideRecurse(AllModel.getRootPerson().getFatherPoint(), temp)){
                temp.setIsFatherSide(true);
            }
            else{
                temp.setIsFatherSide(false);
            }
        }
    }

    // recursive portion of finding side of family
    private boolean findSideRecurse(Person person, Person toFind){
        if (person.equals(toFind)){
            return true;
        }
        else if (person.getFatherPoint() != null && person.getMotherPoint() != null) {
            if (person.getFatherPoint().equals(toFind) || person.getMotherPoint().equals(toFind)) {
                return true;
            } else {
                return findSideRecurse(person.getFatherPoint(), toFind) ||
                        findSideRecurse(person.getMotherPoint(), toFind);
            }
        }
        else {
            return false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        AllModel.setLoginFragment(loginFragment);
        AllModel.setMapInfoFragment(myMapFragment);
        savedInstanceState.putString("MyString", "Welcome back to Main");
        // etc.
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        String myString = savedInstanceState.getString("MyString");
        loginFragment = AllModel.getLoginFragment();
        myMapFragment = AllModel.getMapInfoFragment();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}
