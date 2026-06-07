package rva.com.screens;

public interface GameScreen {
    void show();
    void render(float delta);
    void resize(int width, int height);
    void pause();
    void resume();
    void hide();
    void handle();
    void draw();
    void update(float delta);
    void dispose();
}
