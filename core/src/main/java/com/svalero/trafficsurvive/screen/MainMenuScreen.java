package com.svalero.trafficsurvive.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

/** First screen of the application. Displayed after the application is created. */
public class MainMenuScreen implements Screen {
    private Stage stage;

    @Override
    public void show() {
        if (!VisUI.isLoaded()) { //Si la librería no está cargada
            VisUI.load();
        }

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        VisTextButton playButton = new VisTextButton("Jugar");
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambia a la pantalla de juego
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
            }
        });

        VisTextButton configurationButton = new VisTextButton("Configuración");
        configurationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ConfigurationScreen());
                dispose();
            }
        });

        VisTextButton exitButton = new VisTextButton("Salir");
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0); // Cierra la aplicación
            }
        });

        table.row(); //ordenamos la tabla
        table.add(playButton).center().width(200).height(50).pad(10);
        table.row();
        table.add(configurationButton).center().width(200).height(50).pad(10);
        table.row();
        table.add(exitButton).center().width(200).height(50).pad(10);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        stage.getViewport().update(width, height, true); //hacemos responsive el menu
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}
