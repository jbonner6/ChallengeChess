package com.jnbonner.challengechess.Model.GameBoard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jnbonner.challengechess.Model.GamePieces.GamePiece;
import com.jnbonner.challengechess.Model.GamePieces.King;
import com.jnbonner.challengechess.Model.PlayersAndAI.Player;
import com.jnbonner.challengechess.R;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by James on 6/6/2016.
 */
public class GameBoardAdapter extends BaseAdapter implements Serializable {

    LayoutInflater mInflater;
    Context mContext;
    BoardTile[][] row;
    GamePiece currentPiece;

    public GameBoardAdapter(Context context, BoardTile[][] row){
        mContext = context;
        Context locContext = context.getApplicationContext();
        mInflater = LayoutInflater.from(locContext);
        currentPiece = null;
        this.row = row;
    }

    static class ViewHolder extends ImageView implements Serializable{
        public ImageView square;
        public ImageView piece;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public int getCount() {
        return row.length * row.length;
    }

    @Override
    public Object getItem(int position) {
        return row[getRow(position)][getColumn(position)];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null){
            rowView = mInflater.inflate(R.layout.square, null);
            final ViewHolder viewHolder = new ViewHolder(mContext);
            final int x = getRow(position);
            final int y = getColumn(position);
            viewHolder.square = (ImageView) rowView.findViewById(R.id.square_background);
            viewHolder.square.setImageResource(row[x][y].color.getBackground());
            viewHolder.piece = (ImageView) rowView.findViewById(R.id.piece);
            if (row[x][y].getGamePiece() != null) {
                viewHolder.piece.setImageResource(row[x][y]
                        .getGamePiece().getImage());

                viewHolder.piece.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pieceOnClick(x, y);
                    }

                });
            }
            else if (row[x][y].getGamePiece() == null) {
                viewHolder.square.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tileOnClick(x, y);
                    }
                });
            }

        }

        rowView.setTag(rowView);

        return rowView;
    }

    private void pieceOnClick(int x, int y){
        if (!tileOnClick(x, y)) {
            if (GameBoard.getInstance().getGameBoard()[x][y].getGamePiece().getColor() ==
                    GameBoard.getInstance().getTurnManager().getCurrentTurn()) {
                GamePiece newPiece = row[x][y].getGamePiece();
                if (newPiece != currentPiece) {
                    int locPosition = x * 8 + y;
                    View view = GameBoard.getInstance().getGameGrid().getChildAt(locPosition);
                    ImageView outline = (ImageView) view.findViewById(R.id.selectOutline);
                    ArrayList<PieceLocation> possibleMoves = newPiece.calculatePossibleMoves();
                    currentPiece = newPiece;
                    possibleMoves = GameBoard.getInstance().possibleMovesWontCauseCheck(possibleMoves, currentPiece);
                    markPossibleMoves(possibleMoves);
                    outline.setImageResource(R.drawable.red_outline);
                }
            }
        }
    }

    private boolean tileOnClick(int x, int y){
        boolean hadPiece = false;
        PieceLocation newPieceLocation = new PieceLocation(x, y);
        unMarkPossibleMoves();
        if (currentPiece != null) {
//            for (PieceLocation loc : currentPiece.getPossibleMoves()){
//                if (loc.equals(newPieceLocation)){
//                    newPieceLocation.setIsCastle(loc.isCastle);
//                    currentPiece.movePiece(newPieceLocation);
//                    hadPiece = true;
//                }
//            }
            int index = currentPiece.getPossibleMoves().indexOf(newPieceLocation);
            if (index != -1) {
                currentPiece.movePiece(currentPiece.getPossibleMoves().get(index));
                hadPiece = true;
            }
        }
        currentPiece = null;
        return hadPiece;
    }

    private void markPossibleMoves(ArrayList<PieceLocation> moves){
        for (PieceLocation move : moves){
            int position = move.getX() * 8 + move.getY();
            View view = GameBoard.getInstance().getGameGrid().getChildAt(position);
            ImageView outline = (ImageView)view.findViewById(R.id.selectOutline);
            outline.setImageResource(R.drawable.green_outline);
        }
    }

    private void unMarkPossibleMoves(){
        if (currentPiece != null){
            ArrayList<PieceLocation> moves = currentPiece.getPossibleMoves();
            int curPosition = currentPiece.getLocation().getX() * 8 +
                    currentPiece.getLocation().getY();
            View curView = GameBoard.getInstance().getGameGrid().getChildAt(curPosition);
            ImageView curOutline = (ImageView)curView.findViewById(R.id.selectOutline);
            curOutline.setImageResource(android.R.color.transparent);
            for (PieceLocation move : moves){
                int position = move.getX() * 8 + move.getY();
                View view = GameBoard.getInstance().getGameGrid().getChildAt(position);
                ImageView outline = (ImageView)view.findViewById(R.id.selectOutline);
                outline.setImageResource(android.R.color.transparent);
            }
        }
    }

    private int getRow(int position){
        int holder = position;
        int counter = 0;
        while (holder > 7){
            holder -= 8;
            counter++;
        }
        return counter;
    }

    private int getColumn(int position){
        return position % 8;
    }
}
