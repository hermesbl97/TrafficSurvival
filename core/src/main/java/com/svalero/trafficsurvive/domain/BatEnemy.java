package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.svalero.trafficsurvive.manager.ResourceManager;
import lombok.Getter;

import static com.svalero.trafficsurvive.domain.Character.State.MOVE_LEFT;

public class BatEnemy extends Character{
        private float speed = 105f;
        private float lifeTimer = 0f;       // Tiempo de vida activo
        private final float MAX_TIME = 20f; // Se irá a los 20 segundos
        @Getter
        private boolean shouldRemove = false;
        private Player target;              // El jugador al que debe perseguir

    public BatEnemy(float spawnX, float spawnY, Player target) {
        super(ResourceManager.getRegion("bat_pos1"), new Vector2(spawnX, spawnY), MOVE_LEFT);
        this.target = target;
        this.stateTime = 0f;

        // Configuramos la animación del aleteo con bat_1 y bat_2
        this.movingFrontAnimation = new Animation<>(0.12f,
            ResourceManager.getRegion("bat_pos1"),
            ResourceManager.getRegion("bat_pos2")
        );

        // Ajustamos su tamaño de colisión
        this.rectangle.setSize(16, 16);
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        lifeTimer += dt;

        // A los 20 segundos desaparece
        if (lifeTimer >= MAX_TIME) {
            shouldRemove = true;
            return;
        }

        // Obtener el frame de la animación de vuelo continuamente
        currentFrame = movingFrontAnimation.getKeyFrame(stateTime, true);

        // Persecució n inteligente. Calculamos el vector dirección restando la posición del objetivo menos la del murciélago
        Vector2 direction = new Vector2(target.getPosition().x - position.x, target.getPosition().y - position.y);

        // Normalizamos el vector para que su longitud sea 1 (así mantiene velocidad constante)
        direction.nor();

        // Desplazamos al murciélago hacia el jugador multiplicando por la velocidad y el delta time
        position.x += direction.x * speed * dt;
        position.y += direction.y * speed * dt;

        // Sincronizamos su caja de colisiones
        rectangle.setPosition(position.x, position.y);
    }

    @Override
    public void draw(Batch batch) {
        // Si el jugador está a la derecha del murciélago (target.x > position.x),
        // activamos el volteo horizontal si está a la izquierda, false.
        boolean flipX = target.getPosition().x > position.x;

        // Dibujamos el frame de forma segura sin alterar permanentemente el Atlas
        batch.draw(
            currentFrame.getTexture(),
            position.x,
            position.y,
            currentFrame.getRegionWidth(),
            currentFrame.getRegionHeight(),
            currentFrame.getRegionX(),
            currentFrame.getRegionY(),
            currentFrame.getRegionWidth(),
            currentFrame.getRegionHeight(),
            flipX,  // Se voltea horizontalmente solo si flipX es true
            false   // El volteo vertical se queda en false, para que no vaya boca abajo
        );
    }

}
