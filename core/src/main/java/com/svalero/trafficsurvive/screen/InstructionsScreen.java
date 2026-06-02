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
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class InstructionsScreen implements Screen {
    private Stage stage;

    @Override
    public void show() {
        if (!VisUI.isLoaded()) VisUI.load();
        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        VisLabel title = new VisLabel("Vamos a aprender a jugar a TRAFFIC SURVIVAL");
        VisLabel inst1 = new VisLabel("- Usa las flechas del teclado para moverte y alcanza la salida.");
        VisLabel inst2 = new VisLabel("- Ten cuidado, si te atropella un coche perderás una vida. Evita las burbujas rosas o perderás el control.");
        VisLabel inst3 = new VisLabel("- ¡Cuidado! Recuerda que no sabes nadar, si te caes al agua morirás.");
        VisLabel inst4 = new VisLabel("- No te quedes quieto o te perseguirán... Si te alcanzan pierdes TODOS tus puntos. ¡Ojo! No choques con nadie. Estás avisado");
        VisLabel inst5 = new VisLabel("- Recoge monedas sumarás muchos puntos. Los diamantes te darán inmunidad un tiempo breve. Si encuentras una vida no la dejes pasar sólo empiezas con una. Salvo que juegues con Santi");
        VisLabel inst6 = new VisLabel("- Si necesitas un respiro pulsa escape.");
        VisLabel inst7 = new VisLabel("- Te gusta el frenesí, escoge a Hermes. Si te gusta tener una bala más en la recámara, quédate con Santi. Y sino escoge a Marta y verás como tus puntuaciones suben fácilmente.");

        VisTextButton backButton = new VisTextButton("Volver");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen());
            }
        });

        table.add(title).padBottom(20).row();
        table.add(inst1).left().padBottom(15).row();
        table.add(inst2).left().padBottom(15).row();
        table.add(inst3).left().padBottom(15).row();
        table.add(inst4).left().padBottom(15).row();
        table.add(inst5).left().padBottom(15).row();
        table.add(inst6).left().padBottom(15).row();
        table.add(inst7).left().padBottom(15).row();
        table.add(backButton).center().width(100).height(40);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (width > 0 && height > 0) {
            stage.getViewport().update(width, height, true);
        }
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
        if (stage != null) stage.dispose();
    }
}
