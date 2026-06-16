package rva.com.uix;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Виджет, содержащий слайдер и подпись к нему, размещённые на фоне
 * с закруглёнными углами серого цвета.
 */
public class SliderViewNice extends Table {
    private final Slider slider;
    private final Label valueLabel;
    private final Skin skin;
    private final Texture roundTexture;  // для освобождения ресурсов

    /**
     * Создаёт SliderView.
     *
     * @param x      координата X левого нижнего угла виджета
     * @param y      координата Y левого нижнего угла виджета
     * @param width  ширина слайдера
     * @param height высота слайдера
     * @param min    минимальное значение слайдера
     * @param max    максимальное значение слайдера
     * @param step   шаг изменения
     * @param value  начальное значение
     * @param text   текст перед значением (например, "Громкость")
     * @param font   шрифт для подписи
     */
    public SliderViewNice(float x, float y, int width, int height,
                      float min, float max, float step, float value,
                      String text, BitmapFont font) {
        super();
        setPosition(x, y);

        // 1. Создаём Skin и стили для слайдера (как в оригинале)
        skin = new Skin();
        skin.add("default", new Label.LabelStyle(font, null));

        Pixmap bgPixmap = new Pixmap(width, height, Pixmap.Format.RGBA4444);
        bgPixmap.setColor(Color.BROWN);
        bgPixmap.fill();
        Texture bgTexture = new Texture(bgPixmap);
        bgPixmap.dispose();

        Pixmap knobPixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        knobPixmap.setColor(Color.WHITE);
        knobPixmap.fill();
        Texture knobTexture = new Texture(knobPixmap);
        knobPixmap.dispose();

        skin.add("bgTexture", bgTexture);
        skin.add("knobTexture", knobTexture);

        TextureRegionDrawable sliderBgDrawable =
            new TextureRegionDrawable(new TextureRegion(bgTexture));
        TextureRegionDrawable knobDrawable =
            new TextureRegionDrawable(new TextureRegion(knobTexture));

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = sliderBgDrawable;
        sliderStyle.knob = knobDrawable;
        skin.add("default-horizontal", sliderStyle);

        // 2. Создаём сам слайдер и подпись
        this.slider = new Slider(min, max, step, false, skin, "default-horizontal");
        this.slider.setValue(value);

        this.valueLabel = new Label(text + ": " + value, skin, "default");

        // 3. Создаём фон с закруглёнными углами (NinePatch)
        int radius = 10;          // радиус скругления
        int patchSize = 64;       // размер текстуры для NinePatch
        Pixmap roundPixmap = createRoundedRectPixmap(patchSize, patchSize, radius, Color.BROWN);
        this.roundTexture = new Texture(roundPixmap);
        roundPixmap.dispose();

        NinePatch ninePatch = new NinePatch(roundTexture, radius, radius, radius, radius);
        NinePatchDrawable backgroundDrawable = new NinePatchDrawable(ninePatch);

        // 4. Настраиваем таблицу: фон, отступы и расположение элементов
        setBackground(backgroundDrawable);
        pad(radius + 5);  // отступы от краёв фона

        // Слайдер занимает всю доступную ширину, label справа с отступом
        add(slider).width(width).height(height).expandX().fillX();
        add(valueLabel).padLeft(20);

        // Вычисляем предпочтительный размер таблицы на основе содержимого
        pack();

        // 5. Обновляем подпись при изменении слайдера
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float val = slider.getValue();
                String formatted = (step >= 1)
                    ? String.valueOf((int) val)
                    : String.format("%.1f", val);
                valueLabel.setText(text + ": " + formatted);
            }
        });
    }

    /**
     * Создаёт Pixmap с закруглённым прямоугольником заданного цвета.
     */
    private Pixmap createRoundedRectPixmap(int width, int height, int radius, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        // Прозрачный фон
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();

        // Рисуем прямоугольник, «обрезанный» в углах
        pixmap.setColor(color);

        // Горизонтальные и вертикальные полосы (без углов)
        pixmap.fillRectangle(radius, 0, width - 2 * radius, height);
        pixmap.fillRectangle(0, radius, width, height - 2 * radius);

        // Четыре круга в углах, чтобы скруглить их
        pixmap.fillCircle(radius, radius, radius);
        pixmap.fillCircle(width - radius - 1, radius, radius);
        pixmap.fillCircle(radius, height - radius - 1, radius);
        pixmap.fillCircle(width - radius - 1, height - radius - 1, radius);

        return pixmap;
    }

    // --- Публичные методы ---

    public float getValue() {
        return slider.getValue();
    }

    public void setValue(float val) {
        slider.setValue(val);
        // Обновляем отображаемое значение
        float currentVal = slider.getValue();
        String formatted = String.format("%.1f", currentVal);
        String oldText = valueLabel.getText().toString();
        valueLabel.setText(oldText.replaceFirst(": .*$", ": " + formatted));
    }

    /**
     * Освобождает ресурсы. Должен быть вызван, когда виджет больше не нужен.
     */
    public void dispose() {
        if (skin != null) skin.dispose();
        if (roundTexture != null) roundTexture.dispose();
    }
}
