package com.jnbonner.challengechess.Model.PlayersAndAI;

import com.jnbonner.challengechess.Model.GameBoard.AIGameBoard;
import com.jnbonner.challengechess.Model.GameBoard.BoardTile;
import com.jnbonner.challengechess.Model.GameBoard.Color;
import com.jnbonner.challengechess.Model.GameBoard.GameBoard;
import com.jnbonner.challengechess.Model.GameBoard.PieceLocation;
import com.jnbonner.challengechess.Model.GamePieces.Bishop;
import com.jnbonner.challengechess.Model.GamePieces.GamePiece;
import com.jnbonner.challengechess.Model.GamePieces.King;
import com.jnbonner.challengechess.Model.GamePieces.Knight;
import com.jnbonner.challengechess.Model.GamePieces.Pawn;
import com.jnbonner.challengechess.Model.GamePieces.Queen;
import com.jnbonner.challengechess.Model.GamePieces.Rook;
import com.rits.cloning.Cloner;

import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



/**
 * Created by James on 6/20/2016.
 */
public class AIPlayer extends Player {

    ArrayList<PieceLocation> centerTiles;

    public AIPlayer(Color color, boolean isMyTurn) {
        super(color, isMyTurn);
        centerTiles = new ArrayList<>();
        centerTiles.add(new PieceLocation(3,3));
        centerTiles.add(new PieceLocation(3,4));
        centerTiles.add(new PieceLocation(4,3));
        centerTiles.add(new PieceLocation(4,4));
    }

    /**
     * Called by the TurnManager to signify when an AI's turn begins
     * Ends whenever it moves a piece
     */
    public void startMove(){
        BoardTile[][] newBoard = GameBoard.getInstance().getGameBoard();
        HashMap<GamePiece, ArrayList<PieceLocation>> moveMap =
                getAllMovesForCurrentModel(newBoard, getColor());



        if (moveMap.size() > 0) {
            Random rand = new Random();
            GamePiece toMove = (new ArrayList<>(moveMap.keySet()))
                    .get(rand.nextInt(moveMap.keySet().size()));
            PieceLocation locToMove = moveMap.get(toMove)
                    .get((new Random()).nextInt(moveMap.get(toMove).size()));
            toMove.movePiece(locToMove);
        }
    }



    private  HashMap<GamePiece, ArrayList<PieceLocation>>
        getAllMovesForCurrentModel(BoardTile[][] newBoard, Color nColor){
        HashMap<GamePiece, ArrayList<PieceLocation>> moveMap = new HashMap<>();
        for (int i = 0; i <= 7; i++){
            for (int j = 0; j <= 7; j++){
                BoardTile tempTile = newBoard[i][j];
                if (tempTile.getGamePiece() != null){
                    if (tempTile.getGamePiece().getColor() == nColor){
                        ArrayList<PieceLocation> allMoves;
                        allMoves = GameBoard.getInstance().possibleMovesWontCauseCheck(
                                tempTile.getGamePiece().calculatePossibleMoves(), tempTile.getGamePiece());
                        if (allMoves.size() > 0) {
                            moveMap.put(tempTile.getGamePiece(), allMoves);
                        }
                    }
                }
            }
        }
        return moveMap;
    }


    private void makeBestMove(){
        BoardTile[][] newBoard = GameBoard.getInstance().getGameBoard();
        HashMap<GamePiece, ArrayList<PieceLocation>> moveMap =
                getAllMovesForCurrentModel(newBoard, getColor());

        if (moveMap.size() > 0) {
            ArrayList<GamePiece> allPieces = new ArrayList<>(moveMap.keySet());
            for (GamePiece curPiece : allPieces) {

            }
        }
    }

    private class EvalHolder{
        int i;
        int j;
        int index;
        public EvalHolder(int i, int j, int index){
            this.i = i;
            this.j = j;
            this.index = index;
        }
    }

    private void evaluateGameBoard(BoardTile[][] toEval){
        double blackTotal = 0;
        double whiteTotal = 0;
        EvalHolder bestMove = new EvalHolder(0,0,0);
        for (int i = 0; i <= 7; i++){
            for(int j = 0; j <= 7; j++){
                if (toEval[i][j].getGamePiece() != null){
                    if (toEval[i][j].getGamePiece() instanceof Pawn){
                        evalPawn((Pawn)toEval[i][j].getGamePiece());
                    }
                    if (toEval[i][j].getGamePiece() instanceof Knight){
                        evalKnight((Knight)toEval[i][j].getGamePiece());
                    }
                    if (toEval[i][j].getGamePiece() instanceof Bishop){
                        evalBishop((Bishop)toEval[i][j].getGamePiece());
                    }
                    if (toEval[i][j].getGamePiece() instanceof Rook){
                        evalRook((Rook)toEval[i][j].getGamePiece());
                    }
                    if (toEval[i][j].getGamePiece() instanceof Queen){
                        evalQueen((Queen)toEval[i][j].getGamePiece());
                    }
                    if (toEval[i][j].getGamePiece() instanceof King){
                        evalKing((King)toEval[i][j].getGamePiece());
                    }
                }
            }
        }
    }

    private double evalPawn(Pawn pawn){
        ArrayList<PieceLocation> possibleMoves = pawn.calculatePossibleMoves();

        return 0;
    }

    private double evalKnight(Knight knight){
        return 0;
    }

    private double evalBishop(Bishop bishop){
        return 0;
    }

    private double evalRook(Rook rook){
        return 0;
    }

    private double evalQueen(Queen queen){
        return 0;
    }

    private double evalKing(King king){
        return 0;
    }

}
