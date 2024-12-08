public class Knight extends Piece {
    public Knight(String color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean isValidMove(int destRow, int destCol, Piece[][] board) {
        int rowDiff = Math.abs(destRow - getRow());
        int colDiff = Math.abs(destCol - getCol());

        // Knight moves in an "L" shape: 2 squares in one direction, 1 square perpendicular
        return ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) &&
                !isAllyPiece(destRow, destCol, board);
    }

    @Override
    public String getImagePath() {
        return getClass().getResource(getColor() + "Knight.png").toExternalForm();
    }

    @Override
    public String getType() {
        return "Knight";
    }
}
