package rva.com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import rva.com.Main;
import rva.com.managers.InputManager;
import rva.com.services.GameResources;
import rva.com.services.GameSettings;

public class GameOverScreen extends BaseScreen {
    private int finalScore;
    private boolean isVictory;
    private String message;
    BitmapFont font, whiteFont, titleFont, titleWhiteFont;
    Main game;
    // Текстуры и спрайты для картинок
    private Texture winnerTexture;
    private Texture loserTexture;
    private Sprite winnerSprite;
    private Sprite loserSprite;
    private InputManager inputManager;

    public GameOverScreen(Main game) {
        super(game);
        this.font = game.getSmallFont();
        this.whiteFont = game.getSmallWhiteFont();
        this.titleFont = game.getFont();
        this.titleWhiteFont = game.getWhiteFont();
        this.game = game;
        this.message = "";
        winnerTexture = new Texture(GameResources.WINNER_PATH);
        winnerSprite = new Sprite(winnerTexture);
        loserTexture = new Texture(GameResources.LOSER_PATH);
        loserSprite = new Sprite(loserTexture);
        positionSprites(game.getGameSession().getScreenWidth(), game.getGameSession().getScreenHeight());
        this.inputManager = new InputManager(game);

    }


    @Override
    public void show() {
       //  System.out.println("Game Over Screen shown. Score: " + finalScore);
        Gdx.input.setInputProcessor(inputManager);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameSettings.BACKGROUND_COLOR.r
                           ,GameSettings.BACKGROUND_COLOR.g
                           ,GameSettings.BACKGROUND_COLOR.b
                           ,GameSettings.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Рисуем картинку в зависимости от исхода
        if (isVictory && winnerSprite != null) { winnerSprite.draw(batch); }
        else if (!isVictory && loserSprite != null) { loserSprite.draw(batch);  }
//        font.setColor(Color.WHITE);
        titleWhiteFont.draw(batch, message, this.game.getGameSession().getxSettingsButton() + 1,  this.game.getGameSession().getTitleLine() - 1);
        titleFont.draw(batch, message, this.game.getGameSession().getxSettingsButton(),  this.game.getGameSession().getTitleLine());
//        font.setColor(Color.WHITE);
        whiteFont.draw(batch, "Очки: " + finalScore, this.game.getGameSession().getxSettingsButton() + 1
            ,this.game.getGameSession().getMinLineSettingsButton() + 4 * this.game.getGameSession().getHeightSettingsButton() - 1);
        font.draw(batch, "Очки: " + finalScore, this.game.getGameSession().getxSettingsButton()
            ,this.game.getGameSession().getMinLineSettingsButton() + 4 * this.game.getGameSession().getHeightSettingsButton());

        whiteFont.draw(batch, "Еще раз?",  this.game.getGameSession().getxSettingsButton() + 1
            , this.game.getGameSession().getMinLineSettingsButton() + 3 *  this.game.getGameSession().getHeightSettingsButton() - 1);
        font.draw(batch, "Еще раз?",  this.game.getGameSession().getxSettingsButton()
            , this.game.getGameSession().getMinLineSettingsButton() + 3 *  this.game.getGameSession().getHeightSettingsButton());

        whiteFont.draw(batch, "Нажмите ПРОБЕЛ",  this.game.getGameSession().getxSettingsButton() + 1
            ,  this.game.getGameSession().getMinLineSettingsButton() + 2 * this.game.getGameSession().getHeightSettingsButton() - 1);
        font.draw(batch, "Нажмите ПРОБЕЛ",  this.game.getGameSession().getxSettingsButton()
            ,  this.game.getGameSession().getMinLineSettingsButton() + 2 * this.game.getGameSession().getHeightSettingsButton());

        whiteFont.draw(batch, "Или долгое касание",  this.game.getGameSession().getxSettingsButton() + 1
            ,  this.game.getGameSession().getMinLineSettingsButton() + this.game.getGameSession().getHeightSettingsButton() - 1);
        font.draw(batch, "Или долгое касание",  this.game.getGameSession().getxSettingsButton()
            ,  this.game.getGameSession().getMinLineSettingsButton() + this.game.getGameSession().getHeightSettingsButton());
        batch.end();
//        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { game.setScreen(game.getMenu()); }
        inputManager.update();
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
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

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public void setVictory(boolean victory) {
        isVictory = victory;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public void resize(int width, int height) {
        // Обновляем позицию спрайтов при изменении размера окна
        positionSprites(width, height);
    }

    private void positionSprites(float screenWidth, float screenHeight) {
        if (winnerSprite != null) {
            winnerSprite.setPosition(
                (screenWidth - winnerSprite.getWidth()) / 2,
                (screenHeight - winnerSprite.getHeight()) / 2
            );
        }
        if (loserSprite != null) {
            loserSprite.setPosition(
                (screenWidth - loserSprite.getWidth()) / 2,
                (screenHeight - loserSprite.getHeight()) / 2
            );
        }
    }
    @Override
    public void dispose() {
        // Освобождаем текстуры, чтобы избежать утечек памяти
        if (winnerTexture != null) {
            winnerTexture.dispose();
            winnerTexture = null;
        }
        if (loserTexture != null) {
            loserTexture.dispose();
            loserTexture = null;
        }
    }
}
