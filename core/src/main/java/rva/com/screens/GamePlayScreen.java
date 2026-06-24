package rva.com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import rva.com.Main;
import rva.com.components.Ball;
import rva.com.components.Bomb;
import rva.com.components.BonBon;
import rva.com.components.Brick;
import rva.com.components.Paddle;
import rva.com.components.Wall;
import rva.com.managers.AudioManager;
import rva.com.managers.ExplosionManager;
import rva.com.services.CustomerTimer;
import rva.com.services.GameResources;
import rva.com.services.GameSession;
import rva.com.services.GameSettings;
import rva.com.services.GameState;
import rva.com.uix.IconButtonView;
import rva.com.uix.ImageView;

public class GamePlayScreen extends BaseScreen {
    private ShapeRenderer shapeRenderer;
    private GameSession gameSession;
    private BitmapFont font;
    private Array<Wall> walls;
    private World world;
    private Paddle paddle;
//    private Ball ball;
    private Array<Brick> bricks;
    private float accumulator = 0f;
    private ImageView topBlackoutView;
    private Array<ImageView> lives;
    private float yLine;
    private GlyphLayout layout;
    private Array<BonBon> bonbons;
    private CustomerTimer timer;
    private GameState state;
    private ExplosionManager explosionManager;
    private Array<Bomb> bombs;
    private Main game;
    private String remainder;
    private Array<Ball> balls;
    private int iconSize;
    private IconButtonView exitButton;


    public GamePlayScreen(Main game) {
        super(game);
        this.game = game;
        this.bombs = new Array<>();
        this.balls = new Array<>();
        this.walls = new Array<>();
        this.bonbons = new Array<>();
        this.lives = new Array<>();
        this.bricks = new Array<>();
        this.layout = new GlyphLayout();
        world = new World(new Vector2(0, 0), true); // Без гравитации
        this.shapeRenderer = game.getShapeRenderer();
        this.gameSession = game.getGameSession();
        this.gameSession.setLives(3);
        this.font = game.getFont();
        createWalls();

        this.topBlackoutView = new ImageView(0, this.gameSession.getScreenHeight(),  GameResources.TOP_IMAGE_PATH);
        this.topBlackoutView.setY(this.gameSession.getScreenHeight() - this.topBlackoutView.getHeight());
        this.topBlackoutView.getSprite().setSize(this.gameSession.getScreenWidth(), this.topBlackoutView.getHeight());
        this.topBlackoutView.setWidth(this.gameSession.getScreenWidth());
        createDemoLives();
        this.iconSize = (int) (this.topBlackoutView.getHeight() / 2.0f);
        this.exitButton = new IconButtonView(
            this.gameSession.getScreenWidth() * 2.0f / 3.0f - 2 * iconSize
            , this.gameSession.getScreenHeight() - this.topBlackoutView.getHeight() + iconSize / 2.0f
            , iconSize, GameResources.ICON_EXIT_PATH);


        this.layout.setText(this.font, "Очки: 100");
        this.yLine = this.topBlackoutView.getY() + (this.topBlackoutView.getHeight() + this.layout.height) / 2;

        this.timer = new CustomerTimer(20000);  // 20 секунд в миллисекундах
        this.state = GameState.NOTHING;
        this.remainder = "";
        world.setContactListener(new GameContactListener(this));
        this.explosionManager = new ExplosionManager(this.world);
//        world.setVelocityThreshold(Float.MAX_VALUE);

    }
    private void createDemoLives() {
        for (int i=0; i < this.gameSession.getLives(); i++) {
            ImageView image = new ImageView(this.gameSession.getScreenWidth() * 2.0f / 3.0f + i * 10
                , this.gameSession.getScreenHeight() - this.topBlackoutView.getHeight() / 2.0f - this.gameSession.getBallHeight() / 2.0f
                ,  GameResources.BALL_PATH);
            image.setWidth(this.gameSession.getBallWidth());
            image.setHeight(this.gameSession.getBallHeight());
            image.getSprite().setSize(this.gameSession.getBallWidth(), this.gameSession.getBallHeight());
            this.lives.add(image);
        }
    }
    private void createPaddle() {
        paddle = new Paddle(this.getWorld(), Gdx.graphics.getWidth() / 2.0f, 30, this);
    }
    public AudioManager getAudio() {  return game.getAudioManager(); }
    private void createBall() {
        balls.add(new Ball(this.getWorld(),Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f, this));
    }
    private void createWalls() {
        // Левая стена
        walls.add(new Wall(world, 0, 0, 1, Gdx.graphics.getHeight(), "left"));
        // Верхняя стена
        walls.add(new Wall(world, 0, Gdx.graphics.getHeight() - 1, Gdx.graphics.getWidth(), 1, "top"));
        // Правая стена
        walls.add(new Wall(world, Gdx.graphics.getWidth() - 1, 0, 1, Gdx.graphics.getHeight(), "right"));
    }
    private void createBricks() {

        int brickWidth = this.gameSession.getBrickWidth();
        int brickHeight = this.gameSession.getBrickHeight();
        int spacing = 1;
        int random;

        for (int row = 0; row < GameSettings.BRICKS_LINE; row++) {
            for (int col = 0; col < GameSettings.BRICKS_IN_LINE; col++) {
                random = new Random().nextInt(GameResources.BRICKS.length);
                float x = col * (brickWidth + spacing);
                float y = gameSession.getLowBorder() +  row * (brickHeight + spacing);
                bricks.add(new Brick(world, x, y, brickWidth, brickHeight, random, this, col, row));
//                bricks.add(new Brick(world, x, y, brickWidth, brickHeight,2, this, col, row));
            }
        }
    }

