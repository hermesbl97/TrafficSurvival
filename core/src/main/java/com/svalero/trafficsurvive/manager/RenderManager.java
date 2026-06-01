package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.trafficsurvive.domain.BubbleEnemy;

public class RenderManager implements Disposable {

    private Batch batch;
    private LogicManager logicManager;

    public RenderManager(LogicManager logicManager, Batch batch) {
        this.logicManager = logicManager;
        this.batch = batch;
    }

    public void drawFrame(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Limpiamos la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        logicManager.player.draw(batch);
        for(BubbleEnemy enemy : logicManager.enemies) {
            enemy.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
