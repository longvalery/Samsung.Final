package rva.com.services;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import rva.com.Main;
import rva.com.managers.MemoryManager;

public class GameSession {
    private int screenWidth, screenHeight;
    private int paddleLevel, lowBorder, highBorder;
    private int brickWidth, brickHeight;
    private int mainFontSize, titleFontSize, smallFontSize;
    private int titleLine, menuLine;
    private int paddleVelocity, paddleWidth, paddleHeight;
    private int ballVelocity, ballWidth, ballHeight;
    private int bombHeight, bombWidth;

    private int tableCellPad, tablePad, tableColumn1Width, tableColumn2Width, tableColumn3Width;

    private float musicVolume, soundVolume;

    private int widthSettingsButton, heightSettingsButton
        , minLineSettingsButton, maxLineSettingsButton
        , xSettingsButton, deltaSettingsButton;

    private int score;
    private int lives;
    private Main game;
    private int bonbonSize, bonbonVelocity;

    public GameSession(Main game) {
        this.game = game;
        this.paddleVelocity = 10;
        this.ballVelocity = 500;
    }

    public void calcSizes(int bricksLine, int bricksInLine) {
        this.paddleLevel = (int) (0.05 * this.screenHeight);
        this.lowBorder = (int) (0.6 * this.screenHeight);
        this.highBorder = (int) (0.85 * this.screenHeight);
        this.brickWidth = (int) (screenWidth / bricksInLine);
        this.brickHeight = (int) ((this.highBorder - this.lowBorder) / bricksLine);
        this.mainFontSize = (int) this.screenHeight / 20;
        this.smallFontSize = (int) (0.7 * this.mainFontSize);
        this.titleFontSize = (int) this.screenHeight / 18;
        this.titleLine = (int) this.screenHeight - this.titleFontSize;
        this.menuLine = (int) this.screenHeight / 2;
        this.ballWidth = (int) (0.05 *screenWidth);
        this.ballHeight = this.ballWidth;
        this.paddleWidth = (int) (0.2 * screenWidth);
        this.paddleHeight = this.paddleWidth / 5;
        this.bonbonSize = (int) (0.8 * this.ballWidth);
        this.bonbonVelocity = (int) (0.8 * this.ballVelocity);
        this.bombHeight = 2 * this.ballWidth;
        this.bombWidth = bombHeight;
        calcTablesSizes();
    }


    private void calcTablesSizes() {
        this.tableCellPad = 5;
        this.tablePad = 4 * this.tableCellPad;
        int width = (int) ((this.screenWidth - 2 * this.tablePad) / 7);
        this.tableColumn1Width = 2 * width;
        this.tableColumn2Width =  2 * width;
        this.tableColumn3Width = 3 * width;
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


    public void endGame() {
        MemoryManager.saveMusicVolume(this.getMusicVolume());
        MemoryManager.saveSoundVolume(this.getSoundVolume());
//        if (this.score > 0) { game.getRecordsTable().addResult(this.score); }
        MemoryManager.saveTableOfRecords(game.getRecordsTable().toJson());
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
        this.score = 0;
        this.lives = 3;
    }

    public int getBombHeight() { return bombHeight; }

    public int getBombWidth() { return bombWidth; }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return this.lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
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

    public int getBallHeight() { return ballHeight; }

    public int getBonbonSize() { return bonbonSize; }

    public int getSmallFontSize() {
        return smallFontSize;
    }

    public float getMusicVolume() { return musicVolume; }

    public void setMusicVolume(float musicVolume) { this.musicVolume = musicVolume; }

    public float getSoundVolume() { return soundVolume; }

    public void setSoundVolume(float soundVolume) { this.soundVolume = soundVolume;  }

    public int getTableCellPad() { return tableCellPad;  }

    public int getTablePad() { return tablePad; }

    public int getTableColumn1Width() { return tableColumn1Width; }

    public int getTableColumn2Width() { return tableColumn2Width; }

    public int getBonbonVelocity() { return bonbonVelocity; }
    public int getTableColumn3Width() { return tableColumn3Width; }
}
