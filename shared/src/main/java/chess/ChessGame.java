package chess;

import java.util.Collection;
import java.util.HashSet;

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

    public void switchTurn(){
        if (teamTurn == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
            return;
        }
        teamTurn = TeamColor.WHITE;
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
        ChessPiece curPiece = board.getPiece(startPosition);
        Collection<ChessMove> moveCollection = curPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        for (ChessMove move: moveCollection){
            ChessPiece movePiece = board.popPiece(move.getStartPosition());
            ChessPiece takenPiece = board.popPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), movePiece);
            if (isInCheck(curPiece.getTeamColor())){
                board.addPiece(move.getStartPosition(), movePiece);
                board.addPiece(move.getEndPosition(), takenPiece);
                continue;
            }
            board.addPiece(move.getStartPosition(), movePiece);
            board.addPiece(move.getEndPosition(), takenPiece);
            validMoves.add(move);
            System.out.println("we are doing this move");
        }
        return validMoves;
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
        if (currentPiece == null) {
            throw new InvalidMoveException("No piece at designated starting position");
        }
        Collection<ChessMove> moveCollection = currentPiece.pieceMoves(board, curPosition);
        if (teamTurn != currentPiece.getTeamColor()){
            throw new InvalidMoveException("Making move out of turn");
        }
        if (!moveCollection.contains(move)){
            throw new InvalidMoveException("Piece cannot reach that square");
        }
        for (ChessMove possibleMove: moveCollection){
            if (possibleMove.equals(move)){
                board.popPiece(curPosition);
                ChessPiece takenPiece = board.popPiece(possibleMove.getEndPosition());
                board.addPiece(move.getEndPosition(), currentPiece);
                if (isInCheck(currentPiece.getTeamColor())){
                    board.addPiece(curPosition, currentPiece);
                    board.addPiece(possibleMove.getEndPosition(), takenPiece);
                    throw new InvalidMoveException("That move puts your king in check");
                }
                board.popPiece(move.getEndPosition());
                if (currentPiece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null){
                    board.addPiece(endPosition, new ChessPiece(currentPiece.getTeamColor(), move.getPromotionPiece()));
                }
                else {
                    board.addPiece(move.getEndPosition(), currentPiece);
                }
            }
        }
        switchTurn();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = board.findKing(teamColor);

        //uses the found king position to make sure that no piece can attack it
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                ChessPosition curPosition = new ChessPosition(i, j);
                ChessPiece curPiece = board.getPiece(curPosition);
                if (curPiece == null){continue;}
                if (curPiece.getTeamColor() != teamColor){
                    Collection<ChessMove> moveCollection = curPiece.pieceMoves(board, curPosition);
                    for (ChessMove move: moveCollection){
                        if (move.getEndPosition().equals(kingPosition)){
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
        if (!isInCheck(teamColor)){return false;}
        ChessPosition kingPosition = board.findKing(teamColor);
        ChessPiece king = board.getPiece(kingPosition);
        Collection<ChessMove> validMoves = king.pieceMoves(board, kingPosition);
        Collection<ChessMove> coveredMoves = new HashSet<ChessMove>();
        //finds all the moves that are possible from the other color pieces
        for (ChessMove move: validMoves){
            ChessPosition curPosition = move.getEndPosition();
            ChessPiece tempPiece = board.popPiece(curPosition);
            board.popPiece(move.getStartPosition());
            board.addPiece(curPosition, king);
            if (!isInCheck(teamColor)){
                return false;
            }
            board.popPiece(curPosition);
            board.addPiece(curPosition, tempPiece);
            board.addPiece(move.getStartPosition(), king);
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)){return false;}
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                ChessPosition curPosition = new ChessPosition(i, j);
                ChessPiece curPiece = board.getPiece(curPosition);
                if (curPiece == null || curPiece.getTeamColor() != teamColor){
                    continue;
                }
                Collection<ChessMove> validMoves = curPiece.pieceMoves(board, curPosition);
                Collection<ChessMove> badMoves = new HashSet<ChessMove>();
                for (ChessMove move: validMoves) {
                    ChessPiece movePiece = board.popPiece(move.getStartPosition());
                    ChessPiece takenPiece = board.popPiece(move.getEndPosition());
                    board.addPiece(move.getEndPosition(), movePiece);
                    if (isInCheck(teamColor)) {
                        badMoves.add(move);
                    }
                    board.addPiece(move.getEndPosition(), takenPiece);
                    board.addPiece(move.getStartPosition(), movePiece);
                }
                validMoves.removeAll(badMoves);
                if (!validMoves.isEmpty()){ //if there is a valid move then its not stalemate and it returns false
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
