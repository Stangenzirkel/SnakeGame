package snakegame.logic;

import java.util.ArrayDeque;

/**
 * Author: Yuri Buyanov
 * Date: 24/05/2021 17:18
 */
public class Snake {
    private Direction direction = Direction.LEFT;
    private ArrayDeque<Cell> body = new ArrayDeque<>();
    private Board board;
    private final int id;
    private Cell target;

    public Snake(int x, int y, Board board, int id) {
        body.add(board.getCell(x, y));
        board.getCell(x, y).setType(CellType.SNAKE);
        this.board = board;
        this.id = id;
    }

    public Cell getHead() {
        return body.getLast();
    }

    public Cell getTarget() {
        return target;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void snakeTarget() {
        int newX = (getHead().getX() + direction.getX()) % board.getWidth();
        newX = newX < 0 ? board.getWidth() + newX : newX;
        int newY = (getHead().getY() + direction.getY()) % board.getHeight();
        newY = newY < 0 ? board.getHeight() + newY : newY;

        target =  board.getCell(newX, newY);
    }

    public void moveHead() {
        body.addLast(target);
        target.setType(CellType.SNAKE);
    }

    public void moveTail() {
        body.removeFirst().setType(CellType.EMPTY);
    }

    public void clear() {
        for (Cell cell: body) {
            cell.setType(CellType.EMPTY);
        }

        board.getSnakes().remove(id);
    }
}
