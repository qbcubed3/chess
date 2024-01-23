package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.ArrayList;

public class QueenCalculator implements MoveCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        BishopCalculator bishopMoves = new BishopCalculator();
        Collection<ChessMove> moveCollection = new ArrayList<ChessMove>(bishopMoves.pieceMoves(board, position));
        RookCalculator rookMoves = new RookCalculator();
        moveCollection.addAll(rookMoves.pieceMoves(board, position));
        return moveCollection;
    }
}
