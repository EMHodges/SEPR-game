package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Sound Effects Manager for all in-game audio, accessed via static context.
 * All Music and SoundFX can be enabled/disabled with the StopMusic/PlayMusic.
 */

public class SoundFX {

    /** Used only for the truck attacking sound. True if it is playing, false if it isn't */
    public static boolean isPlaying = false;

    /** All sounds can be played when this is true, else no sound will play */
    public static boolean music_enabled = true;

    public static final Music sfx_menu = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu.mp3"));
    public static final Music sfx_soundtrack = Gdx.audio.newMusic(Gdx.files.internal("sounds/soundtrack.mp3"));

    public static final Sound sfx_truck_attack = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/truck_attack.wav"));
    public static final Sound sfx_truck_damage = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/truck_damage.wav"));
    public static final Sound sfx_truck_spawn = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/truck_spawn.wav"));
    public static final Sound sfx_fortress_destroyed = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/fortress_destroyed.wav"));
    public static final Sound sfx_fortress_attack = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/fortress_attack.wav"));
    public static final Sound sfx_pause = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/pause.wav"));
    public static final Sound sfx_unpause = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/unpause.wav"));
    public static final Sound sfx_horn = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/horn.mp3"));
    public static final Sound sfx_button_clicked = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/button_clicked.wav"));

    /** Plays attacking sound for FireTrucks only if it isn't already playing */
    public static void playTruckAttack() {
        if (!isPlaying) {
            sfx_truck_attack.loop();
            sfx_truck_attack.play();
            isPlaying = true;
        }
    }

    /** Stops the sound of FireTrucks attacking and resets isPlaying to false */
    public static void stopTruckAttack() {
        sfx_truck_attack.stop();
        isPlaying = false;
    }
    /** Plays game music */
    public static void playGameMusic() {
        sfx_soundtrack.play();
        music_enabled = true;
    }

    /** Plays menu music */
    public static void playMenuMusic() {
        sfx_menu.play();
        music_enabled = true;
    }

    /** Stops both menu music and game music */
    public static void stopMusic() {
        sfx_soundtrack.stop();
        sfx_menu.stop();
        music_enabled = false;
    }

}
