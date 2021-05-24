import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import snakegame.logic.Board;
import snakegame.logic.CellType;
import snakegame.logic.Direction;
import snakegame.logic.Snake;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Yuri Buyanov
 * Date: 24/05/2021 15:24
 */
public class AppClass extends Application {
    private final int boardSizeX = 30, boardSizeY = 20;
    private final int cellSize = 20, indentSize = 2;
    private final int windowSizeX = indentSize + (cellSize + indentSize) * boardSizeX,
            windowSizeY = indentSize + (cellSize + indentSize) * boardSizeY;

    private final int playerId = 1;
    private long timerSpeed = 200;

    private Canvas canvas;
    private Board board;

    private Timer timer = new Timer();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        newGame();
        timer.schedule(new TimerTask() {public void run() { update(); }}, timerSpeed);

        Group root = new Group();
        Scene scene = new Scene(root, windowSizeX, windowSizeY);
        scene.setFill(Color.WHITE);

        canvas = new Canvas();
        canvas.setWidth(windowSizeX);
        canvas.setHeight(windowSizeY);

        canvas.setFocusTraversable(true);
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.UP) {
                    board.getSnake(playerId).setDirection(Direction.UP);
                } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                    board.getSnake(playerId).setDirection(Direction.RIGHT);
                } else if (keyEvent.getCode() == KeyCode.DOWN) {
                    board.getSnake(playerId).setDirection(Direction.DOWN);
                } else if (keyEvent.getCode() == KeyCode.LEFT) {
                    board.getSnake(playerId).setDirection(Direction.LEFT);
                }
                
                // update();
            }
        });

        root.getChildren().add(canvas);

        stage.setScene(scene);
        stage.setTitle("Snake game");

        drawBoard();
        stage.show();
    }

    private void newGame() {
        board = new Board(boardSizeX, boardSizeY);
        board.addSnake(5, 5, 1);
        board.addFood();
    }

    private void drawBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        for (int y = 0; y < boardSizeY; y++) {
            for (int x = 0; x < boardSizeX; x++) {
                gc.setFill(board.getCell(x, y).getType().getColor());
                gc.fillRect(indentSize + (indentSize + cellSize) * x,
                        indentSize + (indentSize + cellSize) * y,
                        cellSize,
                        cellSize);
            }
        }
    }

    private void update() {
        timer.cancel();

        board.makeTurn();
        drawBoard();

        if (board.getSnake(playerId) == null) {
            newGame();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {public void run() { update(); }}, timerSpeed);
    }
}
