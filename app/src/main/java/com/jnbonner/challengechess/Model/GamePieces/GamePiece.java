package com.jnbonner.challengechess.Model.GamePieces;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.jnbonner.challengechess.Model.GameBoard.BoardTile;
import com.jnbonner.challengechess.Model.GameBoard.Color;
import com.jnbonner.challengechess.Model.GameBoard.GameBoard;
import com.jnbonner.challengechess.Model.GameBoard.GameMove;
import com.jnbonner.challengechess.Model.GameBoard.PieceLocation;
import com.jnbonner.challengechess.Model.GameBoard.TurnManager;

import java.util.ArrayList;

/**
 * Created by James on 6/5/2016.
 */
public class GamePiece {

    PieceLocation location;
    int image;
    Color color;
    ArrayList<PieceLocation> possibleMoves;
    double pointValue;

    public GamePiece(PieceLocation location, Color color){
        this.location = location;
        this.color = color;
        possibleMoves = new ArrayList<>();
        //GameBoard.getInstance().getTurnManager().getPlayerMap().get(color).getAllPieces().add(this);
    }

    public ArrayList<PieceLocation> calculatePossibleMoves(){
        return null;
    }

    /**
     * Moves a piece to a new location. If a piece is taken in this move,
     * the taken piece is returned, otherwise null is returned
     * @param newLocation   The location being moved to
     * @return              The piece that was taken, or null
     */
    public GamePiece movePiece(PieceLocation newLocation){
        GamePiece piece = movePieceNoRefresh(newLocation);
        GameBoard.getInstance().getGameActivity().getGameBoardFragment().refreshAdapter();
        return piece;
    }

    public GamePiece movePieceNoRefresh(PieceLocation newLocation){
        BoardTile[][] board = GameBoard.getInstance().getGameBoard();
        GamePiece pieceMoved;
        PieceLocation fromLoc;
        PieceLocation toLoc;
        GamePiece pieceTaken;
        Boolean wasCastle = false;

        GamePiece toReturn = board[newLocation.getX()][newLocation.getY()].getGamePiece();

        //****************// GameMove

        pieceTaken = toReturn;
        pieceMoved = board[location.getX()][location.getY()].getGamePiece();
        fromLoc = location;
        toLoc = newLocation;
        boolean couldCastle = false;
        if (pieceMoved instanceof King){
            couldCastle = ((King)pieceMoved).canCastle;
        }

        //****************// GameMove

        board[location.getX()][location.getY()].setGamePiece(null);
        board[newLocation.getX()][newLocation.getY()].setGamePiece(this);
        setLocation(newLocation);
        if (newLocation.getIsCastle() != null){
            moveRookInCastle(newLocation.getIsCastle());
            wasCastle = true;
        }



        if (toReturn != null) {
            GameBoard.getInstance().getTurnManager().getPlayerMap()
                    .get(toReturn.getColor()).getAllPieces().remove(toReturn);
        }
        if (newLocation.getIsCastle() == null){
            GameBoard.getInstance().getTurnManager().switchTurns();

        }
        GameBoard.getInstance().getMoveStack()
                .push(new GameMove(pieceMoved, fromLoc, toLoc, pieceTaken, couldCastle, wasCastle));
        return toReturn;
    }

    private void moveRookInCastle(Rook rook){
        int diff = this.getLocation().getY() - rook.getLocation().getY();
        if(diff < 0){
            rook.movePiece(new PieceLocation(this.getLocation().getX(), this.getLocation().getY() - 1));
        }
        else if (diff > 0){
            rook.movePiece(new PieceLocation(this.getLocation().getX(), this.getLocation().getY() + 1));

        }
        //GameBoard.getInstance().getTurnManager().switchTurns();
    }

    public PieceLocation getLocation() {
        return location;
    }

    public void setLocation(PieceLocation location) {
        this.location = location;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public ArrayList<PieceLocation> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(ArrayList<PieceLocation> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public double getPointValue() {
        return pointValue;
    }

    public void setPointValue(double pointValue) {
        this.pointValue = pointValue;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
