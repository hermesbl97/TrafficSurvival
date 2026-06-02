package com.svalero.trafficsurvive.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.svalero.trafficsurvive.manager.ResourceManager;
import com.svalero.trafficsurvive.manager.ScoreManager;

/** First screen of the application. Displayed after the application is created. */
public class MainMenuScreen implements Screen {
    private Stage stage;

    public MainMenuScreen() {
        ResourceManager.loadAllResources();

        while(!ResourceManager.update()) {

        }
    }

    @Override
    public void show() {
        if (!VisUI.isLoaded()) { //Si la librería no está cargada
            VisUI.load();
        }

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        VisTable menuLeftTable = new VisTable(true);

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

        menuLeftTable.add(playButton).center().width(200).height(50).pad(10).row();
        menuLeftTable.add(configurationButton).center().width(200).height(50).pad(10).row();
        menuLeftTable.add(exitButton).center().width(200).height(50).pad(10).row();

    // --- SUBTABLA DERECHA: LA TABLA DE PUNTUACIONES ---
        VisTable scoreRightTable = new VisTable(true);

        VisLabel rankingTitle = new VisLabel("Top 10 Records");

        VisList<String> scoreList = new VisList<>();
        // Cargamos los datos limpios y ordenados desde el HighScoreManager
        scoreList.setItems(ScoreManager.getTop10Scores());

        // Montamos el título y la lista en la subtabla derecha
        scoreRightTable.add(rankingTitle).padBottom(15).center().row();
        scoreRightTable.add(scoreList).width(280).height(200).center();

        table.add(menuLeftTable).pad(30).center();
        table.add(scoreRightTable).pad(30).center();

        Gdx.input.setInputProcessor(stage);    }

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
        // Destruimos el stage para liberar memoria al cambiar de pantalla
        if (stage != null) {
            stage.dispose();
        }
    }
}
