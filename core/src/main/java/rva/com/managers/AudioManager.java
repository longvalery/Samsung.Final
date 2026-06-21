package rva.com.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import rva.com.services.GameResources;

public class AudioManager {
    private Music backgroundMusic;
    private Sound shootSound;
    private Sound explosionSound;
    private Sound win;
    private Sound lose;

    public AudioManager(float volume) {
        this.backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.BACKGROUND_MUSIC_PATH));
        this.shootSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.SHOOT_SOUND_PATH));
        this.explosionSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.DESTROY_SOUND_PATH));
        this.win = Gdx.audio.newSound(Gdx.files.internal(GameResources.WIN_SOUND_PATH));
        this.lose = Gdx.audio.newSound(Gdx.files.internal(GameResources.LOSE_SOUND_PATH));
        this.backgroundMusic.setVolume(volume); // принимает значение от 0 до 1
        this.backgroundMusic.setLooping(true);
        this.backgroundMusic.play();
    }

    public Music getBackgroundMusic() {
        return backgroundMusic;
    }

    public Sound getShootSound() {
        return shootSound;
    }

    public Sound getExplosionSound() {
        return explosionSound;
    }

    public Sound getWin() { return win; }

    public Sound getLose() { return lose; }

    public void dispose() {
        this.backgroundMusic.dispose();
        this.explosionSound.dispose();
        this.shootSound.dispose();
        this.win.dispose();
        this.lose.dispose();
    };
}
