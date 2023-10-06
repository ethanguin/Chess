package chess;

import java.util.Collection;
import java.util.HashSet;

public class ChessGameImpl implements ChessGame {
    TeamColor turn;
    ChessBoard board = new ChessBoardImpl();
    @Override
    public TeamColor getTeamTurn() {
        return turn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    @Override
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }

        //check for check on all of the moves
        Collection<ChessMove> allMoves = null;
        return piece.pieceMoves(board, startPosition);
    }


    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> moves = new HashSet<>(validMoves(move.getStartPosition()));
        if (moves.contains(move)) {
            board.movePiece(move.getStartPosition(), move.getEndPosition(), move.getPromotionPiece());
        } else {
            throw new InvalidMoveException("Not a valid move");
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
