package com.jnbonner.familymap.Search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.R;

/**
 * Created by James on 4/4/2016.
 */
public class SearchActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_layout);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.search_activity_frame);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.search_activity_frame, fragment)
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

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    protected Fragment createFragment() {
        return new SearchFragment();
    }
}
