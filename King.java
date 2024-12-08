public class King extends Piece {
    public King(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
        int rowDiff = Math.abs(destRow - getRow());
        int colDiff = Math.abs(destCol - getCol());
        
        // King can move one square in any direction
        return rowDiff <= 1 && colDiff <= 1 && !isAllyPiece(destRow, destCol, board);
    }

    @Override
    public String getImagePath() {
        return getClass().getResource(getColor() + "King.png").toExternalForm();
    }

    @Override
    public String getType() {
        return "King";
    }
}
