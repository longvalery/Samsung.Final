package rva.com.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import rva.com.screens.GamePlayScreen;
import rva.com.services.GameResources;
import rva.com.services.GameSession;

public class Paddle {
    private Body body;
    private GamePlayScreen game;
    private int width;
    private int height;
    private Texture texture;
    private Sprite sprite;

    public Paddle(World world, float x, float y, GamePlayScreen game) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody; // Двигается программно
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        this.height = game.getGameSession().getPaddleHeight();
        this.width = game.getGameSession().getPaddleWidth();
        shape.setAsBox(this.width /2, this.height /2); // Ширина 100, высота 20
        Texture texture = new Texture(GameResources.PADDLE_PATH);
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height); // масштабируем спрайт

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.8f;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData("paddle");
        this.game = game;
    }

    public void draw(SpriteBatch batch) {
        Vector2 position = this.body.getPosition();
        float originX, originY;
        originX = position.x - (this.width / 2);
        originY = position.y - (this.height / 2);
        // Устанавливаем центр спрайта как точку вращения
        this.sprite.setOrigin(this.width / 2, this.height / 2);
        // перемещаем картинку
        this.sprite.setPosition(originX,  originY);
        this.sprite.draw(batch);

    }

    public void update() {
        // Управление мышью/касанием
        float targetX = Gdx.input.getX();
        float currentX = body.getPosition().x;

        // Ограничение движения в пределах экрана
//        targetX = Math.max(50, Math.min(Gdx.graphics.getWidth() - 50, targetX));
//        targetX = Math.max(this.game.getGameSession().getPaddleWidth() / 2,
//                Math.min(this.game.getGameSession().getScreenWidth() - this.game.getGameSession().getPaddleWidth() / 2, targetX));
        targetX = Math.max(this.width / 2,
                Math.min(this.game.getGameSession().getScreenWidth() - this.width / 2, targetX));
        // Плавное движение
        float velocity = (targetX - currentX) *  this.game.getGameSession().getPaddleVelocity();
        body.setLinearVelocity(velocity, 0);
    }

    public Body getBody() {
        return body;
    }

    public Texture getTexture() {
        return texture;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getWidth() { return width; }

    public void setWidth(int width) {
        this.width = width;
        this.sprite.setSize(width, height);
        body.destroyFixture(body.getFixtureList().first());

        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(this.width /2, this.height /2);
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.8f;
        body.createFixture(fixtureDef);

    }
}
