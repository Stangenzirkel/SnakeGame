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
import java.util.concurrent.TimeUnit;

public class AppClass extends Application {
    private final int indentSize = 5;
    private final int borderSizeMin = 30;
    private final int textZoneWidth = 200;
    private long timerSpeed = 200;

    private int windowSizeX, windowSizeY;
    private int boardSizeX, boardSizeY;
    private int borderSizeX, borderSizeY, cellSize;

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
        System.out.println("newGame");
        try {
            board = Board.createBoard("./levels/1.txt");
            board.addFood();
            board.setSnake(3, 3);

            boardSizeX = board.getWidth();
            boardSizeY = board.getHeight();

            cellSize = Math.min((windowSizeX - borderSizeMin * 2 - indentSize * (boardSizeX - 1) - textZoneWidth) / boardSizeX,
                    (windowSizeY - borderSizeMin * 2 - indentSize * (boardSizeY - 1)) / boardSizeY);

            borderSizeX = (windowSizeX - cellSize * boardSizeX - indentSize * (boardSizeX - 1) - textZoneWidth) / 2;
            borderSizeY = (windowSizeY - cellSize * boardSizeY - indentSize * (boardSizeY - 1)) / 2;

            System.out.println(boardSizeX);
            System.out.println(boardSizeY);
            System.out.println(cellSize);

        } catch (Exception e) {
            System.out.println(e);
        }
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

        if (board.isGameOver()) {
            try {
                gameOverFunc();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        drawBoard();
        timer = new Timer();
        timer.schedule(new TimerTask() {public void run() { update(); }}, timerSpeed);
    }

    private void gameOverFunc() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        newGame();
    }
}
