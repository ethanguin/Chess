package chess;

import java.util.Collection;
import java.util.HashSet;

public class ChessGameImpl implements ChessGame {
    TeamColor turn;
    ChessBoard board = new ChessBoardImpl();
    //ChessPiece whiteKing = board.getPiece(new ChessPositionImpl(5, 1));
    //ChessPiece blackKing = board.getPiece(new ChessPositionImpl(5, 8));
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

        //check for check on all of the moves, add the ones that aren't in check
        Collection<ChessMove> allMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMovesList = new HashSet<>();
        //make a copy of the board to reset to
        ChessBoard boardOriginal = board.copy();
        for (ChessMove move: allMoves) {
            board.movePiece(move.getStartPosition(), move.getEndPosition(), move.getPromotionPiece());
            if(!isInCheck(piece.getTeamColor())) {
                validMovesList.add(move);
            }
            board = boardOriginal.copy();
        }
        return validMovesList;
    }


    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece ==  null) {
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

//        //check for Queens
//        pieces.add(new ChessPieceImpl(ChessPiece.PieceType.QUEEN, teamColor));
//        //check for Bishops
//        pieces.add(new ChessPieceImpl(ChessPiece.PieceType.BISHOP, teamColor));
//        //check for Rooks
//        pieces.add(new ChessPieceImpl(ChessPiece.PieceType.ROOK, teamColor));
//        //check for knights
//        pieces.add(new ChessPieceImpl(ChessPiece.PieceType.KNIGHT, teamColor));
//        //check for pawns
//        pieces.add(new ChessPieceImpl(ChessPiece.PieceType.PAWN, teamColor));
//        //check for kings (for checkmate)
//        pieces.add(new ChessPieceImpl(ChessPiece.PieceType.KING, teamColor));
//
//
//
//        for (ChessPiece piece: pieces) {
//            Collection<ChessMove> moves = piece.pieceMoves(board, kingPos);
//            //if the end of the move includes the type of piece we have then they are in check
//            for (ChessMove move:moves) {
//                if (board.getPiece(move.getEndPosition()) == null) {
//                    continue;
//                }
//                if (board.getPiece(move.getEndPosition()).getPieceType() == piece.getPieceType()) {
//                    return true;
//                }
//            }
//
//        }


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
                    for (ChessPiece.PieceType promotion:promotions) {
                        checkMoves.add(new ChessMoveImpl(currPosition, kingPos, promotion));
                    }
                    for (ChessMove move:checkMoves) {
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
        for (ChessMove move:kingMoves) {
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
        for (ChessMove move:kingMoves) {
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
}
