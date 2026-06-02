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
import lombok.Getter;

public class LogicManager implements Disposable {

    @Getter
    protected Player player;
    protected Array<Character> enemies;
    protected Array<CarSpawner> spawners;
    @Getter
    protected Array<Item> items = new Array<>();
    private Music backgroundMusic;
    private Sound collisionSound, crashSound, coinSound, diamondSound, lifeSound, bubbleSound, victorySound;
    @Getter
    private boolean partidaFinalizada = false;

    public LogicManager() {
        backgroundMusic = ResourceManager.getMusic("background.mp3");
        collisionSound = ResourceManager.getSound("bump.mp3");
        crashSound = ResourceManager.getSound("crash.mp3");
        coinSound = ResourceManager.getSound("getCoin.mp3");
        diamondSound = ResourceManager.getSound("getDiamond.mp3");
        lifeSound = ResourceManager.getSound("getLife.mp3");
        bubbleSound = ResourceManager.getSound("removeLife.mp3");
        victorySound = ResourceManager.getSound("victory.mp3");

        this.enemies = new Array<>();
        this.spawners = new Array<>();
        this.items = new Array<>();
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

    public void addEnemy(Character enemy) {
        this.enemies.add(enemy);
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public void update(float v, LevelManager levelManager) {
        //si la partida ha terminado nos salimos
        if (partidaFinalizada) return;

        // Actualizamos los spawners para que generen coches continuamente
        for (CarSpawner spawner : spawners) {
            spawner.update(v, this);
        }

        // Guarda la posición antes de moverse
        float oldX = player.getPosition().x;
        float oldY = player.getPosition().y;

        player.handleInput(v);
        player.update(v);

        // Calcula las esquinas del rectangulo de colision
        float playerLeft = player.getRectangle().x;
        float playerRight = player.getRectangle().x + player.getRectangle().width;
        float playerBottom = player.getRectangle().y;
        float playerTop = player.getRectangle().y + player.getRectangle().height;

        // Comprueba la colisión respecto al mapa
        boolean collision =
            levelManager.isCellCellBlocked(playerLeft, playerBottom) || // Inferior Izquierda
                levelManager.isCellCellBlocked(playerRight, playerBottom) || // Inferior Derecha
                levelManager.isCellCellBlocked(playerLeft, playerTop) ||    // Superior Izquierda
                levelManager.isCellCellBlocked(playerRight, playerTop);     // Superior Derecha

        // Si choca deshace el movimiento
        if (collision) {
            // Devolvemos el jugador a la posición donde estaba seguro
            player.getPosition().x = oldX;
            player.getPosition().y = oldY;

            player.getRectangle().setPosition(oldX, oldY);
            player.takeScore(5);

            if (ConfigurationManager.isSoundEnabled()) {
                collisionSound.play();
            }
        }

        for (int i = items.size - 1; i >= 0; i--) {
            Item item = items.get(i);

            if (player.getRectangle().overlaps(item.getRectangle())) {
                if (item instanceof CoinItem) {
                    player.addScore(30);
                    coinSound.play();
                    System.out.println("Moneda cogida. Suma 30 Puntos ");
                } else if (item instanceof DiamondItem) {
                    player.activateImmunity(12f);
                    player.addScore(15);
                    diamondSound.play();
                    System.out.println("Diamante cogido, Inmune por 12 segundos. Sumas 15 puntos");
                } else if (item instanceof LifeItem) {
                    player.addLife();
                    lifeSound.play();
                    System.out.println("Suma una vida");
                } else if (item instanceof ExitItem) {
                    System.out.println("¡HAS LLEGADO A LA META!");
                    player.addScore(50);
                    finalizarPartida();
                }

                // Lo quitamos de la pantalla
                items.removeIndex(i);
            }
        }

        // Actualizar y comprobar colisiones con enemigos
        for (int i = enemies.size - 1; i >= 0; i--) {
            Character enemy = enemies.get(i);
            enemy.update(v);

            if (player.getRectangle().overlaps(enemy.getRectangle())) {
                // Si no eres inmune puedes colisionar
                if (!player.isImmune()) {
                    if (enemy instanceof CarEnemy) {
                        if (ConfigurationManager.isSoundEnabled()) {
                            crashSound.play();
                        }

                        player.removeLife();
                        System.out.println("Pierdes una vida");

                        // Reaparecemos al inicio después de colosionar y hemos perdido una vida
                        player.getPosition().set(100, 0);
                        player.getRectangle().setPosition(100, 0);

                        if (player.getLives() <= 0) {
                            System.out.println(" Te has quedado sin vidas.");
                            finalizarPartida();
                        }
                    } else if (enemy instanceof BubbleEnemy) {
                        bubbleSound.play();
                        player.takeScore(25);
                        System.out.println("Te ha tocado una burbuja rosa");
                        //TODO al chocar te mueves al revés
                    }
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

    private void finalizarPartida() {
        if (partidaFinalizada) return;
        partidaFinalizada = true;

        // Paramos la música de fondo
        if (backgroundMusic.isPlaying()) {
            backgroundMusic.stop();
        }
    }

    @Override
    public void dispose() {
        player.dispose();
    }
}
