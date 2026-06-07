package rva.com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import rva.com.Main;
import rva.com.services.GameSession;

public class GamePlayScreen extends BaseScreen {
    private ShapeRenderer shapeRenderer;
    private GameSession gameSession;
    private BitmapFont font;


    public GamePlayScreen(Main game) {
        super(game);
        this.shapeRenderer = game.getShapeRenderer();
        this.gameSession = game.getGameSession();
        this.font = game.getFont();
    }

    @Override
    public void show() {
        System.out.println("Game Play Screen shown");
        gameSession.resetGame(); // Сброс состояния игры
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // Отрисовка игровых объектов
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Здесь логика отрисовки игрового поля, платформы, мяча, кирпичей
        drawGameElements();

        shapeRenderer.end();

        // Отрисовка текста через SpriteBatch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Score: " + gameSession.getScore(), 20, gameSession.getScreenHeight() - 20);
        font.draw(batch, "Lives: " + gameSession.getLives(), 20, gameSession.getScreenHeight() - 50);
        batch.end();

        updateGameLogic(delta);
        checkGameEndConditions();
    }

    private void drawGameElements() {
        // Логика отрисовки игровых элементов
        // Пример: отрисовка платформы
        shapeRenderer.setColor(Color.BLUE);
//        shapeRenderer.rect(
//            gameSession.getPaddleX(),
//            gameSession.getPaddleY(),
//            gameSession.getPaddleWidth(),
//            gameSession.getPaddleHeight()
//        );
    }

    private void updateGameLogic(float delta) {
        // Обновление позиций объектов, проверка столкновений и т. д.
        gameSession.update(delta);
    }

    private void checkGameEndConditions() {
        if (gameSession.isGameOver()) {
//            game.setScreen(new GameOverScreen(game, gameSession.getScore()));
            game.setScreen(game.getFinish());
        } else if (gameSession.isLevelCompleted()) {
            // Переход на следующий уровень или победа
//            game.setScreen(new GameOverScreen(game, gameSession.getScore(), true));
            game.setScreen(game.getFinish());
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

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
    public void dispose() {
        // Освобождение игровых ресурсов
    }
}
