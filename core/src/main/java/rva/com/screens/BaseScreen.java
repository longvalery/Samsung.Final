package rva.com.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import rva.com.Main;
import rva.com.services.GameSettings;

public abstract class BaseScreen implements GameScreen {
    protected Main game;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    public BaseScreen(Main game) {
        this.game = game;
        this.batch = game.getBatch();
        this.camera = game.getCamera();
    }

    public void handle() {};
    public void draw() {};
    public void update(float delta) {};

    public void resize(int width, int height) {
        this.game.getGameSession().setScreenWidth(Gdx.graphics.getWidth());
        this.game.getGameSession().setScreenHeight(Gdx.graphics.getHeight());
        this.game.getGameSession().calcSizes(GameSettings.BRICKS_LINE, GameSettings.BRICKS_IN_LINE);
        // Настраиваем камеру на точное соответствие пикселям экрана
        this.camera.setToOrtho(false, width, height);
    };

    public void render(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        this.update(delta);
        this.handle();
        this.draw();
                                     }
}
