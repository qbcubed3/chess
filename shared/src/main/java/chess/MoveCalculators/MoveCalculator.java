package chess.MoveCalculators;
import chess.*;

import java.util.Collection;

public interface MoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

}
