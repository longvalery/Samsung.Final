package rva.com.uix;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class SliderView extends Group {
    private Slider slider;
    private Skin skin;
    private Label valueLabel;

    public SliderView(float x, float y, int width, int height
            , float min, float max, float step, float value, String text, BitmapFont font) {
        super();
        skin = new Skin();
        skin.add("default", new Label.LabelStyle(font, null));

// Создаем простые текстуры для слайдера
        Pixmap bgPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(Color.GRAY);
        bgPixmap.fill();
        Texture bgTexture = new Texture(bgPixmap);
        bgPixmap.dispose();

        Pixmap knobPixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        knobPixmap.setColor(Color.WHITE);
        knobPixmap.fill();
        Texture knobTexture = new Texture(knobPixmap);
        knobPixmap.dispose();

        // Регистрируем текстуры в Skin
        skin.add("bgTexture", bgTexture);
        skin.add("knobTexture", knobTexture);

        // Создаем Drawable через TextureRegionDrawable
        TextureRegionDrawable sliderBgDrawable = new TextureRegionDrawable(new TextureRegion(bgTexture));
        TextureRegionDrawable knobDrawable = new TextureRegionDrawable(new TextureRegion(knobTexture));

        // Создаем стиль слайдера
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = sliderBgDrawable;
        sliderStyle.knob = knobDrawable;

        // Регистрируем стиль
        skin.add("default-horizontal", sliderStyle);

        this.slider = new Slider(min, max, step, false, skin, "default-horizontal");
        this.slider.setPosition(x,y);
        this.slider.setSize(width, height);
        this.slider.setValue(value);
        // Создаём Label для отображения значения (можно также выводить text)
        this.valueLabel = new Label(text + ": " + value, skin, "default");

        float labelX = slider.getX() + slider.getWidth() + 20;
        float labelY = slider.getY() + (slider.getHeight() - valueLabel.getHeight()) / 2; // центрируем по вертикали
        this.valueLabel.setPosition(labelX, labelY);

        // Обновляем Label при изменении слайдера
        this.slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float val = slider.getValue();
                // Форматируем вывод: если шаг меньше 1, показываем 2 знака после запятой
                String formatted = (step >= 1) ? String.valueOf((int) val) : String.format("%.1f", val);
                valueLabel.setText(text + ": " + formatted);
            }
        });

        addActor(this.slider);
        addActor(this.valueLabel);


    }


    public void draw(SpriteBatch batch) {

        this.slider.draw(batch, 1.0f);
        this.valueLabel.draw(batch, 1.0f);

    }


    // Метод для получения текущего значения слайдера
    public float getValue() {
        return slider.getValue();
    }

    // Метод для программной установки значения

    public void setValue(float val) {
        slider.setValue(val);
        // Обновляем label (лучше вызвать listener, но можно вручную)
        float currentVal = slider.getValue();
        String formatted = String.format("%.1f", currentVal);
        valueLabel.setText(valueLabel.getText().toString().replaceFirst(": .*$", ": " + formatted));
    }



    public void dispose() {
        if (skin != null) skin.dispose();

    }
}


