package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.trafficsurvive.domain.BubbleEnemy;
import com.svalero.trafficsurvive.domain.Player;
import com.svalero.trafficsurvive.screen.ConfigurationScreen;

public class LogicManager implements Disposable {

    protected Player player;
    protected BubbleEnemy bubbleEnemy;
    private Music backgroundMusic;

    public LogicManager() {
        backgroundMusic = ResourceManager.getMusic("background.mp3");
    }

    public void load() {
        player = new Player(ResourceManager.getRegion("player3_idle_right"));
        bubbleEnemy = new BubbleEnemy(ResourceManager.getRegion("bubble_pink_pos2"), new Vector2(800,200));

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
        player.update(v);
        bubbleEnemy.update(v);

//        //si chocan, lo devolvemos a la posición segura
//        if (player.getRectangle().overlaps(tree.getRectangle())) {
//            player.getPosition().x = oldX;
//            player.getPosition().y = oldY;
//
//            player.getPosition().y -= 5;
//            player.getRectangle().setPosition(oldX, oldY);
//
//            if (ConfigurationManager.isSoundEnabled()) {
//                collisionSound.play();
//            }
//        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            // Volver al menú principal
            ((Game) Gdx.app.getApplicationListener()).setScreen(new ConfigurationScreen());
        }
    }


    @Override
    public void dispose() {
        player.dispose();
    }
}
