package Controller;

import java.awt.*;
import java.util.Random;


public class Settings {
    private static final Random rand = new Random();

    public static boolean isTesting = false;

    /////////////////
    // GAME SETTINGS
    /////////////////
    private static final int GAME_ROUNDS = 20; // -1 for infinite

    private static final int POINTS_TO_WIN = 20; // -1 for infinite

    public static int game_rounds() {
        if (isTesting) return -1;
        return GAME_ROUNDS;
    }

    public static int points_to_win() {
        if (isTesting) return -1;
        return POINTS_TO_WIN;
    }

    public static boolean generate_random_map = false;

    //////////////////
    // BOARD SETTINGS
    //////////////////
    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;

    public static final Color board_color = new Color(231, 224, 210);

    public static int board_circles_radius() {
        return WINDOW_HEIGHT / 10;
    }

    public static int board_character_height() {
        return WINDOW_HEIGHT / 10;
    }

    public static final Color water_source_color = new Color(29, 106, 215);
    public static final Color cistern_color = new Color(248, 255, 0);
    public static final Color pump_color = new Color(255, 135, 0);
    public static final Color pipe_color = new Color(38, 38, 38);
    public static final Color sticky_pipe_color = new Color(161, 126, 90);
    public static final Color slippery_pipe_color = new Color(179, 182, 0);
    public static final Color saboteur_color = new Color(173, 0, 0);
    public static final Color mechanic_color = new Color(0, 121, 0);


    /////////////////
    // PUMP SETTINGS
    /////////////////
    public static int pump_roundsUntilBreakdown_MIN = 5;
    public static int pump_roundsUntilBreakdown_MAX = 20;

    public static int pump_roundsUntilBreakdown() {
        if (isTesting) return 6;
        return randomBetween(pump_roundsUntilBreakdown_MIN, pump_roundsUntilBreakdown_MAX);
    }

    private static final int PUMP_TANK_SIZE = 10;

    public static int pump_tankSize() {
        if (isTesting) return 2;
        return PUMP_TANK_SIZE;
    }

    /////////////////
    // PIPE SETTINGS
    /////////////////
    private static final int PIPE_SLIPPERY = 5;

    public static int pipe_slippery() {
        if (isTesting) return 4;
        return PIPE_SLIPPERY;
    }

    private static final int PIPE_STICKY = 5;

    public static int pipe_sticky() {
        if (isTesting) return 4;
        return PIPE_STICKY;
    }

    public static int pipe_breakable() {
        return 2;
    }

    /////////////////////
    // CISTERN SETTINGS
    /////////////////////
    public static int cistern_roundsUntilNew_MIN = 0;
    public static int cistern_roundsUntilNew_MAX = 10;

    public static int cistern_roundsUntilNew() {
        if (isTesting) return 3;
        return randomBetween(cistern_roundsUntilNew_MIN, cistern_roundsUntilNew_MAX);
    }

    ///////////////////////////////////////////////

    // Helper functions
    private static int randomBetween(int min, int max) {
        return rand.nextInt(max - min) + min;
    }
}