    @Override
    public void show() {
        if (this.balls != null) {
            for (Ball ball : this.balls) { ball.dispose();}
            this.balls.clear();
            createBall();
                                }
        if (this.balls.get(0) != null) { this.balls.get(0).reset(); }
        if (this.paddle == null) { createPaddle(); }
        this.paddle.reset();
        this.timer.reset();
        this.state = GameState.NOTHING;
        this.gameSession.resetGame(); // Сброс состояния игры

        if (this.bricks != null){
            for (Brick brick : this.bricks) {
                brick.dispose();
                this.bricks.removeValue(brick, false);
                                            }
                                }
        this.createBricks();
    }

    @Override
    public void render(float delta) {
        this.drawGameElements();
    }

    private void drawGameElements() {
        Gdx.gl.glClearColor(GameSettings.BACKGROUND_COLOR.r
            ,GameSettings.BACKGROUND_COLOR.g
            ,GameSettings.BACKGROUND_COLOR.b
            ,GameSettings.BACKGROUND_COLOR.a);
//        Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Логика отрисовки игровых элементов
        // Пример: отрисовка платформы
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (this.state != GameState.NOTHING) {
            if (this.state == GameState.SLOW) { shapeRenderer.setColor(Color.RED); }
            else if (this.state == GameState.BOOST) { shapeRenderer.setColor(Color.BLUE); }
            else if (this.state == GameState.EXTENDED_PADDLE) { shapeRenderer.setColor(Color.GREEN); }
            shapeRenderer.rect(0, this.topBlackoutView.getY(), this.topBlackoutView.getWidth(), this.topBlackoutView.getHeight());
        }

        shapeRenderer.end();
        // Отрисовка игровых объектов
        // Отрисовка текста через SpriteBatch
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        this.topBlackoutView.drawTexture(batch);
        font.draw(batch, "Очки: " + gameSession.getScore(), gameSession.getxSettingsButton(), this.yLine);
        font.draw(batch, this.remainder, gameSession.getScreenWidth() - 4 * font.getXHeight(), this.yLine);
        this.paddle.draw(batch);
        for (Ball ball: this.balls) { ball.draw(batch); }
        for (Brick brick: this.bricks) { brick.draw(batch);}
        for (int i=0; i < this.gameSession.getLives() -1 ; i++) { this.lives.get(i).draw(batch); }
        for (BonBon bonbon : this.bonbons) { bonbon.draw(batch); }
        this.explosionManager.draw(batch);
        for (Bomb bomb: this.bombs) { bomb.draw(batch);}
        this.exitButton.draw(batch);
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
        for (BonBon bonbon : this.bonbons) {
            bonbon.update(delta);
            if ((bonbon.getY() < 0) || bonbon.isNeedDestroy()) {
                if (bonbon.isNeedDestroy()) { this.gameSession.setScore(this.gameSession.getScore() + 100);}
                this.bonbons.removeValue(bonbon, true);
                world.destroyBody(bonbon.getBody());
                bonbon.dispose();

            }
        }
        this.explosionManager.update(delta);
        for (int i = bombs.size - 1; i >= 0; i--) {
            bombs.get(i).update(delta);
            if ((bombs.get(i).getY() < 0)  || bombs.get(i).isNeedDestroy()) {
                if (bombs.get(i).isNeedDestroy()) {
                    gameSession.setLives(gameSession.getLives() - 1);
                    this.getAudio().getExplosionSound().play(gameSession.getSoundVolume());
                }
                world.destroyBody(bombs.get(i).getBody());
                bombs.get(i).dispose();
                this.bombs.removeIndex(i);

            }
        }

    }


