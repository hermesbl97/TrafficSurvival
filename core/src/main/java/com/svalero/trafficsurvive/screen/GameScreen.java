package com.svalero.trafficsurvive.screen;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.*;
import com.svalero.trafficsurvive.manager.*;

import static com.svalero.trafficsurvive.util.Constants.GAME_NAME;

public class GameScreen implements Screen {

    private RenderManager renderManager;
    private LogicManager logicManager;
    private LevelManager levelManager;
    private CameraManager cameraManager;

    private Stage stage;
    private boolean showedDialog = false;
    private boolean isPaused = false;
    private VisWindow pauseWindow;

    // Información Jugador
    private VisLabel scoreLabel;
    private VisLabel livesLabel;
    private VisLabel levelLabel;
    private VisLabel alertLabel; // Espacio reservado para avisos personalizados
    private float alertTimer = 0f; // Temporizador para ocultar el aviso

    public GameScreen() {
        if (!VisUI.isLoaded()) VisUI.load();

        logicManager = new LogicManager();
        logicManager.load();
        levelManager = new LevelManager(logicManager);
        levelManager.loadCurrentLevel();
        renderManager = new RenderManager(logicManager, levelManager.batch);
        cameraManager = new CameraManager(logicManager, levelManager);
        stage = new Stage(new ScreenViewport());

        createHUD();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);    }

    @Override
    public void render(float v) {
//        com.badlogic.gdx.Gdx.gl.glClearColor(0, 0, 0, 1);
//        com.badlogic.gdx.Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Si se presiona escape se pausa la partida
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (!logicManager.isPartidaFinalizada()) {
                togglePause();
            }
        }

        // Si no ha terminado la partida y no estamos pausados, gestionamos la camara y renderizamos la lógica
        if (!logicManager.isPartidaFinalizada() && !isPaused) {
            logicManager.update(v, levelManager, this);
            cameraManager.handleCamera();

            // Actualizamos los textos del HUD con los datos
            scoreLabel.setText("Puntos: " + logicManager.getPlayer().getScore());
            livesLabel.setText("Vidas: " + logicManager.getPlayer().getLives());

            // Controlamos el tiempo del mensaje de alerta
            if (alertTimer > 0) {
                alertTimer -= v;
                if (alertTimer <= 0) {
                    alertLabel.setText(""); // Borramos el texto cuando expire el tiempo
                }
            }
        }

        // Pintamos el fondo, mapa, jugador y enemigos
        renderManager.drawFrame(v);

        // Si la partida ha terminado, le pasamos las órdenes a VisUI
        if (logicManager.isPartidaFinalizada()) {
            if (!showedDialog) {
                showedDialog = true;
                showDialogSaveScore();
            }
        }

        // Actualizamos y pintamos el letrero sobre el juego
        stage.act(v);
        stage.draw();
    }

    // Activa/desactiva el menu de pausa
    private void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            crearMenuPausa();
        } else {
            if (pauseWindow != null) {
                pauseWindow.remove(); // Ocultamos el menú flotante
            }
        }
    }

    // Menu de pausa
    private void crearMenuPausa() {
        if (!VisUI.isLoaded()) VisUI.load();

        Preferences prefs = Gdx.app.getPreferences(GAME_NAME);

        // Creamos una ventana flotante centrada
        pauseWindow = new VisWindow("Partida en Pausa");
        pauseWindow.setModal(true);
        pauseWindow.setMovable(false);

        VisTable table = new VisTable(true);

        // Activar/desactivar efectos de sonido
        VisCheckBox checkSound = new VisCheckBox("Efectos de sonido");
        checkSound.setChecked(prefs.getBoolean("sound_effects", true));
        checkSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean isChecked = checkSound.isChecked();
                prefs.putBoolean("sound_effects", checkSound.isChecked());
                prefs.flush();

                if (!isChecked) {
                    logicManager.stopAllSounds();
                }
            }
        });

        // Activar/desactivar música del juego
        VisCheckBox checkMusic = new VisCheckBox("Música de fondo");
        checkMusic.setChecked(prefs.getBoolean("music", true));
        checkMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean isChecked = checkMusic.isChecked();
                prefs.putBoolean("music", checkMusic.isChecked());
                prefs.flush();

                if (!checkMusic.isChecked()) {
                    logicManager.stopAllMusic();
                }
            }
        });

        // Continuar partida
        VisTextButton resumeButton = new VisTextButton("Continuar partida");
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });

        //Reiniciar partida
        VisTextButton restartButton = new VisTextButton("Reiniciar partida");
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Apagamos la música
                logicManager.stopAllMusic();

                // Liberamos la memoria de la pantalla actual
                dispose();

                //Abrimos una nueva partida
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });

        // Volver al menú principal
        VisTextButton menuButton = new VisTextButton("Volver al menú principal");
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logicManager.stopAllMusic();
                logicManager.dispose(); // Liberamos recursos de la partida actual
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        });

        // Salir del juego por completo
        VisTextButton exitButton = new VisTextButton("Salir del juego");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        table.add(checkSound).pad(5).row();
        table.add(checkMusic).pad(5).row();
        table.add(resumeButton).width(180).pad(5).row();
        table.add(restartButton).width(180).pad(5).row();
        table.add(menuButton).width(180).pad(5).row();
        table.add(exitButton).width(180).pad(5);

        pauseWindow.add(table);
        pauseWindow.pack();

        // Centramos la ventana en medio de la pantalla
        pauseWindow.setPosition(
            (Gdx.graphics.getWidth() - pauseWindow.getWidth()) / 2,
            (Gdx.graphics.getHeight() - pauseWindow.getHeight()) / 2
        );

        stage.addActor(pauseWindow);
    }

    //  Guardar puntuacion
    private void showDialogSaveScore() {
        if (!VisUI.isLoaded()) {
            VisUI.load();
        }

        // Damos el control del teclado y ratón al diálogo para poder escribir
        Gdx.input.setInputProcessor(stage);


        String message = logicManager.getEndMessage();

        // Mostramos el input integrado de VisUI que se renderiza dentro del propio juego
        Dialogs.showInputDialog(stage, message, "Introduce tu nombre:",
            true, new InputDialogListener() {

            @Override
            public void finished(String text) {
                String nombre = text.trim().isEmpty() ? "Anónimo" : text;
                // Guardamos usando los puntos acumulados por el jugador
                ScoreManager.saveScore(nombre, logicManager.getPlayer().getScore());

                // Volvemos al menú principal
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }

            @Override
            public void canceled() {
                // Si cancela guardamos la puntuación como Anónimo
                ScoreManager.saveScore("Anónimo", logicManager.getPlayer().getScore());
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        });
    }

    //Tabla informativa del jugador
    private void createHUD() {
        VisTable hudTable = new VisTable();
        hudTable.top().left().setFillParent(true); // Se coloca arriba a la izquierda ocupando la pantalla

        scoreLabel = new VisLabel("Puntos: 0");
        livesLabel = new VisLabel("Vidas: 0");
        levelLabel = new VisLabel("Nivel: " + levelManager.getCurrentLevel());

        // Añadimos a la tabla los elementos
        hudTable.add(scoreLabel).pad(10);
        hudTable.add(livesLabel).pad(10);
        hudTable.add(levelLabel).pad(10);
        stage.addActor(hudTable);

        // Tabla de alertas
        VisTable alertTable = new VisTable();
        alertTable.bottom().setFillParent(true);

        alertLabel = new VisLabel("");
        alertTable.add(alertLabel);
        stage.addActor(alertTable);
    }

    // LogicManager manda alertas personalizadas a la pantalla
    public void showAlert(String message) {
        alertLabel.setText(message);
        alertTimer = 2.0f; // El mensaje durará 2 segundos
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        stage.getViewport().update(width, height, true);
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
        logicManager.dispose();
        renderManager.dispose();
        stage.dispose();
    }
}
