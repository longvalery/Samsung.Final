package rva.com.uix;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ButtonView extends View {

    private Texture texture;
    private BitmapFont bitmapFont;
    private String text;
    private float textX;
    private float textY;
    private float width, height;

    public ButtonView(float x, float y, float width, float height, BitmapFont font, String texturePath, String text) {
        super(x, y, width, height);
        this.text = text;
        this.bitmapFont = font;
        texture = new Texture(texturePath);
        GlyphLayout glyphLayout = new GlyphLayout(bitmapFont, text);
        float textWidth = glyphLayout.width;
        float textHeight = glyphLayout.height;
        textX = x + (width - textWidth) / 2;
        textY = y + (height + textHeight) / 2;
        this.width = width;
        this.height = height;
    }

    public ButtonView(float x, float y, float width, float height, String texturePath) {
        super(x, y, width, height);
        texture  = new Texture(texturePath);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        if (bitmapFont != null) bitmapFont.draw(batch, text, textX, textY);
    }

    @Override
    public boolean isHit(float tx, float ty) {
//        System.out.println(String.format("%6.1f, %6.1f, %6.1f, %6.1f, %6.1f, %6.1f ", getX(), tx,getX() + getWidth(), getY() , ty, getY() + getHeight()));
        return tx >= getX() && tx <= getX() + getWidth() && ty >= getY() && ty <= getY() + getHeight();
    }

    public void setText(String text) {
        this.text = text;
        GlyphLayout glyphLayout = new GlyphLayout(bitmapFont, text);
        float textWidth = glyphLayout.width;
        float textHeight = glyphLayout.height;
        textX = getX() + (width - textWidth) / 2;
        textY = getY() + (height + textHeight) / 2;
    }
    @Override
    public void dispose() {
        super.dispose();
        this.texture.dispose();
        if (this.bitmapFont != null) { this.bitmapFont.dispose(); }
    }
}
