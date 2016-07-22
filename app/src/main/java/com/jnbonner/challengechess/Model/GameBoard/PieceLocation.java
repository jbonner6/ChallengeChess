package com.jnbonner.challengechess.Model.GameBoard;

import com.jnbonner.challengechess.Model.GamePieces.Rook;

/**
 * Created by James on 6/5/2016.
 */
public class PieceLocation {

    int x;
    int y;
    Rook isCastle;

    public PieceLocation(int x, int y){
        this.x = x;
        this.y = y;
        isCastle = null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        }
        if (o == this){
            return true;
        }
        if (!(o instanceof PieceLocation)){
            return false;
        }
        PieceLocation other = (PieceLocation)o;
        if (other.getX() != this.getX()){
            return false;
        }
        if (other.getY() != this.getY()){
            return false;
        }
        return true;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rook getIsCastle() {
        return isCastle;
    }

    public void setIsCastle(Rook isCastle) {
        this.isCastle = isCastle;
    }
}
