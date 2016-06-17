package com.jnbonner.familymap.Person;

import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.jnbonner.familymap.R;

/**
 * Created by James on 3/28/2016.
 */
public class PersonParentViewHolder extends ParentViewHolder {
    public TextView mPersonTitleTextView;
    public ImageButton mParentDropDownArrow;

    public PersonParentViewHolder(View itemView) {
        super(itemView);

        mPersonTitleTextView = (TextView) itemView.
                findViewById(R.id.parent_list_item_person_title_text_view);
        mParentDropDownArrow = (ImageButton) itemView.
                findViewById(R.id.parent_list_item_expand_arrow);
        mParentDropDownArrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (isExpanded()){
                    collapseView();
                }
                else {
                    expandView();
                }
            }
        });
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (mParentDropDownArrow.getRotation() == 180.0F) {
            mParentDropDownArrow.setRotation(0F);
        }
        else if(mParentDropDownArrow.getRotation() == 0F){
            mParentDropDownArrow.setRotation(180.0F);
        }
    }
}
