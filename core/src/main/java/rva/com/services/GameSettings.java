package rva.com.services;


import com.badlogic.gdx.graphics.Color;

public class GameSettings {
    public static final float SCALE = 0.1f;
    public static final int SCREEN_WIDTH = 480;
    public static final int SCREEN_HEIGHT = 600;

    public static final int BRICKS_LINE = 5;
    public static final int BRICKS_IN_LINE = 7;
    public static final Color BACKGROUND_COLOR = new Color(189f/255f, 83f/255f, 44f/255f, 1f);
    public static final int MAX_RECORDS = 5;
    public static final float ROTATION = 1.2f;

    public static final int NUM_PARTICLES = 60; // количество частиц
    public static final float BLAST_POWER = 150f; // сила взрыва

    public static final float  TIME_STEP =  1/60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;

    public static final int MAX_SUB_STEPS = 5;


}
