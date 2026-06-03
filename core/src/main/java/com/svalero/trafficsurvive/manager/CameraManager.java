package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

public class CameraManager {

    private LogicManager logicManager;
    private LevelManager levelManager;
    OrthographicCamera camera;

    private final float MAP_WIDTH = 30 * 16f;  // 480 px
    private final float MAP_HEIGHT = 120 * 16f; // 1920 px

    public CameraManager(LogicManager logicManager, LevelManager levelManager) {
        this.logicManager = logicManager;
        this.levelManager = levelManager;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 13*16, 13*16);
        camera.update();
    }

    public void handleCamera() {

        // Conseguimos la posición objetivo
        float targetX = logicManager.player.getPosition().x;
        float targetY = logicManager.player.getPosition().y;

        // Calculamos la mitad del tamaño de la cámara
        float cameraHalfWidth = camera.viewportWidth / 2f;
        float cameraHalfHeight = camera.viewportHeight / 2f;

        // El límite mínimo es la mitad de la cámara, y el máximo es el tamaño del mapa menos esa mitad
        float minX = cameraHalfWidth;
        float maxX = MAP_WIDTH - cameraHalfWidth;
        float minY = cameraHalfHeight;
        float maxY = MAP_HEIGHT - cameraHalfHeight;

        // Usamos
        float clampX = MathUtils.clamp(targetX, minX, maxX);
        float clampY = MathUtils.clamp(targetY, minY, maxY);

        // Centramos la cámara en las coordenadas protegidas
        camera.position.set(clampX, clampY, 0);
        camera.update();

        // Renderizamos el mapa con la vista actualizada
        levelManager.mapRenderer.setView(camera);
        levelManager.mapRenderer.render();
    }
}
