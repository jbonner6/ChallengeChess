package com.jnbonner.familymap.Settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.R;

/**
 * Created by James on 3/31/2016.
 */
public class SettingsActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity_layout);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.settingsActivityFrame);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.settingsActivityFrame, fragment)
                    .commit();
        }
        fragment.setHasOptionsMenu(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AllModel.setSettingsActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //AllModel.setCurrentEvent(null);
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void returnAfterReSync(){
        NavUtils.navigateUpFromSameTask(this);
    }


    protected SettingsFragment createFragment() {
        return new SettingsFragment();
    }
}
