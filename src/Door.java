import java.awt.*;

public class Door extends GameObject {
    public boolean used = false;
    public boolean visible = true;
    public int type;

    public Door(int x, int y, int width, int height, int type) {
        super(x, y, width, height);
        this.type = type;
    }

    public boolean inProximity(int px, int py) {
        Rectangle playerRect = new Rectangle(px, py + 62 - 18, 18, 18);
        Rectangle rect = new Rectangle(x, y, width, height);

        return playerRect.intersects(rect);
    }
}