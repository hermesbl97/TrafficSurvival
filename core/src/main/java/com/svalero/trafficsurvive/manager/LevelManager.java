package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.svalero.trafficsurvive.domain.BubbleEnemy;

public class LevelManager {
    private LogicManager logicManager;
    OrthogonalTiledMapRenderer mapRenderer;
    public Batch batch;
    private int currentLevel;
    TiledMap map;
    TiledMapTileLayer collisionLayer;
    MapLayer enemiesLayer;
    MapLayer itemsLayer;

    public LevelManager(LogicManager logicManager) {
        this.logicManager = logicManager;

        currentLevel = 2;
    }

    public void loadCurrentLevel() {
        map = new TmxMapLoader().load("maps/level" + currentLevel+".tmx");
        collisionLayer = (TiledMapTileLayer) map.getLayers().get("terrain");
        enemiesLayer = map.getLayers().get("enemies");
        itemsLayer = map.getLayers().get("items");

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        batch = mapRenderer.getBatch();

        loadEnemies();
        loadItems();
    }

    private void loadEnemies() {
        for (MapObject mapObject : enemiesLayer.getObjects()) {

            if (mapObject instanceof TiledMapTileMapObject) {
                TiledMapTileMapObject object = (TiledMapTileMapObject) mapObject;

                if (object.getProperties().get("name") != null &&
                    object.getProperties().get("name").equals("bubble_pink")) {

                    float x = object.getX();
                    float y = object.getY();

                    BubbleEnemy enemy = new BubbleEnemy(ResourceManager.getRegion("bubble_pink_pos2"), new Vector2(x, y));
                    logicManager.addEnemy(enemy);
                }
            }
        }
    }

    private void loadItems() {

    }

    public void restartCurrentLevel() {

    }
}
