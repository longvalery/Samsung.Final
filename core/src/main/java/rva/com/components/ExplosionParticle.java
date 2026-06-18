package rva.com.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class ExplosionParticle {
    public static final int NUM_PARTICLES = 60; // количество частиц
    public static final float BLAST_POWER = 150f; // сила взрыва

    private Body body;

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
        Vector2 direction = new Vector2(x, y).scl(BLAST_POWER);

        // Применяем импульс
        body.setLinearVelocity(direction);

        // Форма частицы — маленький круг
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 2f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0.9f; // упругость
        fixtureDef.filter.groupIndex = -1; // частицы не сталкиваются друг с другом

        body.createFixture(fixtureDef);
        circleShape.dispose();
    }

    public Body getBody() {
        return body;
    }
}

