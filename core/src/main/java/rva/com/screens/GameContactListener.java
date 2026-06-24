package rva.com.screens;

import static java.lang.Math.abs;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import rva.com.components.Bomb;
import rva.com.components.BonBon;
import rva.com.components.Brick;


public class GameContactListener implements ContactListener {
    private GamePlayScreen game;
    public GameContactListener( GamePlayScreen game ) {
        this.game = game;
    }

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
            if ((typeA.equals("ball") && typeB.equals("brick")) ||
                (typeA.equals("brick") && typeB.equals("ball"))) {

                Body brickBody = typeA.equals("brick") ? bodyA : bodyB;
                Brick brick = findBrickByBody(brickBody);
                if (brick != null) {
                    int score = brick.getType() + 1;
                    this.game.getGameSession().setScore(this.game.getGameSession().getScore() + score);
                    brick.setCount(brick.getCount() - 1);
                    if (brick.getCount() == 0) {
                        if (brick.isBroken()) {
                            brick.destroy();

                        }
                        else {
                            brick.setBroken(true);
                            brick.setCount(brick.getMaxCount());
                        }
                    }
                }
            }

//             Столкновение мяча со стенкой или ракеткой — просто отражается
//             Столкновение мяча с ракеткой — специальная логика отскока
            else if ((typeA.equals("ball") && typeB.equals("paddle")) ||
                (typeA.equals("paddle") && typeB.equals("ball"))) {
                Body ballBody = typeA.equals("ball") ? bodyA : bodyB;
                Body paddleBody = typeA.equals("paddle") ? bodyA : bodyB;
                handlePaddleCollision(ballBody, paddleBody);
                game.getAudio().getShootSound().play(game.getGameSession().getSoundVolume());
            }
            else if ((typeA.equals("paddle") && typeB.equals("bonbon")) ||
                (typeA.equals("bonbon") && typeB.equals("paddle"))) {
                Body bonbonBody = typeA.equals("bonbon") ? bodyA : bodyB;
                BonBon bonbon = game.getBonBon(bonbonBody);
                if (bonbon != null) { bonbon.setNeedDestroy(true); }
            }
            else if ((typeA.equals("paddle") && typeB.equals("bomb")) ||
                (typeA.equals("bomb") && typeB.equals("paddle"))) {
                Body bombBody = typeA.equals("bomb") ? bodyA : bodyB;
                Bomb bomb = game.getBomb(bombBody);
                if (bomb != null) {
                    bomb.setNeedDestroy(true);
                }
            }
            else if ((typeA.equals("ball") && typeB.equals("wall")) ||
                (typeA.equals("wall") && typeB.equals("ball"))) {
                Body ballBody = typeA.equals("ball") ? bodyA : bodyB;
                Vector2 velocity = bodyB.getLinearVelocity();
//                System.out.println(String.format("WALL velocity x: %8.3f, y %8.3f, ABS %8.3f", velocity.x, velocity.y, velocity.len()));
                Body wall = typeA.equals("wall") ? bodyA : bodyB;
                Vector2 position = wall.getPosition();
                if (position.x < 1.0) {
//                    System.out.println("Left");
                    velocity.x = - velocity.x;
                    velocity.y = velocity.y;
                                      }
                else if (position.y > (game.getGameSession().getScreenHeight() - 1) ) {
//                    System.out.println("Top");
                    velocity.y = - velocity.y;
                    velocity.x = velocity.x;
                                                                                      }
                else  {
//                    System.out.println("Right");
                    velocity.x = - velocity.x;
                    velocity.y = velocity.y;
                      }
                if (abs(velocity.y) < 5.0f) { velocity.y = - 20.0f; }
//                System.out.println(String.format("WALL velocity x: %8.3f, y %8.3f, ABS %8.3f", velocity.x, velocity.y, velocity.len()));
                ballBody.setLinearVelocity(velocity);

//                System.out.println(String.format("position x: %8.3f, y %8.3f", position.x, position.y));

//
            }

// Убираем случайное горизонтальное движение
//            Vector2 velocity = null;
//            Body ball = null;
//            if (typeA.equals("ball")) { velocity = bodyA.getLinearVelocity(); ball = bodyA; }
//            if (typeB.equals("ball")) { velocity = bodyB.getLinearVelocity(); ball = bodyB;}
//
//            if (velocity != null) {
//                System.out.println(String.format("velocity x: %8.3f, y %8.3f, ABS %8.3f", velocity.x, velocity.y, velocity.len()));
//                if (abs(velocity.y) < 1.0f) {
//                    velocity.y = - 10.0f; ball.setLinearVelocity(velocity);
//                                            }
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
        float paddleWidth = game.getGameSession().getPaddleWidth() ;// 100f; // в пикселях, нужно перевести в метры
        float paddleHalfWidth = paddleWidth / 2f;

        // Относительная позиция точки столкновения на ракетке
        float relativeX = (ballPos.x - paddlePos.x) / paddleHalfWidth;
        // Ограничиваем от -1 до 1
        relativeX = Math.max(-1f, Math.min(1f, relativeX));

        // Базовое отражение (угол падения = углу отражения)
        Vector2 newVelocity = new Vector2(ballVelocity.x, -ballVelocity.y);

        // Коэффициент искажения в зависимости от расстояния от центра
        float distortionFactor = relativeX * 0.8f; // максимум 80% искажения

        // Искажаем горизонтальную составляющую скорости
        newVelocity.x += distortionFactor * abs(ballVelocity.len());

        // Нормализуем и сохраняем исходную скорость
        float speed = ballVelocity.len();
        newVelocity.setLength(speed);

        // Применяем новую скорость
        ballBody.setLinearVelocity(newVelocity);
    }

    private Brick findBrickByBody(Body body) {
        for (Brick brick : this.game.getBricks()) {
            if (brick.getBody() == body) { return brick; }
        }
        return null;
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}

