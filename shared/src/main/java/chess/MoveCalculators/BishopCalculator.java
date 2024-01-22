package chess.MoveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.ArrayList;

public class BishopCalculator implements MoveCalculator {

    @Override
    // calculates all the moves that are possible for a certain bishop and puts them into a collection
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moveCollection = new ArrayList<ChessMove>();
        int row = position.getRow() + 1;
        int column = position.getColumn() + 1;
        System.out.println("row: " + row + "col: " + column);
        //looks for moves in the upper right path
        for (int i = row; i < 9; i++){
            if(column > 8) {
                break;
            }
            else if(board.getPiece(new ChessPosition(i, column)) == null) {
                moveCollection.add(new ChessMove(position, new ChessPosition(i, column), null));
            }
            else if(board.getPiece(new ChessPosition(i, column)).getTeamColor() != board.getPiece(position).getTeamColor()){
                moveCollection.add(new ChessMove(position, new ChessPosition(i, column), null));
                break;
            }
            else {
                break;
            }
            column += 1;
        }

        //looks for moves in the upper left path
        column = position.getColumn() - 1;
        for (int i = row; i < 9; i++){
            if(column < 1) {
                break;
            }
            else if(board.getPiece(new ChessPosition(i, column)) == null) {
                moveCollection.add(new ChessMove(position, new ChessPosition(i, column), null));
            }
            else if(board.getPiece(new ChessPosition(i, column)).getTeamColor() != board.getPiece(position).getTeamColor()){
                moveCollection.add(new ChessMove(position, new ChessPosition(i, column), null));
                break;
            }
            else {
                break;
            }
            column -= 1;
        }

        //looks for moves in the lower right section
        column = position.getColumn() - 1;
        row -= 2;
        for (int i = row; i > 0; i--){
            if(column < 1) {
                break;
            }
            else if(board.getPiece(new ChessPosition(i, column)) == null) {
                moveCollection.add(new ChessMove(position, new ChessPosition(i, column), null));
            }
            else if(board.getPiece(new ChessPosition(i, column)).getTeamColor() != board.getPiece(position).getTeamColor()){
                moveCollection.add(new ChessMove(position, new ChessPosition(i, column), null));
                break;
            }
            else {
                break;
            }
            column -= 1;
        }

        //looks for moves in the lower left section
        column = position.getColumn() + 1;
        for (int i = row; i > 0; i--){
            if(column > 8) {
                break;
            }
            else if(board.getPiece(new ChessPosition(i, column)) == null) {
                moveCollection.add(new ChessMove(position, new ChessPosition(i, column), null));
            }
            else if(board.getPiece(new ChessPosition(i, column)).getTeamColor() != board.getPiece(position).getTeamColor()){
                moveCollection.add(new ChessMove(position, new ChessPosition(i, column), null));
                break;
            }
            else {
                break;
            }
            column += 1;
        }

        for (ChessMove move: moveCollection){
            System.out.println(move);
        }
        return moveCollection;
    }
}
