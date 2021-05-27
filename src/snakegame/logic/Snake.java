package snakegame.logic;

import java.util.ArrayDeque;

public class Snake {
    private Direction direction = Direction.LEFT;
    private final ArrayDeque<Cell> body = new ArrayDeque<>();
    private final Board board;
    private Cell target;

    public Snake(int x, int y, Board board) {
        body.add(board.getCell(x, y));
        board.getCell(x, y).setType(CellType.SNAKE);
        this.board = board;
    }

    public Cell getHead() {
        return body.getLast();
    }

    public Cell getTarget() {
        return target;
    }

    public Direction getDirection() {
        return direction;
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
    }
}
