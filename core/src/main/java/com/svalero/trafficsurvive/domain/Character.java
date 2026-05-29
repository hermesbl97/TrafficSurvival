package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;

@Data
public abstract class Character {
    protected Texture texture;
    protected Vector2 position;
    protected Rectangle rectangle;

    public Character(Texture texture, Vector2 position) {
        this.texture = texture;
        this.position = position;
        rectangle = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void draw(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }
}
