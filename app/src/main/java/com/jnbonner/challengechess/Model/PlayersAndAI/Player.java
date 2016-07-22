package com.jnbonner.challengechess.Model.PlayersAndAI;

import android.widget.Chronometer;

import com.jnbonner.challengechess.Model.GameBoard.Color;
import com.jnbonner.challengechess.Model.GameBoard.PieceLocation;
import com.jnbonner.challengechess.Model.GamePieces.GamePiece;
import com.jnbonner.challengechess.Model.GamePieces.King;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by James on 6/10/2016.
 */
public class Player implements Serializable {

    private Color color;
    private List<GamePiece> allPieces;
    private boolean isMyTurn;
    private King king;
    long timeWhenStopped = 0;
    private Chronometer chronometer;
    private int dirScale; // used to calculate forward vs backward for AI
    private int  baseValue;

    public Player(Color color, boolean isMyTurn){
        this.color = color;
        allPieces = new ArrayList<>();
        this.isMyTurn = isMyTurn;
        dirScale = color.getDirScale();
        baseValue = color.getBaseValue();
    }

    public HashSet<PieceLocation> getAllPossibleMoves(){
        HashSet<PieceLocation> allLocations = new HashSet<>();
        for (GamePiece temp : allPieces){
            allLocations.addAll(temp.calculatePossibleMoves());
        }
        return allLocations;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isMyTurn() {
        return isMyTurn;
    }

    public void setIsMyTurn(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
    }

    public List<GamePiece> getAllPieces() {
        return allPieces;
    }

    public void setAllPieces(List<GamePiece> allPieces) {
        this.allPieces = allPieces;
    }

    public King getKing() {
        return king;
    }

    public void setKing(King king) {
        this.king = king;
    }

    public Chronometer getChronometer() {
        return chronometer;
    }

    public void setChronometer(Chronometer chronometer) {
        this.chronometer = chronometer;
    }

    public long getTimeWhenStopped() {
        return timeWhenStopped;
    }

    public void setTimeWhenStopped(long timeWhenStopped) {
        this.timeWhenStopped = timeWhenStopped;
    }

    public int getDirScale() {
        return dirScale;
    }

    public void setDirScale(int dirScale) {
        this.dirScale = dirScale;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }
}
