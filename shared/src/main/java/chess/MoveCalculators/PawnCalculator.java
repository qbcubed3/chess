package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;
import chess.ChessPiece.PieceType;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;

public class PawnCalculator implements MoveCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPiece currentPiece = board.getPiece(position);
        int row = position.getRow();
        int column = position.getColumn();
        Collection<ChessMove> moveCollection = new HashSet<ChessMove>();

        //finds the moves for the white piece
        if (currentPiece.getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
            for (int i = column - 1; i < column + 2; i++) {
                if (i > 8 || i < 1){
                    continue;
                }
                ChessPosition currentPosition = new ChessPosition(row + 1, i);
                boolean promotion = row + 1 == 8;
                boolean validMove = false;
                //looks for capture moves to the top left and right
                if (i != column) {
                    if (board.getPiece(currentPosition) != null) {
                        if (board.getPiece(currentPosition).getTeamColor() == chess.ChessGame.TeamColor.BLACK) {
                            validMove = true;
                        }
                    }
                }
                //looks for moves one and two forward
                else {
                    if (board.getPiece(currentPosition) == null) {
                        validMove = true;
                        if (row + 2 < 9 && (board.getPiece(new ChessPosition(row + 2, column)) == null) && row == 2) {
                            moveCollection.add(new ChessMove(position, new ChessPosition(row + 2, column), null));
                        }
                    }
                }
                //adds the moves and adds all the promotion pieces if promoting
                if (validMove) {
                    if (promotion) {
                        moveCollection.add(new ChessMove(position, currentPosition, PieceType.QUEEN));
                        moveCollection.add(new ChessMove(position, currentPosition, PieceType.ROOK));
                        moveCollection.add(new ChessMove(position, currentPosition, PieceType.BISHOP));
                        moveCollection.add(new ChessMove(position, currentPosition, PieceType.KNIGHT));
                    } else {
                        moveCollection.add(new ChessMove(position, currentPosition, null));
                    }
                }
            }
        }

        //finds the moves for the black piece
        else {
            for (int i = column - 1; i < column + 2; i++) {
                if (i > 8 || i < 1){
                    continue;
                }
                ChessPosition currentPosition = new ChessPosition(row - 1, i);
                boolean promotion = row - 1 == 1;
                boolean validMove = false;
                //looks for capture moves to the top left and right
                if (i != column) {
                    if (board.getPiece(currentPosition) != null) {
                        if (board.getPiece(currentPosition).getTeamColor() == chess.ChessGame.TeamColor.WHITE) {
                            validMove = true;
                        }
                    }
                }
                //looks for moves one and two forward
                else {
                    if (board.getPiece(currentPosition) == null) {
                        validMove = true;
                        if (row - 2 > 0 && (board.getPiece(new ChessPosition(row - 2, column)) == null) && row == 7) {
                            moveCollection.add(new ChessMove(position, new ChessPosition(row - 2, column), null));
                        }
                    }
                }
                //adds the moves and adds all the promotion pieces if promoting
                if (validMove) {
                    if (promotion) {
                        for (PieceType type: PieceType.values()) {
                            if (type == PieceType.PAWN || type == PieceType.KING) {
                                continue;
                            }
                            moveCollection.add(new ChessMove(position, currentPosition, type));
                        }
                    } else {
                        moveCollection.add(new ChessMove(position, currentPosition, null));
                    }
                }
            }
        }


        return moveCollection;
    }
}
