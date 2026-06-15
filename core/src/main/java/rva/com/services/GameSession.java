package rva.com.services;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class GameSession {
    private int screenWidth, screenHeight;
    private int paddleLevel, lowBorder, highBorder;
    private int brickWidth, brickHeight;
    private int mainFontSize, titleFontSize;
    private int titleLine, menuLine;
    private int paddleVelocity, paddleWidth, paddleHeight;
    private int ballVelocity, ballWidth, ballHeight;
    private int widthSettingsButton, heightSettingsButton
        , minLineSettingsButton, maxLineSettingsButton
        , xSettingsButton, deltaSettingsButton;

    boolean musicFlag, soundFlag;
    private int score;
    private int lives;
    public GameSession() {
        musicFlag = true;
        soundFlag = true;
        this.paddleVelocity = 10;
        this.paddleWidth = 100;
        this.paddleHeight = 20;
        this.ballVelocity = 100;
        this.ballWidth = 50;
        this.ballHeight = 50;
    }

    public void calcSizes() {
        this.paddleLevel = (int) (0.05 * this.screenHeight);
        this.lowBorder = (int) (0.7 * this.screenHeight);
        this.highBorder = (int) (0.95 * this.screenHeight);
        this.brickWidth = (int) (screenWidth / 10);
        this.brickHeight = (int) ((this.highBorder - this.lowBorder) / 5);
        this.mainFontSize = (int) this.screenHeight / 20;
        this.titleFontSize = (int) this.screenHeight / 18;
        this.titleLine = (int) this.screenHeight - this.titleFontSize;
        this.menuLine = (int) this.screenHeight / 2;
    }

    public void calcSettingsButtonSize(int itemSize) {
        this.minLineSettingsButton = (int) (0.03 * this.screenHeight);
        this.maxLineSettingsButton = (int) (0.6 * this.screenHeight);
        this.deltaSettingsButton = (this.maxLineSettingsButton - this.minLineSettingsButton) / itemSize / 3;
        this.heightSettingsButton = 2 * this.deltaSettingsButton;
        this.xSettingsButton = this.deltaSettingsButton;
        this.widthSettingsButton = this.screenWidth - 2 * this.xSettingsButton;
    }

    /**
     * Определяет операционную систему, в которой запущено приложение
     */
    public String detectOperatingSystem() {
        switch (Gdx.app.getType()) {
            case Application.ApplicationType.Desktop:
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    return "Windows";
                } else if (os.contains("mac")) {
                    return "macOS";
                } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                    return "Linux/Unix";
                } else {
                    return "Desktop (неизвестная ОС)";
                }
            case Application.ApplicationType.Android:
                return "Android";
            case Application.ApplicationType.iOS:
                return "iOS";
            case Application.ApplicationType.WebGL:
                return "WebGL (браузер)";
            default:
                return "Неизвестная платформа";
        }
    }

    public int getPaddleVelocity() {
        return paddleVelocity;
    }

    public void setPaddleVelocity(int paddleVelocity) {
        this.paddleVelocity = paddleVelocity;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getPaddleLevel() {
        return paddleLevel;
    }

    public void setPaddleLevel(int paddleLevel) {
        this.paddleLevel = paddleLevel;
    }

    public int getLowBorder() {
        return lowBorder;
    }

    public void setLowBorder(int lowBorder) {
        this.lowBorder = lowBorder;
    }

    public int getHighBorder() {
        return highBorder;
    }

    public void setHighBorder(int highBorder) {
        this.highBorder = highBorder;
    }

    public int getBrickWidth() {
        return brickWidth;
    }

    public void setBrickWidth(int brickWidth) {
        this.brickWidth = brickWidth;
    }

    public int getBrickHeight() {
        return brickHeight;
    }

    public void setBrickHeight(int brickHeight) {
        this.brickHeight = brickHeight;
    }

    public void dispose() {

    }

    public void resetGame() {
    }

    public int getScore() {
        return this.score;
    }

    public int getLives() {
        return this.lives;
    }

    public void update(float delta) {
    }

    public boolean isGameOver() {
        return this.lives == 0;
    }

    public int getTitleLine() {
        return titleLine;
    }

    public int getMainFontSize() {
        return mainFontSize;
    }

    public int getTitleFontSize() {
        return titleFontSize;
    }

    public int getMenuLine() {
        return menuLine;
    }

    public int getWidthSettingsButton() {
        return widthSettingsButton;
    }

    public int getHeightSettingsButton() {
        return heightSettingsButton;
    }

    public int getMinLineSettingsButton() {
        return minLineSettingsButton;
    }

    public int getMaxLineSettingsButton() {
        return maxLineSettingsButton;
    }

    public int getxSettingsButton() {
        return xSettingsButton;
    }

    public int getDeltaSettingsButton() {
        return deltaSettingsButton;
    }

    public boolean isMusicFlag() {
        return musicFlag;
    }

    public void setMusicFlag(boolean musicFlag) {
        this.musicFlag = musicFlag;
    }

    public boolean isSoundFlag() {
        return soundFlag;
    }

    public void setSoundFlag(boolean soundFlag) {
        this.soundFlag = soundFlag;
    }

    public boolean isLevelCompleted() {
        return true; // !!!
    }

    public int getPaddleWidth() {
        return paddleWidth;
    }

    public void setPaddleWidth(int paddleWidth) {
        this.paddleWidth = paddleWidth;
    }

    public int getPaddleHeight() {
        return paddleHeight;
    }

    public void setPaddleHeight(int paddleHeight) {
        this.paddleHeight = paddleHeight;
    }

    public int getBallVelocity() {
        return ballVelocity;
    }

    public int getBallWidth() {
        return ballWidth;
    }

    public int getBallHeight() {
        return ballHeight;
    }
}
