package com.jnbonner.challengechess.Model.GameBoard;

import android.content.Context;
import android.widget.Chronometer;
import android.widget.GridView;

import com.jnbonner.challengechess.BuildConfig;
import com.jnbonner.challengechess.GameActivity;
import com.jnbonner.challengechess.Model.GamePieces.Bishop;
import com.jnbonner.challengechess.Model.GamePieces.GamePiece;
import com.jnbonner.challengechess.Model.GamePieces.King;
import com.jnbonner.challengechess.Model.GamePieces.Knight;
import com.jnbonner.challengechess.Model.GamePieces.Pawn;
import com.jnbonner.challengechess.Model.GamePieces.Queen;
import com.jnbonner.challengechess.Model.GamePieces.Rook;
import com.jnbonner.challengechess.Model.PlayersAndAI.Player;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * Created by James on 6/5/2016.
 */
public class GameBoard implements Serializable{

    BoardTile[][] gameBoard;
    private static GameBoard SINGLETON;
    private GridView gameGrid;
    private GameActivity gameActivity;
    private TurnManager turnManager;
    private Chronometer chronometerOne;
    private Chronometer chronometerTwo;
    private Deque<GameMove> moveStack;

    public static GameBoard getInstance(){
        if (SINGLETON == null){
            SINGLETON = new GameBoard();
        }
        return SINGLETON;
    }

    public static GameBoard getInstance(Context mContext, boolean isSingle){
        if (SINGLETON == null){
            SINGLETON = new GameBoard(mContext, isSingle);
        }
        return SINGLETON;
    }

    private GameBoard(Context mContext, boolean isSingle){
        moveStack = new ArrayDeque<>();
        turnManager = new TurnManager(Color.White, mContext, isSingle);
        createNewBoard();
    }

    public void undoTwoMoves(){
        for (int i = 0; i < 2; i++) {
            undoSingleMove();
        }
    }

    public void undoSingleMove(){
        if (moveStack.size() > 0) {
            GameMove move = moveStack.pop();
            PieceLocation curLoc = move.getToLoc();
            PieceLocation oldLoc = move.getFromLoc();

            if (move.getTaken() != null) {
                move.getTaken().setLocation(curLoc);
            }
            move.getPieceMoved().setLocation(oldLoc);

            gameBoard[curLoc.x][curLoc.y].setGamePiece(move.getTaken());
            gameBoard[oldLoc.x][oldLoc.y].setGamePiece(move.getPieceMoved());

            if (move.getTaken() != null){
                GameBoard.getInstance().getTurnManager()
                        .getPlayerMap().get(move.getTaken()
                        .getColor()).getAllPieces().add(move.getTaken());
            }
            if (move.getPieceMoved().getClass() == Pawn.class) {
                Pawn pawn = (Pawn) move.getPieceMoved();
                if (pawn.getColor() == Color.White) {
                    if (oldLoc.getX() == 6) {
                        pawn.setFirstMove(true);
                    }
                } else {
                    if (oldLoc.getX() == 1) {
                        pawn.setFirstMove(true);
                    }
                }
            }
            if (move.getPieceMoved().getClass() == King.class){
                King king = (King)move.getPieceMoved();
                king.setCanCastle(move.couldCastle);
            }
            if (move.wasCastle){
                ((Rook)moveStack.peek().getPieceMoved()).setCanCastle(true);
                undoSingleMove();
            }
        }
    }


    /**
     * Should never be called. Will crash if ever is called.
     */
    protected GameBoard(){
        assert false;
    }

