package rva.com.components;

import rva.com.Main;
import rva.com.services.GameResources;
import rva.com.services.GameSettings;

public class BonBon extends GameObject {
    private boolean needDestroy;
    public BonBon(float x, float y, Main game) {
        super(x,y,game.getGameSession().getBonbonSize(), game.getGameSession().getBonbonSize(),
             0.0f, - game.getGameSession().getBonbonVelocity(), GameSettings.ROTATION,
             false, GameResources.BONBON_IMAGE_PATH, game );
        this.needDestroy = false;
        this.getBody().setUserData("bonbon");
        // System.out.println("Create BonBon");
    }

    public void setNeedDestroy(boolean needDestroy) { this.needDestroy = needDestroy; }
    public boolean isNeedDestroy() { return needDestroy; }
}
