package com.svalero.trafficsurvive.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.trafficsurvive.domain.Player;
import com.svalero.trafficsurvive.domain.Tree;
import com.svalero.trafficsurvive.manager.ConfigurationManager;
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
