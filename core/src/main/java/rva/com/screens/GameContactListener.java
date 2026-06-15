package rva.com.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        Object userDataA = bodyA.getUserData();
        Object userDataB = bodyB.getUserData();

        if (userDataA instanceof String && userDataB instanceof String) {
            String typeA = (String) userDataA;
            String typeB = (String) userDataB;

            // Столкновение мяча с кирпичом
//            if ((typeA.equals("ball") && typeB.equals("brick")) ||
//                (typeA.equals("brick") && typeB.equals("ball"))) {
//
//                Body brickBody = typeA.equals("brick") ? bodyA : bodyB;
//                Brick brick = findBrickByBody(brickBody);
//                if (brick != null) {
//                    brick.destroy();
//                }
//            }

            // Столкновение мяча со стенкой или ракеткой — просто отражается
            // Столкновение мяча с ракеткой — специальная логика отскока
//            else if ((typeA.equals("ball") && typeB.equals("paddle")) ||
//                (typeA.equals("paddle") && typeB.equals("ball"))) {
//
//                Body ballBody = typeA.equals("ball") ? bodyA : bodyB;
//                Body paddleBody = typeA.equals("paddle") ? bodyA : bodyB;
//
//                handlePaddleCollision(ballBody, paddleBody);
//            }
        }
    }


    private void handlePaddleCollision(Body ballBody, Body paddleBody) {
        // Получаем текущую скорость мяча
        Vector2 ballVelocity = ballBody.getLinearVelocity();

        // Позиции центров
        Vector2 ballPos = ballBody.getPosition();
        Vector2 paddlePos = paddleBody.getPosition();

        // Ширина ракетки (у нас 100 пикселей, но в единицах Box2D)
        float paddleWidth = 100f; // в пикселях, нужно перевести в метры
        float paddleHalfWidth = paddleWidth / 2f;

        // Относительная позиция точки столкновения на ракетке
        float relativeX = (ballPos.x - paddlePos.x) / paddleHalfWidth;
        // Ограничиваем от -1 до 1
        relativeX = Math.max(-1f, Math.min(1f, relativeX));

        // Базовое отражение (угол падения = углу отражения)
        Vector2 newVelocity = new Vector2(ballVelocity.x, -ballVelocity.y);

        // Коэффициент искажения в зависимости от расстояния от центра
        float distortionFactor = relativeX * 0.8f; // максимум 80 % искажения

        // Искажаем горизонтальную составляющую скорости
        newVelocity.x += distortionFactor * Math.abs(ballVelocity.len());

        // Нормализуем и сохраняем исходную скорость
        float speed = ballVelocity.len();
        newVelocity.setLength(speed);

        // Применяем новую скорость
        ballBody.setLinearVelocity(newVelocity);
    }

//    private Brick findBrickByBody(Body body) {
//        GameWorld gameWorld = Main.getGameWorld(); // Предполагаем, что есть статический метод
//        for (Brick brick : gameWorld.getBricks()) {
//            if (brick.getBody() == body) {
//                return brick;
//            }
//        }
//        return null;
//    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}

