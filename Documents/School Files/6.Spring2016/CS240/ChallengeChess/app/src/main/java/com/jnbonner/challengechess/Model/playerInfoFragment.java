package com.jnbonner.challengechess.Model;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.jnbonner.challengechess.Model.GameBoard.Color;
import com.jnbonner.challengechess.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class playerInfoFragment extends Fragment {

    TextView playerName;
    ImageView playerIcon;
    Chronometer chronometer;
    Color color;


    public playerInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_info, container, false);

        color = (Color)getArguments().get("color");

        playerName = (TextView)view.findViewById(R.id.playerName);
        playerIcon = (ImageView)view.findViewById(R.id.playerInfoImage);
        playerIcon.setImageResource(color.getPawn());
        chronometer = (Chronometer)view.findViewById(R.id.playerInfoChronometer);

        return view;
    }

}
