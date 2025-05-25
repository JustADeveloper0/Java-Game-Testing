import java.awt.*;

public class Spike extends GameObject {
    public Spike(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean inProximity(int px, int py) {
        Rectangle playerRect = new Rectangle(px + 4, py + 62 - 18, 10, 18);
        Rectangle rect = new Rectangle(x, y, width, height);

        return playerRect.intersects(rect);
    }
}