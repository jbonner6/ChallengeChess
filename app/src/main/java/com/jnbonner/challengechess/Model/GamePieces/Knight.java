package com.jnbonner.challengechess.Model.GamePieces;

import com.jnbonner.challengechess.Model.GameBoard.BoardTile;
import com.jnbonner.challengechess.Model.GameBoard.Color;
import com.jnbonner.challengechess.Model.GameBoard.GameBoard;
import com.jnbonner.challengechess.Model.GameBoard.PieceLocation;

import java.util.ArrayList;

/**
 * Created by James on 6/7/2016.
 */
public class Knight extends GamePiece {


    public Knight(PieceLocation location, Color color) {
        super(location, color);
        image = color.getKnight();
        pointValue = 3.0d;
    }

    @Override
    public ArrayList<PieceLocation> calculatePossibleMoves() {
        possibleMoves.clear();
        int x = location.getX();
        int y = location.getY();
        for (PieceLocation temp : genPossibleCoordinates(x, y)){
            int curX = temp.getX();
            int curY = temp.getY();
            if (curX >= 0 && curX <= 7 && curY >= 0 && curY <= 7){
                if (GameBoard.getInstance().getGameBoard()[curX][curY].getGamePiece() == null){
                    possibleMoves.add(new PieceLocation(curX, curY));
                }
                else{
                    if (GameBoard.getInstance().getGameBoard()[curX][curY]
                                .getGamePiece().color.getOpposite() == color) {
                        possibleMoves.add(new PieceLocation(curX, curY));
                    }
                }
            }
        }
        return possibleMoves;
    }

    private ArrayList<PieceLocation> genPossibleCoordinates(int x, int y){
        ArrayList<PieceLocation> locs = new ArrayList<>();
        locs.add(new PieceLocation(x + 2, y + 1));
        locs.add(new PieceLocation(x + 2, y - 1));
        locs.add(new PieceLocation(x - 2, y + 1));
        locs.add(new PieceLocation(x - 2, y - 1));
        locs.add(new PieceLocation(x + 1, y + 2));
        locs.add(new PieceLocation(x + 1, y - 2));
        locs.add(new PieceLocation(x - 1, y + 2));
        locs.add(new PieceLocation(x - 1, y - 2));
        return locs;
    }


}
