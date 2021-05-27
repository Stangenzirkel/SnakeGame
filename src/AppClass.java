import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import snakegame.logic.*;

import java.util.Timer;
import java.util.TimerTask;

public class AppClass extends Application {
    private final int boardSizeX = 30, boardSizeY = 20;
    private int cellSize, indentSize = 5;
    private int windowSizeX, windowSizeY;
    private int borderSizeMin = 20, borderSizeX = 20, borderSizeY = 20;
    private long timerSpeed = 200;

    private Canvas canvas;
    private Board board;

    private Timer timer = new Timer();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Snake game");
        stage.setFullScreen(true);
        windowSizeX = (int) Screen.getPrimary().getBounds().getWidth();
        windowSizeY = (int) Screen.getPrimary().getBounds().getHeight();
        stage.setResizable(false);

        Group root = new Group();
        Scene scene = new Scene(root, windowSizeX, windowSizeY);
        scene.setFill(Color.WHITE);
        cellSize = Math.min((windowSizeX - borderSizeMin * 2 - indentSize * (boardSizeX - 1)) / boardSizeX,
                (windowSizeY - borderSizeMin * 2 - indentSize * (boardSizeY - 1)) / boardSizeY);

        borderSizeX = (windowSizeX - cellSize * boardSizeX - indentSize * (boardSizeX - 1)) / 2;
        borderSizeY = (windowSizeY - cellSize * boardSizeY - indentSize * (boardSizeY - 1)) / 2;

        canvas = new Canvas();
        canvas.setWidth(windowSizeX);
        canvas.setHeight(windowSizeY);

        canvas.setFocusTraversable(true);
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.UP && board.getSnake().getDirection() != Direction.DOWN) {
                board.getSnake().setDirection(Direction.UP);
            } else if (keyEvent.getCode() == KeyCode.RIGHT && board.getSnake().getDirection() != Direction.LEFT) {
                board.getSnake().setDirection(Direction.RIGHT);
            } else if (keyEvent.getCode() == KeyCode.DOWN && board.getSnake().getDirection() != Direction.UP) {
                board.getSnake().setDirection(Direction.DOWN);
            } else if (keyEvent.getCode() == KeyCode.LEFT && board.getSnake().getDirection() != Direction.RIGHT) {
                board.getSnake().setDirection(Direction.LEFT);
            }

            // update();
        });

        root.getChildren().add(canvas);

        stage.setScene(scene);

        newGame();
        timer.schedule(new TimerTask() {public void run() { update(); }}, timerSpeed);
        drawBoard();
        stage.show();
    }

    private void newGame() {
        board = new Board(boardSizeX, boardSizeY);
        board.addFood();
        board.setSnake(3, 3);
    }

    private void drawBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        for (int y = 0; y < boardSizeY; y++) {
            for (int x = 0; x < boardSizeX; x++) {
                gc.setFill(board.getCell(x, y).getType().getColor());
                gc.fillRect(borderSizeX + (indentSize + cellSize) * x,
                        borderSizeY + (indentSize + cellSize) * y,
                        cellSize,
                        cellSize);
            }
        }
    }

    private void update() {
        timer.cancel();

        board.makeTurn();
        drawBoard();

        if (board.isGameOver()) {
            newGame();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {public void run() { update(); }}, timerSpeed);
    }
}
