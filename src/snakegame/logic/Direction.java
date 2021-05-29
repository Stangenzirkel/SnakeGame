package snakegame.logic;

public enum Direction {
    UP {public int getX() {return 0;} public int getY() {return -1;} public Direction getReverse() {return Direction.DOWN;}},
    RIGHT {public int getX() {return 1;} public int getY() {return 0;} public Direction getReverse() {return Direction.LEFT;}},
    DOWN {public int getX() {return 0;} public int getY() {return 1;} public Direction getReverse() {return Direction.UP;}},
    LEFT {public int getX() {return -1;} public int getY() {return 0;} public Direction getReverse() {return Direction.RIGHT;}};

    public abstract int getX();
    public abstract int getY();
    public abstract Direction getReverse();
    public static Direction getDirection(String str, Direction elseVar) {
        if (str.equals("UP"))
            return Direction.UP;
        else if (str.equals("RIGHT"))
            return Direction.RIGHT;
        else if (str.equals("DOWN"))
            return Direction.DOWN;
        else if (str.equals("LEFT"))
            return Direction.LEFT;
        else
            return elseVar;
    }
}
