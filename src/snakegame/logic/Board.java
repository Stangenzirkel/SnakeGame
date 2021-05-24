package snakegame.logic;

import java.util.*;

/**
 * Author: Yuri Buyanov
 * Date: 24/05/2021 16:05
 */
public class Board {
    private final int width, height;
    private Cell[][] board;
    private Map<Integer, Snake> snakes = new LinkedHashMap<>();

    public Board(int width, int height) {
        this.width = width;
        this.height = height;

        board = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[y][x] = new Cell(x, y);
            }
        }
    }

    public void clearBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[height][width].setType(CellType.EMPTY);
            }
        }
    }

    public void SOUTBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(getCell(x, y).getType());
                System.out.println();
            }
            System.out.println();
        }

    }

    public Cell getCell (int x, int y) {
        return board[y][x];
    }

    public void setCellType (int x, int y, CellType value) {
        getCell(x, y).setType(value);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addSnake(int x, int y, int id) {
        snakes.put(id, new Snake(x, y, this, id));
    }

    public List<Snake> getSnakesList() {
        return new ArrayList<>(snakes.values());
    }

    public Snake getSnake(int id) {
        return snakes.get(id);
    }

    public Map<Integer, Snake> getSnakes() {
        return snakes;
    }

    public void makeTurn() {
        for (Snake snake: getSnakesList()) {
            snake.snakeTarget();
            if (Math.random() > 0.2) {
                snake.moveTail();
            }
        }

        for (Snake snake: getSnakesList()) {
            if (snake.getTarget().getType() != CellType.SNAKE) {
                snake.moveHead();
            } else {
                snake.clear();
            }
        }
    }
}
