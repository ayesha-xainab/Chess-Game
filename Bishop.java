public class Bishop extends Piece {
    public Bishop(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
        int rowDiff = Math.abs(destRow - getRow());
        int colDiff = Math.abs(destCol - getCol());

        // Bishop moves diagonally
        if (rowDiff == colDiff && !isAllyPiece(destRow, destCol, board)) {
            return isPathClearDiagonal(destRow, destCol, board);
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
                return false; 
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true;
    }

    @Override
    public String getImagePath() {
        return getClass().getResource(getColor() + "Bishop.png").toExternalForm();
    }

    @Override
    public String getType() {
        return "Bishop";
    }
}
