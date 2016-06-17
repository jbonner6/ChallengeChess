package com.jnbonner.familymap.Person;

import android.widget.ExpandableListView;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.jnbonner.familymap.Model.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 3/28/2016.
 */
public class PersonParentObject implements ParentListItem {
    private List<PersonChildObject> mChildrenList;
    String title;

    public PersonParentObject(String title)
    {
        this.title = title;
        mChildrenList = new ArrayList<>();

    }


//    @Override
//    public void setChildObjectList(List<Object> list){
//        mChildrenList = list;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public List<PersonChildObject> getChildItemList() {
        return mChildrenList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
