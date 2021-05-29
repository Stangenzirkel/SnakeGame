package snakegame.logic;

import java.util.ArrayDeque;

public class Snake {
    private Direction direction = Direction.LEFT;
    private Direction newDirection = direction;
    private final ArrayDeque<Cell> body = new ArrayDeque<>();
    private final Board board;
    private Cell target;

    public Snake(int x, int y, int length, Board board) {
        for (int i = 0; i < length; i++) {
            body.addFirst(board.getCell(x + i, y));
            board.getCell(x + i, y).setType(CellType.SNAKE);
        }
        this.board = board;
    }

    public int getLength() {
        return body.size();
    }

    public Cell [] getBody() {
        return body.toArray(Cell[]::new);
    }

    public Cell getHead() {
        return body.getLast();
    }

    public Cell getTail() {
        return body.getFirst();
    }

    public Cell getTarget() {
        return target;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.newDirection = direction;
    }

    public void applyDirection() {
        if (direction.getReverse() != newDirection) {
            direction = newDirection;
        }
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
