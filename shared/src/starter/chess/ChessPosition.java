package chess;

/**
 * Represents a single square position on a chess board
 */
public interface ChessPosition {
    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    int getRow();

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    int getColumn();

    void setPosition(char column, int row);
    void setPosition(int column, int row);
}
