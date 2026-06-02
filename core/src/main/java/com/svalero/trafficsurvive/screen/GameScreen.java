package com.svalero.trafficsurvive.screen;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.svalero.trafficsurvive.manager.*;

public class GameScreen implements Screen {

    private RenderManager renderManager;
    private LogicManager logicManager;
    private LevelManager levelManager;
    private CameraManager cameraManager;

    private Stage stage;
    private boolean dialogoMostrado = false;

    public GameScreen() {
        logicManager = new LogicManager();
        levelManager = new LevelManager(logicManager);
        levelManager.loadCurrentLevel();
        renderManager = new RenderManager(logicManager, levelManager.batch);
        cameraManager = new CameraManager(logicManager, levelManager);
        stage = new Stage();
    }

    @Override
    public void show() {
        logicManager.load();
    }

    @Override
    public void render(float v) {
        com.badlogic.gdx.Gdx.gl.glClearColor(0, 0, 0, 1);
        com.badlogic.gdx.Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Si no ha terminado la partida gestionamos la camara y renderizamos la lógica
        if (!logicManager.isPartidaFinalizada()) {
            logicManager.update(v, levelManager);
            cameraManager.handleCamera();
        }

        // Pintamos el fondo, mapa, jugador y enemigos
        renderManager.drawFrame(v);

        // Si la partida ha terminado, le pasamos las órdenes a VisUI
        if (logicManager.isPartidaFinalizada()) {
            if (!dialogoMostrado) {
                dialogoMostrado = true;
                mostrarDialogoGuardarPuntuacion();
            }

            // Actualizamos y pintamos el letrero flotante sobre el juego
            stage.act(v);
            stage.draw();
        }
    }

    private void mostrarDialogoGuardarPuntuacion() {
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
        logicManager.dispose();
        renderManager.dispose();
    }
}
