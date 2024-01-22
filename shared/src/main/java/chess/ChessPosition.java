package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    int rowPos;
    int colPos;

    @Override
    public String toString() {
        return "ChessPosition{" +
                "rowPos=" + rowPos +
                ", colPos=" + colPos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return rowPos == that.rowPos && colPos == that.colPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowPos, colPos);
    }

    public ChessPosition(int row, int col) {
        this.rowPos = row;
        this.colPos = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return rowPos;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return colPos;
    }
}
