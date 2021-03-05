package ch.laurin.shakechess;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.concurrent.atomic.AtomicBoolean;

import ch.laurin.shakechess.activities.GameActivity;
import ch.laurin.shakechess.model.ChessBoard;
import ch.laurin.shakechess.model.ChessField;
import ch.laurin.shakechess.model.ChessPiece;
import ch.laurin.shakechess.services.ApiService;

public class GameController {
    private ChessBoard chessBoard;
    private GameActivity view;
    private boolean whiteToMove = true;
    private boolean gameIsFinished = false;
    private String moves = "";

    private Board board;

    private ApiService apiService;

    public GameController(GameActivity view, ApiService apiService) {
        chessBoard = new ChessBoard();
        startGame(view, apiService);
        loadStartingPosition();
    }

    public GameController(GameActivity view, ApiService apiService, ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        startGame(view, apiService);
    }

    private void startGame(GameActivity view, ApiService apiService) {
        board = new Board();
        this.view = view;
        this.apiService = apiService;
        apiService.setGameController(this);
        view.updateViewChessBoard(chessBoard);
    }

    public void makeMove(String firstPosition, String secondPosition) {
        if (!gameIsFinished) {
            if (isMoveLegal(firstPosition, secondPosition)) {
                moves = moves + firstPosition + secondPosition;
                ChessPiece pieceToMove = chessBoard.getField(firstPosition).getPiece();
                if (whiteToMove) {
                    if (pieceToMove.getColor().equals("white")) {
                        chessBoard.getField(firstPosition).setFieldToEmpty();
                        chessBoard.getField(secondPosition).setPiece(pieceToMove);
                        view.updateViewChessBoard(chessBoard);
                        whiteToMove = false;
                        apiService.getComputerMove(moves);
                    }
                } else {
                    System.out.println("Computer move: " + firstPosition + " " + secondPosition);
                    if (pieceToMove.getColor().equals("black")) {
                        chessBoard.getField(firstPosition).setFieldToEmpty();
                        chessBoard.getField(secondPosition).setPiece(pieceToMove);
                        view.updateViewChessBoard(chessBoard);
                        whiteToMove = true;
                    }
                }
                updateLegalMovesBoard(firstPosition, secondPosition);
                checkIfSpecialSituation();
            }
        } else {
            view.showText("Game is finished please start a new one");
        }
    }

    public void takeBackLastMove() {
        if (whiteToMove) {
            if(moves.length() >= 8 ) {
                String whitesLastMove = moves.substring(moves.length() - 8, moves.length() - 4);
                String blacksLastMove = moves.substring(moves.length() - 4);
                moves = moves.substring(0, moves.length() - 8);
                board.undoMove();
                board.undoMove();
                ChessField firstFieldWhite = chessBoard.getBoard().get(whitesLastMove.substring(0, whitesLastMove.length() - 2));
                ChessField secondFieldWhite = chessBoard.getBoard().get(whitesLastMove.substring(whitesLastMove.length() - 2));
                firstFieldWhite.setPiece(secondFieldWhite.getPiece());
                secondFieldWhite.setFieldToEmpty();
                ChessField firstFieldBlack = chessBoard.getBoard().get(blacksLastMove.substring(0, blacksLastMove.length() - 2));
                ChessField secondFieldBlack = chessBoard.getBoard().get(blacksLastMove.substring(blacksLastMove.length() - 2));
                firstFieldBlack.setPiece(secondFieldBlack.getPiece());
                secondFieldBlack.setFieldToEmpty();
            }
        } else {
            view.showText("Please wait for your opponent to play");
        }
        view.updateViewChessBoard(chessBoard);
    }

    private void updateLegalMovesBoard(String firstPosition, String secondPosition) {
        Square fromSquare = Square.fromValue(firstPosition.toUpperCase());
        Square toSquare = Square.fromValue(secondPosition.toUpperCase());
        board.doMove(new Move(fromSquare, toSquare));
    }

    private boolean isMoveLegal(String firstPosition, String secondPosition) {
        AtomicBoolean moveIsLegal = new AtomicBoolean(false);
        board.legalMoves().forEach(move -> {
            if (move.toString().equals(firstPosition + secondPosition)) {
                moveIsLegal.set(true);
                view.showText("");
            }
        });
        if (!moveIsLegal.get()) {
            view.showText("This is not a legal move");
        }
        return moveIsLegal.get();
    }

    private void checkIfSpecialSituation() {
        if (board.isDraw()) {
            view.showText("Draw");
            gameIsFinished = true;
        } else if (board.isMated()) {
            view.showText("Checkmate");
            gameIsFinished = true;
        } else if (board.isKingAttacked()) {
            view.showText("Check");
        }
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public boolean isGameFinished() {
        return gameIsFinished;
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
