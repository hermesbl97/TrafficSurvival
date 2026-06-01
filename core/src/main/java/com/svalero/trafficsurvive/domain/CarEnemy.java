package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.svalero.trafficsurvive.manager.ResourceManager;
import lombok.Data;

@Data
public class CarEnemy extends Character {

    private float speed = 150f;
    private boolean shouldRemove = false;

    // El coche está compuesto por dos pixeles
    private TextureRegion parteTrasera;
    private TextureRegion parteDelantera;

    public CarEnemy(TextureRegion texture, Vector2 position, State initialDirection) {
        super(texture, position, initialDirection);

        // Cargamos ambas partes según hacia dónde se mueva el coche
        if (state == State.MOVE_RIGHT) {
            parteTrasera = ResourceManager.getRegion("car_enemy1_right");
            parteDelantera = ResourceManager.getRegion("car_enemy2_right");
        } else {
            parteDelantera = ResourceManager.getRegion("car_enemy1_left");
            parteTrasera = ResourceManager.getRegion("car_enemy2_left");
        }

        // El coche mide 32 píxeles de ancho y 16 de alto
        this.rectangle.setSize(32, 16);
    }

    @Override
    public void update(float dt) {

        // Movemos la posición del coche
        if (state == State.MOVE_RIGHT) {
            position.x += speed * dt;
        } else if (state == State.MOVE_LEFT) {
            position.x -= speed * dt;
        }

        // Sincronizamos el rectángulo de colisión
        rectangle.setPosition(position.x, position.y);

        // Límites de borrado de la pantalla
        if (position.x > 600 || position.x < -150) {
            shouldRemove = true;
        }
    }

    @Override
    public void draw(Batch batch) {
        if (state == State.MOVE_RIGHT) {
            // Si va a la derecha: primero la parte trasera, y 16 píxeles más a la derecha la delantera
            batch.draw(parteTrasera, position.x, position.y);
            batch.draw(parteDelantera, position.x + 16, position.y);
        } else {
            // Si va a la izquierda: primero la parte delantera, y 16 píxeles más a la derecha la trasera
            batch.draw(parteDelantera, position.x, position.y);
            batch.draw(parteTrasera, position.x + 16, position.y);
        }
    }

    public boolean isShouldRemove() {
        return shouldRemove;
    }
}
