package com.jnbonner.challengechess.Model.GamePieces;

import com.jnbonner.challengechess.Model.GameBoard.BoardTile;
import com.jnbonner.challengechess.Model.GameBoard.Color;
import com.jnbonner.challengechess.Model.GameBoard.GameBoard;
import com.jnbonner.challengechess.Model.GameBoard.PieceLocation;

import java.util.ArrayList;

/**
 * Created by James on 6/5/2016.
 */
public class Queen extends GamePiece{

    public Queen(PieceLocation location, Color color) {
        super(location, color);
        image = color.getQueen();
        pointValue = 9.0d;
    }

    @Override
    public ArrayList<PieceLocation> calculatePossibleMoves() {
        possibleMoves.clear();
        int x = location.getX();
        int y = location.getY();
        BoardTile[][] newBoard = GameBoard.getInstance().getGameBoard();
        runLoopIter(x, y, 1, 1, newBoard);
        runLoopIter(x, y, 1, -1, newBoard);
        runLoopIter(x, y, -1, 1, newBoard);
        runLoopIter(x, y, -1, -1, newBoard);
        runLoopIter(x, y, 1, 0, newBoard);
        runLoopIter(x, y, 0, 1, newBoard);
        runLoopIter(x, y, -1, 0, newBoard);
        runLoopIter(x, y, 0, -1, newBoard);

        return possibleMoves;
    }

    private void runLoopIter(int x, int y, int counterX, int counterY, BoardTile[][] newBoard){
        int xInc = counterX;
        int yInc = counterY;
        while (checkCondForLoop(x + counterX, y + counterY, newBoard)){
            possibleMoves.add(new PieceLocation(x + counterX, y + counterY));
            counterX += xInc;
            counterY += yInc;
        }
        if ((x + counterX <= 7) && (x + counterX >= 0) &&
                (y + counterY <= 7) && (y + counterY >= 0)) {
            if (newBoard[x + counterX][y + counterY]
                    .getGamePiece().color.getOpposite() == color) {
                possibleMoves.add(new PieceLocation(x + counterX, y + counterY));
            }
        }
    }

    private boolean checkCondForLoop(int x, int y, BoardTile[][] newBoard){
        if (x > 7 || x < 0 || y > 7 || y < 0){
            return false;
        }
        if (newBoard[x][y].getGamePiece() != null){
            return false;
        }
        return true;
    }

}
