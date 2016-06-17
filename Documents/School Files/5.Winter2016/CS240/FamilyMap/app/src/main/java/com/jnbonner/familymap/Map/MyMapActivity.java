package com.jnbonner.familymap.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.maps.MapFragment;
import com.jnbonner.familymap.MapInfoFragment;
import com.jnbonner.familymap.Person.PersonFragment;
import com.jnbonner.familymap.R;

/**
 * Created by James on 3/30/2016.
 */
public class MyMapActivity extends AppCompatActivity{

    private MapInfoFragment myMapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.map_activity_frame);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.map_activity_frame, fragment)
                    .commit();
        }
        fragment.setHasOptionsMenu(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected Fragment createFragment() {
        return new MapInfoFragment();
    }
}
