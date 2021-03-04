package ch.laurin.shakechess;

import ch.laurin.shakechess.activities.GameActivity;
import ch.laurin.shakechess.model.ChessBoard;
import ch.laurin.shakechess.model.ChessPiece;
import ch.laurin.shakechess.services.ApiService;

public class GameController {
    private ChessBoard chessBoard;
    private GameActivity view;
    private boolean whiteToMove = true;
    private String moves = "";

    private ApiService apiService;

    public GameController(GameActivity view, ApiService apiService) {
        chessBoard = new ChessBoard();
        this.view = view;
        this.apiService = apiService;
        apiService.setGameController(this);
        loadStartingPosition();
    }

    public void makeMove(String firstPosition, String secondPosition) {
        moves = moves + firstPosition + secondPosition;
        ChessPiece pieceToMove = chessBoard.getField(firstPosition).getPiece();
        if(whiteToMove) {
            if(pieceToMove.getColor().equals("white")) {
                chessBoard.getField(firstPosition).setFieldToEmpty();
                chessBoard.getField(secondPosition).setPiece(pieceToMove);
                view.updateViewChessBoard(chessBoard);
                whiteToMove = false;
                apiService.getComputerMove(moves);
            }
        } else {
            if(pieceToMove.getColor().equals("black")) {
                chessBoard.getField(firstPosition).setFieldToEmpty();
                chessBoard.getField(secondPosition).setPiece(pieceToMove);
                view.updateViewChessBoard(chessBoard);
                whiteToMove = true;
            }
        }
    }


    public void loadStartingPosition() {
        chessBoard.getField("a1").setPiece(new ChessPiece("T", "white"));
        chessBoard.getField("h1").setPiece(new ChessPiece("T", "white"));
        chessBoard.getField("a8").setPiece(new ChessPiece("T", "black"));
        chessBoard.getField("h8").setPiece(new ChessPiece("T", "black"));
        chessBoard.getField("b1").setPiece(new ChessPiece("S", "white"));
        chessBoard.getField("g1").setPiece(new ChessPiece("S", "white"));
        chessBoard.getField("b8").setPiece(new ChessPiece("S", "black"));
        chessBoard.getField("g8").setPiece(new ChessPiece("S", "black"));
        chessBoard.getField("c1").setPiece(new ChessPiece("L", "white"));
        chessBoard.getField("f1").setPiece(new ChessPiece("L", "white"));
        chessBoard.getField("c8").setPiece(new ChessPiece("L", "black"));
        chessBoard.getField("f8").setPiece(new ChessPiece("L", "black"));
        chessBoard.getField("d1").setPiece(new ChessPiece("D", "white"));
        chessBoard.getField("d8").setPiece(new ChessPiece("D", "black"));
        chessBoard.getField("e1").setPiece(new ChessPiece("K", "white"));
        chessBoard.getField("e8").setPiece(new ChessPiece("K", "black"));
        chessBoard.getField("a2").setPiece(new ChessPiece("B", "white"));
        chessBoard.getField("b2").setPiece(new ChessPiece("B", "white"));
        chessBoard.getField("c2").setPiece(new ChessPiece("B", "white"));
        chessBoard.getField("d2").setPiece(new ChessPiece("B", "white"));
        chessBoard.getField("e2").setPiece(new ChessPiece("B", "white"));
        chessBoard.getField("f2").setPiece(new ChessPiece("B", "white"));
        chessBoard.getField("g2").setPiece(new ChessPiece("B", "white"));
        chessBoard.getField("h2").setPiece(new ChessPiece("B", "white"));
        chessBoard.getField("a7").setPiece(new ChessPiece("B", "black"));
        chessBoard.getField("b7").setPiece(new ChessPiece("B", "black"));
        chessBoard.getField("c7").setPiece(new ChessPiece("B", "black"));
        chessBoard.getField("d7").setPiece(new ChessPiece("B", "black"));
        chessBoard.getField("e7").setPiece(new ChessPiece("B", "black"));
        chessBoard.getField("f7").setPiece(new ChessPiece("B", "black"));
        chessBoard.getField("g7").setPiece(new ChessPiece("B", "black"));
        chessBoard.getField("h7").setPiece(new ChessPiece("B", "black"));

        view.updateViewChessBoard(chessBoard);
    }

}
