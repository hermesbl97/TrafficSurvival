package com.svalero.trafficsurvive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Texture playerTexture;
    private Vector2 playerPosition;
    private Texture treeTexture;

    @Override
    public void show() {
        playerTexture = new Texture(Gdx.files.internal("tile_0024.png"));
        treeTexture = new Texture(Gdx.files.internal("tile_0238.png"));

        playerPosition = new Vector2(100, 100);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Limpiamos la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(playerTexture, playerPosition.x,playerPosition.y);
        batch.draw(treeTexture, 350, 200);

        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerPosition.x -= 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerPosition.x += 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerPosition.y += 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerPosition.y -= 10;
        }
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
