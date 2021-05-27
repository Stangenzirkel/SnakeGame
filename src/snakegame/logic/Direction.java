package snakegame.logic;

public enum Direction {
    UP {public int getX() {return 0;} public int getY() {return -1;} public Direction getReverse() {return Direction.DOWN;}},
    RIGHT {public int getX() {return 1;} public int getY() {return 0;} public Direction getReverse() {return Direction.LEFT;}},
    DOWN {public int getX() {return 0;} public int getY() {return 1;} public Direction getReverse() {return Direction.UP;}},
    LEFT {public int getX() {return -1;} public int getY() {return 0;} public Direction getReverse() {return Direction.RIGHT;}};

    public abstract int getX();
    public abstract int getY();
    public abstract Direction getReverse();
}
