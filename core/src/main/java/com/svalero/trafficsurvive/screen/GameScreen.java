package com.svalero.trafficsurvive.screen;


import com.badlogic.gdx.Screen;
import com.svalero.trafficsurvive.manager.LogicManager;
import com.svalero.trafficsurvive.manager.RenderManager;

public class GameScreen implements Screen {

    private RenderManager renderManager;
    private LogicManager logicManager;

    public GameScreen() {
        logicManager = new LogicManager();
        renderManager = new RenderManager(logicManager);
    }

    @Override
    public void show() {
        logicManager.load();
        renderManager.load();
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
