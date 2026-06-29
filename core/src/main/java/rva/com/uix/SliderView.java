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
import com.badlogic.gdx.utils.Align;

import rva.com.Main;

public class SliderView extends Table {
    private final Slider slider;
    private final Label valueLabel;
    private final Skin skin;
    private final Main game;
    private final int type;

    private final Texture roundTexture;  // для освобождения ресурсов
    // Ресурсы для освобождения
    private final Texture bgTexture;
    private final Texture knobTexture;


    /**
     * Создаёт SliderView.
     *
     * @param x      координата X левого нижнего угла виджета
     * @param y      координата Y левого нижнего угла виджета
     * @param width  ширина виджета (вместе с меткой и слайдером)
     * @param height высота виджета (должна быть больше толщины линии)
     * @param min    минимальное значение слайдера
     * @param max    максимальное значение слайдера
     * @param step   шаг изменения
     * @param value  начальное значение
     * @param text   текст перед значением (например, "Громкость")
     * @param font   шрифт для подписи
     */
    public SliderView(float x, float y, int width, int height,
                      float min, float max, float step, float value,
                      String text, BitmapFont font, Main game, int type) {
        super();
        this.game = game;
        this.type = type;

        // Устанавливаем позицию и точный размер виджета
        setPosition(x, y);
        setSize(width, height);

        // 1. Создаём Skin и стили для слайдера
        skin = new Skin();
        skin.add("default", new Label.LabelStyle(font, null));

        String initialText = text + ": " + formatValue(value, step);
        this.valueLabel = new Label(initialText, skin, "default");
//        int sliderWidth = (int) (width - valueLabel.getWidth()) - 40;

        // --- Создаём фон слайдера (тонкая линия) ---
        int lineThickness = 5;
        // ТЕПЕРЬ создаём текстуру с реальной шириной = width (переданная ширина виджета)
        // Она будет растянута до размера ячейки слайдера, но линия останется видимой.
        Pixmap bgPixmap = new Pixmap(width , height, Pixmap.Format.RGBA8888);
        bgPixmap.setColor(0, 0, 0, 0); // прозрачный фон
        bgPixmap.fill();

        bgPixmap.setColor(Color.GRAY);
        int yLine = (height - lineThickness) / 2;
        bgPixmap.fillRectangle(0, yLine, width , lineThickness); // линия на всю ширину width
        this.bgTexture = new Texture(bgPixmap);
        bgPixmap.dispose();

        // --- Ручка слайдера (круглая) ---
        int knobSize = 16;
        Pixmap knobPixmap = new Pixmap(knobSize, knobSize, Pixmap.Format.RGBA8888);
        knobPixmap.setColor(Color.WHITE);
        knobPixmap.fillCircle(knobSize / 2, knobSize / 2, knobSize / 2 - 1);
        this.knobTexture = new Texture(knobPixmap);
        knobPixmap.dispose();

        skin.add("bgTexture", this.bgTexture);
        skin.add("knobTexture", this.knobTexture);

        TextureRegionDrawable sliderBgDrawable =
            new TextureRegionDrawable(new TextureRegion(bgTexture));
        TextureRegionDrawable knobDrawable =
            new TextureRegionDrawable(new TextureRegion(knobTexture));

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = sliderBgDrawable;
        sliderStyle.knob = knobDrawable;
        skin.add("default-horizontal", sliderStyle);
        // 2. Создаём слайдер и подпись
        this.slider = new Slider(min, max, step, false, skin, "default-horizontal");
        this.slider.setValue(value);
        // 3. Создаём фон всей группы (скруглённый прямоугольник)
        int radius = 10;
        int textureSize = 2 * radius + 1; // минимальный размер для NinePatch
        Pixmap roundPixmap = createRoundedRectPixmap(textureSize, textureSize, radius, Color.BROWN);
        this.roundTexture = new Texture(roundPixmap);
        roundPixmap.dispose();

        NinePatch ninePatch = new NinePatch(roundTexture, radius, radius, radius, radius);
        NinePatchDrawable backgroundDrawable = new NinePatchDrawable(ninePatch);
        setBackground(backgroundDrawable);

        // 4. Настраиваем отступы внутри таблицы
        int padValue = radius + 5;
        pad(padValue);

        // 5. Добавляем элементы в таблицу
        add(valueLabel).left();
        add(slider).padLeft(10).padRight(10).minWidth(10).growX().fillY(); // слайдер занимает всё оставшееся место
        // 6. Выполняем компоновку
        layout();

        // 7. Обработчик изменения слайдера
        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float val = slider.getValue();
                if (type == 1) { game.getSettings().changeMusicVolume(val); }
                else           { game.getSettings().changeSoundVolume(val); }
                valueLabel.setText(text + ": " + formatValue(val, step));
                invalidate();
                layout();
            }
        });

    }

    // Вспомогательный метод для форматирования значения
    private String formatValue(float val, float step) {
        if (step >= 1) { return String.valueOf((int) val);}
        else { return String.format("%.1f", val); }
    }

    // Переопределяем setSize для автоматического пересчёта при изменении размера виджета
