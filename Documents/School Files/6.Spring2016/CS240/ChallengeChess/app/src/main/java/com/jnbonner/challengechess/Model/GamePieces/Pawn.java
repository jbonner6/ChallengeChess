package com.jnbonner.challengechess.Model.GamePieces;

import com.jnbonner.challengechess.Model.GameBoard.BoardTile;
import com.jnbonner.challengechess.Model.GameBoard.Color;
import com.jnbonner.challengechess.Model.GameBoard.GameBoard;
import com.jnbonner.challengechess.Model.GameBoard.PieceLocation;

import java.util.ArrayList;

/**
 * Created by James on 6/7/2016.
 */
public class Pawn extends GamePiece {

    double value = 1.0d;

    boolean firstMove;

    public Pawn(PieceLocation location, Color color) {
        super(location, color);
        image = color.getPawn();
        firstMove = true;
    }

    @Override
    public ArrayList<PieceLocation> calculatePossibleMoves() {
        return handlePossibleMoves(GameBoard.getInstance().getGameBoard());
    }

    private ArrayList<PieceLocation> handlePossibleMoves(BoardTile[][] newBoard){
        BoardTile[][] gameBoard = newBoard;
        possibleMoves.clear();
        int x = location.getX();
        int y = location.getY();

        boolean forCheck = GameBoard.getInstance().getTurnManager().isCalculatingCheck();

        if (color == Color.Black) {
            if (x <= 6) {
                if (gameBoard[x + 1][y].getGamePiece() == null && !forCheck) {
                    possibleMoves.add(new PieceLocation(x + 1, y));
                    if (firstMove) {
                        if (gameBoard[x + 2][y].getGamePiece() == null) {
                            possibleMoves.add(new PieceLocation(x + 2, y));
                        }
                    }
                }
                if (y <= 6) {
                    if (gameBoard[x + 1][y + 1].getGamePiece() != null || forCheck) {
                        if (!forCheck){
                            if (color == gameBoard[x + 1][y + 1].getGamePiece().getColor().getOpposite()) {
                                possibleMoves.add(new PieceLocation(x + 1, y + 1));
                            }
                        }
                        else {
                            possibleMoves.add(new PieceLocation(x + 1, y + 1));
                        }
                    }
                }
                if (y >= 1) {
                    if (gameBoard[x + 1][y - 1].getGamePiece() != null || forCheck) {
                        if (!forCheck) {
                            if (color == gameBoard[x + 1][y - 1].getGamePiece().getColor().getOpposite()) {
                                possibleMoves.add(new PieceLocation(x + 1, y - 1));
                            }
                        }
                        else {
                            possibleMoves.add(new PieceLocation(x + 1, y - 1));
                        }
                    }
                }
            }
        }
        else if (color == Color.White){
            if (x >= 1) {
                if (gameBoard[x - 1][y].getGamePiece() == null && !forCheck) {
                    possibleMoves.add(new PieceLocation(x - 1, y));
                    if (firstMove) {
                        if (gameBoard[x - 2][y].getGamePiece() == null) {
                            possibleMoves.add(new PieceLocation(x - 2, y));
                        }
                    }
                }
                if (y <= 6) {
                    if (gameBoard[x - 1][y + 1].getGamePiece() != null || forCheck) {
                        if (!forCheck) {
                            if (color == gameBoard[x - 1][y + 1].getGamePiece().getColor().getOpposite()) {
                                possibleMoves.add(new PieceLocation(x - 1, y + 1));
                            }
                        }
                        else {
                            possibleMoves.add(new PieceLocation(x - 1, y + 1));
                        }
                    }
                }
                if (y >= 1) {
                    if (gameBoard[x - 1][y - 1].getGamePiece() != null || forCheck) {
                        if (!forCheck) {
                            if (color == gameBoard[x - 1][y - 1].getGamePiece().getColor().getOpposite()) {
                                possibleMoves.add(new PieceLocation(x - 1, y - 1));
                            }
                        }
                        else {
                            possibleMoves.add(new PieceLocation(x - 1, y - 1));
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public GamePiece movePiece(PieceLocation newLocation) {
        GamePiece toReturn =  super.movePiece(newLocation);
        firstMove = false;
        return toReturn;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }
}
