package rva.com.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

public class MemoryManager {
    private static final Preferences preferences = Gdx.app.getPreferences("User saves");

    public static void saveSoundVolume(float value) {
        preferences.putFloat("Sound", value);
        preferences.flush();
    }
    public static float loadSoundVolume() {
        return preferences.getFloat("Sound");
    }
    public static void saveMusicVolume(float value) {
        preferences.putFloat("Music", value);
        preferences.flush();
    }
    public static float loadMusicVolume() {
        return preferences.getFloat("Music");
    }

    public static void saveTableOfRecords(String data) {
        preferences.putString("recordTable", data);
        preferences.flush();
    }

    public static String loadRecordsTable() {
        if (!preferences.contains("recordTable")){ return null; }
        String scores = preferences.getString("recordTable");
        return scores;
    }

}
