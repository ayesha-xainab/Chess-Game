public abstract class Piece {
    private String color;
    private int row;
    private int col;

    public Piece(String color, int row, int col) {
        this.color = color;
        this.row = row;
        this.col = col;
    }

    public String getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public boolean isAllyPiece(int row, int col, Piece[][] board) {
        return board[row][col] != null && board[row][col].getColor().equals(this.color);
    }

    public abstract boolean isValidMove(int destRow, int destCol, Piece[][] board);

    public abstract String getImagePath();

    public abstract String getType();
}
