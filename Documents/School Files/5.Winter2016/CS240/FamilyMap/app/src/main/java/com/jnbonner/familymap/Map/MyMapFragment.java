package com.jnbonner.familymap.Map;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jnbonner.familymap.MapInfoFragment;
import com.jnbonner.familymap.Model.AllModel;
import com.jnbonner.familymap.R;

/**
 * Created by James on 3/30/2016.
 */
public class MyMapFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_maps, container, false);

        updateUI(view);

        return view;
    }

    private void updateUI(View view) {

    }
}
