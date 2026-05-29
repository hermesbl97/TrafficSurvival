package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import lombok.Data;

@Data
public class Tree implements Disposable {
    private Texture texture;
    private Vector2 position;
    private Rectangle rectangle;

    public Tree(Texture texture) {
        this.texture = texture;
        position = new Vector2(350, 200);
        rectangle = new Rectangle(350, 200, texture.getWidth(), texture.getHeight());
    }

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
