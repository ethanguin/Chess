package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;

public class ChessBoardImpl implements ChessBoard {
    int boardSize = 8 + 1;
    ChessPiece[][] board = new ChessPiece[boardSize][boardSize];

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

    private static final int BLACK = 0;
    //private static final int RED = 1;
    private static final int GREEN = 2;
    //private static final int YELLOW = 3;
    private static final int BLUE = 4;
    private static final int MAGENTA = 5;
    //private static final int CYAN = 6;
    private static final int WHITE = 7;
    private static final String COLOR_RESET = "\u001b[0m";

    private static String color(int FG, int BG) {
        return String.format("\u001b[3%d;4%dm", FG, BG);
    }

    private static String color(int FG) {
        return String.format("\u001b[1;3%dm", FG);
    }

    private static final Map<ChessPiece.PieceType, String> pieceMap = Map.of(
            ChessPiece.PieceType.KING, "K",
            ChessPiece.PieceType.QUEEN, "Q",
            ChessPiece.PieceType.BISHOP, "B",
            ChessPiece.PieceType.KNIGHT, "N",
            ChessPiece.PieceType.ROOK, "R",
            ChessPiece.PieceType.PAWN, "P"
    );

    private static final String BORDER = color(WHITE, BLACK);

    private static final String BOARD_BLACK = color(WHITE, BLACK);
    private static final String BOARD_WHITE = color(BLACK, WHITE);
    private static final String BOARD_HIGHLIGHT = color(GREEN, MAGENTA);

    private static final String BLACK_PIECE = color(BLUE);
    private static final String WHITE_PIECE = color(GREEN);

    @Override
    public String toString(ChessGame.TeamColor playerColor, Collection<ChessPosition> highlights) {
        var sb = new StringBuilder();
        try {
            var currentSquare = BOARD_WHITE;
            var rows = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
            var columns = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
            var columnsLetters = "    a  b  c  d  e  f  g  h    ";
            if (playerColor == ChessGame.TeamColor.BLACK) {
                columnsLetters = "    h  g  f  e  d  c  b  a    ";
                rows = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
                columns = new int[]{8, 7, 6, 5, 4, 3, 2, 1};
            }
            sb.append(BORDER).append(columnsLetters).append(COLOR_RESET).append("\n");
            for (var i : rows) {
                var row = " " + (i) + " ";
                sb.append(BORDER).append(row).append(COLOR_RESET);
                for (var j : columns) {
                    var squareColor = currentSquare;
                    var piece = board[j][i];
                    if (piece != null) {
                        var color = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? WHITE_PIECE : BLACK_PIECE;
                        var p = pieceMap.get(piece.getPieceType());
                        sb.append(squareColor).append(color).append(" ").append(p).append(" ").append(COLOR_RESET);
                    } else {
                        sb.append(squareColor).append("   ").append(COLOR_RESET);
                    }
                    currentSquare = currentSquare.equals(BOARD_BLACK) ? BOARD_WHITE : BOARD_BLACK;
                }
                sb.append(BORDER).append(row).append(COLOR_RESET);
                sb.append('\n');
                currentSquare = currentSquare.equals(BOARD_BLACK) ? BOARD_WHITE : BOARD_BLACK;
            }
            sb.append(BORDER).append(columnsLetters).append(COLOR_RESET).append("\n");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return sb.toString();
    }

    public String blackToString() {
        StringBuilder out = new StringBuilder();
        for (int i = 1; i < boardSize; i++) {
            out.append(" ");
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
