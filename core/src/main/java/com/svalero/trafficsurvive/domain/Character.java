package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.g2d.Animation;
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
    protected float stateTime;

    public enum State {
        IDLE_LEFT, IDLE_FRONT, IDLE_BACK, IDLE_RIGHT, MOVE_LEFT, MOVE_RIGHT, MOVE_FRONT, MOVE_BACK
    }

    public State state;
    protected Animation<TextureRegion> movingRightAnimation;
    protected Animation<TextureRegion> movingLeftAnimation;
    protected Animation<TextureRegion> movingFrontAnimation;
    protected Animation<TextureRegion> movingBackAnimation;

    public Character(TextureRegion texture, Vector2 position, State initialState) {
        this.currentFrame = texture;
        this.position = position;
        state =initialState;
        rectangle = new Rectangle(position.x, position.y, texture.getRegionWidth(), texture.getRegionHeight());
    }

    public abstract void update(float dt);

    public void draw(Batch batch) {
        batch.draw(currentFrame, position.x, position.y);
    }
}
