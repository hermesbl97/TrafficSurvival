package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import lombok.Data;

import static com.svalero.trafficsurvive.util.Constants.PLAYER_SPEED;

@Data
public class Player extends Character implements Disposable {

    public Player(TextureRegion texture) {
        super(texture, new Vector2(100, 100));
    }

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void handleInput(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= PLAYER_SPEED * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += PLAYER_SPEED * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y += PLAYER_SPEED * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y -= PLAYER_SPEED * delta;
        }

        // Actualizamos posición después del movimiento
        rectangle.setPosition(position.x, position.y);

    }

    @Override
    public void dispose() {
    }
}
