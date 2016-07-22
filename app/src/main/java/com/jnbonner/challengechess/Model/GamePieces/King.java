package com.jnbonner.challengechess.Model.GamePieces;

import com.jnbonner.challengechess.Model.GameBoard.BoardTile;
import com.jnbonner.challengechess.Model.GameBoard.Color;
import com.jnbonner.challengechess.Model.GameBoard.GameBoard;
import com.jnbonner.challengechess.Model.GameBoard.PieceLocation;
import com.jnbonner.challengechess.Model.GameBoard.TurnManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by James on 6/5/2016.
 */
public class King extends GamePiece{

    double value = 10000.0d;

    HashSet<PieceLocation> allEnemyMoves;
    boolean canCastle;
    boolean isInCheck;
    GameBoard gameBoard;

    public King(PieceLocation location, Color color){
        super(location, color);
        image = color.getKing();
        canCastle = true;
        isInCheck = false;
        gameBoard = GameBoard.getInstance();
    }

    @Override
    public ArrayList<PieceLocation> calculatePossibleMoves() {
        this.gameBoard = GameBoard.getInstance();
        return calcPossibleMoves(gameBoard);
    }

//    @Override
//    public ArrayList<PieceLocation> calculatePossibleMoves(GameBoard gameBoard){
//        this.gameBoard = gameBoard;
//        return calcPossibleMoves(gameBoard);
//    }
    
    public ArrayList<PieceLocation>calcPossibleMoves(GameBoard gameBoard){
        possibleMoves.clear();
        TurnManager turnManager = gameBoard.getTurnManager();
        if (turnManager.getPlayerMap().get(color).isMyTurn()) {
            turnManager.setIsCalculatingCheck(true);
            allEnemyMoves = gameBoard.getTurnManager().getPlayerMap()
                    .get(color.getOpposite()).getAllPossibleMoves();
        }
        int x = location.getX();
        int y = location.getY();

        runLoopIter(x, y, 1, 1);
        runLoopIter(x, y, 1, -1);
        runLoopIter(x, y, -1, 1);
        runLoopIter(x, y, -1, -1);
        runLoopIter(x, y, 1, 0);
        runLoopIter(x, y, 0, 1);
        runLoopIter(x, y, -1, 0);
        runLoopIter(x, y, 0, -1);
        if (turnManager.getPlayerMap().get(color).isMyTurn()) {
            turnManager.setIsCalculatingCheck(false);
        }
        if (canCastle && !isInCheck){
            checkCanCastle(x, y, 0, 1);
            checkCanCastle(x, y, 0, -1);
        }
        return possibleMoves;
    }

    private void runLoopIter(int x, int y, int counterX, int counterY){
        if (checkCondForMove(x + counterX, y + counterY)){
            possibleMoves.add(new PieceLocation(x + counterX, y + counterY));
        }
        if ((x + counterX <= 7) && (x + counterX >= 0) &&
                (y + counterY <= 7) && (y + counterY >= 0) &&
                !moveIsInCheck(x + counterX, y + counterY)) {
            if (gameBoard.getGameBoard()[x + counterX][y + counterY]
                    .getGamePiece() != null) {
                if (gameBoard.getGameBoard()[x + counterX][y + counterY]
                        .getGamePiece().color.getOpposite() == color) {
                    possibleMoves.add(new PieceLocation(x + counterX, y + counterY));
                }
            }
        }
    }

    private void checkCanCastle(int x, int y, int counterX, int counterY){
        int xInc = counterX;
        int yInc = counterY;
        while (checkCondForMove(x + counterX, y + counterY)){
            counterX += xInc;
            counterY += yInc;
        }
        if ((x + counterX <= 7) && (x + counterX >= 0) &&
                (y + counterY <= 7) && (y + counterY >= 0)) {
            if (gameBoard.getGameBoard()[x + counterX][y + counterY]
                    .getGamePiece() != null) {
                if (gameBoard.getGameBoard()[x + counterX][y + counterY]
                        .getGamePiece().getClass() == Rook.class) {
                    Rook rook = (Rook) gameBoard.getGameBoard()[x + counterX][y + counterY]
                            .getGamePiece();
                    if (rook.isCanCastle()) {
                        addCastleMoves(x, y, rook);
                    }
                }
            }
        }
    }

    private void addCastleMoves(int x, int y, Rook rook){
        int diff = y - rook.getLocation().getY();
        if (diff < 0){
            PieceLocation loc = new PieceLocation(x, y + 2);
            loc.setIsCastle(rook);
            possibleMoves.add(loc);
        }
        else if (diff > 0){
            PieceLocation loc = new PieceLocation(x, y - 2);
            loc.setIsCastle(rook);
            possibleMoves.add(loc);
        }
    }

    private boolean checkCondForMove(int x, int y){
        BoardTile[][] board = gameBoard.getGameBoard();
        if (x > 7 || x < 0 || y > 7 || y < 0){
            return false;
        }
        if (board[x][y].getGamePiece() != null){
            return false;
        }
        if (moveIsInCheck(x, y)){
            return false;
        }
        return true;
    }

    /**
     * Checks whether a move will put the king in to check
     * @return
     */
    public boolean moveIsInCheck(int x, int y){
        TurnManager turnManager = gameBoard.getTurnManager();
        if (turnManager == null){
            turnManager = GameBoard.getInstance().getTurnManager();
        }
        if (turnManager.getPlayerMap().get(color).isMyTurn()) {
            for (PieceLocation temp: allEnemyMoves){
                if (temp.equals(new PieceLocation(x, y))){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public GamePiece movePiece(PieceLocation newLocation) {
        GamePiece gamePiece = super.movePiece(newLocation);
        canCastle = false;
        return gamePiece;
    }

    public void setAllEnemyMoves(HashSet<PieceLocation> allEnemyMoves) {
        this.allEnemyMoves = allEnemyMoves;
    }

    public boolean isInCheck() {
        return isInCheck;
    }

    public void setIsInCheck(boolean isInCheck) {
        this.isInCheck = isInCheck;
    }

    public boolean isCanCastle() {
        return canCastle;
    }

    public void setCanCastle(boolean canCastle) {
        this.canCastle = canCastle;
    }
}
