package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.math.Vector2;
import com.svalero.trafficsurvive.manager.LogicManager;
import com.svalero.trafficsurvive.manager.ResourceManager;

public class CarSpawner {

    private Vector2 position;
    private String direction; // "left" o "right"
    private float spawnTimer = 0f;
    private float spawnDelay = 3.0f; // Cada cuántos segundos sale un coche

    public CarSpawner(Vector2 position, String direction) {
        this.position = position;
        this.direction = direction;
    }

    public void update(float dt, LogicManager logicManager) {
        spawnTimer += dt;

        if (spawnTimer >= spawnDelay) {
            spawnTimer = 0f;
            spawnCar(logicManager);
        }
    }

    private void spawnCar(LogicManager logicManager) {
        // Seleccionamos la textura según la dirección
        String regionName = direction.equals("right") ? "car_enemy1_right" : "car_enemy1_left";
        Character.State carState = direction.equals("right") ? Character.State.MOVE_RIGHT : Character.State.MOVE_LEFT;

        // Creamos el coche
        CarEnemy car = new CarEnemy(
            ResourceManager.getRegion(regionName),
            new Vector2(position.x, position.y),
            carState
        );

        // Lo mandamos a la lista general de enemigos
        logicManager.addEnemy(car);
    }
}
