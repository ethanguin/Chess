package chess;

import java.util.Arrays;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;

public class ChessBoardImpl implements ChessBoard {
    private final int boardSize = 8 + 1;
    ChessPiece[][] board = new ChessPiece[boardSize][boardSize];

    public ChessBoardImpl() {
        resetBoard();
    }

    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getColumn()][position.getRow()] = piece;
    }

    @Override
    public void removePiece(ChessPosition position) {
        board[position.getColumn()][position.getRow()] = null;
    }

    public void movePiece(ChessPosition oldPos, ChessPosition newPos) {
        ChessPiece piece = getPiece(oldPos);
        addPiece(newPos, piece);
        removePiece(oldPos);
    }

    @Override
    public void movePiece(ChessPosition oldPos, ChessPosition newPos, ChessPiece.PieceType promotionPiece) {
        ChessPiece piece = getPiece(oldPos);
        if (promotionPiece == null) {
            addPiece(newPos, piece);
        } else {
            addPiece(newPos, new ChessPieceImpl(promotionPiece, piece.getTeamColor()));
        }
        removePiece(oldPos);
    }

    @Override
    public ChessPosition findPiecePosition(ChessPiece piece) {
        ChessPosition position = new ChessPositionImpl();
        for (int i = boardSize - 1; i > 0; i--) {
            for (int j = 1; j < boardSize; j++) {
                if (board[j][i] == null) {
                    continue;
                }
                if (board[j][i].equals(piece)) {
                    position.setPosition(j, i);
                    break;
                }
            }
        }

        return position;
    }

    @Override
    public int getSize() {
        return boardSize;
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getColumn()][position.getRow()];
    }

    @Override
    public void resetBoard() {
        board = new ChessPiece[boardSize][boardSize]; // does this clear the board??

        //pawns
        //black pawns
        for (int i = 1; i < boardSize; i++) {
            addPiece(new ChessPositionImpl(i, 7), new ChessPieceImpl(PAWN, ChessGame.TeamColor.BLACK));
        }
        //white pawns
        for (int i = 1; i < boardSize; i++) {
            board[i][2] = new ChessPieceImpl(PAWN, ChessGame.TeamColor.WHITE);
        }
        //kings
        board[5][1] = new ChessPieceImpl(KING, ChessGame.TeamColor.WHITE);
        board[5][8] = new ChessPieceImpl(KING, ChessGame.TeamColor.BLACK);

        //queens
        board[4][1] = new ChessPieceImpl(QUEEN, ChessGame.TeamColor.WHITE);
        board[4][8] = new ChessPieceImpl(QUEEN, ChessGame.TeamColor.BLACK);

        //knights
        board[2][1] = new ChessPieceImpl(KNIGHT, ChessGame.TeamColor.WHITE);
        board[7][1] = new ChessPieceImpl(KNIGHT, ChessGame.TeamColor.WHITE);
        board[2][8] = new ChessPieceImpl(KNIGHT, ChessGame.TeamColor.BLACK);
        board[7][8] = new ChessPieceImpl(KNIGHT, ChessGame.TeamColor.BLACK);

        //bishops
        board[3][1] = new ChessPieceImpl(BISHOP, ChessGame.TeamColor.WHITE);
        board[6][1] = new ChessPieceImpl(BISHOP, ChessGame.TeamColor.WHITE);
        board[3][8] = new ChessPieceImpl(BISHOP, ChessGame.TeamColor.BLACK);
        board[6][8] = new ChessPieceImpl(BISHOP, ChessGame.TeamColor.BLACK);

        //rooks
        board[1][1] = new ChessPieceImpl(ROOK, ChessGame.TeamColor.WHITE);
        board[8][1] = new ChessPieceImpl(ROOK, ChessGame.TeamColor.WHITE);
        board[1][8] = new ChessPieceImpl(ROOK, ChessGame.TeamColor.BLACK);
        board[8][8] = new ChessPieceImpl(ROOK, ChessGame.TeamColor.BLACK);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (int i = boardSize - 1; i > 0; i--) {
            out.append("|");
            for (int j = 1; j < boardSize; j++) {
                if (board[j][i] != null) {
                    out.append(board[j][i].toString());
                    out.append("|");
                } else {
                    out.append(" ");
                    out.append("|");
                }
            }
            out.append("\n");
        }
        return out.toString();
    }

    @Override
    public ChessBoard copy() {
        ChessBoard boardCopy = new ChessBoardImpl();
        for (int i = 1; i < boardSize; i++) {
            for (int j = 1; j < boardSize; j++) {
                if (board[i][j] == null) {
                    continue;
                }
                boardCopy.addPiece(new ChessPositionImpl(i, j), new ChessPieceImpl(board[i][j].getPieceType(), board[i][j].getTeamColor()));
            }
        }
        return boardCopy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoardImpl that = (ChessBoardImpl) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(boardSize);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }
}
