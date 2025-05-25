import java.awt.*;

public class Diamond extends GameObject implements Collectible {
    public boolean collected = false;

    public Diamond(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean inProximity(int px, int py) {
        Rectangle playerRect = new Rectangle(px, py + 62, 18, 18);
        Rectangle rect = new Rectangle(x, y, width, height);

        return playerRect.intersects(rect) && !collected;
    }

    @Override
    public void collect() {
        collected = true;
    }

    @Override
    public boolean isCollected() {
        return collected;
    }
}