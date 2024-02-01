package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn;
    ChessBoard board;
    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition curPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece currentPiece = board.getPiece(curPosition);
        Collection<ChessMove> moveCollection = currentPiece.pieceMoves(board, curPosition);

        for (ChessMove possibleMove: moveCollection){
            if (possibleMove.equals(move)){
                board.popPiece(curPosition);
                if (isInCheck(currentPiece.getTeamColor())){
                    board.addPiece(curPosition, currentPiece);
                    throw new InvalidMoveException("That move puts your king in check");
                }
                else{
                    board.popPiece(endPosition);
                    board.addPiece(endPosition, currentPiece);
                }
            }
        }

        throw new InvalidMoveException("Piece cannot reach that square");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        //finds where the king is of the specified teamColor
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition curPosition = new ChessPosition(i, j);
                if (board.getPiece(curPosition).getTeamColor() == teamColor
                        && board.getPiece(curPosition).getPieceType() == ChessPiece.PieceType.KING){
                    kingPosition = curPosition;
                    break;
                }
            }
            if (kingPosition != null){
                break;
            }
        }

        //uses the found king position to make sure that no piece can attack it
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition curPosition = new ChessPosition(i, j);
                ChessPiece curPiece = board.getPiece(curPosition);
                if (board.getPiece(curPosition).getTeamColor() != teamColor){
                    Collection<ChessMove> moveCollection = curPiece.pieceMoves(board, curPosition);
                    for (ChessMove move: moveCollection){
                        if (move.getEndPosition() == kingPosition){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                ChessPosition curPosition = new ChessPosition(i, j);
                ChessPiece curPiece = board.getPiece(curPosition);
                if (curPiece.getTeamColor() != teamColor){
                    continue;
                }
                Collection<ChessMove> validMoves = curPiece.pieceMoves(board, curPosition);
                if (!validMoves.isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
