package com.jnbonner.familymap.Person;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapterHelper;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.jnbonner.familymap.Filter.FilterListItem;
import com.jnbonner.familymap.MainActivity;
import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.Model.Event;
import com.jnbonner.familymap.Model.Person;
import com.jnbonner.familymap.R;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by James on 3/28/2016.
 */
public class PersonFragment extends Fragment{
    private RecyclerView recyclerView;
    private PersonExpandableAdapter mAdapter;
    private Person person;

    private TextView firstName;
    private TextView lastName;
    private TextView gender;

    private MenuItem toTop;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        person = AllModel.getCurrentEventPerson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.person_fragment_layout, container, false);

        // Create recycler view
        recyclerView = (RecyclerView) view
                .findViewById(R.id.person_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI(view);

        return view;
    }

    private void updateUI(View view) {
        mAdapter = new PersonExpandableAdapter(this.getContext(), populateParentList());
        recyclerView.setAdapter(mAdapter);

        firstName = (TextView)view.findViewById(R.id.textView_person_firstName);
        lastName = (TextView)view.findViewById(R.id.textView_person_lastName);
        gender = (TextView)view.findViewById(R.id.textView_person_gender);

        firstName.setText(AllModel.toUp(person.getFirstName()));
        lastName.setText(AllModel.toUp(person.getLastName()));
        if(person.isGender()){
            gender.setText("Male");
        }
        else{
            gender.setText("Female");
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map_activity_menu, menu);

        toTop = menu.findItem(R.id.map_go_to_top);
        Drawable toSetToTop = new IconDrawable(getActivity(), Iconify.IconValue.fa_angle_double_up)
                .colorRes(R.color.menuIcon)
                .sizeDp(40);
        toTop.setIcon(toSetToTop);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_go_to_top:
                goToTop();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Takes the user back to the main activity*/
    private void goToTop(){
        AllModel.setCurrentEvent(null);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /** Creates the Parent list items and their designated children items*/
    private List<ParentListItem> populateParentList(){
        List<ParentListItem> parentItemList = new ArrayList<>();

        // The Life events parent object and children
        String title = "LIFE EVENTS";
        PersonParentObject toAddLife = new PersonParentObject(title);
        for (Event temp: person.getPersEvents()){
            String childTitle = AllModel.toUp(temp.getDescription()) +
                    ": " +
                    AllModel.toUp(temp.getCity()) +
                    ", " +
                    AllModel.toUp(temp.getCountry()) +
                    " (" +
                    temp.getYear() +
                    ")";
            String childDescription = AllModel.toUp(person.getFirstName())
                    + " " + AllModel.toUp(person.getLastName());
            Iconify.IconValue image = Iconify.IconValue.fa_map_marker;

            toAddLife.getChildItemList()
                    .add(new PersonChildObject(childTitle, childDescription, image, person, temp));
        }
        parentItemList.add(toAddLife);


        // The Family members parent object and children
        title = "FAMILY";
        PersonParentObject toAddFamily = new PersonParentObject(title);

        if (person.getFather() != null) {
            String fTitle = AllModel.toUp(person.getFatherPoint().getFirstName()) + " "
                    + AllModel.toUp(person.getFatherPoint().getLastName());
            String fDescription = "Father";
            Iconify.IconValue fImage = Iconify.IconValue.fa_male;
            toAddFamily.getChildItemList()
                    .add(new PersonChildObject(fTitle, fDescription, fImage, person.getFatherPoint()));
        }
        if (person.getMother() != null){
            String mTitle = AllModel.toUp(person.getMotherPoint().getFirstName()) + " "
                    + AllModel.toUp(person.getMotherPoint().getLastName());
            String mDescription = "Mother";
            Iconify.IconValue mImage = Iconify.IconValue.fa_female;
            toAddFamily.getChildItemList()
                    .add(new PersonChildObject(mTitle, mDescription, mImage, person.getMotherPoint()));
        }
        if (person.getSpouse() != null){
            String sTitle = AllModel.toUp(person.getSpousePoint().getFirstName()) + " "
                    + AllModel.toUp(person.getSpousePoint().getLastName());
            String sDescription = "Spouse";
            Iconify.IconValue sImage = (person.getSpousePoint().isGender())?
                    Iconify.IconValue.fa_male : Iconify.IconValue.fa_female;
            toAddFamily.getChildItemList()
                    .add(new PersonChildObject(sTitle, sDescription, sImage, person.getSpousePoint()));
        }
        if (person.getChildren().size() > 0){
            for (Person child: person.getChildren()){
                String cTitle = AllModel.toUp(child.getFirstName()) + " "
                        + AllModel.toUp(child.getLastName());
                String cDescription = "Child";
                Iconify.IconValue cImage =(child.isGender())?
                        Iconify.IconValue.fa_male : Iconify.IconValue.fa_female;
                toAddFamily.getChildItemList()
                        .add(new PersonChildObject(cTitle, cDescription, cImage, child));
            }
        }
        parentItemList.add(toAddFamily);

        return parentItemList;
    }


}