//    @Override
//    public void setSize(float width, float height) {
//        super.setSize(width, height);
//        invalidate();  // заставляем Table пересчитать размеры дочерних элементов
//        layout();
//    }


    // Метод создания скруглённого прямоугольника (оставлен без изменений)
    private Pixmap createRoundedRectPixmap(int width, int height, int radius, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, width, height);
        // Обрезаем углы, рисуя прозрачные пиксели там, где они не должны быть
        // Центры окружностей для каждого угла:
        int cx1 = radius, cy1 = radius;                         // левый верхний
        int cx2 = width - radius, cy2 = radius;                 // правый верхний
        int cx3 = radius, cy3 = height - radius;                // левый нижний
        int cx4 = width - radius, cy4 = height - radius;        // правый нижний
        clearCorner(pixmap, cx1, cy1, radius, 0, 0);                     // левый верхний
        clearCorner(pixmap, cx2, cy2, radius, width - radius, 0);        // правый верхний
        clearCorner(pixmap, cx3, cy3, radius, 0, height - radius);       // левый нижний
        clearCorner(pixmap, cx4, cy4, radius, width - radius, height - radius); // правый нижний

//        clearCorner(pixmap, 0, 0, radius, true, true);   // левый верхний
//        clearCorner(pixmap, width - radius, 0, radius, false, true); // правый верхний
//        clearCorner(pixmap, 0, height - radius, radius, true, false); // левый нижний
//        clearCorner(pixmap, width - radius, height - radius, radius, false, false); // правый нижний

        return pixmap;
    }

    // Очистка угловой области: пиксели вне круга с центром (cx, cy) становятся прозрачными
    private void clearCorner(Pixmap pixmap, int cx, int cy, int r, int startX, int startY) {
        int r2 = r * r;
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < r; j++) {
                int px = startX + i;
                int py = startY + j;
                int dx = px - cx;
                int dy = py - cy;
                if (dx * dx + dy * dy > r2) {
                    pixmap.drawPixel(px, py, 0xbd532cff); // Color.RED.toIntBits()) ; //  0x00000000); // полностью прозрачный   .CLEAR.
                }
            }
        }
    }
//    private void clearCorner(Pixmap pixmap, int x, int y, int r, boolean left, boolean top) {
//        int r2 = r * r;
//        for (int i = 0; i < r; i++) {
//            for (int j = 0; j < r; j++) {
//                // Расстояние от угла до текущей точки
//                int dx = left ? r - i - 1 : i;
//                int dy = top ? r - j - 1 : j;
//                if (dx * dx + dy * dy > r2) {
//                    // Точка вне круга — делаем прозрачной
//                    pixmap.drawPixel(x + i, y + j, 0x00000000);
//                }
//            }
//        }
//    }
    // Освобождение ресурсов (добавьте при необходимости)
    public void dispose() {
        if (bgTexture != null) bgTexture.dispose();
        if (knobTexture != null) knobTexture.dispose();
        if (roundTexture != null) roundTexture.dispose();
        if (skin != null) skin.dispose();
    }
}
