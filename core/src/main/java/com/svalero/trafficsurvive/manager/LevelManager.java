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
import com.badlogic.gdx.utils.Array;
import com.svalero.trafficsurvive.domain.*;

public class LevelManager {
    private LogicManager logicManager;
    OrthogonalTiledMapRenderer mapRenderer;
    public Batch batch;
    private int currentLevel;
    TiledMap map;
    TiledMapTileLayer collisionLayer;
    MapLayer enemiesLayer;
    MapLayer itemsLayer;
    protected Array<Item> items = new Array<>();

    public LevelManager(LogicManager logicManager) {
        this.logicManager = logicManager;
        this.items = new Array<>();
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

                float x = object.getProperties().get("x", Float.class);
                float y = object.getProperties().get("y", Float.class);

                String objectName = object.getProperties().get("name", String.class);

                if (objectName != null && objectName.equals("bubble_pink")) {

                    BubbleEnemy enemy = new BubbleEnemy(ResourceManager.getRegion("bubble_pink_pos2"), new Vector2(x, y));
                    logicManager.addEnemy(enemy);

                } else if (objectName != null && objectName.equals("car_spawner")) {

                    // Recogemos la dirección de tu otra propiedad personalizada
                    String direction = object.getProperties().get("direction", "right", String.class);

                    CarSpawner spawner = new CarSpawner(new Vector2(x, y), direction);
                    logicManager.addSpawner(spawner);
                }
            }
        }
    }

    // Comprueba si una coordenada del nivel colisiona con el terreno
    public boolean isCellCellBlocked(float x, float y) {
        // Convertimos los píxeles del juego a coordenadas de la rejilla de Tiled
        int cellX = (int) (x / collisionLayer.getTileWidth());
        int cellY = (int) (y / collisionLayer.getTileHeight());

        // Si el jugador se sale de los límites del mapa, decimos que está bloqueado
        if (cellX < 0 || cellX >= collisionLayer.getWidth() || cellY < 0 || cellY >= collisionLayer.getHeight()) {
            return true;
        }

        // Conseguimos la celda de esa posición
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(cellX, cellY);

        // Si la celda no es nula, significa que contiene un azulejo del terreno, por lo que colisiona
        return cell != null;
    }



    private void loadItems() {
        for (MapObject mapObject : itemsLayer.getObjects()) {

            if (mapObject instanceof TiledMapTileMapObject) {
                TiledMapTileMapObject object = (TiledMapTileMapObject) mapObject;

                float x = object.getProperties().get("x", Float.class);
                float y = object.getProperties().get("y", Float.class);

                String itemName = object.getProperties().get("name", String.class);

                if (itemName != null && itemName.equals("item_coin")) {
                    CoinItem item = new CoinItem(ResourceManager.getRegion("gold_coin"), new Vector2(x, y));
                    logicManager.addItem(item);

                } else if (itemName != null && itemName.equals("item_diamond")) {
                    DiamondItem item = new DiamondItem(ResourceManager.getRegion("diamante"), new Vector2(x, y));
                    logicManager.addItem(item);

                } else if (itemName != null && itemName.equals("item_life")) {
                    LifeItem item = new LifeItem(ResourceManager.getRegion("heart"), new Vector2(x, y));
                    logicManager.addItem(item);
                }
            }
        }
    }

    public void restartCurrentLevel() {

    }
}
