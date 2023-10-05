package chess;

import java.util.Objects;

public class ChessPositionImpl implements ChessPosition {
    int column;
    int row;

    public ChessPositionImpl() {}
    public ChessPositionImpl(char column, int row) {
        this.column = (int)(column - 'a');
        this.row = row;
    }
    public ChessPositionImpl(int column, int row) {
        this.column = column;
        this.row = row;
    }
    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public void setPosition(char column, int row) {
        this.column = (int)(column - 'a');
        this.row = row;
    }
    @Override
    public void setPosition(int column, int row) {
        this.column = column;
        this.row = row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPositionImpl that = (ChessPositionImpl) o;
        return column == that.column && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }

    @Override
    public String toString() {
        return "(" + column + ", " + row + ")";
    }
}
