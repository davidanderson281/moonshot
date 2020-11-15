package com.first.gamedemomoonshot.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Prefs {

    private Preferences pref;
    private boolean hasSound;

    public Prefs() {
        pref = Gdx.app.getPreferences("moonshot");
        hasSound = pref.getBoolean("hasSound", true);
    }

    public void setSound(boolean hasSound){
        this.hasSound = hasSound;
        pref.putBoolean("hasSound", hasSound);
        pref.flush();
    }

    public boolean isSoundActive() {
        return hasSound;
    }
}
