import java.awt.*;

public class Key extends GameObject implements Collectible {
    public boolean collected = false;
    Door door;

    public Key(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void opens(Door door) {
        this.door = door;
    }

    public boolean inProximity(int px, int py) {
        Rectangle playerRect = new Rectangle(px, py + 62, 18, 18);
        Rectangle rect = new Rectangle(x, y, width, height + 18);

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