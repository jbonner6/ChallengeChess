package com.jnbonner.challengechess.Model.GameBoard;

import android.graphics.drawable.Drawable;

import com.jnbonner.challengechess.R;

import java.io.Serializable;

/**
 * Created by James on 6/5/2016.
 */
public enum Color implements Serializable{
    Black{
        @Override
        public int getBackground() {
            return R.color.blackThrough;
        }

        @Override
        public int getKing() {
            return R.drawable.black_king;
        }

        @Override
        public int getQueen() {
            return R.drawable.black_queen;
        }

        @Override
        public int getBishop() {
            return R.drawable.black_bishop;
        }

        @Override
        public int getKnight() {
            return R.drawable.black_knight;
        }

        @Override
        public int getRook() {
            return R.drawable.black_rook;
        }

        @Override
        public int getPawn() {
            return R.drawable.black_pawn;
        }

        @Override
        public int getDirScale() {
            return -1;
        }

        @Override
        public int getBaseValue() {
            return 0;
        }
    },
    White{
        @Override
        public int getBackground() {
            return R.color.whiteThrough;
        }

        @Override
        public int getKing() {
            return R.drawable.white_king;
        }

        @Override
        public int getQueen() {
            return R.drawable.white_queen;
        }

        @Override
        public int getBishop() {
            return R.drawable.white_bishop;
        }

        @Override
        public int getKnight() {
            return R.drawable.white_knight;
        }

        @Override
        public int getRook() {
            return R.drawable.white_rook;
        }

        @Override
        public int getPawn() {
            return R.drawable.white_pawn;
        }

        @Override
        public int getDirScale() {
            return 1;
        }

        @Override
        public int getBaseValue() {
            return 7;
        }
    };

    public Color getOpposite(){
        switch (this){
            case White: return Black;
            case Black: return White;
        }
        return null;
    }

    public abstract int getBackground();
    public abstract int getKing();
    public abstract int getQueen();
    public abstract int getBishop();
    public abstract int getKnight();
    public abstract int getRook();
    public abstract int getPawn();
    public abstract int getDirScale();
    public abstract int getBaseValue();
}
