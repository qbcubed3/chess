package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;

import java.util.Collection;
import java.util.ArrayList;

public class KnightCalculator implements MoveCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece currentPiece = board.getPiece(position);
        Collection<ChessMove> moveCollection = new ArrayList<ChessMove>();
        int row = position.getRow();
        int column = position.getColumn();
        //gets the knight movements that move two up and two down
        for (int i = row - 2; i < row + 3; i += 4){
            if (i < 1 || i > 8) {
                continue;
            }
            for (int j = column - 1; j < column + 2; j += 2){
                if (j < 1 || j > 8) {
                    continue;
                }
                ChessPosition curPosition = new ChessPosition(i, j);
                if (board.getPiece(curPosition) == (null) || board.getPiece(curPosition).getTeamColor() != currentPiece.getTeamColor()) {
                    moveCollection.add(new ChessMove(position, curPosition, null));
                }
            }
        }
        //gets the knight movements that move one up and one down
        for (int i = row - 1; i < row + 2; i += 2) {
            if (i < 1 || i > 8) {
                continue;
            }
            for (int j = column - 2; j < column + 3; j += 4) {
                if (j < 1 || j > 8){
                    continue;
                }
                ChessPosition curPosition = new ChessPosition(i, j);
                if (board.getPiece(curPosition) == (null) || board.getPiece(curPosition).getTeamColor() != currentPiece.getTeamColor()) {
                    moveCollection.add(new ChessMove(position, curPosition, null));
                }
            }
        }

        return moveCollection;
    }
}
