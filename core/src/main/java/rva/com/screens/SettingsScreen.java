package rva.com.screens;

import static java.lang.Math.round;
import static rva.com.services.GameResources.BUTTON_PATH;
import static rva.com.services.GameResources.SETTINGS_ITEMS;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import rva.com.Main;
import rva.com.services.GameSettings;
import rva.com.uix.ButtonView;
import rva.com.uix.SliderView;


public class SettingsScreen  extends BaseScreen {
    private ArrayList<ButtonView> buttonArray;
    private ArrayList<SliderView> sliderArray;
    private Stage stage;

    public SettingsScreen(Main game) {
        super(game);
        this.buttonArray = new ArrayList<>();
        this.sliderArray = new ArrayList<>();
        this.stage = new Stage(new ScreenViewport());
    }

    private void createButtons() {
        this.buttonArray.clear();
        for (int i = 0; i < SETTINGS_ITEMS.length; i++) {
            if (i > 1) {
                this.buttonArray.add(new ButtonView(
                    this.game.getGameSession().getxSettingsButton(),
                    this.game.getGameSession().getMaxLineSettingsButton()
                        - i * (this.game.getGameSession().getDeltaSettingsButton()
                        + this.game.getGameSession().getHeightSettingsButton()),
                    this.game.getGameSession().getWidthSettingsButton(),
                    this.game.getGameSession().getHeightSettingsButton(),
                    this.game.getWhiteFont(),
                    BUTTON_PATH,
                    SETTINGS_ITEMS[i]
                ));
            } else {
                String name = (i == 0) ? "Музыка": "Звук";
                float value = (i == 0) ? this.game.getGameSession().getMusicVolume(): this.game.getGameSession().getSoundVolume();
                SliderView slider = new SliderView(this.game.getGameSession().getxSettingsButton()
                    , this.game.getGameSession().getMaxLineSettingsButton()
                    - i * (this.game.getGameSession().getDeltaSettingsButton()
                    + this.game.getGameSession().getHeightSettingsButton())
                    , this.game.getGameSession().getWidthSettingsButton()
                    , this.game.getGameSession().getHeightSettingsButton()
                    , 0.0f, 1.0f, 0.1f
                    , value
                    , name
                    , game.getWhiteFont()
                    , game, i + 1);
                stage.addActor(slider);
                this.sliderArray.add(slider);

            }
        }
    }

    @Override
    public void handle() {
        if (Gdx.input.isTouched()) {
//            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 touch = this.game.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int index = -1;
            for (int i = 0; i < this.buttonArray.size(); i++) {
                if (this.buttonArray.get(i).isHit(touch.x, touch.y)) {
                    index = i;
                    break;
                }
            }
            String line;
            switch (index) {
//                case 0:
//                    this.game.getGameSession().setMusicFlag(! this.game.getGameSession().isMusicFlag());
//                    line = "Музыка: " + (this.game.getGameSession().isMusicFlag() ? "Вкл.": "ВЫКЛ.");
//                    this.buttonArray.get(0).setText(line);
//                    break;
//                case 1:
//                    this.game.getGameSession().setSoundFlag(! this.game.getGameSession().isSoundFlag());
//                    line = "Звук: " + (this.game.getGameSession().isSoundFlag() ? "Вкл.": "ВЫКЛ.");
//                    this.buttonArray.get(1).setText(line);
//                    break;
                case 0:
                    this.game.setScreen(this.game.getRecords());
                    break;
                case 1:
                    this.game.setScreen(this.game.getMenu());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void draw() {
//        Gdx.gl.glClearColor(0.78f, 0.43f, 0.03f, 1);
        Gdx.gl.glClearColor(GameSettings.BACKGROUND_COLOR.r
            ,GameSettings.BACKGROUND_COLOR.g
            ,GameSettings.BACKGROUND_COLOR.b
            ,GameSettings.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.game.getLayout().setText(this.game.getTitleFont(), "НАСТРОЙКИ");
        float widthInPixels = this.game.getLayout().width;
        float centerX = (Gdx.graphics.getWidth() - widthInPixels) / 2f;
        float centerY = this.game.getGameSession().getTitleLine() - this.game.getLayout().height; // / 2f;

        batch.begin();
        this.game.getTitleFont().draw(batch, "НАСТРОЙКИ", centerX, centerY);
        for (ButtonView button : this.buttonArray) {
            button.draw(batch);
        }

        for (SliderView slider : this.sliderArray) {
            slider.draw(batch, 1.0f);
        }

        batch.end();


//        stage.draw();
    }

    public void changeMusicVolume(float value) {
        value = (float) (Math.round(value * 100.0) / 100.0);
        game.getGameSession().setMusicVolume(value);
        game.getAudioManager().getBackgroundMusic().setVolume(value);
    }

    public void changeSoundVolume(float value) {
        value = (float) (Math.round(value * 100.0) / 100.0);
        game.getGameSession().setSoundVolume(value);
        game.getAudioManager().getShootSound().play(value);
    }

    @Override
    public void show() {
        this.game.getGameSession().calcSettingsButtonSize(SETTINGS_ITEMS.length);
        this.createButtons();
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.game.getGameSession().calcSettingsButtonSize(SETTINGS_ITEMS.length);
        this.createButtons();
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
    public void update(float delta) {
        stage.act(delta);

    }

    @Override
    public void dispose() {
        for (SliderView slider : this.sliderArray) { slider.dispose();  }
        for( ButtonView button :this.buttonArray) {  button.dispose();  }
        if (this.stage != null) { this.stage.dispose(); }
    }

}
