public class Rook extends Piece {
    public Rook(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
        // Check if the move is in a straight line
        if (destRow != getRow() && destCol != getCol()) {
            return false;
        }

        // Determine the direction of movement
        int rowStep = Integer.compare(destRow, getRow()); 
        int colStep = Integer.compare(destCol, getCol()); 

        int currentRow = getRow() + rowStep;
        int currentCol = getCol() + colStep;

        // Checking for obstacles
        while (currentRow != destRow || currentCol != destCol) {
            if (board[currentRow][currentCol] != null) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }

        return !isAllyPiece(destRow, destCol, board);
    }


    @Override
    public String getImagePath() {
        return getClass().getResource(getColor() + "Rook.png").toExternalForm();
    }

    @Override
    public String getType() {
        return "Rook";
    }
}
