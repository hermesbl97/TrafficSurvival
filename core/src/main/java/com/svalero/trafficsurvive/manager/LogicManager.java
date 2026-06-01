package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.trafficsurvive.domain.*;
import com.svalero.trafficsurvive.domain.Character;
import com.svalero.trafficsurvive.screen.ConfigurationScreen;

public class LogicManager implements Disposable {

    protected Player player;
    protected Array<Character> enemies;
    protected Array<CarSpawner> spawners;
    private Music backgroundMusic;
    private Sound collisionSound;

    public LogicManager() {
        backgroundMusic = ResourceManager.getMusic("background.mp3");
        collisionSound = ResourceManager.getSound("bump.mp3");
        this.enemies = new Array<>();
        this.spawners = new Array<>();

    }

    public void load() {
        player = new Player(ResourceManager.getRegion("player3_idle_right"));

        if (ConfigurationManager.isMusicEnabled()) {
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.3f);
            backgroundMusic.play();
        }
    }

    public void addSpawner(CarSpawner spawner) {
        this.spawners.add(spawner);
    }

    // Modificamos el método de añadir enemigos para que acepte cualquier Character enemigo
    public void addEnemy(Character enemy) {
        this.enemies.add(enemy);
    }

    public void update(float v, LevelManager levelManager) {
        // Actualizamos los spawners para que generen coches continuamente
        for (CarSpawner spawner : spawners) {
            spawner.update(v, this);
        }

        // GUARDA POSICIÓN ANTES DEL MOVIMIENTO
        float oldX = player.getPosition().x;
        float oldY = player.getPosition().y;

        player.handleInput(v);
        player.update(v);

        // CALCULAR ESQUINAS DEL RECTÁNGULO ACTUALIZADO
        float playerLeft = player.getRectangle().x;
        float playerRight = player.getRectangle().x + player.getRectangle().width;
        float playerBottom = player.getRectangle().y;
        float playerTop = player.getRectangle().y + player.getRectangle().height;

        // COMPROBAR COLISIÓN CONTRA EL MAPA
        boolean collision =
            levelManager.isCellCellBlocked(playerLeft, playerBottom) || // Inferior Izquierda
                levelManager.isCellCellBlocked(playerRight, playerBottom) || // Inferior Derecha
                levelManager.isCellCellBlocked(playerLeft, playerTop) ||    // Superior Izquierda
                levelManager.isCellCellBlocked(playerRight, playerTop);     // Superior Derecha

        // SI CHOCO, DESHACO EL MOVIMIENTO INMEDIATAMENTE
        if (collision) {
            // Devolvemos el vector de posición a donde estaba seguro
            player.getPosition().x = oldX;
            player.getPosition().y = oldY;

            player.getRectangle().setPosition(oldX, oldY);

            if (ConfigurationManager.isSoundEnabled()) {
                collisionSound.play();
            }
        }

        // Actualizar y comprobar colisiones con enemigos (burbujas y coches)
        for (int i = enemies.size - 1; i >= 0; i--) {
            Character enemy = enemies.get(i);
            enemy.update(v);

            if (player.getRectangle().overlaps(enemy.getRectangle())) {
                if (enemy instanceof CarEnemy) {
                    System.out.println("Un coche te ha atropellado.");
                } else if (enemy instanceof BubbleEnemy) {
                    System.out.println("Te ha tocado una burbuja rosa.");
                }
            }

            if (enemy instanceof CarEnemy && ((CarEnemy) enemy).isShouldRemove()) {
                enemies.removeIndex(i);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new ConfigurationScreen());
        }
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
