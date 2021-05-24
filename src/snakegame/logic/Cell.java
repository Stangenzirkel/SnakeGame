package snakegame.logic;

/**
 * Author: Yuri Buyanov
 * Date: 24/05/2021 17:21
 */
public class Cell {
    private int x, y;
    private CellType type = CellType.EMPTY;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", type=" + type +
                '}';
    }
}
