package rva.com.components;

import static rva.com.services.GameSettings.SCALE;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class GameObject {
    private float x;
    private float y;
    int width;
    int height;
    World world;
//    Texture texture;
    Body body;
    private short cBits;
    private Fixture fixture;
    private Sprite sprite;


    GameObject(String texturePath, int x, int y, int width, int height, World world, short cBits) {
        this.width = width;
        this.height = height;
        Texture texture = new Texture(texturePath);
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height); // масштабируем спрайт
        this.sprite.setPosition(x, y);
        this.cBits = cBits;
        this.x = x;
        this.y = y;
        this.body = createBody(x, y, world);
    }

    public void draw(SpriteBatch batch) {
        float originX, originY;
        float angleRad = body.getAngle();       // в радианах
        originX = this.getX() - (this.width / 2);
        originY = this.getY() - (this.height / 2);
        // Устанавливаем центр спрайта как точку вращения
        this.sprite.setOrigin(this.width / 2, this.height / 2);
        // перемещаем картинку
        this.sprite.setPosition(originX,  originY);
        // вращаем картинку
        this.sprite.setRotation(angleRad * MathUtils.radiansToDegrees);
        this.sprite.draw(batch);
    }
    public void hit() {}
    private Body createBody(float x, float y, World world) {
        BodyDef def = new BodyDef(); // def - defenition (определение) это объект, который содержит все данные, необходимые для посторения тела
        def.type = BodyDef.BodyType.DynamicBody; // тип тела, который имеет массу и может быть подвинут под действием сил
        def.fixedRotation = true; // разрешаем телу вращаться вокруг своей оси

        Body body = world.createBody(def); // создаём в мире world объект по описанному нами определению
        body.setSleepingAllowed(false); // Не спать !!!

        CircleShape circleShape = new CircleShape(); // задаём коллайдер в форме круга
        circleShape.setRadius(Math.max(width, height) * SCALE / 2f); // определяем радиус круга коллайдера

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = circleShape; // устанавливаем коллайдер
        fixtureDef.density = 1.0f; // устанавливаем плотность тела
        fixtureDef.friction = 0.1f; // устанвливаем коэффициент трения

        fixtureDef.filter.maskBits = -1; // All // cBits
        fixtureDef.filter.categoryBits = 1; // All
        body.createFixture(fixtureDef);
        circleShape.dispose(); // так как коллайдер уже скопирован в fixutre, то circleShape может быть отчищена, чтобы не забивать оперативную память.

        body.setTransform(x * SCALE, y * SCALE, 0); // устанавливаем позицию тела по координатным осям и угол поворота
        body.setUserData(this);
        // Если радиус < 0.01f – тело практически невидимо для физики.
        return body;
    }
    public void update() { // !!!
        // Синхронизация пиксельных координат с физическим телом
        this.x = this.body.getPosition().x / SCALE;
        this.y = this.body.getPosition().y / SCALE;
    }
    public Body getBody() {
        return body;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public short getcBits() {
        return cBits;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }

}
