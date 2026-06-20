package rva.com.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

public interface MovingObject {
    void draw(SpriteBatch batch);
    void update(float delta);
    float getY();
    float getX();
    Body getBody();
    void dispose();
}
