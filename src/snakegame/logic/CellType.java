package snakegame.logic;

import javafx.scene.paint.Color;

public enum CellType {
    EMPTY {
        @Override
        public String toString() {
            return "EMPTY";
        }

        public Color getColor() {
            return Color.rgb(20, 20, 20);
        }
    },
    FOOD {
        @Override
        public String toString() {
            return "FOOD";
        }

        public Color getColor() {
            return Color.SILVER;
        }
    },
    SNAKE {
        @Override
        public String toString() {
            return "SNAKE";
        }

        public Color getColor() {
            return Color.WHITE;
        }
    },

    WALL {
        @Override
        public String toString() {
            return "WALL";
        }

        public Color getColor() {
            return Color.WHITE;
        }
    };

    public abstract Color getColor();
}
