package rva.com.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import rva.com.Main;

public class InputManager implements InputProcessor {
    private Main game;
    private long touchStartTime = -1;
    private static final long LONG_PRESS_DURATION = 1000;

    public InputManager(Main game) {
        this.game = game;
    }

    public void update() {
        // Обработка клавиши SPACE
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) { handleMenuTransition(); }

        // Обработка касания
        if (Gdx.input.isTouched()) {
            if (touchStartTime == -1) {
                touchStartTime = System.currentTimeMillis();
                                      }
            else if (System.currentTimeMillis() - touchStartTime >= LONG_PRESS_DURATION) {
                handleMenuTransition();
                touchStartTime = -1; // Сброс после срабатывания
                                                                                        }
                                   }
        else {
            touchStartTime = -1; // Касание прервано
              }
                         }

    private void handleMenuTransition() {
        game.setScreen(game.getMenu());
    }

    // Пустые реализации методов InputProcessor
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchUp(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
}
