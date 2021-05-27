package snakegame.logic;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class Board {
    private final int width, height;
    private final Cell[][] board;
    private Snake snake;
    private boolean gameOver = false;

    private Board(Cell [][] data) {
        assert(data.length > 0);
        assert(data[0].length > 0);

        this.width = data[0].length;
        this.height = data.length;
        this.board = data;
    }

    public static Board createBoard(int width, int height) {
        Cell [][] data = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = new Cell(x, y);
            }
        }

        return new Board(data);
    }

    public static Board createBoard(String fileName) throws FileNotFoundException, IOException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        List<String> stringList = new ArrayList<>();

        while((line = br.readLine()) != null){
            if (line.contains(".") || line.contains("#") || line.contains("s")) {
                stringList.add(line);
            }
        }

        ArrayList<String> copyOfStringList = new ArrayList<String>(stringList);

        Collections.sort(copyOfStringList, Comparator.comparingInt(String::length));
        Collections.reverse(copyOfStringList);

        int width = copyOfStringList.get(0).length();
        int height = copyOfStringList.size();

        Cell [][] data = new Cell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = new Cell(x, y);
            }
        }

        for (int y = 0; y < height; y++) {
            String row = stringList.get(y).concat(new String(new char[width - stringList.get(y).length()]).replace("\0", "."));
            for (int x = 0; x < width; x++) {
                char sym = row.charAt(x);
                if (sym == '#') {
                    data[y][x].setType(CellType.WALL);
                }

            }
        }

        return new Board(data);
    }

    public void SOUTBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(getCell(x, y).getType());
                System.out.print("\t");
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
        snake.applyDirection();
        snake.snakeTarget();

        if ((snake.getTarget().getType() != CellType.SNAKE || snake.getTarget() == snake.getTail()) && snake.getTarget().getType() != CellType.WALL) {
            if (snake.getTarget().getType() != CellType.FOOD) {
                snake.moveTail();
            } else {
                addFood();
            }
            
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
