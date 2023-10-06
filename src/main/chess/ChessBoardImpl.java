package chess;

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

    @Override
    public void movePiece(ChessPosition oldPos, ChessPosition newPos) {}
    @Override
    public void movePiece(ChessPosition oldPos, ChessPosition newPos, ChessPiece.PieceType promotionPiece) {
        ChessPiece piece = getPiece(oldPos);
        piece.setPieceType(promotionPiece);
        addPiece(newPos, piece);
        removePiece(oldPos);
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
}
