import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import snakegame.logic.Board;
import snakegame.logic.Cell;
import snakegame.logic.CellType;
import snakegame.logic.Direction;

import java.io.File;
import java.util.Random;
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
    private boolean onPause = false;

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
            } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                newGame();
            } else if (keyEvent.getCode() == KeyCode.SPACE) {
                GraphicsContext gc = canvas.getGraphicsContext2D();

                if (onPause) {
                    unPause(gc);

                } else {
                    setOnPause(gc);
                }
            }

            // update();
        });

        root.getChildren().add(canvas);
        stage.setScene(scene);
        newGame();
        stage.show();
    }

    private void setOnPause(GraphicsContext gc, String mainText) {
        if (!onPause) {
            drawBoard();
            timer.cancel();
            onPause = true;
        }
        gc.setFill(Color.WHITE);
        gc.setTextBaseline(VPos.CENTER);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.loadFont("file:./static/19187.ttf", 150));
        gc.fillText(mainText, (windowSizeX - textZoneWidth) / 2,
                windowSizeY / 2);

        gc.setFont(Font.loadFont("file:./static/19187.ttf", 30));
        gc.fillText("press SPACE to start", (windowSizeX - textZoneWidth) / 2,
                windowSizeY / 2 + 70);
    }

    private void setOnPause(GraphicsContext gc) {
        setOnPause(gc, "PAUSED");
    }

    private void unPause(GraphicsContext gc) {
        if (!onPause) {
            return;
        }
        Timer waiter0 = new Timer();
        waiter0.schedule(new TimerTask() {
            @Override
            public void run() {
                drawBoard();
                gc.setFill(Color.WHITE);
                gc.setTextBaseline(VPos.CENTER);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setFont(Font.loadFont("file:./static/19187.ttf", 150));
                gc.fillText("3", (windowSizeX - textZoneWidth) / 2,
                        windowSizeY / 2);
            }
        }, 0);

        Timer waiter1 = new Timer();
        waiter1.schedule(new TimerTask() {
            @Override
            public void run() {
                drawBoard();
                gc.setFill(Color.WHITE);
                gc.setTextBaseline(VPos.CENTER);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setFont(Font.loadFont("file:./static/19187.ttf", 150));
                gc.fillText("2", (windowSizeX - textZoneWidth) / 2,
                        windowSizeY / 2);
            }
        }, 1000);

        Timer waiter2 = new Timer();
        waiter2.schedule(new TimerTask() {
            @Override
            public void run() {
                drawBoard();
                gc.setFill(Color.WHITE);
                gc.setTextBaseline(VPos.CENTER);
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setFont(Font.loadFont("file:./static/19187.ttf", 150));
                gc.fillText("1", (windowSizeX - textZoneWidth) / 2,
                        windowSizeY / 2);
            }
        }, 2000);

        Timer waiter3 = new Timer();
        waiter3.schedule(new TimerTask() {
            @Override
            public void run() {
                drawBoard();
                timer = new Timer();
                timer.schedule(new TimerTask() {public void run() { update(); }}, timerSpeedArray[timerSpeedLevel]);
                onPause = false;
            }
        }, 3000);
    }

    private void newGame() {
        try {
            File dir = new File("./static/levels"); //path указывает на директорию
            File[] arrFiles = dir.listFiles();
            String randomFilename = arrFiles[new Random().nextInt(arrFiles.length)].getCanonicalPath();
            System.out.println(randomFilename);
            board = Board.createBoard(randomFilename);
            
            board.addFood();
            board.addSnake(3, 3);

            boardSizeX = board.getWidth();
            boardSizeY = board.getHeight();

            cellSize = Math.min((windowSizeX - borderSizeMin * 2 - indentSize * (boardSizeX - 1) - textZoneWidth) / boardSizeX,
                    (windowSizeY - borderSizeMin * 2 - indentSize * (boardSizeY - 1)) / boardSizeY);

            borderSizeX = (windowSizeX - cellSize * boardSizeX - indentSize * (boardSizeX - 1) - textZoneWidth) / 2;
            borderSizeY = (windowSizeY - cellSize * boardSizeY - indentSize * (boardSizeY - 1)) / 2;

        } catch (Exception e) {
            System.out.println(e);
        }

//        timer = new Timer();
//        timer.schedule(new TimerTask() {public void run() { update(); }}, timerSpeedArray[timerSpeedLevel]);
        drawBoard();
        setOnPause(canvas.getGraphicsContext2D(), "NEW GAME");
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
        drawText(Integer.toString(board.getSnake().getLength() * 5), 1);
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
                borderSizeMin + num * (TEXTSIZE + TEXTINDENT));
        

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

    private void fillAll(Color color) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void gameOverFunc() throws InterruptedException {
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
