package rva.com.components;

public class MenuItemDimensions {
    private float width, height, x, y;

    public MenuItemDimensions(float x, float y, float width, float height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public boolean isInside(float x, float y) {
        System.out.println(String.format("x: %f, this.x: %f, this.x + this.width: %f,y: %f, this.y: %f, this.y + this.height: %f"
            , x,this.x, this.x + this.width, y, this.y, this.y + this.height));
        return (x >= this.x) && (x < (this.x + this.width)) &&
               (y >= this.y) && (y < (this.y + this.height));
    }
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
