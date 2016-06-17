package com.jnbonner.familymap.Search;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.jnbonner.familymap.Filter.FilterListItem;
import com.jnbonner.familymap.MainActivity;
import com.jnbonner.familymap.Map.MyMapActivity;
import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.Model.Event;
import com.jnbonner.familymap.Model.Person;
import com.jnbonner.familymap.Person.PersonActivity;
import com.jnbonner.familymap.Person.PersonChildObject;
import com.jnbonner.familymap.Person.PersonExpandableAdapter;
import com.jnbonner.familymap.R;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 4/4/2016.
 */
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapter mAdapter;
    private EditText searchField;
    private ImageButton searchButton;


    List<PersonChildObject> personChildObjects;
    private String searchQuery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personChildObjects = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment_layout, container, false);
        recyclerView = (RecyclerView) view
                .findViewById(R.id.search_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI(view);

        return view;
    }

    private void updateUI(View view){
        mAdapter = new SearchAdapter(personChildObjects);
        recyclerView.setAdapter(mAdapter);

        searchField = (EditText)view.findViewById(R.id.search_edit_text);
        // Text change listener to auto update options in search as search occurs
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                searchQuery = searchField.getText().toString();
                personChildObjects.clear();
                populateParentList(searchQuery);
                mAdapter.notifyDataSetChanged();
            }
        });

        searchButton = (ImageButton)view.findViewById(R.id.search_image_button);
        Drawable toSetSearch = new IconDrawable(getActivity(), Iconify.IconValue.fa_search)
                .colorRes(R.color.menuIcon)
                .sizeDp(40);
        searchButton.setImageDrawable(toSetSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchQuery = searchField.getText().toString();
                personChildObjects.clear();
                populateParentList(searchQuery);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void populateParentList(String query){
        query = query.toLowerCase();
        for (Person person: AllModel.getPersonList()){
            boolean found = false;
            if (person.getFirstName().contains(query)){
                found = true;
            }
            else if (person.getLastName().contains(query)){
                found = true;
            }
            else if ((person.getFirstName() + " " + person.getLastName()).contains(query)){
                found = true;
            }
            if (found) {
                String title = AllModel.toUp(person.getFirstName()) + " "
                        + AllModel.toUp(person.getLastName());
                String description = "";
                Iconify.IconValue image;
                if (person.isGender()) {
                    image = Iconify.IconValue.fa_male;
                } else {
                    image = Iconify.IconValue.fa_female;
                }
                personChildObjects.add(new PersonChildObject(title, description, image, person));
            }
        }
        for (Event event: AllModel.getEventList()){
            boolean found = false;
            if (event.getDescription().contains(query)){
                found = true;
            }
            else if (event.getCountry().contains(query)){
                found = true;
            }
            else if (event.getCity().contains(query)){
                found = true;
            }
            else if (String.valueOf(event.getYear()).contains(query)){
                found = true;
            }
            if (found){
                String childTitle = AllModel.toUp(event.getDescription()) +
                        ": " +
                        AllModel.toUp(event.getCity()) +
                        ", " +
                        AllModel.toUp(event.getCountry()) +
                        " (" +
                        event.getYear() +
                        ")";
                String childDescription = AllModel.toUp(event.getPerson().getFirstName())
                        + " " + AllModel.toUp(event.getPerson().getLastName());
                Iconify.IconValue image = Iconify.IconValue.fa_map_marker;

                personChildObjects.add(new PersonChildObject(childTitle,
                        childDescription, image, event.getPerson(), event));
            }
        }
        if (personChildObjects.size() == 0){
            String title = "No Results Found";
            String description = "";
            Iconify.IconValue image = Iconify.IconValue.fa_edit;
            personChildObjects.add(new PersonChildObject(title, description, image, new Person(true)));
        }
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

    private void goToTop(){
        AllModel.setCurrentEvent(null);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // The adapter for the searched items
    private class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

        private List<PersonChildObject> mSearchOptions;

        public SearchAdapter(List<PersonChildObject> filterOptions) {
            mSearchOptions = filterOptions;
        }

        @Override
        public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.person_child_layout, parent, false);
            return new SearchHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchHolder holder, int position) {
            PersonChildObject personChildItem = mSearchOptions.get(position);
            holder.bindFilterItem(personChildItem);
        }
        @Override
        public int getItemCount() {
            return mSearchOptions.size();
        }
    }

    // The view holder for the searched items
    private class SearchHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView personListItemTitle;
        private TextView personListItemDescription;
        private ImageView personListItemImage;
        private PersonChildObject personChildObject;

        public SearchHolder(View itemView) {
            super(itemView);
            personListItemTitle = (TextView)itemView
                    .findViewById(R.id.person_item_title_text_view);
            personListItemDescription = (TextView)itemView
                    .findViewById(R.id.person_item_description_text_view);
            personListItemImage = (ImageView) itemView.findViewById(R.id.person_child_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            AllModel.setCurrentEventPerson(personChildObject.getPersonPoint());
            if (!personChildObject.getPersonPoint().isBlankOnPurpose()){
                if(personChildObject.getPersonEvent() != null){
                    AllModel.setCurrentEvent(personChildObject.getPersonEvent());
                    Intent intent = new Intent(getActivity(), MyMapActivity.class);
                    getActivity().startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    getActivity().startActivity(intent);
                }
            }
            else {
                Toast.makeText(getActivity(),
                        personListItemTitle.getText(), Toast.LENGTH_SHORT).show();
            }
        }

        public void bindFilterItem(PersonChildObject personListItem){
            personChildObject = personListItem;
            personListItemTitle.setText(personListItem.getTitle());
            personListItemDescription.setText(personListItem.getDescription());
            personListItemImage.setImageDrawable(new IconDrawable(getActivity(),
                    personListItem.getImage())
                    .sizeDp(40)
                    .colorRes(personListItem.getColor()));
        }

    }
}
