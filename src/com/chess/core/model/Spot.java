package com.chess.core.model;


import com.chess.pieces.Piece;

public class Spot {
    private Piece piece;
    private Character column;
    private Integer row;

    public Spot(Piece piece, Character column, Integer row) {
        this.piece = piece;
        this.column = column;
        this.row = row;
    }

    public Spot() {

    }

    public String getConcatinated() {
        return column + String.valueOf(row);
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Character getColumn() {
        return column;
    }

    public void setColumn(Character column) {
        this.column = column;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public boolean isValid(String position) {
        if ((position.charAt(0) >= 'a' && position.charAt(0) <= 'h') &&
                (position.charAt(1) >= '1' && position.charAt(1) <= '8')) {
            this.setColumn(position.charAt(0));
            this.setRow((int) (position.charAt(1) - '0'));
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Spot spot = (Spot) object;
        if (column != null ? !column.equals(spot.column) : spot.column != null) return false;
        return row != null ? row.equals(spot.row) : spot.row == null;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.column != null ? this.column.hashCode() : 0);
        hash = 53 * hash + this.row;
        return hash;
    }
}
