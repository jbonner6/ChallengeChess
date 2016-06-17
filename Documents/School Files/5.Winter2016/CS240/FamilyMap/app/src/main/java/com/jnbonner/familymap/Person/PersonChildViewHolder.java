package com.jnbonner.familymap.Person;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.jnbonner.familymap.R;
import com.joanzapata.android.iconify.IconDrawable;
import com.joanzapata.android.iconify.Iconify;


/**
 * Created by James on 3/28/2016.
 */
public class PersonChildViewHolder extends ChildViewHolder {
    public TextView mTitleTextView;
    public TextView mDescriptionTextView;
    public ImageView mChildImage;


    public PersonChildViewHolder(View itemView) {
        super(itemView);

        mTitleTextView = (TextView) itemView.findViewById(R.id.person_item_title_text_view);
        mDescriptionTextView = (TextView) itemView.findViewById(R.id.person_item_description_text_view);
        mChildImage = (ImageView) itemView.findViewById(R.id.person_child_image);
    }
}
