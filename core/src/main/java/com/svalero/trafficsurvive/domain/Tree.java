package com.svalero.trafficsurvive.domain;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import lombok.Data;

@Data
public class Tree extends Character implements Disposable {

    public Tree(Texture texture) {
        super(texture, new Vector2(350, 200));
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
