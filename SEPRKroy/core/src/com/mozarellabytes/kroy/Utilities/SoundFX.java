package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundFX {
    public static Music sfx_menu = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu.mp3"));
    public static Music sfx_soundtrack = Gdx.audio.newMusic(Gdx.files.internal("sounds/soundtrack.mp3"));

    public static Sound sfx_truck_attack = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/truck_attack.wav"));
    public static Sound sfx_truck_damage = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/truck_damage.wav"));
    public static Sound sfx_truck_spawn = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/truck_spawn.wav"));
    public static Sound sfx_fortress_destroyed = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/fortress_destroyed.wav"));
    public static Sound sfx_fortress_attack = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/fortress_attack.wav"));
    public static Sound sfx_pause = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/pause.wav"));
    public static Sound sfx_unpause = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/unpause.wav"));
    public static Sound sfx_horn = Gdx.audio.newSound(Gdx.files.internal("sounds/sfx/horn.mp3"));


}
