import java.awt.*;

public class Lever extends GameObject {
    public boolean activated = false;

    public Lever(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean inProximity(int px, int py) {
        Rectangle playerRect = new Rectangle(px, py + 32, 18, 18);
        Rectangle rect = new Rectangle(x, y - 8, width, height);

        return playerRect.intersects(rect) && !activated;
    }
}