import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import snakegame.logic.*;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AppClass extends Application {
    private final int indentSize = 5;
    private final int borderSizeMin = 30;
    private final int textZoneWidth = 350;

    private int windowSizeX, windowSizeY;
    private int boardSizeX, boardSizeY;
    private int borderSizeX, borderSizeY, cellSize;

    private Canvas canvas;
    private Board board;

    private Timer timer = new Timer();
    private int timerSpeedLevel = 3;
    private final long [] timerSpeedArray= {50L, 70L, 100L, 150L, 200L};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(javafx.scene.text.Font.getFamilies());
        stage.setTitle("Snake game");
        stage.setFullScreen(true);
        windowSizeX = (int) Screen.getPrimary().getBounds().getWidth();
        windowSizeY = (int) Screen.getPrimary().getBounds().getHeight();
        stage.setResizable(false);

        Group root = new Group();
        Scene scene = new Scene(root, windowSizeX, windowSizeY);
        scene.setFill(Color.WHITE);

        canvas = new Canvas();
        canvas.setWidth(windowSizeX);
        canvas.setHeight(windowSizeY);

        canvas.setFocusTraversable(true);
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP) {
                board.getSnake().setDirection(Direction.UP);
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                board.getSnake().setDirection(Direction.RIGHT);
            } else if (keyEvent.getCode() == KeyCode.DOWN) {
                board.getSnake().setDirection(Direction.DOWN);
            } else if (keyEvent.getCode() == KeyCode.LEFT) {
                board.getSnake().setDirection(Direction.LEFT);
            } else if (keyEvent.getCode() == KeyCode.DIGIT0 && timerSpeedLevel > 0) {
                timerSpeedLevel--;
            } else if (keyEvent.getCode() == KeyCode.DIGIT9 && timerSpeedLevel < timerSpeedArray.length - 1) {
                timerSpeedLevel++;
            }

            // update();
        });

        root.getChildren().add(canvas);
        stage.setScene(scene);
        newGame();
        stage.show();
    }

    private void newGame() {
        System.out.println("newGame");
        try {
            board = Board.createBoard("./static/levels/1.txt");
            board.addFood();
            board.setSnake(3, 3);

            boardSizeX = board.getWidth();
            boardSizeY = board.getHeight();

            cellSize = Math.min((windowSizeX - borderSizeMin * 2 - indentSize * (boardSizeX - 1) - textZoneWidth) / boardSizeX,
                    (windowSizeY - borderSizeMin * 2 - indentSize * (boardSizeY - 1)) / boardSizeY);

            borderSizeX = (windowSizeX - cellSize * boardSizeX - indentSize * (boardSizeX - 1) - textZoneWidth) / 2;
            borderSizeY = (windowSizeY - cellSize * boardSizeY - indentSize * (boardSizeY - 1)) / 2;

        } catch (Exception e) {
            System.out.println(e);
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {public void run() { update(); }}, timerSpeedArray[timerSpeedLevel]);
        drawBoard();
    }

    private void drawBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        for (int y = 0; y < boardSizeY; y++) {
            for (int x = 0; x < boardSizeX; x++) {
                gc.setFill(board.getCell(x, y).getType().getColor());
                // Боже храни костыли
                if (board.getCell(x, y).getType() == CellType.WALL) {
                    int wallIndentSize = 4;
                    gc.fillRect(borderSizeX + (indentSize + cellSize) * x,
                            borderSizeY + (indentSize + cellSize) * y,
                            cellSize,
                            (cellSize - wallIndentSize) / 2);

                    gc.fillRect(borderSizeX + (indentSize + cellSize) * x,
                            borderSizeY + (indentSize + cellSize) * y + (cellSize - wallIndentSize) / 2 + wallIndentSize,
                            cellSize,
                            (cellSize - wallIndentSize) / 2);

                } else {
                    gc.fillRect(borderSizeX + (indentSize + cellSize) * x,
                            borderSizeY + (indentSize + cellSize) * y,
                            cellSize,
                            cellSize);
                }
            }
        }

        drawText("Score:", 0);
        drawText(Integer.toString(board.getSnake().getLength()), 1);
        drawText("Game speed: ", 2);
        drawText(new String(new char[timerSpeedArray.length - timerSpeedLevel]).replace("\0", "|"), 3);
    }

    private void drawText(String line, int num) {
        int TEXTSIZE = 50, TEXTINDENT = 0;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.setFont(Font.loadFont("file:./static/19187.ttf", TEXTSIZE));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.fillText(line, borderSizeX * 2 + (indentSize + cellSize) * boardSizeX,
                borderSizeY + num * (TEXTSIZE + TEXTINDENT));
        

    }

    private void update() {
        timer.cancel();

        board.makeTurn();

        if (board.isGameOver()) {
            try {
                gameOverFunc();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        else {
            drawBoard();
            timer = new Timer();
            timer.schedule(new TimerTask() {public void run() { update(); }}, timerSpeedArray[timerSpeedLevel]);
        }
    }

    private void blackAll(Color color) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void gameOverFunc() throws InterruptedException {
        System.out.println("gameOverFunc");
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // gc.setFill(Color.BLACK);
        for (int i = 0; i < 5; i++) {
            gc.setFill(CellType.EMPTY.getColor());
            // blackAll(CellType.EMPTY.getColor());
            for (Cell cell: board.getSnake().getBody()) {
                gc.fillRect(borderSizeX + (indentSize + cellSize) * cell.getX(),
                        borderSizeY + (indentSize + cellSize) * cell.getY(),
                        cellSize,
                        cellSize);
            }
            TimeUnit.MILLISECONDS.sleep(200);

            gc.setFill(CellType.SNAKE.getColor());
            // blackAll(CellType.SNAKE.getColor());
            for (Cell cell: board.getSnake().getBody()) {
                gc.fillRect(borderSizeX + (indentSize + cellSize) * cell.getX(),
                        borderSizeY + (indentSize + cellSize) * cell.getY(),
                        cellSize,
                        cellSize);
            }
            TimeUnit.MILLISECONDS.sleep(200);
        }
        newGame();
    }
}
