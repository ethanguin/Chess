import chess.*;

public class Main {
    public static void main(String[] args) {
        ChessGame game = new ChessGameImpl();

        ChessBoard board = new ChessBoardImpl();

        //white king
//        board.addPiece(TestFactory.getNewPosition(3, 2),
//                TestFactory.getNewPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
//
//        //black king
//        board.addPiece(TestFactory.getNewPosition(8, 8),
//                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
//
//        //threatening piece
//        board.addPiece(TestFactory.getNewPosition(3, 6),
//                TestFactory.getNewPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
//
//        //set up game
//        game = TestFactory.getNewGame();
        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
    }
}