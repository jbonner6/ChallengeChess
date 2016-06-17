package com.jnbonner.familymap.Person;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jnbonner.familymap.Filter.FilterFragment;
import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.R;
import com.joanzapata.android.iconify.Iconify;

/**
 * Created by James on 3/28/2016.
 */
public class PersonActivity extends AppCompatActivity{



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_activity_layout);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.person_activity_frame);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.person_activity_frame, fragment)
                    .commit();
        }
        fragment.setHasOptionsMenu(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AllModel.setCurrentEvent(null);
        if (item.getItemId() == android.R.id.home){
           onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    protected Fragment createFragment() {
        return new PersonFragment();
    }

}
