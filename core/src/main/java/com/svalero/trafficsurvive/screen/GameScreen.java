package com.svalero.trafficsurvive.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.svalero.trafficsurvive.domain.Player;
import com.svalero.trafficsurvive.domain.Tree;

public class GameScreen implements Screen {

    private SpriteBatch batch;
    private Player player;
    private Tree tree;

    @Override
    public void show() {
        player = new Player(new Texture(Gdx.files.internal("tile_0024.png")));
        tree = new Tree(new Texture(Gdx.files.internal("tile_0238.png")));

        batch = new SpriteBatch();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1); // Limpiamos la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float oldX = player.getPosition().x;
        float oldY = player.getPosition().y;

        player.handleInput(v);

        //si chocan, lo devolvemos a la posición segura
        if (player.getRectangle().overlaps(tree.getRectangle())) {
            player.getPosition().x = oldX;
            player.getPosition().y = oldY;

            player.getPosition().y -= 5;
            player.getRectangle().setPosition(player.getPosition().x, player.getPosition().y);
        }

        batch.begin();
        player.draw(batch);
        tree.draw(batch);
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
        player.dispose();
        tree.dispose();
    }
}
