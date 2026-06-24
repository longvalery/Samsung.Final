package rva.com.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import rva.com.Main;
import rva.com.services.GameResources;

public class Bomb extends GameObject {
    private boolean needDestroy;

    public Bomb(float x, float y, Main game) {
        super(x, y, game.getGameSession().getBombWidth(), game.getGameSession().getBombHeight(),
            0.0f, - game.getGameSession().getBonbonVelocity(), 0.0f, false
            , GameResources.BOMB_PATH, game);
        this.needDestroy = false;

        this.getBody().setUserData("bomb");
        // System.out.println("Create Bomb");
    }


    public void setNeedDestroy(boolean needDestroy) { this.needDestroy = needDestroy; }
    public boolean isNeedDestroy() { return needDestroy; }
}
