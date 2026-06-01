package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.trafficsurvive.domain.BubbleEnemy;
import com.svalero.trafficsurvive.domain.Character;
import com.svalero.trafficsurvive.domain.Item;

public class RenderManager implements Disposable {

    private Batch batch;
    private LogicManager logicManager;

    public RenderManager(LogicManager logicManager, Batch batch) {
        this.logicManager = logicManager;
        this.batch = batch;
    }

    public void drawFrame(float v) {
        batch.begin();

        for (Item item : logicManager.getItems()) {
            item.draw(batch);
        }

        logicManager.player.draw(batch);

        for (Character enemy : logicManager.enemies) {
            enemy.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