    private void terminate(boolean success) {
        game.getFinish().setFinalScore(this.gameSession.getScore());
        if (success) {
            this.getAudio().getWin().play(gameSession.getSoundVolume());
            game.getFinish().setMessage("Победа");
            game.getFinish().setVictory(true);
                     }
        else {
            this.getAudio().getLose().play(gameSession.getSoundVolume());
            game.getFinish().setMessage("Проигрыш");
            game.getFinish().setVictory(false);
             }
        if (this.gameSession.getScore() > 0) { game.getRecordsTable().addResult(this.gameSession.getScore()); }
        game.setScreen(game.getFinish());
    }

    private void checkGameEndConditions() {
        if (gameSession.isGameOver()) {
            this.terminate(false);
        }
        else if (this.bricks.size == 0) {
            this.terminate(true);
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
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void handle() {
        super.handle();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            this.terminate(false);
        }
        if (Gdx.input.isTouched()) {
            Vector3 touch = this.game.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (this.exitButton.isHit(touch.x, touch.y)) { this.terminate(false);}
                                   }
    }

    @Override
    public void draw() { }

    public World getWorld() { return world; }

    @Override
    public void update(float delta) {
//        Замедлить игру: увеличьте delta (например, delta * 2.0f).
//        Ускорить игру: уменьшите delta (например, delta * 0.5f).
//            world.setVelocityThreshold(Float.MAX_VALUE); // Отключаем порог скорости для ускорения
//            world.step(BOOST_SPEED * delta, 6, 2); // Устанавливаем ускоренный шаг физики
        this.handle();
        this.timer.update(delta);
        if (timer.isActive()) {
            this.remainder = String.format("%d", timer.remainder());
            //                world.setVelocityThreshold(Float.MAX_VALUE); // Отключаем порог скорости для ускорения
            if (this.state == GameState.BOOST) { delta = delta * 0.5f; }
            if (this.state == GameState.SLOW)  { delta = delta * 1.5f; }
        }
        else {
            this.remainder = "";
            if (this.state != GameState.NOTHING) { this.paddle.setWidth(this.gameSession.getPaddleWidth()); }
            this.state = GameState.NOTHING;
        }
        accumulator += delta;
        float scaledStep = GameSettings.TIME_STEP * GameSettings.SCALE;
        while (accumulator >= scaledStep) {
            accumulator -= scaledStep;
            this.camera.update();
            updateGameLogic(delta);
            checkGameEndConditions();
            this.paddle.update();
            for (Ball ball: this.balls) { ball.update(); }
            // Удаляем разрушенные кирпичи
            for (int i = this.bricks.size - 1; i >= 0; i--) {
                if (this.bricks.size == 0) { break; }
                if (i > (this.bricks.size - 1)) {continue;}
                if (this.bricks.get(i) == null) {continue;}
                if (this.bricks.get(i).isDestroyed()) {
                    if (this.bricks.get(i).getType() == 8) {
                        this.bonbons.add(new BonBon(this.bricks.get(i).getX()
                            , this.bricks.get(i).getY(), this.game));
                                                           }
                    if ((this.bricks.get(i).getType() == 7) && (! this.timer.isActive()))  {
                        this.timer.activate(30000);
                        this.state = GameState.BOOST;
                                                                                      }
                    if ((this.bricks.get(i).getType() == 6) && (! this.timer.isActive()))  {
                        this.timer.activate(60000);
                        this.state = GameState.EXTENDED_PADDLE;
                        this.paddle.setWidth(2 * this.paddle.getWidth());
                    }
                    if ((bricks.get(i).getType() == 5) && (! this.timer.isActive()))  {
                        this.timer.activate(30000);
                        this.state = GameState.SLOW;
                    }

                    if (bricks.get(i).getType() == 4) {
                        this.explosion(bricks.get(i));
                        continue;
                                                      }
                    if (bricks.get(i).getType() == 3) {
                        this.bombs.add(new Bomb(bricks.get(i).getX(), bricks.get(i).getY(), this.game));
                    }
                    if (bricks.get(i).getType() == 2) {
                        Ball ball = new Ball(this.getWorld(),Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f, this);
                        ball.getBody().setLinearVelocity(game.getGameSession().getBallVelocity(), game.getGameSession().getBallVelocity());
                        balls.add(ball);
                        ball = new Ball(this.getWorld(),Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f, this);
                        ball.getBody().setLinearVelocity(- game.getGameSession().getBallVelocity(), - game.getGameSession().getBallVelocity());
                        balls.add(ball);
                    }

                    world.destroyBody(bricks.get(i).getBody());
                    bricks.removeIndex(i);
                }
            }
            for (Ball ball: this.balls) {
                if ((ball.getY() < 0) || (ball.getX() < 0 )
                    || (ball.getX() > gameSession.getScreenWidth())) {
                    this.balls.removeValue(ball, false);
                    ball.dispose();

                }

            }
            if (this.balls.size == 0) {
                gameSession.setLives(gameSession.getLives() - 1);
                createBall();
            }


            world.step(delta, GameSettings.VELOCITY_ITERATIONS, GameSettings.POSITION_ITERATIONS);
        }
    }

    private void explosion(Brick brick) {
        Vector2 position = new Vector2(brick.getX(), brick.getY());
        this.explosionManager.createExplosion(position);
        this.getAudio().getExplosionSound().play(gameSession.getSoundVolume());
        int row = brick.getRow();
        int column = brick.getColumn();
        int leftColumm = (column > 0) ? column - 1 : column;
        int righColumm = (column < (GameSettings.BRICKS_IN_LINE - 1)) ? column + 1 : column;
        int lowRow = (row > 0) ? row - 1 : row;
        int highRow = (row < (GameSettings.BRICKS_LINE - 1)) ? row + 1 : row;
        for (int i = this.bricks.size - 1; i >= 0; i--) {
            int current_row = bricks.get(i).getRow();
            int current_column = bricks.get(i).getColumn();
            if (
                ((current_row == lowRow) &&
                    ((current_column == leftColumm) || (current_column == column) || (current_column == righColumm))
                )
             ||
                ((current_row == row) &&
                ((current_column == leftColumm)
                    || (current_column == column)
                    || (current_column == righColumm))
               )
             ||
                ((current_row == highRow) &&
                    ((current_column == leftColumm)
                        || (current_column == column)
                        || (current_column == righColumm))
                )
            )
            {
                world.destroyBody(bricks.get(i).getBody());
                bricks.removeIndex(i);
            }
        }

    }

    public GameSession getGameSession() { return gameSession; }

    public Array<Brick> getBricks() { return bricks; }

    public BonBon getBonBon(Body body) {
        BonBon result = null;
        for (BonBon bonbon: this.bonbons) {
            if (bonbon.getBody() == body) {
                result = bonbon;
                break;
            }
        }
        return result;
    }


    @Override
    public void dispose() {
        // Освобождение игровых ресурсов
        if (this.topBlackoutView != null) {topBlackoutView.dispose();}

    }

    public Bomb getBomb(Body bombBody) {
        Bomb result = null;
        for (Bomb bomb: this.bombs) {
            if (bomb.getBody() == bombBody) {
                result = bomb;
                break;
            }
        }
        return result;

    }
}
