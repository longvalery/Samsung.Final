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
import rva.com.screens.GamePlayScreen;
import rva.com.services.GameResources;

public class Bomb {
    private Body body;
    private int width;
    private int height;

    private Sprite sprite;
    private GamePlayScreen game;
    private Texture texture;
    private boolean needDestroy;


    public Bomb(float x, float y, Main game) {
        this.needDestroy = false;
        this.game = game.getGame();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = this.game.getWorld().createBody(bodyDef);
        this.height = game.getGameSession().getBombHeight();
        this.width = game.getGameSession().getBombWidth();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 1f; // Максимальное отражение
        body.createFixture(fixtureDef);
        shape.dispose();

        this.texture = new Texture(GameResources.BOMB_PATH);
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height); // масштабируем спрайт

        // Задаём начальную скорость
        body.setLinearVelocity(0, - game.getGameSession().getBonbonVelocity());

        body.setUserData("bomb");
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
//        System.out.println(String.format("originX %6.1f,  originY %6.1f", originX,  originY));
    }

    public void update() {
        // Поддерживаем постоянную скорость мяча
        Vector2 velocity = body.getLinearVelocity();
        float speed = velocity.len();
        if (speed < game.getGameSession().getBonbonVelocity()) {
            velocity.setLength(game.getGameSession().getBonbonVelocity());
            body.setLinearVelocity(velocity);
        }
    }

    public float getY() {
        Vector2 position = this.body.getPosition();
        return position.y;
    }

    public Body getBody() { return body; }

    public void setNeedDestroy(boolean needDestroy) { this.needDestroy = needDestroy; }
    public boolean isNeedDestroy() { return needDestroy; }

    public void dispose() {
        this.texture.dispose();
    }

}
