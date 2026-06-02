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

    private int score = 0;
    private int lives = 2;
    private int typePlayer;

    private boolean immune = false;
    private float immunityTimer;

    private boolean controlsInverted = false;
    private float inversionTimer = 0f;


    public Player(TextureRegion texture, int typePlayer) {
        super(texture, new Vector2(100, 0), IDLE_FRONT);

        this.typePlayer = typePlayer;
        stateTime = 0f;

        // Si se juega con el personaje 3 se empieza con una vida extra
        if (typePlayer == 3) {
            this.lives = 3;
        } else {
            this.lives = 2;
        }

        //Definimos el número de jugador para reutilizar las animaciones
        String playerNumber = "player" + typePlayer;

        movingFrontAnimation = new Animation<>(0.15f,
            ResourceManager.getRegion( playerNumber + "_move_front"),
            ResourceManager.getRegion( playerNumber + "_move_front2")
        );

        movingRightAnimation = new Animation<>(0.15f,
            ResourceManager.getRegion(playerNumber + "_move_right"),
            ResourceManager.getRegion(playerNumber + "_idle_right"),
            ResourceManager.getRegion( playerNumber + "_move_right2")
        );

        movingLeftAnimation = new Animation<>(0.15f,
            ResourceManager.getRegion( playerNumber + "_move_left"),
            ResourceManager.getRegion( playerNumber + "_idle_left"),
            ResourceManager.getRegion(playerNumber + "_move_left2")
        );

        movingBackAnimation = new Animation<>(0.15f,
            ResourceManager.getRegion(playerNumber + "_move_back"),
            ResourceManager.getRegion( playerNumber + "_move_back2")
        );

        this.rectangle.setSize(16, 16);
    }

    public void draw(Batch batch) {
        batch.draw(currentFrame, position.x, position.y);
    }

    public void handleInput(float delta) {
        stateTime += delta;
        boolean isMoving = false;

        // El jugador 1 se mueve un 40% más rápido que el resto
        float speed = PLAYER_SPEED;
        if (typePlayer == 1) {
            speed = PLAYER_SPEED * 1.4f;
        }

        // Recogemos el movimiento normal al presionar las flechas
        boolean pressLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean pressRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean pressUp = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean pressDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        // Si los controles están invertidos, cambiamos el valor de las variables
        if (controlsInverted) {
            boolean transLeft = pressLeft;
            boolean transUp = pressUp;

            pressLeft = pressRight;  // Izquierda ahora es Derecha
            pressRight = transLeft;   // Derecha ahora es Izquierda
            pressUp = pressDown;     // Arriba ahora es Abajo
            pressDown = transUp;      // Abajo ahora es Arriba
        }

        // Aplicamos el movimiento en función de la tecla pulsada
        if (pressLeft) {
            state = MOVE_LEFT;
            position.x -= speed * delta;
            isMoving = true;
        } else if (pressRight) {
            state = MOVE_RIGHT;
            position.x += speed * delta;
            isMoving = true;
        } else if (pressUp) {
            state = MOVE_FRONT;
            position.y += speed * delta;
            isMoving = true;
        } else if (pressDown) {
            state = MOVE_BACK;
            position.y -= speed * delta;
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
        String playerNumber = "player" + typePlayer;

        switch (state) {
            case IDLE_FRONT:
                currentFrame = ResourceManager.getRegion( playerNumber + "_idle_front");
                break;
            case IDLE_BACK:
                currentFrame = ResourceManager.getRegion( playerNumber + "_idle_back");
                break;
            case IDLE_LEFT:
                currentFrame = ResourceManager.getRegion(playerNumber + "_idle_left");
                break;
            case IDLE_RIGHT:
                currentFrame = ResourceManager.getRegion(playerNumber + "_idle_right");
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

        if (immune) {
            immunityTimer -= delta;
            if (immunityTimer <= 0) {
                immune = false;
                System.out.println("La inmunidad se ha terminado");
            }
        }

        if (controlsInverted) {
            inversionTimer -= delta;
            if (inversionTimer <= 0) {
                controlsInverted = false;
                System.out.println("¡Los controles han vuelto a la normalidad!");
            }
        }
    }

    public void addScore(int points) { this.score += points; }

    public void takeScore(int points) { this.score -= points; }

    public void addLife() { this.lives++; }

    public void activateImmunity(float duration) {
        this.immune = true;
        this.immunityTimer = duration;
    }

    public void invertControls(float duration) {
        this.controlsInverted = true;
        this.inversionTimer = duration;
    }


    public void removeLife() {
        lives --;
    }

    @Override
    public void dispose() {
    }
}
