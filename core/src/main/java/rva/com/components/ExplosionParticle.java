package rva.com.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import rva.com.services.GameResources;
import rva.com.services.GameSettings;

public class ExplosionParticle {


    private Body body;
    private Texture texture;
    private float width, height;

    public ExplosionParticle(World world, Vector2 explosionCenter, float angle) {
        // Создаём тело частицы
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(explosionCenter);
        bodyDef.fixedRotation = true;
        bodyDef.bullet = true; // предотвращаем «проскакивание» на высоких скоростях
        bodyDef.linearDamping = 8f; // сопротивление воздуха
        bodyDef.gravityScale = 0f; // отключаем гравитацию

        body = world.createBody(bodyDef);
        body.setUserData(this); // для идентификации при очистке

        // Направление и скорость частицы
        float x = (float) Math.cos(angle);
        float y = (float) Math.sin(angle);
        Vector2 direction = new Vector2(x, y).scl(GameSettings.BLAST_POWER);

        // Применяем импульс
        body.setLinearVelocity(direction);
        int random = new Random().nextInt(5);
        this.texture = createSilverCircleTexture(random);
        this.width = random;
        this.height = random;
        // Форма частицы — маленький круг
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(random / 2.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 2f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.9f; // упругость
        fixtureDef.filter.groupIndex = -1; // частицы не сталкиваются друг с другом

        body.createFixture(fixtureDef);
        circleShape.dispose();
       //  System.out.println("Create ExplosionParticle");
    }

    public Body getBody() {
        return body;
    }

    public Texture createSilverCircleTexture(int radius) {
        int diameter = radius * 2;
        Pixmap pixmap = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CLEAR);
        pixmap.fill();

        // Рисуем серебристый круг
        pixmap.setColor(new Color(0.75f, 0.75f, 0.75f, 1f));
        for (int y = 0; y < diameter; y++) {
            for (int x = 0; x < diameter; x++) {
                float dx = x - radius;
                float dy = y - radius;
                if (dx * dx + dy * dy <= radius * radius) {
                    pixmap.drawPixel(x, y);
                }
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void dispose() {
       this.texture.dispose();
    }

    public void draw(SpriteBatch batch) {
        Vector2 position = this.body.getPosition();
        float originX, originY;
        originX = position.x - (this.width / 2);
        originY = position.y - (this.height / 2);
        batch.draw(this.texture, originX, originY);
    }
}

