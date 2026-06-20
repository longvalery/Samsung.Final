package rva.com.components;

import com.badlogic.gdx.Gdx;
import rva.com.Main;
import rva.com.services.GameResources;

public class Ball extends GameObject{

    public Ball(float x, float y, Main game) {
        super(x, y, game.getGameSession().getBallWidth(), game.getGameSession().getBallHeight(),
            game.getGameSession().getBallVelocity(), - game.getGameSession().getBallVelocity(), 0.0f,
            false, GameResources.BALL_PATH, game );

        this.getBody().setUserData("ball");
        System.out.println("Create Ball");
    }

    public void reset() {
        getBody().setTransform(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
        getBody().setLinearVelocity(this.getGame().getGameSession().getBallVelocity()
                                  , - getGame().getGameSession().getBallVelocity());
    }

}
