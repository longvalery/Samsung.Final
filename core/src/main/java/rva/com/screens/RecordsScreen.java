package rva.com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.time.format.DateTimeFormatter;

import rva.com.Main;
import rva.com.services.GameSettings;
import rva.com.services.TableRecordItem;

public class RecordsScreen extends BaseScreen {
    private Stage stage;
    private Skin skin;
    DateTimeFormatter dateFormatter;
    public RecordsScreen(Main game) {
        super(game);
        // Создаём форматер один раз
        this.dateFormatter = DateTimeFormatter.ofPattern("dd.MM.YYYY");
        this.skin = createSimpleSkin();
        this.stage = new Stage(new FitViewport(game.getGameSession().getScreenWidth()
            , game.getGameSession().getScreenHeight(), camera));
        createUI();
    }

    private Skin createSimpleSkin() {
        Skin skin = new Skin();

        // Создаём простой шрифт (в реальном проекте лучше загружать из файла)

        skin.add("default", this.game.getSmallWhiteFont());

        // Создаём простую текстуру для фона кнопок
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture whiteTexture = new Texture(pixmap);
        skin.add("white", whiteTexture);
        pixmap.dispose();

        // Стиль для меток
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.game.getSmallWhiteFont();
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // Стиль для кнопок
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.newDrawable("white", Color.GRAY);
        buttonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        buttonStyle.checked = skin.newDrawable("white", Color.BLUE);
        buttonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        buttonStyle.font = this.game.getSmallWhiteFont();
        buttonStyle.fontColor = Color.BLACK;
        skin.add("default", buttonStyle);

        return skin;
    }

    private void createUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Заголовок
        Label title = new Label("ТАБЛИЦА РЕКОРДОВ", skin, "default");
        mainTable.add(title).padBottom(50).row();

        // Таблица рекордов
        Table recordsTable = new Table();
        recordsTable.setSkin(skin); // устанавливаем скин
//        recordsTable.setBackground(skin.newDrawable("white", GameSettings.BACKGROUND_COLOR));
        recordsTable.pad(20);

        // Заголовки столбцов
        recordsTable.add("МЕСТО").width(120).expandX().pad(5);
        recordsTable.add("ОЧКИ").width(120).expandX().pad(5);
        recordsTable.add("ДАТА").width(180).expandX().pad(5).row();

        // Данные рекордов
        var records = game.getRecordsTable();
        for (int i = 0; i < records.size(); i++) {
            TableRecordItem record = records.get(i);
            recordsTable.add(String.valueOf(i + 1)).width(120).pad(5);
            recordsTable.add(String.valueOf(record.value())).width(120).pad(5);
            String empty = "";
            if (record.date() == null) {recordsTable.add(empty).width(180).pad(5).row();}
            else                       { recordsTable.add(record.date().format(dateFormatter)).width(180).pad(5).row();}
        }

        mainTable.add(recordsTable).padBottom(30).row();

        // Кнопка возврата
        TextButton backButton = new TextButton("НАЗАД", skin);

        backButton.addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean touchDown(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(game.getSettings());
            }
        });
        mainTable.add(backButton).padTop(20).padLeft(100).padRight(100);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameSettings.BACKGROUND_COLOR.r
            ,GameSettings.BACKGROUND_COLOR.g
            ,GameSettings.BACKGROUND_COLOR.b
            ,GameSettings.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.createUI();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
// Снимаем InputProcessor при скрытии экрана
        Gdx.input.setInputProcessor(null);
        if (stage != null) {
            stage.unfocusAll(); // снимает фокус со всех актёров
            stage.getRoot().clearActions(); // отменяет все анимации/действия
        }

    }

    @Override
    public void dispose() {
        this.skin.dispose();
        this.stage.dispose();
    }
}
