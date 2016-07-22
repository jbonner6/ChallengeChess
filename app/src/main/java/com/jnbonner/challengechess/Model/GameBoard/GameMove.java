package com.jnbonner.challengechess.Model.GameBoard;

import com.jnbonner.challengechess.Model.GamePieces.GamePiece;

/**
 * Created by James on 6/26/2016.
 */
public class GameMove {

    GamePiece pieceMoved;
    PieceLocation fromLoc;
    PieceLocation toLoc;
    GamePiece taken;
    Boolean couldCastle;
    Boolean wasCastle;

    public GameMove(GamePiece pieceMoved, PieceLocation fromLoc, PieceLocation toLoc,
                    GamePiece taken, Boolean couldCastle, Boolean wasCastle){
        this.pieceMoved = pieceMoved;
        this.fromLoc = fromLoc;
        this.toLoc = toLoc;
        this.taken = taken;
        this.couldCastle = couldCastle;
        this.wasCastle = wasCastle;
    }

    public GamePiece getPieceMoved() {
        return pieceMoved;
    }

    public void setPieceMoved(GamePiece pieceMoved) {
        this.pieceMoved = pieceMoved;
    }

    public PieceLocation getFromLoc() {
        return fromLoc;
    }

    public void setFromLoc(PieceLocation fromLoc) {
        this.fromLoc = fromLoc;
    }

    public PieceLocation getToLoc() {
        return toLoc;
    }

    public void setToLoc(PieceLocation toLoc) {
        this.toLoc = toLoc;
    }

    public GamePiece getTaken() {
        return taken;
    }

    public void setTaken(GamePiece taken) {
        this.taken = taken;
    }

    public Boolean getCouldCastle() {
        return couldCastle;
    }

    public void setCouldCastle(Boolean couldCastle) {
        this.couldCastle = couldCastle;
    }

    public Boolean getWasCastle() {
        return wasCastle;
    }

    public void setWasCastle(Boolean wasCastle) {
        this.wasCastle = wasCastle;
    }
}
