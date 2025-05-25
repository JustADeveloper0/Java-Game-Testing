import java.awt.*;

public class Pipe {
    public int x, y;
    public int width, height;
    public int direction;
    Pipe otherPipe;

    public Pipe(int x, int y, int width, int height, int direction) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.direction = direction;
    }

    public void connectPipe(Pipe pipe) {
        otherPipe = pipe;
    }

    public int[] getCoords() {
        if (direction == 0) return new int[] {x + width, y};
        if (direction == 1) return new int[] {x - width, y};
        if (direction == 2) return new int[] {x, y + height};
        if (direction == 3) return new int[] {x, y - height};
        return null;
    }

    public int[] inProximity(int px, int py) {
        int[] coordinates;
        Rectangle hitBox = null;

        if (direction == 0) {
            hitBox = new Rectangle(x + 4, y + 36, width,height);
        }

        if (direction == 1) {
            hitBox = new Rectangle(x - 4, y + 36, width,height);
        }

        if (direction == 3) {
            hitBox = new Rectangle(x, y + 22, width, height);
        }


        Rectangle playerRect = new Rectangle(px, py + 62, 18, 18);
        if (hitBox.intersects(playerRect)) {
            coordinates = otherPipe.getCoords();
            return coordinates;
        }

        return null;
    }
}