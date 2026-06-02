package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.trafficsurvive.manager.ResourceManager;

public class ElderlyEnemy extends Character {

    private float startX;
    private float maxDistance = 10 * 16f; // 10 casillas de desplazamiento máximo
    private float speed = 40f;

    public ElderlyEnemy(Vector2 position) {
        super(ResourceManager.getRegion("elderly_idle_left"), position, State.MOVE_LEFT);

        this.startX = position.x;
        this.stateTime = 0f;

        movingLeftAnimation = new Animation<>(0.20f,
            ResourceManager.getRegion("elderly_move_left"),
            ResourceManager.getRegion("elderly_move_left2")
        );
        movingRightAnimation = new Animation<>(0.20f,
            ResourceManager.getRegion("elderly_move_right"),
            ResourceManager.getRegion("elderly_move_right2")
        );

        this.rectangle.setSize(16, 16);
    }

    @Override
    public void update(float dt) {
        stateTime += dt;

        if (state == State.IDLE_BACK) {
            currentFrame = ResourceManager.getRegion("elderly_idle_back");
        } else if (state == State.MOVE_LEFT) {
            position.x -= speed * dt;
            currentFrame = movingLeftAnimation.getKeyFrame(stateTime, true);

            // Si llega al tope izquierdo (10 celdas desde el origen), da la vuelta
            if (position.x <= startX - maxDistance) {
                position.x = startX - maxDistance;
                state = State.MOVE_RIGHT;
            }
        } else if (state == State.MOVE_RIGHT){
            position.x += speed * dt;
            currentFrame = movingRightAnimation.getKeyFrame(stateTime, true);

            // Regresa a la posicion original y vuelve a ir a la izquierda
            if (position.x >= startX) {
                position.x = startX;
                state = State.MOVE_LEFT;
            }
        }

        // Sincronizamos la caja de colisiones
        rectangle.setPosition(position.x, position.y);
    }
}
