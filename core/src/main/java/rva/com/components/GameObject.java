package rva.com.components;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import rva.com.Main;


public abstract class GameObject implements MovingObject {
    private Body body;
    private float height, x, y;
    protected float width;
    private Sprite sprite;
    private Main game;
    private Texture texture;

    public GameObject(float x, float y, float width, float height,
                      float vx, float vy, float va,
                      boolean isBox,
                      String path, Main game) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.game = game;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = this.game.getGame().getWorld().createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 1f; // Максимальное отражение
        if (isBox) {
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width / 2, height / 2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
            shape.dispose();
                   }
        else {
            CircleShape shape = new CircleShape();
            shape.setRadius(game.getGameSession().getBallWidth() / 2);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
            shape.dispose();
             }
        this.texture = new Texture(path);
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height); // масштабируем спрайт

        // Задаём начальную скорость
        body.setLinearVelocity(vx, vy);
        this.body.setAngularVelocity(va);
    }

    @Override
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

    @Override
    public void update(float delta) {
        Vector2 position = this.body.getPosition();
        this.x = position.x;
        this.y = position.y;
        Vector2 velocity = body.getLinearVelocity();
        float speed = velocity.len();
        if (speed < game.getGameSession().getBonbonVelocity()) {
            velocity.setLength(game.getGameSession().getBonbonVelocity());
            body.setLinearVelocity(velocity);
        }
    }

    @Override
    public float getY() { return this.y; }

    @Override
    public float getX() { return this.x; }

    @Override
    public Body getBody() { return this.body; }

    @Override
    public void dispose() {
//      this.game.getGame().getWorld().destroyBody(this.body);
      this.texture.dispose();

    }

    public float getWidth() { return width; }

    public Sprite getSprite() { return sprite; }

    public float getHeight() { return height; }

    public Main getGame() { return game; }
}
