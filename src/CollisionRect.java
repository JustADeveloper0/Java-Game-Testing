import java.awt.Rectangle;

public class CollisionRect {
    private Rectangle rectangle;
    private int type;

    public CollisionRect(int x, int y, int width, int height, int type) {
        this.rectangle = new Rectangle(x, y, width, height);
        this.type = type;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public int getType() {
        return type;
    }
}