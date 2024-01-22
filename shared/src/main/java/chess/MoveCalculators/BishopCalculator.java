package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;

import java.util.Collection;
import java.util.ArrayList;

public class BishopCalculator implements MoveCalculator {

    @Override
    // calculates all the moves that are possible for a certain bishop and puts them into a collection
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moveCollection = new ArrayList<ChessMove>();
        int row = position.getRow() + 1;
        int column = position.getColumn() + 1;
        for (int i = row; i < 8; i++){
            if(column > 7) {
                break;
            }
            else if(board.getPiece(new ChessPosition(row, column)) == null) {
                moveCollection.add(new ChessMove(position, new ChessPosition(row, column), ChessPiece.PieceType.BISHOP));
            }
            column += 1;
        }

        return moveCollection;
    }
}
