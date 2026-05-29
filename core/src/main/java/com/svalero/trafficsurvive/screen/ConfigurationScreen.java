package com.svalero.trafficsurvive.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class ConfigurationScreen implements Screen {
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

        VisCheckBox checkSound = new VisCheckBox("Efectos de sonido");
        checkSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO activar y desactivar sonido
            }
        });

        VisCheckBox checkMusic = new VisCheckBox("Música");
        checkMusic.addListener(new ClickListener() {
            //TODO Activar/desactivar musica
        });

        VisTextButton goBackButton = new VisTextButton("Volver al menú principal");
        goBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        });

        table.row(); //ordenamos la tabla
        table.add(checkSound).center().width(200).height(50).pad(10);
        table.row();
        table.add(checkMusic).center().width(200).height(50).pad(10);
        table.row();
        table.add(goBackButton).center().width(200).height(50).pad(10);

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

        if(width <= 0 || height <= 0) return;
        stage.getViewport().update(width, height, true); //hacemos responsive el menu
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

    }
}
