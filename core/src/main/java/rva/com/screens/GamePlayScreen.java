package rva.com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import rva.com.Main;
import rva.com.components.Ball;
import rva.com.components.Brick;
import rva.com.components.Paddle;
import rva.com.components.Wall;
import rva.com.services.GameResources;
import rva.com.services.GameSession;
import rva.com.services.GameSettings;

public class GamePlayScreen extends BaseScreen {
    private ShapeRenderer shapeRenderer;
    private GameSession gameSession;
    private BitmapFont font;
    private Array<Wall> walls;
    private World world;
    private Paddle paddle;
    private Ball ball;
    private Array<Brick> bricks;


    public GamePlayScreen(Main game) {
        super(game);
        world = new World(new Vector2(0, 0), true); // Без гравитации
        this.shapeRenderer = game.getShapeRenderer();
        this.gameSession = game.getGameSession();
        this.font = game.getFont();
        createWalls();
        createPaddle();
        createBall();
        createBricks();
        world.setContactListener(new GameContactListener(this));
    }

    private void createPaddle() {
        paddle = new Paddle(world, Gdx.graphics.getWidth() / 2, 30, this);
    }

    private void createBall() {
        ball = new Ball(world, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, this);
    }

    private void createWalls() {
        walls = new Array<>();
        // Левая стена
        walls.add(new Wall(world, 0, 0, 1, Gdx.graphics.getHeight(), "left"));
        // Верхняя стена
        walls.add(new Wall(world, 0, Gdx.graphics.getHeight() - 1, Gdx.graphics.getWidth(), 1, "top"));
        // Правая стена
        walls.add(new Wall(world, Gdx.graphics.getWidth() - 1, 0, 1, Gdx.graphics.getHeight(), "right"));
    }


    private void createBricks() {
        this.bricks = new Array<>();
        int brickWidth = this.gameSession.getBrickWidth() - 1;
        int brickHeight = this.gameSession.getBrickHeight() - 1;
        int spacing = 1;
        int random;

        for (int row = 0; row < GameSettings.BRICKS_LINE; row++) {
            for (int col = 0; col < GameSettings.BRICKS_IN_LINE; col++) {
                random = new Random().nextInt(GameResources.BRICKS.length);
                float x = col * (brickWidth + spacing);
                float y = gameSession.getLowBorder() +  row * (brickHeight + spacing);
                bricks.add(new Brick(world, x, y, brickWidth, brickHeight,random, this));
            }
        }
    }

    @Override
    public void show() {
        this.gameSession.resetGame(); // Сброс состояния игры
        this.ball.reset();
    }

    @Override
    public void render(float delta) {
        this.drawGameElements();
        camera.update();
        updateGameLogic(delta);
        checkGameEndConditions();
    }

    private void drawGameElements() {
        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Логика отрисовки игровых элементов
        // Пример: отрисовка платформы
        shapeRenderer.setColor(Color.BLUE);
        // Отрисовка игровых объектов
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Здесь логика отрисовки игрового поля, платформы, мяча, кирпичей
        shapeRenderer.end();

        // Отрисовка текста через SpriteBatch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Очки: " + gameSession.getScore(), 20, gameSession.getScreenHeight() - 20);
//        font.draw(batch, "Lives: " + gameSession.getLives(), 20, gameSession.getScreenHeight() - 50);
        font.draw(batch, "Жизни: " + gameSession.getLives(), gameSession.getScreenWidth() - 150, gameSession.getScreenHeight() - 20);
        this.paddle.draw(batch);
        this.ball.draw(batch);
        for (Brick brick: this.bricks) { brick.draw(batch);}
        batch.end();


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
//        if (gameSession.isGameOver()) {
////            game.setScreen(new GameOverScreen(game, gameSession.getScore()));
//            game.setScreen(game.getFinish());
//        } else if (gameSession.isLevelCompleted()) {
//            // Переход на следующий уровень или победа
////            game.setScreen(new GameOverScreen(game, gameSession.getScore(), true));
//            game.setScreen(game.getFinish());
//        }
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
        // world.step(delta, 6, 2);
        world.step(delta, 2, 1);
        paddle.update();
        ball.update();
        // Удаляем разрушенные кирпичи
        for (int i = bricks.size - 1; i >= 0; i--) {
            if (bricks.get(i).isDestroyed()) {
                world.destroyBody(bricks.get(i).getBody());
                bricks.removeIndex(i);
            }
        }

    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public Array<Brick> getBricks() {
        return bricks;
    }

    @Override
    public void dispose() {
        // Освобождение игровых ресурсов
    }
}
