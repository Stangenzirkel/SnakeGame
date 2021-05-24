package snakegame.logic;

/**
 * Author: Yuri Buyanov
 * Date: 24/05/2021 17:41
 */
public enum Direction {
    UP {public int getX() {return 0;} public int getY() {return -1;}},
    RIGHT {public int getX() {return 1;} public int getY() {return 0;}},
    DOWN {public int getX() {return 0;} public int getY() {return 1;}},
    LEFT {public int getX() {return -1;} public int getY() {return 0;}};

    public abstract int getX();
    public abstract int getY();
}
