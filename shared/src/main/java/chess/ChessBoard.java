package chess;

import static chess.ChessGame.TeamColor;
import static chess.ChessPiece.PieceType;
import java.lang.StringBuilder;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    static ChessPiece[][] pieces = new ChessPiece[9][9];

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()){
            return false;
        }
        else{
            ChessBoard other = (ChessBoard) obj;
            for (int i = 1; i < 9; i++){
                for (int j = 1; j < 9; j++){
                    ChessPosition cur = new ChessPosition(i, j);
                    if (other.getPiece(cur) != this.getPiece(cur)){
                        return false;
                    }
                }
            }
        }
        return true;
    }

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
     * takes a piece off the chessboard and returns it
     *
     * @param position of piece to be popped
     * @return the chess piece that is popped
     */
    public ChessPiece popPiece(ChessPosition position){
        if (ChessBoard.pieces[position.getRow()][position.getColumn()] == null){
            return null;
        }
        ChessPiece curPiece = getPiece(position);
        pieces[position.getRow()][position.getColumn()] = null;
        return curPiece;
    }

    /**
     * finds the king of the given color on the board
     * @param teamColor the color of the piece the function should find
     * @return the position of the piece when it is found
     */
    public ChessPosition findKing(ChessGame.TeamColor teamColor){
        ChessPosition kingPosition = null;
        //finds where the king is of the specified teamColor
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition curPosition = new ChessPosition(i, j);
                if (getPiece(curPosition) == null){continue;}
                if (getPiece(curPosition).getTeamColor() == teamColor
                        && getPiece(curPosition).getPieceType() == ChessPiece.PieceType.KING){
                    kingPosition = curPosition;
                    break;
                }
            }
            if (kingPosition != null){
                break;
            }
        }
        return kingPosition;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessBoard.pieces = new ChessPiece[9][9];
        //places all the starting pieces in the correct spots for chess
        for (int row = 1; row < 9; row++){
            for (int col = 1; col < 9; col++){
                ChessPosition currentPosition = new ChessPosition(row, col);
                if (row == 2) {
                    this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.PAWN));
                }
                if (row == 7) {
                    this.addPiece(currentPosition, new ChessPiece(TeamColor.BLACK, PieceType.PAWN));
                }
                if (row == 1){
                    if (col == 1 || col == 8){
                        this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.ROOK));
                    }
                    else if (col == 2 || col == 7) {
                        this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.KNIGHT));
                    }
                    else if (col == 3 || col == 6) {
                        this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.BISHOP));
                    }
                    else if (col == 4) {
                        this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.QUEEN));
                    }
                    else {
                        this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.KING));
                    }
                }
                if (row == 8) {
                    if (col == 1 || col == 8) {
                        this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.ROOK));
                    } else if (col == 2 || col == 7) {
                        this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.KNIGHT));
                    } else if (col == 3 || col == 6) {
                        this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.BISHOP));
                    } else if (col == 4) {
                        this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.QUEEN));
                    } else {
                        this.addPiece(currentPosition, new ChessPiece(TeamColor.WHITE, PieceType.KING));
                    }
                }
            }
        }
    }

    public String toString(){
        StringBuilder finalString = new StringBuilder();
        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length; j++){
                finalString.append(pieces[i][j]);
            }
        }

        return finalString.toString();
    }
}
