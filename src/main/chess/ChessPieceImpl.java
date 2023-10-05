package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import static chess.ChessPiece.PieceType.*;

public class ChessPieceImpl implements ChessPiece {
    PieceType type;
    ChessGame.TeamColor color;
    public ChessPieceImpl() {}
    public ChessPieceImpl(PieceType type, ChessGame.TeamColor color) {
        this.type = type;
        this.color = color;
    }
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    @Override
    public PieceType getPieceType() {
        return this.type;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (!isOnBoard(board, myPosition)) {
            return null;
        }
        return switch (type) {
            case KING -> kingMoves(myPosition, board);
            case QUEEN -> queenMoves(myPosition, board);
            case BISHOP -> bishopMoves(myPosition, board);
            case ROOK -> rookMoves(myPosition, board);
            case KNIGHT -> knightMoves(myPosition, board);
            case PAWN -> pawnMoves(myPosition, board);
        };
    }
    private Collection<ChessMove> kingMoves(ChessPosition startPosition, ChessBoard board) {
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition endPosition = new ChessPositionImpl();
        //1 up except for top of the board
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());

        endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() + 1);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //1 up right
        endPosition.setPosition(endPosition.getColumn() + 1, endPosition.getRow());
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //1 right
        endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() - 1);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //1 down right
        endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() - 1);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //1 down
        endPosition.setPosition(endPosition.getColumn() - 1, endPosition.getRow());
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //1 down left
        endPosition.setPosition(endPosition.getColumn() - 1, endPosition.getRow());
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //1 left
        endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() + 1);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //1 up left
        endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() + 1);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }
        return moves;
    }
    private Collection<ChessMove> queenMoves(ChessPosition startPosition, ChessBoard board) {
        //queen is basically a rook and a bishop, so call on both and return them
        Collection<ChessMove> moves = new HashSet<>(bishopMoves(startPosition, board));
        moves.addAll(rookMoves(startPosition, board));
        return moves;
    }
    private Collection<ChessMove> bishopMoves(ChessPosition startPosition, ChessBoard board) {
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition endPosition = new ChessPositionImpl();
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());

        //up right until: edge of board (top or right), hits one of its own players, is on TOP of the other player
        while (isOnBoard(board, endPosition)) {
            //move the piece once
            endPosition.setPosition(endPosition.getColumn() + 1, endPosition.getRow() + 1);
            if (!isOnBoard(board, endPosition)) {
                break;
            }
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() == color) {
                break;
            } else {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                break;
            }
        }
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());
        //up left until similar conditions
        while (isOnBoard(board, endPosition)) {
            //move the piece once
            endPosition.setPosition(endPosition.getColumn() - 1, endPosition.getRow() + 1);
            if (!isOnBoard(board, endPosition)) {
                break;
            }
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() == color) {
                break;
            } else {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                break;
            }
        }
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());
        //down right until
        while (isOnBoard(board, endPosition)) {
            //move the piece once
            endPosition.setPosition(endPosition.getColumn() + 1, endPosition.getRow() - 1);
            if (!isOnBoard(board, endPosition)) {
                break;
            }
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() == color) {
                break;
            } else {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                break;
            }
        }
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());
        //down left until
        while (isOnBoard(board, endPosition)) {
            //move the piece once
            endPosition.setPosition(endPosition.getColumn() - 1, endPosition.getRow() - 1);
            if (!isOnBoard(board, endPosition)) {
                break;
            }
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() == color) {
                break;
            } else {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                break;
            }
        }
        return moves;
    }
    private Collection<ChessMove> rookMoves(ChessPosition startPosition, ChessBoard board) {
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition endPosition = new ChessPositionImpl();
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());

        //up until: edge of board (top or right), hits one of its own players, is on TOP of the other player
        while (isOnBoard(board, endPosition)) {
            //move the piece once
            endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() + 1);
            if (!isOnBoard(board, endPosition)) {
                break;
            }
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() == color) {
                break;
            } else {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                break;
            }
        }
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());
        //left until similar conditions
        while (isOnBoard(board, endPosition)) {
            //move the piece once
            endPosition.setPosition(endPosition.getColumn() - 1, endPosition.getRow());
            if (!isOnBoard(board, endPosition)) {
                break;
            }
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() == color) {
                break;
            } else {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                break;
            }
        }
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());
        //right until
        while (isOnBoard(board, endPosition)) {
            //move the piece once
            endPosition.setPosition(endPosition.getColumn() + 1, endPosition.getRow());
            if (!isOnBoard(board, endPosition)) {
                break;
            }
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() == color) {
                break;
            } else {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                break;
            }
        }
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());
        //down until
        while (isOnBoard(board, endPosition)) {
            //move the piece once
            endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() - 1);
            if (!isOnBoard(board, endPosition)) {
                break;
            }
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() == color) {
                break;
            } else {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                break;
            }
        }
        return moves;
    }
    private Collection<ChessMove> knightMoves(ChessPosition startPosition, ChessBoard board) {
        Collection<ChessMove> moves = new HashSet<>();
        ChessPosition endPosition = new ChessPositionImpl();
        //up left
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());

        endPosition.setPosition(endPosition.getColumn() - 1, endPosition.getRow() + 2);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //up right
        endPosition.setPosition(endPosition.getColumn() + 2, endPosition.getRow());
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //right up
        endPosition.setPosition(endPosition.getColumn() + 1, endPosition.getRow() - 1);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //right down
        endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() - 2);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //down right
        endPosition.setPosition(endPosition.getColumn() - 1, endPosition.getRow() - 1);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //down left
        endPosition.setPosition(endPosition.getColumn() - 2, endPosition.getRow());
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //left down
        endPosition.setPosition(endPosition.getColumn() - 1, endPosition.getRow() + 1);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //left up
        endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() + 2);
        if (isOnBoard(board, endPosition)) {
            if (board.getPiece(endPosition) == null) {
                //add position
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            } else if (board.getPiece(endPosition).getTeamColor() != color) {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }
        return moves;
    }
    private Collection<ChessMove> pawnMoves(ChessPosition startPosition, ChessBoard board) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition endPosition = new ChessPositionImpl();
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());
        int moveDir;
        int promotionRow;
        int startingRow;
        if (color == ChessGame.TeamColor.WHITE) {
            moveDir = 1;
            promotionRow = 8;
            startingRow = 2;
        } else {
            moveDir = -1;
            promotionRow = 1;
            startingRow = 7;
        }

        //double move
        if (startPosition.getRow() == startingRow) {
            endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() + moveDir);
            if (board.getPiece(endPosition) == null && isOnBoard(board, endPosition)) {
                endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() + moveDir);
                if (board.getPiece(endPosition) == null && isOnBoard(board, endPosition)) {
                    moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                }
            }
        }

        //single move
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());
        endPosition.setPosition(endPosition.getColumn(), endPosition.getRow() + moveDir);
        if (isOnBoard(board, endPosition) && board.getPiece(endPosition) == null) {
            if (endPosition.getRow() == promotionRow) {
                moves.addAll(getPromotions(startPosition, endPosition));
            } else {
                moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
            }
        }

        //capture left
        endPosition.setPosition(startPosition.getColumn(), startPosition.getRow());
        endPosition.setPosition(endPosition.getColumn() - 1, endPosition.getRow() + moveDir);
        if (isOnBoard(board, endPosition) && board.getPiece(endPosition) != null) {
            if (board.getPiece(endPosition).getTeamColor() != color) {
                if (endPosition.getRow() == promotionRow) {
                    moves.addAll(getPromotions(startPosition, endPosition));
                } else {
                    moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                }
            }
        }

        //capture right
        endPosition.setPosition(endPosition.getColumn() + 2, endPosition.getRow());
        if (isOnBoard(board, endPosition) && board.getPiece(endPosition) != null) {
            if (board.getPiece(endPosition).getTeamColor() != color) {
                if (endPosition.getRow() == promotionRow) {
                    moves.addAll(getPromotions(startPosition, endPosition));
                } else {
                    moves.add(new ChessMoveImpl(startPosition, new ChessPositionImpl(endPosition.getColumn(), endPosition.getRow())));
                }
            }
        }

        return moves;
    }
    private Collection<ChessMove> getPromotions(ChessPosition start, ChessPosition end) {
        Collection<ChessMove> moves = new ArrayList<>();
        //go through and add a move for each kind of promotion piece
        PieceType[] promotions = {QUEEN, ROOK, KNIGHT, BISHOP};
        for (PieceType piece: promotions) {
            moves.add(new ChessMoveImpl(start, end, piece));
        }

        return moves;
    }
    private boolean isOnBoard(ChessBoard board, ChessPosition position) {
        int boardSize = board.getSize() - 1;
        return (position.getColumn() <= boardSize && position.getColumn() >= 1 && position.getRow() <= boardSize && position.getRow() >= 1);
    }

    @Override
    public String toString() {
        if (color == ChessGame.TeamColor.WHITE) {
            return switch (type) {
                case KING -> "K";
                case QUEEN -> "Q";
                case BISHOP -> "B";
                case PAWN -> "P";
                case ROOK -> "R";
                case KNIGHT -> "N";
                default -> "";
            };
        } else {
            return switch (type) {
                case KING -> "k";
                case QUEEN -> "q";
                case BISHOP -> "b";
                case PAWN -> "p";
                case ROOK -> "r";
                case KNIGHT -> "n";
                default -> "";
            };
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPieceImpl that = (ChessPieceImpl) o;
        return type == that.type && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }
}
