package rva.com.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import rva.com.Main;
import rva.com.services.GameResources;


public class Paddle extends GameObject{

    public Paddle(float x, float y, Main game) {
        super(x, y, game.getGame().getGameSession().getPaddleWidth()
            ,game.getGame().getGameSession().getPaddleHeight(),
            0.0f, 0.0f, 0.0f, true, GameResources.PADDLE_PATH, game);

        getBody().setUserData("paddle");
      //  System.out.println("Create Paddle");
    }
     @Override
    public void update(float delta) {
        // Управление мышью/касанием
        float targetX = Gdx.input.getX();
        float currentX = getBody().getPosition().x;

        // Ограничение движения в пределах экрана
        targetX = Math.max(this.getWidth() / 2,
            Math.min(this.getGame().getGameSession().getScreenWidth()
                         - this.getWidth() / 2, targetX));
        // Плавное движение
        float velocity = (targetX - currentX) *  this.getGame().getGameSession().getPaddleVelocity();
        getBody().setLinearVelocity(velocity, 0);
    }


    public void reset() { this.setWidth(game.getGameSession().getPaddleWidth());}

    public void setWidth(int width) {
        this.width = width;
        getBody().destroyFixture(getBody().getFixtureList().first());
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width /2, this.getHeight() /2);
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 1f;
        getBody().createFixture(fixtureDef);
        this.getSprite().setSize(width, getHeight());
        shape.dispose();
    }
}
