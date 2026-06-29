package rva.com.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import rva.com.Main;
import rva.com.screens.GamePlayScreen;
import rva.com.services.GameResources;

public class Ball {
    private Body body;
    private int width;
    private int height;

    private Sprite sprite;
    private GamePlayScreen game;
    private Texture texture;

    public Ball(World world, float x, float y, GamePlayScreen game) {
        this.game = game;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        this.height = game.getGameSession().getBallHeight();
        this.width = game.getGameSession().getBallWidth();
        CircleShape shape = new CircleShape();
        shape.setRadius(game.getGameSession().getBallWidth() / 2.0f); // 8f

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 1f; // Максимальное отражение
        body.createFixture(fixtureDef);
        shape.dispose();

        this.texture = new Texture(GameResources.BALL_PATH);
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height); // масштабируем спрайт

        // Задаём начальную скорость
        body.setLinearVelocity(game.getGameSession().getBallVelocity(), - game.getGameSession().getBallVelocity());

        body.setUserData("ball");
    }

    public void draw(SpriteBatch batch) {
        this.sprite.draw(batch);
    }

    public void reset() {
        body.setTransform(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0f);
        body.setLinearVelocity(game.getGameSession().getBallVelocity(), - game.getGameSession().getBallVelocity());
    }

    public Body getBody() {
        return body;
    }

    public float getY() {
        Vector2 position = body.getPosition();
        return position.y;
                        }
    public float getX() {
        Vector2 position = body.getPosition();
        return position.x;
    }


    public void dispose() {
        this.game.getWorld().destroyBody(this.body);
        if (this.texture != null) { this.texture.dispose(); }
    }

    public void update() {
        Vector2 position = this.body.getPosition();
        float originX, originY;
        originX = position.x - (this.width / 2.0f);
        originY = position.y - (this.height / 2.0f);
        // Устанавливаем центр спрайта как точку вращения
        this.sprite.setOrigin(this.width / 2.0f, this.height / 2.0f);
        // перемещаем картинку
        this.sprite.setPosition(originX,  originY);

    }
}
