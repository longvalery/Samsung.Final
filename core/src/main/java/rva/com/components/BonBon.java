package rva.com.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import rva.com.Main;
import rva.com.services.GameResources;
import rva.com.services.GameSettings;

public class BonBon  {
    private Main game;
    private Body body;
    private int width, height;
    private Sprite sprite;
    private Texture texture;
    private boolean needDestroy;
    public BonBon(float x, float y, Main game) {
        this.game = game;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        this.body = game.getGame().getWorld().createBody(bodyDef);
        this.height = game.getGameSession().getBonbonSize();
        this.width = game.getGameSession().getBonbonSize();
        CircleShape shape = new CircleShape();
        shape.setRadius(game.getGameSession().getBonbonSize() / 2); // 8f

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 1f; // Максимальное отражение
        this.body.createFixture(fixtureDef);
        shape.dispose();

        this.texture = new Texture(GameResources.BONBON_IMAGE_PATH);
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height); // масштабируем спрайт

        // Задаём начальную скорость
        this.body.setLinearVelocity(0.0f, - game.getGameSession().getBonbonVelocity());
        this.body.setAngularVelocity(GameSettings.ROTATION);
        this.needDestroy = false;
        this.body.setUserData("bonbon");
    }


    public void draw(SpriteBatch batch) {
        Vector2 position = this.body.getPosition();
        float angleRad = body.getAngle();
        float originX, originY;
        originX = position.x - (this.width / 2);
        originY = position.y - (this.height / 2);
        // Устанавливаем центр спрайта как точку вращения
        this.sprite.setOrigin(this.width / 2, this.height / 2);
        // перемещаем картинку
        this.sprite.setPosition(originX,  originY);
        // Вращаем
        this.sprite.setRotation(angleRad * MathUtils.radiansToDegrees);
        this.sprite.draw(batch);

    }

    public void update(float delta) {
        // Поддерживаем постоянную скорость конфеты
        Vector2 velocity = this.body.getLinearVelocity();
        float speed = velocity.len();
        if (speed < this.game.getGameSession().getBonbonVelocity()) {
            velocity.setLength(this.game.getGameSession().getBonbonVelocity());
            this.body.setLinearVelocity(velocity);
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
