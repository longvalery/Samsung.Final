package rva.com.services;

import com.badlogic.gdx.graphics.Pixmap;

import java.lang.reflect.Array;
import java.util.Arrays;

public class GameResources {
    public static final String MAIN_FONT_PATH = "fonts/Roboto-Bold.ttf";
    public static final String TITLE_FONT_PATH = "fonts/impact.ttf";

    public static final String BACKGROUND_PATH = "textures/wall.png";
    public static final String PADDLE_PATH = "textures/paddle.png";
    public static final String BALL_PATH = "textures/ball.png";
    public static final String BUTTON_PATH = "textures/button_background_long_brown.png";
    public static final String  TOP_IMAGE_PATH = "textures/blackout_top.png";
    public static final String  BONBON_IMAGE_PATH = "textures/bonbon.png";
    public static final String BOMB_PATH = "textures/bomb.png";
    public static final String WINNER_PATH = "icons/winner.png";
    public static final String LOSER_PATH = "icons/loser.png";

    public static final String BACKGROUND_MUSIC_PATH = "media/background_music.mp3";
    public static final String DESTROY_SOUND_PATH = "media/explosion.mp3";
    public static final String SHOOT_SOUND_PATH = "media/paddle.mp3";

    public static final String GAME_NAME = "Разбей здесь ВСЁ !!!";
    public static final String COPYRIGHT = "(c) RVA, 2026";


    public static final String[] SETTINGS_ITEMS = new String[] {
        "Музыка",
        "Звук",
        "Рекорды",
        "Назад"
    };

    public static final String[] MENU_ITEMS = new String[] {
        "ИГРАТЬ",
        "НАСТРОЙКИ",
        "ВЫХОД"
    };
    public static final String[] BRICKS = new String[] {
          "textures/blue_brick.png"
        , "textures/braun_brick.png"
        , "textures/cyan_brick.png"
        , "textures/gray_brick.png"
        , "textures/green_brick.png"
        , "textures/lime_brick.png"
        , "textures/orange_brick.png"
        , "textures/purple_brick.png"
        , "textures/red_brick.png"
        , "textures/yellow_brick.png"
    };
    public static final String[] BROKEN_BRICKS = new String[] {
        "textures/blue_brick_broken.png"
        , "textures/braun_brick_broken.png"
        , "textures/cyan_brick_broken.png"
        , "textures/gray_brick_broken.png"
        , "textures/green_brick_broken.png"
        , "textures/lime_brick_broken.png"
        , "textures/orange_brick_broken.png"
        , "textures/purple_brick_broken.png"
        , "textures/red_brick_broken.png"
        , "textures/yellow_brick_broken.png"
    };


}
