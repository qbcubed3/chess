package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;

import java.util.Collection;
import java.util.ArrayList;

public class PawnCalculator implements MoveCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPiece currentPiece = board.getPiece(position);
        int row = position.getRow();
        int column = position.getColumn();
        Collection<ChessMove> moveCollection = new ArrayList<ChessMove>();

        //finds the moves that a white pawn has
        if (currentPiece.getTeamColor() == chess.ChessGame.TeamColor.WHITE){
            //forward one or two
            if (board.getPiece(new ChessPosition(row + 1, column)) == null) {
                moveCollection.add(new ChessMove(position, new ChessPosition(row + 1, column), null));
                if (board.getPiece(new ChessPosition(row + 2, column)) == null && position.getRow() == 2){
                    moveCollection.add(new ChessMove(position, new ChessPosition(row + 2, column), null));
                }
            }
            //captures a piece
            if (board.getPiece(new ChessPosition(row + 1, column + 1)) != null){
                if (board.getPiece(new ChessPosition(row + 1, column + 1)).getTeamColor() == chess.ChessGame.TeamColor.BLACK){
                    moveCollection.add(new ChessMove(position, new ChessPosition(row + 1, column + 1), null));
                }
            }
            if (board.getPiece(new ChessPosition(row + 1, column - 1)) != null) {
                if (board.getPiece(new ChessPosition(row + 1, column - 1)).getTeamColor() == chess.ChessGame.TeamColor.BLACK) {
                    moveCollection.add(new ChessMove(position, new ChessPosition(row + 1, column - 1), null));
                }
            }
        }

        //finds the moves that a black pawn can make
        else{
            //forward one or two
            if (board.getPiece(new ChessPosition(row - 1, column)) == null) {
                moveCollection.add(new ChessMove(position, new ChessPosition(row - 1, column), null));
                if (board.getPiece(new ChessPosition(row - 2, column)) == null && position.getRow() == 2){
                    moveCollection.add(new ChessMove(position, new ChessPosition(row - 2, column), null));
                }
            }
            //capturing pieces
            if (board.getPiece(new ChessPosition(row - 1, column + 1)) != null) {
                if (board.getPiece(new ChessPosition(row - 1, column + 1)).getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
                    moveCollection.add(new ChessMove(position, new ChessPosition(row - 1, column + 1), null));
                }
            }

            if (board.getPiece(new ChessPosition(row - 1, column - 1)) != null) {
                if (board.getPiece(new ChessPosition(row - 1, column - 1)).getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
                    moveCollection.add(new ChessMove(position, new ChessPosition(row - 1, column - 1), null));
                }
            }
        }

        return moveCollection;
    }
}
