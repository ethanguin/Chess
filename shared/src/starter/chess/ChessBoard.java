package chess;

import java.util.Collection;

/**
 * A chessboard that can hold and rearrange chess pieces
 */
public interface ChessBoard {

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    void addPiece(ChessPosition position, ChessPiece piece);

    int getSize();

    ChessPosition findPiecePosition(ChessPiece piece);

    void removePiece(ChessPosition position);

    void movePiece(ChessPosition oldPos, ChessPosition newPos);

    void movePiece(ChessPosition oldPos, ChessPosition newPos, ChessPiece.PieceType promotionPiece);

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that position
     */
    ChessPiece getPiece(ChessPosition position);

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    void resetBoard();

    ChessBoard copy();

    String toString(ChessGame.TeamColor color, Collection<ChessPosition> highlights);
}
