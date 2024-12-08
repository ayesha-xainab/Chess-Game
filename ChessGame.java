import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChessGame extends Application {
    private ChessBoard chessBoard;
    private Piece selectedPiece;

    public static void main(String[] args) {
        launch(args);
    }

    //Primary Stage Setup
    @Override
    public void start(Stage primaryStage) {
        chessBoard = new ChessBoard();
        chessBoard.initializeBoard();

        BorderPane root = new BorderPane();
        root.setCenter(chessBoard.getBoardPane());

        Scene scene = new Scene(root, 480, 480);
        primaryStage.setTitle("Chess Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        setupClickHandlers();
    }

    //Player's Moves
    private void setupClickHandlers() {
        chessBoard.getBoardPane().setOnMouseClicked(event -> {
            int row = (int) (event.getY() / chessBoard.getBoardPane().getHeight() * 8);
            int col = (int) (event.getX() / chessBoard.getBoardPane().getWidth() * 8);

            if (selectedPiece == null) {
                Piece piece = chessBoard.getPiece(row, col);
                if (piece != null && chessBoard.isCorrectTurn(piece)) {
                    selectedPiece = piece;
                    chessBoard.highlightPossibleMoves(piece);
                }
            } else {
                if (chessBoard.movePiece(selectedPiece.getRow(), selectedPiece.getCol(), row, col)) {
                    selectedPiece = null; 
                } else {
                    selectedPiece = null; 
                    chessBoard.clearHighlights();
                }
            }
        });
    }
}

