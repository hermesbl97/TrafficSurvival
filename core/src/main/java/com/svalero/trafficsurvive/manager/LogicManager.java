package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.trafficsurvive.domain.BubbleEnemy;
import com.svalero.trafficsurvive.domain.Player;
import com.svalero.trafficsurvive.screen.ConfigurationScreen;

public class LogicManager implements Disposable {

    protected Player player;
    protected Array<BubbleEnemy> enemies;
    private Music backgroundMusic;
    private Sound collisionSound;

    public LogicManager() {
        backgroundMusic = ResourceManager.getMusic("background.mp3");
        collisionSound = ResourceManager.getSound("bump.mp3");
        this.enemies = new Array<>();

    }

    public void load() {
        player = new Player(ResourceManager.getRegion("player3_idle_right"));

        if (ConfigurationManager.isMusicEnabled()) {
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.3f);
            backgroundMusic.play();
        }
    }

    public void update(float v, LevelManager levelManager) {
        // Guardamos posición segura
        float oldX = player.getPosition().x;
        float oldY = player.getPosition().y;

        // Movemos al jugador según las teclas pulsadas y lo actualizamos para crear la animación
        player.handleInput(v);
        player.update(v);

        // Calculamos las esquinas de la caja de colisión (Rectangle) del jugador en su nueva posición
        float playerLeft = player.getRectangle().x;
        float playerRight = player.getRectangle().x + player.getRectangle().width;
        float playerBottom = player.getRectangle().y;
        float playerTop = player.getRectangle().y + player.getRectangle().height;

        // Comprobamos las 4 esquinas del jugador contra la capa terrain
        boolean collision =
            levelManager.isCellCellBlocked(playerLeft, playerBottom) || // Esquina inferior izquierda
                levelManager.isCellCellBlocked(playerRight, playerBottom) || // Esquina inferior derecha
                levelManager.isCellCellBlocked(playerLeft, playerTop) ||    // Esquina superior izquierda
                levelManager.isCellCellBlocked(playerRight, playerTop);     // Esquina superior derecha

        // Hay colisión
        if (collision) {
            player.getPosition().x = oldX;
            player.getPosition().y = oldY;
            player.getRectangle().setPosition(oldX, oldY); // Sincronizamos el rectángulo también

            if (ConfigurationManager.isSoundEnabled()) {
                collisionSound.play(); //Sonido colisión
            }
        }

        // Actualización de enemigos
        for(BubbleEnemy enemy : enemies) {
            enemy.update(v);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new ConfigurationScreen());
        }
    }

    public void addEnemy(BubbleEnemy enemy) {
        this.enemies.add(enemy);
    }


    @Override
    public void dispose() {
        player.dispose();
    }
}
