package com.jnbonner.challengechess.Model;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;

import com.jnbonner.challengechess.GameActivity;
import com.jnbonner.challengechess.Model.GameBoard.Color;
import com.jnbonner.challengechess.Model.GameBoard.GameBoard;
import com.jnbonner.challengechess.Model.GameBoard.GameBoardAdapter;
import com.jnbonner.challengechess.R;

/**
 * Created by James on 6/12/2016.
 */
public class GameBoardFragment extends Fragment{

    GridView chessBoardGrid;
    GameBoardAdapter adapter;
    GameBoard chessBoard;
    Button undoButton;

    private Chronometer chronometerOne;
    private Chronometer chronometerTwo;
    private boolean isSingle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        isSingle = bundle.getBoolean("isSingle");
        chessBoard = GameBoard.getInstance(getActivity().getBaseContext(), isSingle);
        chessBoard.setGameActivity((GameActivity) getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_board, container, false);

        undoButton = (Button)view.findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameBoard.getInstance().undoTwoMoves();
                GameBoard.getInstance().getGameActivity().getGameBoardFragment().refreshAdapter();
            }
        });

        chessBoardGrid = (GridView)view.findViewById(R.id.chessboard);
        GameBoard.getInstance().setGameGrid(chessBoardGrid);
        adapter = new GameBoardAdapter(getContext(), chessBoard.getGameBoard());
        GameBoard.getInstance().getTurnManager().setAdapter(adapter);
        chessBoardGrid.setAdapter(adapter);
        initChronos(view);
        return view;
    }

    public void initChronos(View view){
        chronometerOne = (Chronometer)view.findViewById(R.id.playerOneChrono);
        chronometerOne.setBase(SystemClock.elapsedRealtime());
        chronometerOne.start();
        chronometerTwo = (Chronometer)view.findViewById(R.id.playerTwoChrono);

        GameBoard.getInstance().setChronometerOne(chronometerOne);
        GameBoard.getInstance().setChronometerTwo(chronometerTwo);

        GameBoard.getInstance().getTurnManager().getPlayerMap()
                .get(Color.White).setChronometer(chronometerOne);
        GameBoard.getInstance().getTurnManager().getPlayerMap()
                .get(Color.Black).setChronometer(chronometerTwo);
    }

    public void refreshAdapter(){
        adapter.notifyDataSetChanged();
        chessBoardGrid.setAdapter(adapter);
    }

    public GameBoard getChessBoard() {
        return chessBoard;
    }

    public Chronometer getChronometerOne() {
        return chronometerOne;
    }

    public void setChronometerOne(Chronometer chronometerOne) {
        this.chronometerOne = chronometerOne;
    }

    public Chronometer getChronometerTwo() {
        return chronometerTwo;
    }

    public void setChronometerTwo(Chronometer chronometerTwo) {
        this.chronometerTwo = chronometerTwo;
    }
}
