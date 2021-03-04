package ch.laurin.shakechess.model;

import java.util.HashMap;

public class ChessBoard {
    private HashMap<String, ChessField> board = new HashMap<>();

    public ChessBoard() {
        String[] columns = {"a", "b", "c", "d", "e", "f", "g", "h"};
        for (int columnCounter = 0; columnCounter < 8; columnCounter++) {
            for (int row = 1; row <= 8; row++) {
                board.put(columns[columnCounter]+row, new ChessField());
            }
        }
    }

    public ChessField getField(String field) {
        return board.get(field);
    }

    public HashMap<String, ChessField> getBoard() {
        return board;
    }
}
