package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraManager {

    private LogicManager logicManager;
    private LevelManager levelManager;
    OrthographicCamera camera;
    private CameraManager cameraManager;

    public CameraManager(LogicManager logicManager, LevelManager levelManager) {
        this.logicManager = logicManager;
        this.levelManager = levelManager;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 13*16, 13*16);
        camera.update();
    }

    public void handleCamera() {
        camera.position.set(logicManager.player.getPosition().x, logicManager.player.getPosition().y, 0);
        camera.update();

        levelManager.mapRenderer.setView(camera);

        levelManager.mapRenderer.render(new int[]{0, 1, 2,5});
    }
}
