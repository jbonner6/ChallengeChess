package com.jnbonner.challengechess.Model.GameBoard;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;

import com.jnbonner.challengechess.Model.GamePieces.GamePiece;
import com.jnbonner.challengechess.Model.GamePieces.King;
import com.jnbonner.challengechess.Model.PlayersAndAI.AIPlayer;
import com.jnbonner.challengechess.Model.PlayersAndAI.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by James on 6/9/2016.
 */
public class TurnManager implements Serializable{

    private Color currentTurn;
    Map<Color, Player> playerMap;
    boolean isCalculatingCheck;
    Context mContext;
    GameBoardAdapter adapter;
    boolean firstTimerSwitch;

    public TurnManager(Color startingColor, Context context, boolean isSingle){
        currentTurn = startingColor;
        mContext = context;
        isCalculatingCheck = false;
        firstTimerSwitch = true;
        playerMap = new HashMap<>();
        playerMap.put(startingColor, new Player(startingColor, true));
        if (!isSingle) {
            playerMap.put(startingColor.getOpposite(), new Player(startingColor.getOpposite(), false));
        }
        else {
            playerMap.put(startingColor.getOpposite(), new AIPlayer(startingColor.getOpposite(), false));
        }
    }

//    public TurnManager(Color startingColor, Context context, Boolean isAI){
//        currentTurn = startingColor;
//        mContext = context;
//        isCalculatingCheck = false;
//        firstTimerSwitch = true;
//        playerMap = new HashMap<>();
//    }

    public void switchTurns(){
        playerMap.get(currentTurn).setTimeWhenStopped(playerMap.get(currentTurn)
                .getChronometer().getBase() - SystemClock.elapsedRealtime());
        playerMap.get(currentTurn).getChronometer().stop();
        currentTurn = currentTurn.getOpposite();
        playerMap.get(currentTurn).getChronometer().setBase(SystemClock.elapsedRealtime() +
                playerMap.get(currentTurn).getTimeWhenStopped());
        playerMap.get(currentTurn).getChronometer().start();
        for (Color temp : playerMap.keySet()){
            Player player = playerMap.get(temp);
            player.setIsMyTurn(!player.isMyTurn());
        }
        setCheckForKing(currentTurn);
        if (playerMap.get(currentTurn) instanceof AIPlayer){
            ((AIPlayer) playerMap.get(currentTurn)).startMove();
        }
    }

    public void setCheckForKing(Color color){
        Player player = playerMap.get(color);
        Player opPlayer = playerMap.get(color.getOpposite());
        player.getKing().setAllEnemyMoves(opPlayer.getAllPossibleMoves());
        PieceLocation playerKingLoc = player.getKing().getLocation();
        player.getKing().setIsInCheck(player.getKing()
                .moveIsInCheck(playerKingLoc.getX(), playerKingLoc.getY()));
        if (player.getKing().isInCheck()) {
            calcCheckMateOrDraw(player.getKing());
        }
    }

    public void calcCheckMateOrDraw(King king){
        ArrayList<PieceLocation> possibleMovesForKing = new ArrayList<>();
        for (GamePiece curPiece : GameBoard.getInstance().getTurnManager()
                .getPlayerMap().get(king.getColor()).getAllPieces()){
            possibleMovesForKing.addAll(GameBoard.getInstance().possibleMovesWontCauseCheck(
                    curPiece.calculatePossibleMoves(), curPiece));
        }
        if (possibleMovesForKing.size() == 0){
            playerMap.get(currentTurn).setTimeWhenStopped(playerMap.get(currentTurn)
                    .getChronometer().getBase() - SystemClock.elapsedRealtime());
            playerMap.get(currentTurn).getChronometer().stop();
            if (king.isInCheck()){
                handleCheckMate();
            }
            else {
                handleDraw();
            }
        }
    }

   // private HashSet<PieceLocation> refinePossibleMoves()

    public void handleCheckMate(){
        Toast.makeText(mContext, "CHECK MATE! " +
                currentTurn.getOpposite().toString().toUpperCase() + " WINS", Toast.LENGTH_LONG).show();
    }

    public void handleDraw(){
        Toast.makeText(mContext, "IT\'S A DRAW!", Toast.LENGTH_LONG).show();
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public Map<Color, Player> getPlayerMap() {
        return playerMap;
    }

    public boolean isCalculatingCheck() {
        return isCalculatingCheck;
    }

    public void setIsCalculatingCheck(boolean isCalculatingCheck) {
        this.isCalculatingCheck = isCalculatingCheck;
    }

    public GameBoardAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(GameBoardAdapter adapter) {
        this.adapter = adapter;
    }
}
