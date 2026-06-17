package rva.com;

import static rva.com.services.FontBuilder.createTwoColorFont;

import static rva.com.services.GameResources.GAME_NAME;
import static rva.com.services.GameResources.SETTINGS_ITEMS;
import static rva.com.services.GameSettings.SCREEN_HEIGHT;
import static rva.com.services.GameSettings.SCREEN_WIDTH;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import rva.com.managers.AudioManager;
import rva.com.managers.MemoryManager;
import rva.com.managers.RecordsTableManager;
import rva.com.screens.BaseScreen;
import rva.com.screens.GameOverScreen;
import rva.com.screens.GamePlayScreen;
import rva.com.screens.MainMenuScreen;
import rva.com.screens.RecordsScreen;
import rva.com.screens.SettingsScreen;
import rva.com.services.FontBuilder;
import rva.com.services.GameResources;
import rva.com.services.GameSession;
import rva.com.services.GameSettings;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private String osName;
    private BitmapFont font, whiteFont, yellowFont, titleFont, litleFont, smallWhiteFont, smallFont;
    GameSession gameSession;
    ShapeRenderer shapeRenderer;

    MainMenuScreen menu;
    GamePlayScreen game;
    GameOverScreen finish;
    SettingsScreen settings;
    private BaseScreen screen;
    private GlyphLayout layout;
    private ShaderProgram shader;
    private FontBuilder.FontWithShader fontWithShader;
    private RecordsTableManager recordsTable;
    private AudioManager audioManager;
    private RecordsScreen records;

    @Override
    public void create() {
        this.gameSession = new GameSession(this);
        Gdx.graphics.setTitle(GAME_NAME);
        // Определяем операционную систему
        osName = this.gameSession.detectOperatingSystem();
        System.out.println("Running into: " + osName);
        if (this.osName.contains("Windows")) {
            this.gameSession.setScreenWidth(SCREEN_WIDTH);
            this.gameSession.setScreenHeight(SCREEN_HEIGHT);
            Gdx.graphics.setWindowedMode(this.gameSession.getScreenWidth(), this.gameSession.getScreenHeight());
        }
        else {
            // Получаем размеры экрана
            this.gameSession.setScreenWidth(Gdx.graphics.getWidth());
            this.gameSession.setScreenHeight(Gdx.graphics.getHeight());
              }
        System.out.println("Screen sizes: " + this.gameSession.getScreenWidth() + "x" + this.gameSession.getScreenHeight());
        this.gameSession.calcSizes(GameSettings.BRICKS_LINE, GameSettings.BRICKS_IN_LINE);
        this.gameSession.calcSettingsButtonSize(SETTINGS_ITEMS.length);
        // Создаём камеру с размерами экрана
        camera = new OrthographicCamera();
        camera.setToOrtho(false, this.gameSession.getScreenWidth(), this.gameSession.getScreenHeight());
        camera.update();


        batch = new SpriteBatch();
        this.font = FontBuilder.generate(this.gameSession.getMainFontSize(), Color.BLACK,  GameResources.MAIN_FONT_PATH);
        this.litleFont = FontBuilder.generate(10, Color.BLACK,  GameResources.MAIN_FONT_PATH);
        this.whiteFont = FontBuilder.generate(this.gameSession.getMainFontSize(), Color.WHITE,  GameResources.MAIN_FONT_PATH);
        this.smallFont = FontBuilder.generate(this.gameSession.getSmallFontSize(), Color.BLACK,  GameResources.MAIN_FONT_PATH);
        this.smallWhiteFont = FontBuilder.generate(this.gameSession.getSmallFontSize(), Color.WHITE,  GameResources.MAIN_FONT_PATH);
        this.yellowFont = FontBuilder.generate(this.gameSession.getMainFontSize(), Color.YELLOW,  GameResources.MAIN_FONT_PATH);
//        this.titleFont = FontBuilder.generate(this.gameSession.getTitleFontSize(), Color.BLACK,  GameResources.GOTHIC_FONT_PATH);
//        this.shader =  generateShader(this.gameSession.getTitleFontSize(), GameResources.GOTHIC_FONT_PATH);
        this.fontWithShader = createTwoColorFont(this.gameSession.getTitleFontSize(), GameResources.TITLE_FONT_PATH);
        this.titleFont = fontWithShader.font;
        this.shader = fontWithShader.shader;
        this.shapeRenderer = new ShapeRenderer();


        this.menu = new MainMenuScreen(this);
        this.game = new GamePlayScreen(this);
        this.finish = new GameOverScreen(this);
        this.settings = new SettingsScreen(this);

        this.layout = new GlyphLayout();
        this.audioManager = new AudioManager();
        gameSession.setMusicVolume(MemoryManager.loadMusicVolume());
        this.audioManager.getBackgroundMusic().setVolume(MemoryManager.loadMusicVolume());
        gameSession.setSoundVolume(MemoryManager.loadSoundVolume());

        String data = MemoryManager.loadRecordsTable();
//        this.recordsTable = new RecordsTableManager(GameSettings.MAX_RECORDS);
        if ((data == null) || (data.length() == 0)) { this.recordsTable = new RecordsTableManager(GameSettings.MAX_RECORDS);}
        else { this.recordsTable = new RecordsTableManager(GameSettings.MAX_RECORDS, data); }

        this.records = new RecordsScreen(this);

        this.setScreen(this.getMenu());
//        System.out.println("Line: " + "0 " + this.gameSession.getPaddleLevel() + " " + this.gameSession.getScreenWidth() + " " + this.gameSession.getPaddleLevel());
    }




    public void setScreen(BaseScreen screen) {
        // Сбрасываем состояние клавиатуры
        Gdx.input.justTouched(); // сбрасывает флаг касания
        Gdx.input.setOnscreenKeyboardVisible(false);
        Gdx.input.setInputProcessor(null);

        if (this.screen != null) { this.screen.hide(); }
        this.screen = screen;
        this.screen.show();
    }

    private void update(float delta) {
         this.screen.update(delta);
    }

    private void draw() {


/*
       ScreenUtils.clear(0.55f, 0.55f, 0.55f, 0.5f);
        batch.begin();

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 0, 0, 1); // Red line
//        Vector3 start = camera.unproject(new Vector3(0, this.gameSession.getPaddleLevel(), 0));
//        Vector3 finish = camera.unproject(new Vector3(this.gameSession.getScreenWidth(), this.gameSession.getPaddleLevel(), 0));
//        System.out.println(start);
//        shapeRenderer.line(start.x, start.y, finish.x, finish.y);
        shapeRenderer.line(0, this.gameSession.getPaddleLevel(), this.gameSession.getScreenWidth(), this.gameSession.getPaddleLevel());
        shapeRenderer.line(0, this.gameSession.getLowBorder(), this.gameSession.getScreenWidth(), this.gameSession.getLowBorder());
        shapeRenderer.line(0, this.gameSession.getHighBorder(), this.gameSession.getScreenWidth(), this.gameSession.getHighBorder());
        shapeRenderer.end();
*/
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        this.screen.render(delta);
        this.update(delta);
//        this.draw();

    }


    @Override
    public void resize(int width, int height) {
        if (this.screen != null) { this.screen.resize(width, height); }
        // Обновляем камеру при изменении размера окна
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void pause() {
        if (this.screen != null) { this.screen.pause(); }
    }

    @Override
    public void resume() {
        if (this.screen != null) { this.screen.resume(); }
    }
    public SpriteBatch getBatch() {
        return batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public BitmapFont getFont() { return font; }

    public GameSession getGameSession() {
        return gameSession;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public MainMenuScreen getMenu() {
        return menu;
    }

    public GamePlayScreen getGame() {
        return game;
    }

    public RecordsScreen getRecords() { return records; }

    public GameOverScreen getFinish() {
        return finish;
    }

    public BitmapFont getWhiteFont() {
        return whiteFont;
    }

    public BitmapFont getYellowFont() {
        return yellowFont;
    }

    public BaseScreen getScreen() {
        return screen;
    }

    public SettingsScreen getSettings() { return settings; }

    public BitmapFont getTitleFont() {
        return titleFont;
    }

    public GlyphLayout getLayout() {
        return layout;
    }

    public ShaderProgram getShader() {
        return shader;
    }

    public FontBuilder.FontWithShader getFontWithShader() {
        return fontWithShader;
    }

    public BitmapFont getLitleFont() {
        return litleFont;
    }

    public AudioManager getAudioManager() { return audioManager; }

    public RecordsTableManager getRecordsTable() { return recordsTable; }

    public BitmapFont getSmallWhiteFont() { return smallWhiteFont; }

    public BitmapFont getSmallFont() { return smallFont;  }

    @Override
    public void dispose() {
        super.dispose();
        this.batch.dispose();
        this.gameSession.dispose();
        this.shapeRenderer.dispose();
        this.menu.dispose();
        this.game.dispose();
        this.finish.dispose();
        this.settings.dispose();
        this.screen.dispose();
        this.font.dispose();
        this.yellowFont.dispose();
        this.whiteFont.dispose();
        this.titleFont.dispose();
        this.shader.dispose();
        this.recordsTable.dispose();
    }
}
