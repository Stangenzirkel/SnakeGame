package snakegame.logic;

import java.util.*;

public class Board {
    private final int width, height;
    private final Cell[][] board;
    private Snake snake;
    private boolean gameOver = false;

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

    public void SOUTBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(getCell(x, y).getType());
                System.out.print(" ");
            }
            System.out.println();
        }

    }

    public void setSnake(int x, int y) {
        snake = new Snake(x, y, this);
    }

    public Snake getSnake() {
        return snake;
    }

    public Cell getCell (int x, int y) {
        return board[y][x];
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void makeTurn() {
        snake.snakeTarget();
        if (snake.getTarget().getType() != CellType.FOOD) {
            snake.moveTail();
        } else {
            addFood();
        }

        if (snake.getTarget().getType() != CellType.SNAKE) {
            snake.moveHead();
        } else {
            gameOver = true;
        }
    }

    public boolean addFood(int x, int y) {
        if (getCell(x, y).getType() != CellType.EMPTY) {
            return false;
        }

        getCell(x, y).setType(CellType.FOOD);
        return true;
    }

    public boolean addFood() {
        Cell newFood = getFreeCells().get(new Random().nextInt(getFreeCells().size()));
        return addFood(newFood.getX(), newFood.getY());
    }

    public List<Cell> getFreeCells() {
        ArrayList<Cell> output = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (getCell(x, y).getType() == CellType.EMPTY) {
                    output.add(getCell(x, y));
                }
            }
        }

        return output;
    }
}
