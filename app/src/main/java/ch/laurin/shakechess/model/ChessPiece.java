package ch.laurin.shakechess.model;

public class ChessPiece {
    private String pieceName;
    private String color;

    public ChessPiece(String pieceName, String color) {
        this.pieceName = pieceName;
        this.color = color;
    }

    public String getPieceName() {
        return pieceName;
    }

    public String getColor() {
        return color;
    }
}
