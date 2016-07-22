package com.jnbonner.challengechess.Model.GameBoard;

import com.jnbonner.challengechess.Model.GamePieces.GamePiece;

/**
 * Created by James on 6/5/2016.
 */
public class BoardTile {

    GamePiece gamePiece;
    Color color;

    public BoardTile(Color color){
        this.color = color;
    }

    public GamePiece getGamePiece() {
        return gamePiece;
    }

    public void setGamePiece(GamePiece gamePiece) {
        this.gamePiece = gamePiece;
    }
}
