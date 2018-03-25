package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


/**
 * Created by Samu Koivulahti on 24.3.2018.
 */

public class Settings {

    private static Settings settings;
    private Preferences prefs;

    private Settings() {
        prefs = Gdx.app.getPreferences("Rikollisentunnistus.config");
    }

    public float getFloat(String key) {
        return prefs.getFloat(key);
    }
    public void setFloat(String key, float value) {
        prefs.putFloat(key, value);
    }

    public int getInteger(String key) {
        return prefs.getInteger(key);
    }

    public void setInteger(String key, int value) {
        prefs.putInteger(key, value);
    }

    public boolean getBoolean(String key) {
        return prefs.getBoolean(key);
    }

    public void setBoolean(String key, boolean value) {
        prefs.putBoolean(key, value);
    }

    public void saveSettings() {
        prefs.flush();
    }

    public static Settings getInstance() {
        if (settings == null) {
            settings = new Settings();
        }
        return settings;
    }
}
