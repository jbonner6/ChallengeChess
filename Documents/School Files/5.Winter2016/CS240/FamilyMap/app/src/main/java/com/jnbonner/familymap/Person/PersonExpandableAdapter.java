package com.jnbonner.familymap.Person;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.jnbonner.familymap.Map.MyMapActivity;
import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.R;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;

import java.util.List;

/**
 * Created by James on 3/28/2016.
 */
public class PersonExpandableAdapter extends ExpandableRecyclerAdapter<PersonParentViewHolder, PersonChildViewHolder> {

    LayoutInflater mInflater;
    Activity activity;
    List<ParentListItem> parentItemList;

    public PersonExpandableAdapter(Context context, List<ParentListItem> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
        activity = (Activity)context;
        this.parentItemList = parentItemList;
    }

    @Override
    public PersonParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.person_parent_layout, viewGroup, false);
        return new PersonParentViewHolder(view);
    }

    @Override
    public PersonChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.person_child_layout, viewGroup, false);
        return new PersonChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(PersonParentViewHolder personParentViewHolder, int i, ParentListItem parentObject) {
        PersonParentObject pObject = (PersonParentObject) parentObject;
        personParentViewHolder.mPersonTitleTextView.setText(pObject.getTitle());
    }

    @Override
    public void onBindChildViewHolder(PersonChildViewHolder personChildViewHolder, int i, Object childObject) {
        final PersonChildObject cObject = (PersonChildObject) childObject;
        personChildViewHolder.mTitleTextView.setText(cObject.getTitle());
        personChildViewHolder.mDescriptionTextView.setText(cObject.getDescription());
        personChildViewHolder.mChildImage.setImageDrawable(new
                IconDrawable(activity, cObject.getImage())
                .sizeDp(40)
                .colorRes(cObject.color));
       personChildViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onChildClick(v, cObject);
           }
       });
    }

    private void onChildClick(View v, PersonChildObject cObject){
        AllModel.setCurrentEventPerson(cObject.getPersonPoint());
        if(cObject.getPersonEvent() != null){
            AllModel.setCurrentEvent(cObject.getPersonEvent());
            Intent intent = new Intent(activity, MyMapActivity.class);
            activity.startActivity(intent);
        }
        else {
            Intent intent = new Intent(activity, PersonActivity.class);
            activity.startActivity(intent);
        }

    }
}
