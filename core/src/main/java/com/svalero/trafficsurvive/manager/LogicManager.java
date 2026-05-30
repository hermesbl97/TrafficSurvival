package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.trafficsurvive.domain.Player;
import com.svalero.trafficsurvive.domain.Tree;
import com.svalero.trafficsurvive.screen.ConfigurationScreen;

public class LogicManager implements Disposable {

    protected Player player;
    protected Tree tree;
    private Sound collisionSound;
    private Music backgroundMusic;

    public LogicManager() {

    }

    public void load() {
        player = new Player(new Texture(Gdx.files.internal("textures/tile_0024.png")));
        tree = new Tree(new Texture(Gdx.files.internal("textures/tile_0238.png")));
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bump.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/background.mp3"));

        if (ConfigurationManager.isMusicEnabled()) {
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.3f);
            backgroundMusic.play();
        }
    }

    public void update(float v) {
        float oldX = player.getPosition().x;
        float oldY = player.getPosition().y;

        player.handleInput(v);

        //si chocan, lo devolvemos a la posición segura
        if (player.getRectangle().overlaps(tree.getRectangle())) {
            player.getPosition().x = oldX;
            player.getPosition().y = oldY;

            player.getPosition().y -= 5;
            player.getRectangle().setPosition(oldX, oldY);

            if (ConfigurationManager.isSoundEnabled()) {
                collisionSound.play();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            // Volver al menú principal
            ((Game) Gdx.app.getApplicationListener()).setScreen(new ConfigurationScreen());
        }
    }


    @Override
    public void dispose() {
        player.dispose();
        tree.dispose();
    }
}
