package rva.com.components;

import rva.com.Main;
import rva.com.services.GameResources;

public class Bomb extends GameObject {
    private boolean needDestroy;

    public Bomb(float x, float y, Main game) {
        super(x, y, game.getGameSession().getBombWidth(), game.getGameSession().getBombHeight(),
            0.0f, - game.getGameSession().getBonbonVelocity(), 0.0f, false
            , GameResources.BOMB_PATH, game);
        this.needDestroy = false;

        this.getBody().setUserData("bomb");
        // System.out.println("Create Bomb");
    }


    public void setNeedDestroy(boolean needDestroy) { this.needDestroy = needDestroy; }
    public boolean isNeedDestroy() { return needDestroy; }
}
