package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;

@Data
public abstract class Item {
    protected TextureRegion texture;
    protected Vector2 position;
    protected Rectangle rectangle;

    public Item(TextureRegion texture, Vector2 position) {
        this.texture = texture;
        this.position = position;
        this.rectangle = new Rectangle(position.x, position.y, 16, 16);
    }

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }
}
