package chess;

import static chess.ChessGame.TeamColor;
import static chess.ChessPiece.PieceType;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    static ChessPiece[][] pieces;

    public ChessBoard() {
        pieces = new ChessPiece[9][9];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        ChessBoard.pieces[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (ChessBoard.pieces[position.getRow()][position.getColumn()] == null){
            return null;
        }
        return ChessBoard.pieces[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessBoard.pieces = new ChessPiece[8][8];
        //places all the starting pieces in the correct spots for chess
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                if (row == 1) {
                    pieces[row][col] = new ChessPiece(TeamColor.WHITE, PieceType.PAWN);
                }
                if (row == 6) {
                    pieces[row][col] = new ChessPiece(TeamColor.BLACK, PieceType.PAWN);
                }
                if (row == 0){
                    if (col == 0 || col == 7){
                        pieces[row][col] = new ChessPiece(TeamColor.WHITE, PieceType.ROOK);
                    }
                    else if (col == 1 || col == 6) {
                        pieces[row][col] = new ChessPiece(TeamColor.WHITE, PieceType.KNIGHT);
                    }
                    else if (col == 2 || col == 5) {
                        pieces[row][col] = new ChessPiece(TeamColor.WHITE, PieceType.BISHOP);
                    }
                    else if (col == 3) {
                        pieces[row][col] = new ChessPiece(TeamColor.WHITE, PieceType.QUEEN);
                    }
                    else {
                        pieces[row][col] = new ChessPiece(TeamColor.WHITE, PieceType.QUEEN);
                    }
                }
                if (row == 7) {
                    if (col == 0 || col == 7) {
                        pieces[row][col] = new ChessPiece(TeamColor.BLACK, PieceType.ROOK);
                    } else if (col == 1 || col == 6) {
                        pieces[row][col] = new ChessPiece(TeamColor.BLACK, PieceType.KNIGHT);
                    } else if (col == 2 || col == 5) {
                        pieces[row][col] = new ChessPiece(TeamColor.BLACK, PieceType.BISHOP);
                    } else if (col == 3) {
                        pieces[row][col] = new ChessPiece(TeamColor.BLACK, PieceType.QUEEN);
                    } else {
                        pieces[row][col] = new ChessPiece(TeamColor.BLACK, PieceType.QUEEN);
                    }
                }
            }
        };
    }
}
