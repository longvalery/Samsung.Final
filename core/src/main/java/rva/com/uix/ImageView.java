package rva.com.uix;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ImageView extends View{

    private Sprite sprite;

    public ImageView(float x, float y, String imagePath) {
        super(x, y);
        this.setTexture(new Texture(imagePath));
        this.setWidth(this.getTexture().getWidth());
        this.setHeight(this.getTexture().getHeight());
        this.sprite = new Sprite(this.getTexture());
        this.sprite.setSize(this.getTexture().getWidth(), this.getTexture().getHeight()); // масштабируем спрайт
        this.sprite.setPosition(x, y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        this.sprite.draw(batch);
//        batch.draw(getTexture(), this.getX(), this.getY());
    }

    public void drawTexture(SpriteBatch batch) {
       batch.draw(getTexture(), this.getX(), this.getY());
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
