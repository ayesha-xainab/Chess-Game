public class Pawn extends Piece {
    public Pawn(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
        int rowDiff = destRow - getRow();
        int colDiff = Math.abs(destCol - getCol());

        if (!isWithinBounds(destRow, destCol)) {
            return false;
        }

        // Get the pawn's direction
        int direction = getColor().equals("white") ? -1 : 1;

        // Single step forward
        if (colDiff == 0 && rowDiff == direction && board[destRow][destCol] == null) {
            return true;
        }

        // Two steps forward on the first move
        int startRow = getColor().equals("white") ? 6 : 1;
        if (getRow() == startRow && colDiff == 0 && rowDiff == 2 * direction
                && board[getRow() + direction][getCol()] == null && board[destRow][destCol] == null) {
            return true;
        }

     // Diagonal capture
        if (colDiff == 1 && rowDiff == direction && board[destRow][destCol] != null
                && !isAllyPiece(destRow, destCol, board)) {
            // Check for promotion
            if ((getColor().equals("white") && destRow == 0) || (getColor().equals("black") && destRow == 7)) {
                promoteToQueen(destRow, destCol, board);
            }
            return true;
        }

        return false;
    }

    private void promoteToQueen(int row, int col, Piece[][] board) {
        board[row][col] = new Queen(getColor(), row, col);
        
    }


    @Override
    public String getImagePath() {
        return getClass().getResource(getColor() + "Pawn.png").toExternalForm();
    }

    @Override
    public String getType() {
        return "Pawn";
    }
}
