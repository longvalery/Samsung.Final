package rva.com.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import rva.com.screens.GamePlayScreen;
import rva.com.services.GameResources;

public class Brick {
    private Body body;
    private boolean destroyed = false;
    private boolean broken = false;
    private int type;
    private Texture texture, textureBroken;
    private GamePlayScreen game;
    private Sprite sprite, spriteBroken;
    private int width, height;
    private int column, row;

    private int count, maxCount;


    public Brick(World world, float x, float y, float width, float height, int type, GamePlayScreen game, int column, int row) {
        this.type = type;
        this.game = game;
        this.column = column;
        this.row = row;
        this.texture = new Texture(GameResources.BRICKS[type]);
        this.textureBroken = new Texture(GameResources.BROKEN_BRICKS[type]);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + width / 2, y + height / 2);
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.8f;
        body.createFixture(fixtureDef);
        shape.dispose();
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height); // масштабируем спрайт
        this.spriteBroken = new Sprite(textureBroken);
        this.spriteBroken.setSize(width, height); // масштабируем спрайт
        float originX, originY;
        originX = x - (this.width / 2);
        originY = y - (this.height / 2);
        // Устанавливаем центр спрайта как точку вращения
        this.sprite.setOrigin(this.width / 2, this.height / 2);
        this.spriteBroken.setOrigin(this.width / 2, this.height / 2);
        // перемещаем картинку
        this.sprite.setPosition(originX,  originY);
        this.spriteBroken.setPosition(originX,  originY);
        this.body.setUserData("brick");
        this.count = 1;
        this.maxCount = 1;
        if (type == 9) { this.count = 2; this.maxCount = 2;}
        if (type == 10) { this.count = 3; this.maxCount = 3;}
        // System.out.println("Create Brick");
    }

    public void draw(SpriteBatch batch) {
        if (! this.broken) { this.sprite.draw(batch); }
        else               { this.spriteBroken.draw(batch); }
//        System.out.println(String.format("originX %6.1f,  originY %6.1f", originX,  originY));

    }

    public void destroy() { this.destroyed = true; }
    public boolean isBroken() { return this.broken;  }
    public boolean isDestroyed() { return this.destroyed; }
    public void setBroken(boolean broken) { this.broken = broken; }
    public Body getBody() { return this.body;  }
    public int getType() { return type; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }

    public int getMaxCount() { return maxCount;  }
    public float getY() {
        Vector2 position = body.getPosition();
        return position.y;
    }
    public float getX() {
        Vector2 position = body.getPosition();
        return position.x;
    }

    public int getColumn() { return column; }
    public int getRow() { return row; }

    public void dispose() {
        this.game.getWorld().destroyBody(this.body);
        this.texture.dispose();
    }
}
