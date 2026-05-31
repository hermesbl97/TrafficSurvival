package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Data;

@Data
public abstract class Character {
    protected TextureRegion currentFrame;
    protected Vector2 position;
    protected Rectangle rectangle;

    public Character(TextureRegion texture, Vector2 position) {
        this.currentFrame = texture;
        this.position = position;
        rectangle = new Rectangle(position.x, position.y, texture.getRegionWidth(), texture.getRegionHeight());
    }

    public void draw(Batch batch) {
        batch.draw(currentFrame, position.x, position.y);
    }
}
