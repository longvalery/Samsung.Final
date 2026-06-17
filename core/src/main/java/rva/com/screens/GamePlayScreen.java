package rva.com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import rva.com.Main;
import rva.com.components.Ball;
import rva.com.components.Brick;
import rva.com.components.Paddle;
import rva.com.components.Wall;
import rva.com.managers.AudioManager;
import rva.com.services.GameResources;
import rva.com.services.GameSession;
import rva.com.services.GameSettings;
import rva.com.uix.ImageView;

public class GamePlayScreen extends BaseScreen {
    private ShapeRenderer shapeRenderer;
    private GameSession gameSession;
    private BitmapFont font;
    private Array<Wall> walls;
    private World world;
    private Paddle paddle;
    private Ball ball;
    private Array<Brick> bricks;
    private float timeScale = 0.4f; // 1.0f !!
    private float timeStep = 1/60f;   // базовый шаг
    private float accumulator = 0f;
    private ImageView topBlackoutView;
    private Array<ImageView> lives;
    private float yLine;
    private GlyphLayout layout;



    public GamePlayScreen(Main game) {
        super(game);
        world = new World(new Vector2(0, 0), true); // Без гравитации
        this.shapeRenderer = game.getShapeRenderer();
        this.gameSession = game.getGameSession();
        this.gameSession.setLives(3);
        this.font = game.getFont();
        createWalls();
        createPaddle();
        createBall();
        createBricks();

        this.topBlackoutView = new ImageView(0, this.gameSession.getScreenHeight(),  GameResources.TOP_IMAGE_PATH);
        this.topBlackoutView.setY(this.gameSession.getScreenHeight() - this.topBlackoutView.getHeight());
        createDemoLives();
        this.layout = new GlyphLayout();
        this.layout.setText(this.font, "Очки: 100");
        this.yLine = this.topBlackoutView.getY() + (this.topBlackoutView.getHeight() + this.layout.height) / 2;
        world.setContactListener(new GameContactListener(this));

    }

    private void createDemoLives() {
        this.lives = new Array<>();
        for (int i=0; i < this.gameSession.getLives(); i++) {
            ImageView image = new ImageView(this.gameSession.getScreenWidth() / 2 + i * 10
                , this.gameSession.getScreenHeight() - this.topBlackoutView.getHeight() / 2 - this.gameSession.getBallHeight() / 2
                ,  GameResources.BALL_PATH);
            image.setWidth(this.gameSession.getBallWidth());
            image.setHeight(this.gameSession.getBallHeight());
            image.getSprite().setSize(this.gameSession.getBallWidth(), this.gameSession.getBallHeight());
            this.lives.add(image);
        }
    }

    private void createPaddle() {
        paddle = new Paddle(world, Gdx.graphics.getWidth() / 2, 30, this);
    }

    public AudioManager getAudio() {  return game.getAudioManager(); }

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
        int brickWidth = this.gameSession.getBrickWidth();
        int brickHeight = this.gameSession.getBrickHeight();
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
//        Gdx.gl.glClearColor(0.78f, 0.43f, 0.03f, 1);
        Gdx.gl.glClearColor(GameSettings.BACKGROUND_COLOR.r
            ,GameSettings.BACKGROUND_COLOR.g
            ,GameSettings.BACKGROUND_COLOR.b
            ,GameSettings.BACKGROUND_COLOR.a);
//        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
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
        this.topBlackoutView.drawTexture(batch);
        font.draw(batch, "Очки: " + gameSession.getScore(), gameSession.getxSettingsButton(), this.yLine);
//        font.draw(batch, "Lives: " + gameSession.getLives(), 20, gameSession.getScreenHeight() - 50);
        // font.draw(batch, "Жизни: " + gameSession.getLives(), gameSession.getScreenWidth() - 150, gameSession.getScreenHeight() - 20);
        this.paddle.draw(batch);
        this.ball.draw(batch);
        for (Brick brick: this.bricks) { brick.draw(batch);}
        for (int i=0; i < this.gameSession.getLives(); i++) { this.lives.get(i).draw(batch); }
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
        if (gameSession.isGameOver()) {
            game.getFinish().setFinalScore(this.gameSession.getScore());
            game.getFinish().setMessage("Проигрыш");
            game.getFinish().setVictory(false);
            if (this.gameSession.getScore() > 0) { game.getRecordsTable().addResult(this.gameSession.getScore()); }
            game.setScreen(game.getFinish());
        }
        else if (this.bricks.size == 0) {
            game.getFinish().setFinalScore(this.gameSession.getScore());
            game.getFinish().setMessage("Победа");
            game.getFinish().setVictory(true);
            if (this.gameSession.getScore() > 0) { game.getRecordsTable().addResult(this.gameSession.getScore()); }
            game.setScreen(game.getFinish());
        }

//        else if (gameSession.isLevelCompleted()) {
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
//        Ускорить игру: увеличьте delta (например, delta * 2.0f).
//        Замедлить игру: уменьшите delta (например, delta * 0.5f).

        accumulator += delta;
        float scaledStep = timeStep * timeScale;
        while (accumulator >= scaledStep) {
            world.step(scaledStep, 6, 2);
            accumulator -= scaledStep;
            paddle.update();
            ball.update();
            // Удаляем разрушенные кирпичи
            for (int i = bricks.size - 1; i >= 0; i--) {
                if (bricks.get(i).isDestroyed()) {
                    world.destroyBody(bricks.get(i).getBody());
                    bricks.removeIndex(i);
                }
            }

            if ((ball.getY() < 0) || (ball.getX() < 0) || ball.getX() > gameSession.getScreenWidth()) {
                ball.dispose();
                gameSession.setLives(gameSession.getLives() - 1);
                createBall();
                                                                                                      }

        }
//        delta = delta * 0.2f;
//        world.step(delta, 10, 8);
////        world.step(delta, 6, 2);
////

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
        if (this.topBlackoutView != null) {topBlackoutView.dispose();}

    }
}
