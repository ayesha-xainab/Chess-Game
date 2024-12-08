import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ChessBoard {
    private Piece[][] board;
    private GridPane boardPane;
    private boolean whiteTurn;
    private double cellSize = 60; // Default cell size on the board
    private List<Rectangle> highlights = new ArrayList<>();
    private Rectangle[][] tiles = new Rectangle[8][8]; 

    public ChessBoard() {
        board = new Piece[8][8];
        boardPane = new GridPane();
        initializeEmptyBoard();
        whiteTurn = true; // White starts the game
    }

    // Initialize the empty board layout
    private void initializeEmptyBoard() {
        boardPane.getChildren().clear(); 
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Rectangle square = createSquare(row, col);
                tiles[row][col] = square; 
                boardPane.add(square, col, row);
            }
        }
    }

    private Rectangle createSquare(int row, int col) {
        Rectangle square = new Rectangle();
        square.setFill((row + col) % 2 == 0 ? Color.WHITE : Color.LIGHTBLUE);

        // Bind the size of the rectangle to the cellSize property
        square.widthProperty().bind(Bindings.createDoubleBinding(() -> cellSize, boardPane.widthProperty(), boardPane.heightProperty()));
        square.heightProperty().bind(square.widthProperty()); // Maintain a square shape

        return square;
    }

    // Initialize the board with all pieces
    public void initializeBoard() {
        // Adding pawns to the board
        for (int col = 0; col < 8; col++) {
            setPiece(1, col, new Pawn("black", 1, col));
            setPiece(6, col, new Pawn("white", 6, col));
        }

        // Adding other pieces
        String[] order = {"Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook"};
        for (int col = 0; col < 8; col++) {
            try {
                setPiece(0, col, (Piece) Class.forName(order[col]).getConstructor(String.class, int.class, int.class)
                        .newInstance("black", 0, col));
                setPiece(7, col, (Piece) Class.forName(order[col]).getConstructor(String.class, int.class, int.class)
                        .newInstance("white", 7, col));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public GridPane getBoardPane() {
        return boardPane;
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public void setPiece(int row, int col, Piece piece) {
        board[row][col] = piece;
        updateBoardView(row, col, piece);
    }

    private void updateBoardView(int row, int col, Piece piece) {
        // Remove any existing Piece at this position
        removeNodeAt(row, col);

        // Add a new image for the piece, if it exists
        if (piece != null) {
            ImageView imageView = new ImageView(new Image(piece.getImagePath()));

            // Bind the size of the piece's image to the cellSize
            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() -> cellSize, boardPane.widthProperty(), boardPane.heightProperty()));
            imageView.fitHeightProperty().bind(imageView.fitWidthProperty()); // Maintain square proportions

            boardPane.add(imageView, col, row);
        }
    }

    //Remove the piece from previous location
    private void removeNodeAt(int row, int col) {
        Node toRemove = null;
        for (Node node : boardPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                if (node instanceof ImageView) { 
                    toRemove = node;
                    break;
                }
            }
        }
        if (toRemove != null) {
            boardPane.getChildren().remove(toRemove);
        }
    }

    // Highlights the possible moves of a piece when clicked
    public void highlightPossibleMoves(Piece piece) {
        clearHighlights();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (piece.isValidMove(row, col, board)) {
                    Rectangle highlight = new Rectangle(cellSize, cellSize, Color.BLACK);
                    highlight.setOpacity(0.3);
                    highlights.add(highlight);
                    boardPane.add(highlight, col, row);
                }
            }
        }
    }
    public void clearHighlights() {
        for (Rectangle highlight : highlights) {
            boardPane.getChildren().remove(highlight);
        }
        highlights.clear();
    }

    // Keeping a check for Turns
    public boolean isCorrectTurn(Piece piece) {
        return (whiteTurn && piece.getColor().equals("white")) || (!whiteTurn && piece.getColor().equals("black"));
    }
    
    // Checking for Legal Moves
    public boolean movePiece(int sourceRow, int sourceCol, int destRow, int destCol) {
        Piece piece = getPiece(sourceRow, sourceCol);
        if (piece != null && piece.isValidMove(destRow, destCol, board)) {
            String currentPlayerColor = whiteTurn ? "white" : "black";

            // Prevent moving into check 
            if (isMoveLeavingKingInCheck(sourceRow, sourceCol, destRow, destCol, currentPlayerColor)) {
                showIllegalMoveAlert();
                return false; 
            }

            // Move the piece
            piece.setPosition(destRow, destCol);
            setPiece(destRow, destCol, piece);
            setPiece(sourceRow, sourceCol, null);

            // Check for Check-mate or Stale-mate
            String opponentColor = whiteTurn ? "black" : "white";
            if (isKingInCheck(opponentColor)) {
                if (!hasValidMoves(opponentColor)) {
                    // Check-mate
                    showGameOverAlert((whiteTurn ? "White" : "Black") + " wins by Checkmate!");
                        // Reset the game state
                        whiteTurn = true; 
                        board = new Piece[8][8]; 
                        initializeEmptyBoard(); 
                        initializeBoard(); 
                } 
                else {
                    // Check
                    showCheckAlert();
                }
            } 
            
            else if (!hasValidMoves(opponentColor)) {
                // Stale-mate
                showGameOverAlert("Draw (Stalemate) - No valid moves left.");
                
                // Reset the game state
                whiteTurn = true; 
                board = new Piece[8][8]; 
                initializeEmptyBoard(); 
                initializeBoard(); 
            }

            // Switch turns
            whiteTurn = !whiteTurn;
            clearHighlights();
            return true;
        }
        return false;
    }

    // Alert that prevents Illegal moves
    private void showIllegalMoveAlert() {
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Illegal Move");
            alert.setHeaderText(null);
            alert.setContentText("This move leaves your King in check! Please try again.");
            alert.showAndWait();
        } else {
            Platform.runLater(this::showIllegalMoveAlert);
        }
    }

    // Check Alert
    private void showCheckAlert() {
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Check");
            alert.setHeaderText(null);
            alert.setContentText("Check! The opponent's King is in danger.");
            alert.showAndWait();
        } else {
            Platform.runLater(this::showCheckAlert);
        }
    }

    // Game Over Alert after Check-mate
    private void showGameOverAlert(String message) {
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        } else {
            Platform.runLater(() -> showGameOverAlert(message));
        }
    }

    // Checking for the Valid Moves
    public boolean hasValidMoves(String color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor().equals(color)) {
                    for (int destRow = 0; destRow < 8; destRow++) {
                        for (int destCol = 0; destCol < 8; destCol++) {
                            if (piece.isValidMove(destRow, destCol, board)) {
                                Piece originalDest = board[destRow][destCol];
                                board[destRow][destCol] = piece;
                                board[row][col] = null;

                                boolean inCheck = isKingInCheck(color);

                                board[row][col] = piece;
                                board[destRow][destCol] = originalDest;

                                if (!inCheck) {
                                    return true; 
                                }
                            }
                        }
                    }
                }
            }
        }
        return false; 
    }

 // Checking if the move puts the current player's King in check
    public boolean isMoveLeavingKingInCheck(int sourceRow, int sourceCol, int destRow, int destCol, String color) {
        Piece tempPiece = getPiece(sourceRow, sourceCol);
        Piece targetPiece = getPiece(destRow, destCol);

        setPiece(destRow, destCol, tempPiece);
        setPiece(sourceRow, sourceCol, null);

        // Check if the King is in check after the move
        boolean isInCheck = isKingInCheck(color);

        // Undo the move
        setPiece(sourceRow, sourceCol, tempPiece);
        setPiece(destRow, destCol, targetPiece);

        return isInCheck; 
    }

 // Checking if the given player's king is in check
    public boolean isKingInCheck(String color) {
        int kingRow = -1, kingCol = -1;

        // Find the king 
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && piece.getColor().equals(color)) {
                    kingRow = row;
                    kingCol = col;
                    break;
                }
            }
        }

        if (kingRow == -1 || kingCol == -1) return false; //This is never possible

        // Check if any opposing piece can attack the king
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && !piece.getColor().equals(color) && piece.isValidMove(kingRow, kingCol, board)) {
                    return true; 
                }
            }
        }
        return false; 
    }
 
}