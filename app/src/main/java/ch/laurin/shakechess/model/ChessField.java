package ch.laurin.shakechess.model;

public class ChessField {
    private ChessPiece piece;
    private boolean fieldIsEmpty = true;

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
        fieldIsEmpty = false;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public void setFieldToEmpty() {
        fieldIsEmpty = true;
    }

    public boolean isEmpty() {
        return fieldIsEmpty;
    }
}
