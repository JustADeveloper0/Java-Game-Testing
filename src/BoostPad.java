import java.awt.Rectangle;

public class BoostPad {
    public int x, y;
    public int width, height;
    public boolean activated;

    private double retractTimer = 0.5;
    private final double retractTimerMax = 0.5;

    public void timer() {
        if (retractTimer <= 0) {
            activated = false;
            retractTimer = retractTimerMax;
        }
        retractTimer -= 0.01;
    }

    public BoostPad(int x, int y, int width, int height, boolean activated) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.activated = activated;
    }

    public boolean inProximity(Rectangle playerRect) {
        int ny = y - 36;
        return (playerRect.x + playerRect.getSize().width > x) &&
                (playerRect.x < x + width) &&
                (playerRect.y + playerRect.getSize().height >= ny) &&
                (playerRect.y <= ny + height);
    }
}