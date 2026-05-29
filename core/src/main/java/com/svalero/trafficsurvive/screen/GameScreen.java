package com.svalero.trafficsurvive.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
    private Sound collisionSound;
    private Music backgroundMusic;

    @Override
    public void show() {
        player = new Player(new Texture(Gdx.files.internal("textures/tile_0024.png")));
        tree = new Tree(new Texture(Gdx.files.internal("textures/tile_0238.png")));
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bump.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background.mp3"));

        batch = new SpriteBatch();

        backgroundMusic.play();
        backgroundMusic.setVolume(0.3f);
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
            player.getRectangle().setPosition(oldX, oldY);
            collisionSound.play();
        }

        batch.begin();
        player.draw(batch);
        tree.draw(batch);
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            // Volver al menú principal
            ((Game) Gdx.app.getApplicationListener()).setScreen(new ConfigurationScreen());
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
        player.dispose();
        tree.dispose();
    }
}
