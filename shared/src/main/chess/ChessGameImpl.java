package chess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import model.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class ChessGameImpl implements ChessGame {
    TeamColor turn;
    ChessBoard board = new ChessBoardImpl();

    public ChessGameImpl() {
        board.resetBoard();
        turn = TeamColor.WHITE;
    }

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

        //check for check on every move, add the ones that aren't in check
        Collection<ChessMove> allMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMovesList = new HashSet<>();
        //make a copy of the board to reset to
        ChessBoard boardOriginal = board.copy();
        for (ChessMove move : allMoves) {
            board.movePiece(move.getStartPosition(), move.getEndPosition(), move.getPromotionPiece());
            if (!isInCheck(piece.getTeamColor())) {
                validMovesList.add(move);
            }
            board = boardOriginal.copy();
        }
        return validMovesList;
    }


    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No piece on starting position");
        }
        if (piece.getTeamColor() != turn) {
            throw new InvalidMoveException("Move made out of turn");
        }
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if (moves.contains(move)) {
            board.movePiece(move.getStartPosition(), move.getEndPosition(), move.getPromotionPiece());
            if (piece.getTeamColor() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {
                setTeamTurn(TeamColor.WHITE);
            }
        } else {
            throw new InvalidMoveException("Not a valid move");
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        //Collection<ChessPiece> pieces = new ArrayList<>();
        //finds the position of the king and checks if it is in the set of the other team's pieceMoves
        ChessPosition kingPos = board.findPiecePosition(new ChessPieceImpl(ChessPiece.PieceType.KING, teamColor));

        for (int i = 1; i < board.getSize(); i++) {
            for (int j = 1; j < board.getSize(); j++) {
                ChessPosition currPosition = new ChessPositionImpl(i, j);
                ChessPiece currPiece = board.getPiece(currPosition);
                if (currPiece == null) {
                    continue;
                }

                Collection<ChessMove> currMoves = currPiece.pieceMoves(board, currPosition);
                ChessMove checkMove = new ChessMoveImpl(currPosition, kingPos);
                if (currMoves.contains(checkMove)) {
                    return true;
                }
                if (currPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                    ChessPiece.PieceType[] promotions = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK};
                    Collection<ChessMove> checkMoves = new HashSet<>();
                    for (ChessPiece.PieceType promotion : promotions) {
                        checkMoves.add(new ChessMoveImpl(currPosition, kingPos, promotion));
                    }
                    for (ChessMove move : checkMoves) {
                        if (currMoves.contains(move)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {

        if (!isInCheck(teamColor)) { //if the king isn't currently in check, then it's not in checkmate
            return false;
        }
        //keep original board saved
        ChessBoard boardOriginal = board.copy();
        ChessPiece king = new ChessPieceImpl(ChessPiece.PieceType.KING, teamColor);
        ChessPosition kingPosition = board.findPiecePosition(king);
        Collection<ChessMove> kingMoves = king.pieceMoves(board, kingPosition);

        //if the king can make a single move without being in check, then it's not checkmate
        for (ChessMove move : kingMoves) {
            board.movePiece(kingPosition, move.getEndPosition());
            if (!isInCheck(teamColor)) {
                board = boardOriginal.copy();
                return false;
            }
            board = boardOriginal.copy();
        }
        return true;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        //keep original board saved
        ChessBoard boardOriginal = board.copy();
        ChessPiece king = new ChessPieceImpl(ChessPiece.PieceType.KING, teamColor);
        ChessPosition kingPosition = board.findPiecePosition(king);
        Collection<ChessMove> kingMoves = king.pieceMoves(board, kingPosition);

        //if the king can make a single move without being in check, then it's not checkmate
        for (ChessMove move : kingMoves) {
            board.movePiece(kingPosition, move.getEndPosition());
            if (!isInCheck(teamColor)) {
                board = boardOriginal.copy();
                return false;
            }
            board = boardOriginal.copy();
        }
        return true;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGameImpl chessGame = (ChessGameImpl) o;
        if (turn != null && chessGame.turn != null) {
            if (!turn.equals(chessGame.turn)) {
                return false;
            }
        } else if (!(turn == null && chessGame.turn == null)) {
            return false;
        }
        return Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }

    public static Gson serializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessGame.class, new ChessGameAdapter());
        gsonBuilder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
        gsonBuilder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        gsonBuilder.registerTypeAdapter(ChessMove.class, new ChessMoveAdapter());
        gsonBuilder.registerTypeAdapter(ChessPosition.class, new ChessPositionAdapter());
        return gsonBuilder.create();
    }
}
