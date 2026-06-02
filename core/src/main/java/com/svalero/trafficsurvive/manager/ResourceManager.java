package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class ResourceManager {

    private static AssetManager assetManager = new AssetManager();

    public static void loadAllResources() {
        //Cargamos las texturas del juego
        assetManager.load("trafficsurvival.atlas", TextureAtlas.class); // cargamos TextureAtlas
        loadMusics();
        loadSounds();
    }

    public static boolean update() {
        return assetManager.update();
    }


    public static void loadMusics() {
        assetManager.load("sounds/background.mp3", Music.class);
        assetManager.load("sounds/bat_wings.mp3", Music.class);
    }

    public static void loadSounds() {
        assetManager.load("sounds/bump.mp3", Sound.class);
        assetManager.load("sounds/crash.mp3", Sound.class);
        assetManager.load("sounds/getCoin.mp3", Sound.class);
        assetManager.load("sounds/getDiamond.mp3", Sound.class);
        assetManager.load("sounds/getLife.mp3", Sound.class);
        assetManager.load("sounds/removeLife.mp3", Sound.class);
        assetManager.load("sounds/victory.mp3", Sound.class);
        assetManager.load("sounds/bite.mp3", Sound.class);
    }

    public static Music getMusic(String name) {
        return assetManager.get("sounds/" + name, Music.class);
    }

    public static Sound getSound(String name) {
        return assetManager.get("sounds/" + name, Sound.class);
    }

    public static TextureRegion getRegion(String name) {
        return assetManager.get("trafficsurvival.atlas", TextureAtlas.class).findRegion(name);
    }

    public static Array<TextureAtlas.AtlasRegion> getRegions(String name) {
        return assetManager.get("trafficsurvival.atlas", TextureAtlas.class).findRegions(name);
    }
}
