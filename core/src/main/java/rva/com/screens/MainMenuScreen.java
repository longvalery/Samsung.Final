package rva.com.screens;

import static rva.com.services.GameResources.BACKGROUND_PATH;
import static rva.com.services.GameResources.COPYRIGHT;
import static rva.com.services.GameResources.GAME_NAME;
import static rva.com.services.GameResources.MENU_ITEMS;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

import rva.com.Main;
import rva.com.components.MenuItemDimensions;

public class MainMenuScreen extends BaseScreen {
    private BitmapFont yellowFont, whiteFont, titleFont, grayFont;
    private String[] menuItems = MENU_ITEMS;//    {"Start Game", "Settings", "Exit"};
    private int selectedItem = 0;
    private Texture texture;
    private Sprite sprite;

    private ArrayList<MenuItemDimensions> menuArray;


    public MainMenuScreen(Main game) {
        super(game);

        this.yellowFont = game.getYellowFont();
        this.whiteFont = game.getWhiteFont();
        this.titleFont = game.getTitleFont();
        this.grayFont = game.getFont();
        this.texture = new Texture(BACKGROUND_PATH);
        this.sprite = new Sprite(texture);
        this.sprite.setSize(game.getGameSession().getScreenWidth(), game.getGameSession().getScreenHeight()); // масштабируем спрайт
        this.sprite.setPosition(0, 0);
        this.menuArray = new ArrayList<>();
    }

    @Override
    public void show() {
        this.selectedItem = 0;
        System.out.println("Main Menu Screen shown");
    }
    @Override
    public void draw() {
        Gdx.gl.glClearColor(0.95f, 0.8f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        this.sprite.draw(batch);
        batch.end();

        this.game.getLayout().setText(this.titleFont, GAME_NAME);
        float widthInPixels = this.game.getLayout().width;
        float heightInPixels = this.game.getLayout().height;
        float centerX = (Gdx.graphics.getWidth() - widthInPixels) / 2f;
        float centerY = this.game.getGameSession().getTitleLine() - this.game.getLayout().height; // / 2f;
        float diagonalAngle = (float) Math.tan(Math.toRadians(5));
        float centerTextX = centerX + widthInPixels / 2f;
        float centerTextY = centerY - heightInPixels / 2f;
        float correctOffset = centerTextY + diagonalAngle * centerTextX;
        this.game.getShader().bind();
        this.game.getShader().setUniformf("u_diagonalOffset", correctOffset);
        this.game.getShader().setUniformf("u_diagonalAngle", diagonalAngle); // обязательно обновляем!
        batch.setShader(this.game.getShader());
        batch.begin();
        titleFont.draw(batch, GAME_NAME, centerX, centerY);
        batch.end();
        batch.setShader(null);

        batch.begin();
        int y, delta;
        delta = this.game.getGameSession().getMainFontSize() * 2;

        for (int i = 0; i < menuItems.length; i++) {
            y = this.game.getGameSession().getMenuLine() - i * delta;
            // Тень
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            this.grayFont.draw(batch,menuItems[i], delta + 5, y - 5);
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); // вернуть обратно
            // Основной текст
            if (i == selectedItem) { this.yellowFont.draw(batch,menuItems[i], delta, y); }
            else                   { this.whiteFont.draw(batch,menuItems[i], delta, y); }
        }

        this.game.getLitleFont().draw(batch,COPYRIGHT, this.game.getGameSession().getScreenWidth() - 100,20);
        batch.end();

    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void handle() {
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 touch = this.game.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            System.out.println(String.format("x: %f, y: %f", touch.x, touch.y));
//            System.out.println(String.format("x: %d, y: %d", Gdx.input.getX(), Gdx.input.getY()));
            int index = -1;
            for (int i = 0; i < this.menuItems.length; i++) {
                if (this.menuArray.get(i).isInside(touch.x, touch.y)) {
                    index = i;
                    break;
                }
                                                            }
            switch (index) {
                case 0:
                    game.setScreen(game.getGame());
                    break;
                case 1:
                    game.setScreen(game.getSettings());
                    break;
                case 2:
                    game.getGameSession().endGame();
                    Gdx.app.exit();
                    break;
                default:
                    break;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) { selectedItem = Math.max(0, selectedItem - 1); }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) { selectedItem = Math.min(menuItems.length - 1, selectedItem + 1); }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (selectedItem) {
                case 0:
                    game.setScreen(game.getGame());
                    break;
                case 1:
                    game.setScreen(game.getSettings());
                    break;
                case 2:
                    game.getGameSession().endGame();
                    Gdx.app.exit();
                    break;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.sprite.setSize(width, height);
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
    public void update(float floatDelta) {
        this.menuArray.clear();
        int y, delta;
        delta = this.game.getGameSession().getMainFontSize() * 2;
        for (int i = 0; i < menuItems.length; i++) {
            y = this.game.getGameSession().getMenuLine() - i * delta;
            this.game.getLayout().setText(this.whiteFont, MENU_ITEMS[i]);
            float width = this.game.getLayout().width;
            float height = this.game.getLayout().height;
            this.menuArray.add(new MenuItemDimensions(delta, y - height / 2, width, height));
//            System.out.println(String.format("MENU x: %d, y: %d, width: %f, height: %f", delta, y, width, height));

                                                   }
    }

    @Override
    public void dispose() {
        this.texture.dispose();
        // Освобождение ресурсов
    }
}
