package com.svalero.trafficsurvive.screen;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
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

    public GameScreen() {
        logicManager = new LogicManager();
        logicManager.load();
        levelManager = new LevelManager(logicManager);
        levelManager.loadCurrentLevel();
        renderManager = new RenderManager(logicManager, levelManager.batch);
        cameraManager = new CameraManager(logicManager, levelManager);
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);    }

    @Override
    public void render(float v) {
        com.badlogic.gdx.Gdx.gl.glClearColor(0, 0, 0, 1);
        com.badlogic.gdx.Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Si se presiona escape se pausa la partida
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (!logicManager.isPartidaFinalizada()) {
                togglePause();
            }
        }

        // Si no ha terminado la partida y no estamos pausados, gestionamos la camara y renderizamos la lógica
        if (!logicManager.isPartidaFinalizada() && !isPaused) {
            logicManager.update(v, levelManager);
            cameraManager.handleCamera();
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

        // Mostramos el input integrado de VisUI que se renderiza dentro del propio juego
        Dialogs.showInputDialog(stage, "¡Partida Terminada!", "Introduce tu nombre:",
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
    }
}
