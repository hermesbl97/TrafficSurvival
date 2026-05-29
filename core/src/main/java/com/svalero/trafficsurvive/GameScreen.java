package com.svalero.trafficsurvive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Texture playerTexture;
    private Vector2 playerPosition;
    private Rectangle playerRectangle;
    private Texture treeTexture;
    private Rectangle treeRectangle;

    @Override
    public void show() {
        playerTexture = new Texture(Gdx.files.internal("tile_0024.png"));
        playerPosition = new Vector2(100, 100);
        playerRectangle = new Rectangle(playerPosition.x, playerPosition.y, playerTexture.getWidth(), playerTexture.getHeight());

        treeTexture = new Texture(Gdx.files.internal("tile_0238.png"));
        treeRectangle = new Rectangle(350, 200, treeTexture.getWidth(), treeTexture.getHeight());

        batch = new SpriteBatch();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Limpiamos la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float oldX = playerPosition.x;
        float oldY = playerPosition.y;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerPosition.x -= 10;
            playerRectangle.setPosition(playerPosition.x, playerPosition.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerPosition.x += 10;
            playerRectangle.setPosition(playerPosition.x, playerPosition.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerPosition.y += 10;
            playerRectangle.setPosition(playerPosition.x, playerPosition.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerPosition.y -= 10;
            playerRectangle.setPosition(playerPosition.x, playerPosition.y);
        }

        // Actualizamos posición después del movimiento
        playerRectangle.setPosition(playerPosition.x, playerPosition.y);

        //si chocan, lo devolvemos a la posición segura
        if (playerRectangle.overlaps(treeRectangle)) {
            playerPosition.x = oldX;
            playerPosition.y = oldY;

            playerPosition.y -= 5;

            playerRectangle.setPosition(playerPosition.x, playerPosition.y);
        }

        batch.begin();
        batch.draw(playerTexture, playerPosition.x,playerPosition.y);
        batch.draw(treeTexture, treeRectangle.x, treeRectangle.y);

        batch.end();
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
        playerTexture.dispose();
    }
}
