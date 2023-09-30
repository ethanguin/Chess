package chess;

public class ChessBoardImpl implements ChessBoard {
    int[][] board = new int[8][8];
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //cast position to
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        //search if a piece is at that position
        //if there is, return it, if not, return null
        return null;
    }

    @Override
    public void resetBoard() {
        // delete all pieces
        //create new pieces in all new positions
    }
}
