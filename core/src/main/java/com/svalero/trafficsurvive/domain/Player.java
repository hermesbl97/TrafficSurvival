package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.svalero.trafficsurvive.manager.ResourceManager;
import lombok.Data;

import static com.svalero.trafficsurvive.domain.Character.State.*;
import static com.svalero.trafficsurvive.util.Constants.PLAYER_SPEED;

@Data
public class Player extends Character implements Disposable {

    public Player(TextureRegion texture) {
        super(texture, new Vector2(100, 100), IDLE_FRONT);

        stateTime = 0f;

        movingFrontAnimation = new Animation<>(0.15f,
            ResourceManager.getRegion("player3_move_front"),
            ResourceManager.getRegion("player3_move_front2")
        );

        movingRightAnimation = new Animation<>(0.15f,
            ResourceManager.getRegion("player3_move_right"),
            ResourceManager.getRegion("player3_idle_right"),
            ResourceManager.getRegion("player3_move_right2")
        );

        movingLeftAnimation = new Animation<>(0.15f,
            ResourceManager.getRegion("player3_move_left"),
            ResourceManager.getRegion("player3_idle_left"),
            ResourceManager.getRegion("player3_move_left2")
        );

        movingBackAnimation = new Animation<>(0.15f,
            ResourceManager.getRegion("player3_move_back"),
            ResourceManager.getRegion("player3_move_back2")
        );
    }

    public void draw(Batch batch) {
        batch.draw(currentFrame, position.x, position.y);
    }

    public void handleInput(float delta) {
        stateTime += delta;
        boolean isMoving = false;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            state = MOVE_LEFT;
            position.x -= PLAYER_SPEED * delta;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            state = MOVE_RIGHT;
            position.x += PLAYER_SPEED * delta;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            state = MOVE_FRONT;
            position.y += PLAYER_SPEED * delta;
            isMoving = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            state = MOVE_BACK;
            position.y -= PLAYER_SPEED * delta;
            isMoving = true;
        }

        // Si se sueltan las teclas, pasamos al IDLE correspondiente
        if (!isMoving) {
            if (state == MOVE_FRONT) state = IDLE_FRONT;
            if (state == MOVE_BACK) state = IDLE_BACK;
            if (state == MOVE_LEFT) state = IDLE_LEFT;
            if (state == MOVE_RIGHT) state = IDLE_RIGHT;
        }

        rectangle.setPosition(position.x, position.y);
    }

    public void update(float delta) {
        switch (state) {
            case IDLE_FRONT:
                currentFrame = ResourceManager.getRegion("player3_idle_front");
                break;
            case IDLE_BACK:
                currentFrame = ResourceManager.getRegion("player3_idle_back");
                break;
            case IDLE_LEFT:
                currentFrame = ResourceManager.getRegion("player3_idle_left");
                break;
            case IDLE_RIGHT:
                currentFrame = ResourceManager.getRegion("player3_idle_right");
                break;
            case MOVE_FRONT:
                currentFrame = movingFrontAnimation.getKeyFrame(stateTime, true);
                break;
            case MOVE_BACK:
                currentFrame = movingBackAnimation.getKeyFrame(stateTime, true);
                break;
            case MOVE_LEFT:
                currentFrame = movingLeftAnimation.getKeyFrame(stateTime, true);
                break;
            case MOVE_RIGHT:
                currentFrame = movingRightAnimation.getKeyFrame(stateTime, true);
                break;
        }
    }

    @Override
    public void dispose() {
    }
}
