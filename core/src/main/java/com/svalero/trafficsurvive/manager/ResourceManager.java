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
    }

    public static boolean update() {
        return assetManager.update();
    }


    public static void loadMusics() {
        assetManager.load("sounds/background.mp3", Music.class);
    }


    public static Music getMusic(String name) {
        return assetManager.get("sounds/" + name, Music.class);
    }

    public static TextureRegion getRegion(String name) {
        return assetManager.get("trafficsurvival.atlas", TextureAtlas.class).findRegion(name);
    }

    public static Array<TextureAtlas.AtlasRegion> getRegions(String name) {
        return assetManager.get("trafficsurvival.atlas", TextureAtlas.class).findRegions(name);
    }
}
