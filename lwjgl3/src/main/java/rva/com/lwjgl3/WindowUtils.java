package rva.com.lwjgl3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;

public class WindowUtils {

    /**
     * Изменяет размер окна игры (работает только на десктопе).
     * @param newWidth  Новая ширина окна.
     * @param newHeight Новая высота окна.
     */
    public static void setWindowSize(int newWidth, int newHeight) {
        // Проверяем, что мы действительно на десктопном бэкенде
        if (Gdx.graphics instanceof Lwjgl3Graphics) {
            ((Lwjgl3Graphics) Gdx.graphics).setWindowedMode(newWidth, newHeight);
        } else {
            Gdx.app.error("WindowUtils", "Изменение размера окна поддерживается только в LWJGL3 бэкенде.");
        }
    }
    // Здесь могут быть другие методы для работы с окном
}
