import chess.*;

public class Main {
    public static void main(String[] args) {
        ChessGame game = new ChessGameImpl();

        ChessBoard board = new ChessBoardImpl();


        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
    }
}