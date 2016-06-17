package com.jnbonner.familymap.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

import com.jnbonner.familymap.MainActivity;
import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.R;

/**
 * Created by James on 3/31/2016.
 */
public class SettingsFragment extends Fragment{

    LinearLayout reSync;
    LinearLayout logout;
    Spinner mapSpinner;
    Spinner spouseSpinner;
    Spinner familyTreeSpinner;
    Spinner lifeStorySpinner;
    Switch spouseSwitch;
    Switch familyTreeSwitch;
    Switch lifeStorySwitch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.settings_fragment_layout, container, false);

        updateUI(view);

        return view;
    }

    private void updateUI(View view) {
        reSync = (LinearLayout)view.findViewById(R.id.settings_reSync_linear_layout);
        logout = (LinearLayout)view.findViewById(R.id.settings_logout_linear_layout);

        // set on click listener to Logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AllModel();
                AllModel.logout();
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });

        // set on click listener to reSync data
        reSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllModel.reSync();
                AllModel.getServerAccess().syncData(getActivity());
            }
        });

        setSpinners(view);
        setSwitches(view);
    }

    /** sets all switches and their states upon loading fragment*/
    private void setSwitches(View view){
        spouseSwitch = (Switch)view.findViewById(R.id.settings_spouseLines_switch);
        spouseSwitch.setChecked(AllModel.isSpouseSwitch());
        spouseSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllModel.setSpouseSwitch(!AllModel.isSpouseSwitch());
            }
        });

        familyTreeSwitch = (Switch)view.findViewById(R.id.settings_familyTreeLines_switch);
        familyTreeSwitch.setChecked(AllModel.isFamilySwitch());
        familyTreeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllModel.setFamilySwitch(!AllModel.isFamilySwitch());
            }
        });

        lifeStorySwitch = (Switch)view.findViewById(R.id.settings_lifeStoryLines_switch);
        lifeStorySwitch.setChecked(AllModel.isLifeSwitch());
        lifeStorySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllModel.setLifeSwitch(!AllModel.isLifeSwitch());
            }
        });
    }

    /** Sets all spinners and their adapters*/
    private void setSpinners(View view){
        mapSpinner = (Spinner)view.findViewById(R.id.settings_mapType_spinner);
        ArrayAdapter<CharSequence> mapTypeAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.map_types_array,
                        android.R.layout.simple_spinner_item);
        mapTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mapSpinner.setAdapter(mapTypeAdapter);
        mapSpinner.setSelection(AllModel.getSettingMapSpinner());

        mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != AllModel.getSettingMapSpinner()) {
                    String type = (String) parent.getItemAtPosition(position);
                    AllModel.setSettingMapSpinner(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spouseSpinner = (Spinner)view.findViewById(R.id.settings_spouseLines_spinner);
        ArrayAdapter<CharSequence> spouseSpinnerAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.line_color_options,
                        android.R.layout.simple_spinner_item);
        spouseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spouseSpinner.setAdapter(spouseSpinnerAdapter);
        spouseSpinner.setSelection(AllModel.getSettingSpouseSpinner());
        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AllModel.setSettingSpouseSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        familyTreeSpinner = (Spinner)view.findViewById(R.id.settings_familyTreeLines_spinner);
        ArrayAdapter<CharSequence> familyTreeSpinnerAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.line_color_options,
                        android.R.layout.simple_spinner_item);
        familyTreeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        familyTreeSpinner.setAdapter(familyTreeSpinnerAdapter);
        familyTreeSpinner.setSelection(AllModel.getSettingFamilyTreeSpinner());
        familyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AllModel.setSettingFamilyTreeSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lifeStorySpinner = (Spinner)view.findViewById(R.id.settings_lifeStoryLines_spinner);
        ArrayAdapter<CharSequence> lifeStorySpinnerAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.line_color_options,
                        android.R.layout.simple_spinner_item);
        lifeStorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lifeStorySpinner.setAdapter(lifeStorySpinnerAdapter);
        lifeStorySpinner.setSelection(AllModel.getSettingLifeEventSpinner());
        lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AllModel.setSettingLifeEventSpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