    public ArrayList<PieceLocation> possibleMovesWontCauseCheck(ArrayList<PieceLocation> possibleMoves, GamePiece piece){
        ArrayList<Integer> moveIndexesToRemove = new ArrayList<>();
        King king = getKingByColor(piece.getColor());
        PieceLocation curLoc = piece.getLocation();
        int counter = 0;
        BoardTile[][] board = GameBoard.getInstance().getGameBoard();
        for (PieceLocation temp : possibleMoves){
            GamePiece toReturn = board[temp.getX()][temp.getY()].getGamePiece();
            if (toReturn != null) {
                GameBoard.getInstance().getTurnManager().getPlayerMap().get(toReturn.getColor()).
                        getAllPieces().remove(toReturn);
            }
            board[piece.getLocation().getX()][piece.getLocation().getY()].setGamePiece(null);
            board[temp.getX()][temp.getY()].setGamePiece(piece);
            piece.setLocation(temp);
            king.setAllEnemyMoves(GameBoard.getInstance().getTurnManager().getPlayerMap()
                    .get(king.getColor().getOpposite()).getAllPossibleMoves());
            if (king.moveIsInCheck(king.getLocation().getX(), king.getLocation().getY())){
                moveIndexesToRemove.add(counter);
            }
            board[piece.getLocation().getX()][piece.getLocation().getY()].setGamePiece(toReturn);
            board[curLoc.getX()][curLoc.getY()].setGamePiece(piece);
            piece.setLocation(curLoc);
            if (toReturn != null) {
                GameBoard.getInstance().getTurnManager().getPlayerMap().get(toReturn.getColor()).
                        getAllPieces().add(toReturn);
            }
            counter++;
        }
        for (int i = moveIndexesToRemove.size() -  1; i >= 0; i--){
            int index = moveIndexesToRemove.get(i);
            possibleMoves.remove(index);
        }
        return possibleMoves;
    }

    private King getKingByColor(Color color){
        Player player = GameBoard.getInstance().getTurnManager().getPlayerMap().get(color);
        for (GamePiece temp : player.getAllPieces()){
            if (temp instanceof King){
                return (King)temp;
            }
        }
        return null;
    }

    private void createNewBoard(){
        BoardTile[][] gameBoard = new BoardTile[8][8];
        Color color = Color.White;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                gameBoard[i][j] = new BoardTile(color);
                if (j != 7){color = color.getOpposite();}
            }
        }
        this.gameBoard = gameBoard;
        setStartingPieces();
    }

    private void setStartingPieces(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (i == 0){
                    if (j == 0 || j == 7){
                        setStartingPiece(i, j, Rook.class, Color.Black);
                    }
                    if (j == 1 || j == 6){
                        setStartingPiece(i, j, Knight.class, Color.Black);
                    }
                    if (j == 2 || j == 5){
                        setStartingPiece(i, j, Bishop.class, Color.Black);
                    }
                    if (j == 3){
                        setStartingPiece(i, j, Queen.class, Color.Black);
                    }
                    if (j == 4){
                        setStartingPiece(i, j, King.class, Color.Black);
                    }
                }
                else if (i == 7){
                    if (j == 0 || j == 7){
                        setStartingPiece(i, j, Rook.class, Color.White);
                    }
                    if (j == 1 || j == 6){
                        setStartingPiece(i, j, Knight.class, Color.White);
                    }
                    if (j == 2 || j == 5){
                        setStartingPiece(i, j, Bishop.class, Color.White);
                    }
                    if (j == 3){
                        setStartingPiece(i, j, Queen.class, Color.White);
                    }
                    if (j == 4){
                        setStartingPiece(i, j, King.class, Color.White);
                    }
                }
                else if (i == 1){
                    setStartingPiece(i, j, Pawn.class, Color.Black);
                }
                else if (i == 6){
                    setStartingPiece(i, j, Pawn.class, Color.White);
                }
                else {
                    gameBoard[i][j].setGamePiece(null);

                }
            }
        }
    }

    private void setStartingPiece(int i, int j, Class<?> pieceType, Color color){
        GamePiece gamePiece = null;
        try {
            gamePiece = (GamePiece)pieceType.getConstructor(PieceLocation.class, Color.class)
                    .newInstance(new PieceLocation(i, j), color);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        gameBoard[i][j].setGamePiece(gamePiece);
        turnManager.playerMap.get(color).getAllPieces().add(gamePiece);
        if (gamePiece instanceof King){
            turnManager.playerMap.get(color).setKing((King)gamePiece);
        }
    }

    public BoardTile[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(BoardTile[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public GridView getGameGrid() {
        return gameGrid;
    }

    public void setGameGrid(GridView gameGrid) {
        this.gameGrid = gameGrid;
    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public void setTurnManager(TurnManager turnManager) {
        this.turnManager = turnManager;
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

    public Deque<GameMove> getMoveStack() {
        return moveStack;
    }

    public void setMoveStack(Deque<GameMove> moveStack) {
        this.moveStack = moveStack;
    }
}
