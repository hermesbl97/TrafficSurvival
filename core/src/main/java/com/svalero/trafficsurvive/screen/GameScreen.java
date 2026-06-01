package com.svalero.trafficsurvive.screen;


import com.badlogic.gdx.Screen;
import com.svalero.trafficsurvive.manager.LevelManager;
import com.svalero.trafficsurvive.manager.LogicManager;
import com.svalero.trafficsurvive.manager.RenderManager;

public class GameScreen implements Screen {

    private RenderManager renderManager;
    private LogicManager logicManager;
    private LevelManager levelManager;

    public GameScreen() {
        logicManager = new LogicManager();
        levelManager = new LevelManager(logicManager);
        levelManager.loadCurrentLevel();
        renderManager = new RenderManager(logicManager, levelManager.batch);
    }

    @Override
    public void show() {
        logicManager.load();
    }

    @Override
    public void render(float v) {
        logicManager.update(v);
        renderManager.drawFrame(v);
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        logicManager.dispose();
        renderManager.dispose();
    }
}
