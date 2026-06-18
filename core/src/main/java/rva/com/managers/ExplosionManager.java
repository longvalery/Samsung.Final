package rva.com.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.List;

import rva.com.components.ExplosionParticle;
import rva.com.services.GameSettings;

public class ExplosionManager {
    private World world;
    private List<ExplosionParticle> particles;
    private float particleLifetime = 3f; // время жизни частицы в секундах
    private float currentLifetime = 0f;

    public ExplosionManager(World world) {
        this.world = world;
        this.particles = new ArrayList<>();
    }

    // Создание взрыва в заданной точке
    public void createExplosion(Vector2 position) {
        for (int i = 0; i < GameSettings.NUM_PARTICLES; i++) {
            // Равномерно распределяем частицы по кругу
            float angle = i * (360f / GameSettings.NUM_PARTICLES) * MathUtils.degreesToRadians;
            particles.add(new ExplosionParticle(world, position, angle));
        }
    }

    // Обновление состояния взрыва
    public void update(float deltaTime) {
        currentLifetime += deltaTime;

        // Удаляем старые частицы
        if (currentLifetime >= particleLifetime) {
            for (ExplosionParticle particle : particles) {
                world.destroyBody(particle.getBody());
                particle.dispose();
            }
            particles.clear();
            currentLifetime = 0f;
        }
    }

    public void draw (SpriteBatch batch) {
        for (ExplosionParticle particle : particles) { particle.draw(batch); }
    }

//    // Отрисовка (для отладки)
//    public void render(Box2DDebugRenderer debugRenderer, OrthographicCamera camera) {
//        debugRenderer.render(world, camera.combined);
//    }
}
