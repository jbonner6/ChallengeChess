package com.jnbonner.challengechess.Model.GameBoard;

import android.content.Context;

import com.jnbonner.challengechess.Model.GamePieces.GamePiece;
import com.jnbonner.challengechess.Model.PlayersAndAI.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by James on 6/26/2016.
 */
public class AIGameBoard extends GameBoard{

    BoardTile[][] gameBoard;
    private TurnManager turnManager;
    Context mContext;

    public AIGameBoard(Context context){
        mContext = context;
        gameBoard = makeCopyOfGameBoard(GameBoard.getInstance().getGameBoard());
        turnManager = new TurnManager(GameBoard.getInstance().getTurnManager().getCurrentTurn(), mContext, true);
        setPlayersForTurnManager(turnManager);
    }

    private BoardTile[][] makeCopyOfGameBoard(BoardTile[][] gameBoard){
        BoardTile[][] toReturn = new BoardTile[8][8];
        for (int i = 0; i < 7; i++){
            for (int j = 0; j < 7; j++){
                toReturn[i][j] = new BoardTile(gameBoard[i][j].color);
                if (gameBoard[i][j].getGamePiece() != null){
                    Class<?> classType =  gameBoard[i][j].getGamePiece().getClass();
                    try {
                        GamePiece gamePiece = (GamePiece)classType.getConstructor(PieceLocation.class, Color.class)
                                .newInstance(new PieceLocation(i, j), gameBoard[i][j].getGamePiece().getColor());
                        toReturn[i][j].setGamePiece(gamePiece);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    toReturn[i][j].setGamePiece(null);
                }
            }
        }
        return toReturn;
    }

    private void setPlayersForTurnManager(TurnManager turnManager){
        //turnManager.getPlayerMap().put()
    }

    private Player makeCopyOfPlayer(){
        return null;
    }
}
