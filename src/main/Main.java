import chess.ChessBoard;
import chess.ChessBoardImpl;

public class Main {
    public static void main(String[] args) {
        ChessBoard board = new ChessBoardImpl();
        board.resetBoard();
        System.out.println(board);
    }
}