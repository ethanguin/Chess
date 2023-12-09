package chess;

import java.util.Locale;
import java.util.Objects;

public class ChessMoveImpl implements ChessMove {
    ChessPosition startPosition;
    ChessPosition endPosition;
    ChessPiece.PieceType promotionPiece;

    public ChessMoveImpl(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    public ChessMoveImpl(ChessPosition startPosition, ChessPosition endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = null;
    }

    public ChessMoveImpl(String chessMoveString) throws Exception {
        chessMoveString = chessMoveString.toLowerCase(Locale.ROOT);
        if (chessMoveString.length() >= 4) {
            int colStart = chessMoveString.charAt(0) - 'a' + 1;
            int rowStart = chessMoveString.charAt(1) - '1' + 1;
            int colEnd = chessMoveString.charAt(2) - 'a' + 1;
            int rowEnd = chessMoveString.charAt(3) - '1' + 1;

            this.startPosition = new ChessPositionImpl(rowStart, colStart);
            this.endPosition = new ChessPositionImpl(rowEnd, colEnd);
            if (chessMoveString.length() == 5) {
                promotionPiece = switch (chessMoveString.charAt(4)) {
                    case 'q' -> ChessPiece.PieceType.QUEEN;
                    case 'b' -> ChessPiece.PieceType.BISHOP;
                    case 'n' -> ChessPiece.PieceType.KNIGHT;
                    case 'r' -> ChessPiece.PieceType.ROOK;
                    default -> null;
                };
            }
            return;
        }
        throw new Exception("Invalid notation");
    }

    @Override
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    @Override
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMoveImpl chessMove = (ChessMoveImpl) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(startPosition.toString());
        out.append(" -> ");
        out.append(endPosition.toString());
        if (promotionPiece != null) {
            out.append(", promotionPiece = ");
            out.append(promotionPiece);
            out.append('}');
        }
        return out.toString();
    }
}
