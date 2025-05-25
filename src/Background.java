import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Background extends JPanel {
    private int tileSize;
    private int height;
    private int width;
    private String levelPath;
    private int[][] levelData = new int[24][24];

    public int level = 1;

    private Image tile0, tile1;

    public void nextLevel() {
        level++;

        levelPath = levelPath = "levels/level" + level + ".txt";
        try {
            loadLevel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Background(int tileSize, int width, int height, int level) throws IOException {
        setOpaque(false);

        tile0 = ImageIO.read(new File("assets/btile_0000.png"));
        tile1 = ImageIO.read(new File("assets/btile_0019.png"));

        this.level = level;

        this.tileSize = tileSize;
        this.width = width;
        this.height = height;
        levelPath = levelPath = "levels/level" + level + ".txt";
        try {
            loadLevel();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setBackground(Color.WHITE);
        setBounds(0, 0, width, height);
    }

    private void loadLevel() throws IOException {
        try (Scanner scanner = new Scanner(new File(levelPath))) {
            for (int i = 0; i < levelData.length; i++) {
                for (int j = 0; j < levelData[i].length; j++) {
                    if (scanner.hasNext()) {
                        levelData[i][j] = Integer.parseInt(scanner.next());
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            drawLevel1(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawLevel1(Graphics window) throws IOException {
        for (int i = 0; i < levelData.length; i++) {
            for (int j = 0; j < levelData[i].length; j++) {
                int[] pos = {j * 24, i * 23};


                if (levelData[i][j] == 0) {
                    window.drawImage(tile0, pos[0], pos[1], null);
                } else if (levelData[i][j] == 1) {
                    window.drawImage(tile1, pos[0], pos[1], null);
                }
            }
        }
    }
}
