package rva.com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import rva.com.Main;

public class GameOverScreen extends BaseScreen {
    private int finalScore;
    private boolean isVictory;
    private String message;
    BitmapFont font;
    Main game;

    public GameOverScreen(Main game) {
        super(game);
        // , int score
//        this(game, score, false);
        this.font = game.getFont();
        this.game = game;
    }

//    public GameOverScreen(Main game, int score, boolean victory) {
//        super(game);
//        this.finalScore = score;
//        this.isVictory = victory;
//        this.message = victory ? "YOU WIN!" : "GAME OVER";
//    }

    @Override
    public void show() {
        System.out.println("Game Over Screen shown. Score: " + finalScore);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.setColor(Color.RED);
        font.draw(batch, message, 200, 400);
        font.setColor(Color.WHITE);
        font.draw(batch, "Final Score: " + finalScore, 200, 350);
        font.draw(batch, "Press SPACE to return to menu", 200, 300);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(game.getMenu());
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void handle() {

    }

    @Override
    public void draw() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void dispose() {}
}
