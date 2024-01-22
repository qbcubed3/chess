package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;

import java.util.Collection;
import java.util.ArrayList;

public class KingCalculator implements MoveCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece currentPiece = board.getPiece(position);
        Collection<ChessMove> moveCollection = new ArrayList<ChessMove>();
        int row = position.getRow();
        int column = position.getColumn();
        for (int i = row - 1; i < row + 2; i++){
            if (i < 1 || i > 8){
                continue;
            }
            for (int j = column - 1; j < column + 2; j++){
                if (j < 1 || j > 8){
                    continue;
                }
                ChessPosition curPosition = new ChessPosition(i, j);
                if (!curPosition.equals(position) && (board.getPiece(curPosition).getTeamColor() != currentPiece.getTeamColor())){
                    moveCollection.add(new ChessMove(position, curPosition, null));
                }
            }
        }
        return moveCollection;
    }
}
