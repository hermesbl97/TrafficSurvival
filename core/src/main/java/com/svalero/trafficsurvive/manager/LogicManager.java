package com.svalero.trafficsurvive.manager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.trafficsurvive.domain.*;
import com.svalero.trafficsurvive.domain.Character;
import com.svalero.trafficsurvive.screen.ConfigurationScreen;
import lombok.Getter;

import static com.svalero.trafficsurvive.util.Constants.GAME_NAME;

public class LogicManager implements Disposable {

    @Getter
    protected Player player;
    protected Array<Character> enemies;
    protected Array<CarSpawner> spawners;
    @Getter
    protected Array<Item> items = new Array<>();
    private Music backgroundMusic, batEntranceMusic;
    private Sound collisionSound, crashSound, coinSound, diamondSound, lifeSound, bubbleSound, victorySound, biteSound;
    @Getter
    private boolean partidaFinalizada = false;
    private float idleTimer = 0f;           // Cuenta el tiempo que lleva quieto
    private boolean batSpawned = false;     // Evita que salgan varios murciélagos a la vez

    public LogicManager() {
        backgroundMusic = ResourceManager.getMusic("background.mp3");
        batEntranceMusic = ResourceManager.getMusic("bat_wings.mp3");
        collisionSound = ResourceManager.getSound("bump.mp3");
        crashSound = ResourceManager.getSound("crash.mp3");
        coinSound = ResourceManager.getSound("getCoin.mp3");
        diamondSound = ResourceManager.getSound("getDiamond.mp3");
        lifeSound = ResourceManager.getSound("getLife.mp3");
        bubbleSound = ResourceManager.getSound("removeLife.mp3");
        victorySound = ResourceManager.getSound("victory.mp3");
        biteSound = ResourceManager.getSound("bite.mp3");

        this.enemies = new Array<>();
        this.spawners = new Array<>();
        this.items = new Array<>();
    }

    public void load() {
        // Leemos qué personaje guardó el usuario en la pantalla de configuración (por defecto 3)
        Preferences prefs = Gdx.app.getPreferences(GAME_NAME);
        int selectedPlayer = prefs.getInteger("selected_player", 3);

        // Cargamos la textura inicial estática según el personaje escogido
        TextureRegion initialTexture = ResourceManager.getRegion("player" + selectedPlayer + "_idle_right");

        // Creamos al jugador pasándole la textura y su tipo
        player = new Player(initialTexture, selectedPlayer);

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

        // Comprobamos si el estado actual del jugador es uno de los IDLE
        boolean isPlayerIdle = player.getState() == Character.State.IDLE_FRONT ||
            player.getState() == Character.State.IDLE_BACK ||
            player.getState() == Character.State.IDLE_LEFT ||
            player.getState() == Character.State.IDLE_RIGHT;

        if (isPlayerIdle) {
            if (!batSpawned) { //si no hay muricelago
                idleTimer += v;
                if (idleTimer >= 5f) { // Si ha estado 5 segundos sin moverse aparece el muricelago
                    batSpawned = true;
                    if (ConfigurationManager.isMusicEnabled()) {
                        batEntranceMusic.setLooping(true);
                        batEntranceMusic.play();
                    }
                    idleTimer = 0f; //devuelve el valor a 0

                    // Aparece en el lateral derecho del mapa a la misma altura (Y) que el jugador
                    float spawnX = (30 * 16f) + 30f;
                    float spawnY = player.getPosition().y;

                    // Lo añadimos a la lista de enemigos
                    addEnemy(new BatEnemy(spawnX, spawnY, player));
                    System.out.println("Un murciélago ha detectado que no te mueves");
                }
            }
        } else {
            // Si el jugador realiza cualquier movimiento, el temporizador vuelve a empezar
            idleTimer = 0f;
        }

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

        // Colisión con agua
        boolean isInWater =
            levelManager.isCellWater(playerLeft, playerBottom) ||
                levelManager.isCellWater(playerRight, playerBottom) ||
                levelManager.isCellWater(playerLeft, playerTop) ||
                levelManager.isCellWater(playerRight, playerTop);

        if (isInWater) {
            player.takeScore(50);
            finalizarPartida();
            System.out.println("Te has ahogado");
        }

        for (int i = items.size - 1; i >= 0; i--) {
            Item item = items.get(i);

            if (player.getRectangle().overlaps(item.getRectangle())) {
                // Si el jugador es el 2 Suma 10 puntos extra por moneda
                if (item instanceof CoinItem) {
                    if (player.getTypePlayer() == 2) {
                        player.addScore(45);
                        System.out.println("Suma 45 Puntos por la moneda");
                    } else {
                        player.addScore(35);
                        System.out.println("Suma 35 Puntos por la moneda");
                    }
                    coinSound.play();
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
                    victorySound.play();
                    player.addScore(50);
                    finalizarPartida();
                }

                // Lo quitamos de la pantalla
                items.removeIndex(i);
            }
        }

        // Actualizar y comprobar colisiones con enemigos
        for(int i = enemies.size - 1; i >= 0; i--) {
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
                            player.takeScore(50);
                            finalizarPartida();
                        }
                    } else if (enemy instanceof BubbleEnemy) {
                        bubbleSound.play();
                        player.takeScore(20);
                        player.invertControls(15f);
                        System.out.println("Te ha tocado una burbuja rosa");
                        enemies.removeIndex(i);
                    } else if (enemy instanceof BatEnemy) {
                        if (ConfigurationManager.isSoundEnabled()) {
                            if (batEntranceMusic.isPlaying()) {
                                batEntranceMusic.stop();
                            }
                            biteSound.play();
                        }

                        player.setScore(0); //Deja su puntuación a 0
                        System.out.println("El murcielago te ha alcanzado");

                        // Lo ponemos a false por si vuelve a quedarse a 0
                        batSpawned = false;
                        enemies.removeIndex(i); // Lo eliminamos del juego
                        continue;
                    }
                }
            }

            if (enemy instanceof CarEnemy && ((CarEnemy) enemy).isShouldRemove()) {
                enemies.removeIndex(i);
            } else if (enemy instanceof BatEnemy && ((BatEnemy) enemy).isShouldRemove()) {
                batSpawned = false; // Permitimos que pueda volver a salir otro si el jugador se queda quieto de nuevo
                enemies.removeIndex(i); // Lo eliminamos de la lista definitivamente
                System.out.println("El murciélago se ha cansado de esperar y se ha ido.");
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
