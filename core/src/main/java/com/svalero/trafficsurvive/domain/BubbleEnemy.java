package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.trafficsurvive.manager.ResourceManager;

import static com.svalero.trafficsurvive.domain.Character.State.MOVE_LEFT;

public class BubbleEnemy extends Character {

    private Player player;
    private boolean active = false;
    private final float ACTIVATION_DISTANCE_Y = 4 * 16f;

    public BubbleEnemy(TextureRegion texture, Vector2 position, Player player) {
        super(texture, position, MOVE_LEFT);

        this.player = player;

        stateTime = 0f;

        movingLeftAnimation = new Animation<>(0.15f,
            ResourceManager.getRegion("bubble_pink_pos1"),
            ResourceManager.getRegion("bubble_pink_pos2"),
            ResourceManager.getRegion("bubble_pink_pos3"),
            ResourceManager.getRegion("bubble_pink_pos2")
        );
    }

    @Override
    public void update(float dt) {

        // Si no está activa, comprueba si el jugador está a una altura próxima
        if (!active) {
            float distanceY = Math.abs(this.position.y - player.getPosition().y);

            //Si la distancia y de distancia con el jugador es menor o igual a la definida se activa la burbuja
            if (distanceY <= ACTIVATION_DISTANCE_Y) {
                active = true;
            }
        }

        stateTime += dt;

        //  Extraemos el fotograma de la animación
        currentFrame = movingLeftAnimation.getKeyFrame(stateTime, true);

        // Movemos al enemigo hacia la izquierda
        if (active) {
            position.x -= 80 * dt;
        }

        // Actualizamos el rectángulo de colisiones para que siga al sprite
        rectangle.setPosition(position.x, position.y);
    }
}
