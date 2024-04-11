package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;

import java.util.Collection;
import java.util.ArrayList;

public class RookCalculator implements MoveCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece currentPiece = board.getPiece(position);
        Collection<ChessMove> moveCollection = new ArrayList<ChessMove>();
        int row = position.getRow();
        int column = position.getColumn();
        //gets all the moves that are on the upper column
        for (int i = row + 1; i < 9; i++){
            ChessPosition curPosition = new ChessPosition(i, column);
            if (board.getPiece(curPosition) == null){
                moveCollection.add(new ChessMove(position, curPosition, null));
            }
            else{
                if (board.getPiece(curPosition).getTeamColor() != currentPiece.getTeamColor()){
                    moveCollection.add(new ChessMove(position, curPosition, null));
                }
                break;
            }
        }
        //gets all the moves that are on the lower column
        for (int j = row - 1; j > 0; j--){
            ChessPosition cur = new ChessPosition(j, column);
            if (board.getPiece(cur) == null){moveCollection.add(new ChessMove(position, cur, null));}
            else{
                if (board.getPiece(cur).getTeamColor() != currentPiece.getTeamColor()){
                    moveCollection.add(new ChessMove(position, cur, null));
                }
                break;}
        }
        //gets all the moves that are on the lower row
        for (int i = column - 1; i > 0; i--){
            ChessPosition curPosition = new ChessPosition(row, i);
            if (board.getPiece(curPosition) == null){
                moveCollection.add(new ChessMove(position, curPosition, null));
            }
            else{
                if (board.getPiece(curPosition).getTeamColor() != currentPiece.getTeamColor()){
                    moveCollection.add(new ChessMove(position, curPosition, null));
                }
                break;
            }
        }
        //gets all the moves that are on the upper row
        for (int i = column + 1; i < 9; i++){
            ChessPosition cur = new ChessPosition(row, i);
            if (board.getPiece(cur) == null){
                moveCollection.add(new ChessMove(position, cur, null));
            }
            else{
                if (board.getPiece(cur).getTeamColor() != currentPiece.getTeamColor()){
                    moveCollection.add(new ChessMove(position, cur, null));
                }
                break;
            }
        }
        return moveCollection;
    }
}
