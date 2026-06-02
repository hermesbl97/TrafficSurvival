package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.svalero.trafficsurvive.manager.ResourceManager;

public class AlienEnemy extends Character{

    public AlienEnemy(Vector2 position) {
        super(ResourceManager.getRegion("alien_dance_1"), position, State.IDLE_FRONT);

        this.stateTime = 0f;

        // Coreografía baile
        Array<TextureRegion> danceFrames = new Array<>();
        danceFrames.add(ResourceManager.getRegion("alien_dance_1"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_2"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_3"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_4"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_3"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_4"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_3"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_4"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_5"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_6"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_5"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_6"));
        danceFrames.add(ResourceManager.getRegion("alien_dance_7"));

        // Se ejecuta en bucle la secuencia de animaciones
        this.movingFrontAnimation = new Animation<>(0.15f, danceFrames, Animation.PlayMode.LOOP);
        this.rectangle.setSize(16, 16);
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        currentFrame = movingFrontAnimation.getKeyFrame(stateTime, true);
        rectangle.setPosition(position.x, position.y);
    }
}
