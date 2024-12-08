public class Queen extends Piece {
    public Queen(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
        int rowDiff = Math.abs(destRow - getRow());
        int colDiff = Math.abs(destCol - getCol());

        // Checking if it's moving one square in any direction 
        if (rowDiff <= 1 && colDiff <= 1) {
            return !isAllyPiece(destRow, destCol, board);
        }

        // Moving diagonally 
        if (rowDiff == colDiff) {
            return isPathClearDiagonal(destRow, destCol, board) && !isAllyPiece(destRow, destCol, board);
        }

        // Moving straight
        if (destRow == getRow() || destCol == getCol()) {
            return isPathClearStraight(destRow, destCol, board) && !isAllyPiece(destRow, destCol, board);
        }

        return false;
    }

    // Checking if the diagonal path is clear
    private boolean isPathClearDiagonal(int destRow, int destCol, Piece[][] board) {
        int rowStep = Integer.compare(destRow, getRow());
        int colStep = Integer.compare(destCol, getCol());

        int currentRow = getRow() + rowStep;
        int currentCol = getCol() + colStep;

        while (currentRow != destRow && currentCol != destCol) {
            if (board[currentRow][currentCol] != null) {
                return false; // Path is blocked
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true;
    }

    // Checking if the straight path is clear
    private boolean isPathClearStraight(int destRow, int destCol, Piece[][] board) {
        int rowStep = destRow == getRow() ? 0 : Integer.compare(destRow, getRow());
        int colStep = destCol == getCol() ? 0 : Integer.compare(destCol, getCol());

        int currentRow = getRow() + rowStep;
        int currentCol = getCol() + colStep;

        while (currentRow != destRow || currentCol != destCol) {
            if (board[currentRow][currentCol] != null) {
                return false; 
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true;
    }

    @Override
    public String getImagePath() {
        return getClass().getResource(getColor() + "Queen.png").toExternalForm();
    }

    @Override
    public String getType() {
        return "Queen";
    }
}
