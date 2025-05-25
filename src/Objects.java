import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Objects extends JPanel {
    private int objectTileSize;
    private int height;
    private int width;
    private String levelPath;
    private int[][] levelData = new int[32][32];
    ArrayList<BoostPad> boostPads = new ArrayList<>();
    ArrayList<Integer> cloudIndexes = new ArrayList<>();
    ArrayList<Pipe> pipes = new ArrayList<>();
    ArrayList<Diamond> diamonds = new ArrayList<>();
    ArrayList<Spike> spikes = new ArrayList<>();
    ArrayList<Key> keys = new ArrayList<>();
    ArrayList<Door> doors = new ArrayList<>();
    private final java.util.Map<Integer, BufferedImage> objectImages = new java.util.HashMap<>();
    private ArrayList<CollisionRect> collisions = new ArrayList<>();
    private ArrayList<Lever> levers = new ArrayList<>();
    public static boolean doorsShown = true;
    public int level = 1;
    public ArrayList<BoostPad> getBoostPads() {
        return boostPads;
    }

    public ArrayList<Integer> getCloudsIndexes() {
        return cloudIndexes;
    }

    public ArrayList<Pipe> getPipes() { return pipes; }

    public ArrayList<Diamond> getDiamonds() { return diamonds; }

    public ArrayList<Spike> getSpikes() { return spikes; }

    public ArrayList<Key> getKeys() { return keys; }

    public ArrayList<Door> getDoors() { return doors; }

    public ArrayList<Lever> getLevers() { return levers; }

    int[] objectIds;
    String[] tileFiles;

    public void nextLevel() {
        level++;
        levelPath = "levels/level" + level + "Objects.txt";
        loadLevel();
    }

    public void loadImages(int index) {
        if (index >= objectIds.length || index >= tileFiles.length) {
            return;
        }

        try {
            BufferedImage img = ImageIO.read(new File("assets/" + tileFiles[index]));
            objectImages.put(objectIds[index], img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadImages(index + 1);
    }

    public Objects(int objectTileSize, int width, int height, int level) {
        setOpaque(false);

        this.level = level;

        objectIds = new int[] {
                1, 2, 3, 4, 5, 6, 7,
                8, 9, 10, 11, 12, 13, 14,
                15, 16, 17, 18, 19, 20, 21,
                22, 23, 24, 25, 26, 27, 28,
                29, 30, 31, 32, 33, 34, 35,
                36, 37, 38, 39, 40, 41, 42,
                43, 44, 45, 46, 47, 48, 49,
                50, 51, 52, 53, 54
        };

        tileFiles = new String[] {
                "tile_0022.png", "tile_0122.png", "tile_0017.png", "tile_0018.png", "tile_0019.png", "tile_0037.png", "tile_0038.png",
                "tile_0039.png", "tile_0057.png", "tile_0058.png", "tile_0059.png", "tile_0107.png", "tile_0108.png", "tile_0153.png",
                "tile_0154.png", "tile_0155.png", "tile_0096.png", "tile_0068.png", "tile_0069.png", "tile_0109.png", "tile_0027.png",
                "tile_0067.png", "tile_0067.png", "tile_0099.png", "tile_0134.png", "tile_0135.png", "tile_0150.png", "tile_0110.png",
                "tile_0021.png", "tile_0023.png", "tile_0044.png", "tile_0108.png", "tile_0072.png", "tile_0032.png", "tile_0012.png",
                "tile_0014.png", "tile_0015.png", "tile_0013.png", "tile_0095.png", "tile_0111.png", "tile_0131.png", "tile_0066.png",
                "tile_0130.png", "tile_0089.png", "tile_0156.png", "tile_0137.png", "tile_0097.png", "tile_0077.png", "tile_0079.png",
                "tile_0132.png", "tile_0181.png", "tile_0091.png", "tile_0064.png", "tile_0119.png"
        };
        try {
            for (int i = 0; i < objectIds.length; i++) {
                BufferedImage img = ImageIO.read(new File("assets/" + tileFiles[i]));
                objectImages.put(objectIds[i], img);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.objectTileSize = objectTileSize;
        this.width = width;
        this.height = height;

        levelPath = "levels/level" + level + "Objects.txt";

        loadLevel();

        setOpaque(false);
        setBackground(Color.WHITE);
        setBounds(0, 0, width, height);
    }

    private void loadLevel() {
        try (Scanner scanner = new Scanner(new File(levelPath))) {
            for (int i = 0; i < levelData.length; i++) {
                for (int j = 0; j < levelData[i].length; j++) {
                    if (scanner.hasNext()) {
                        levelData[i][j] = Integer.parseInt(scanner.next());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLevel(g);

        //drawCollisions(g);
    }

    private void drawCollisions(Graphics g) {
        g.setColor(Color.RED);
        for (CollisionRect rect : collisions) {
            g.drawRect(rect.getRectangle().x, rect.getRectangle().y, rect.getRectangle().width, rect.getRectangle().height);
        }
    }

    private void drawLevel(Graphics g) {
        for (int i = 0; i < levelData.length; i++) {
            for (int j = 0; j < levelData[i].length; j++) {
                int[] position = {j * objectTileSize, i * objectTileSize + 6};
                drawObject(g, levelData[i][j], position);
            }
        }
    }

    public ArrayList<CollisionRect > getCollisions() {
        collisions.clear();
        for (int i = 0; i < levelData.length; i++) {
            for (int j = 0; j < levelData[i].length; j++) {
                int[] position = {j * objectTileSize, i * objectTileSize + 6};
                CollisionRect rect;
                switch (levelData[i][j]) {
                    case 1: case 2: case 3: case 4: case 5: case 9: case 11: case 29: case 30:
                        rect = new CollisionRect(position[0], position[1] - 9, 18, 18, 0);
                        collisions.add(rect);
                        break;
                    case 12:
                        rect = new CollisionRect(position[0], position[1] - 1, 18, 10, 1);
                        collisions.add(rect);
                        break;
                    case 14: case 15: case 16: case 45:
                        rect = new CollisionRect(position[0], position[1] - 9, 18, 9, 2);
                        collisions.add(rect);
                        cloudIndexes.add(cloudIndexes.size());
                        break;
                    case 24:
                        if (this.level != 2) {
                            rect = new CollisionRect(position[0], position[1] - 9, 18, 9, 2);
                            cloudIndexes.add(cloudIndexes.size());
                        } else
                            rect = new CollisionRect(position[0], position[1] - 9, 18, 9, 0);
                        collisions.add(rect);
                        break;
                    case 25:
                        rect = new CollisionRect(position[0], position[1] - 9, 18, 18, 2);
                        collisions.add(rect);
                        pipes.add(new Pipe(rect.getRectangle().x, rect.getRectangle().y, rect.getRectangle().width, rect.getRectangle().height, 0));
                        break;
                    case 26:
                        rect = new CollisionRect(position[0], position[1] - 9, 18, 18, 3);
                        pipes.add(new Pipe(rect.getRectangle().x, rect.getRectangle().y, rect.getRectangle().width, rect.getRectangle().height, 2));
                        break;
                    case 39:
                        rect = new CollisionRect(position[0], position[1] - 9, 18, 18, 3);
                        collisions.add(rect);
                        pipes.add(new Pipe(rect.getRectangle().x, rect.getRectangle().y, rect.getRectangle().width, rect.getRectangle().height, 3));
                        break;
                    case 50:
                        rect = new CollisionRect(position[0], position[1] - 9, 18, 18, 3);
                        pipes.add(new Pipe(rect.getRectangle().x, rect.getRectangle().y, rect.getRectangle().width, rect.getRectangle().height, 1));
                        break;
                    case 22: case 23:
                        diamonds.add(new Diamond(position[0], position[1] - 9, 18, 18));
                        break;
                    case 18:
                        spikes.add(new Spike(position[0], position[1] + 9, 18, 9));
                        break;
                    case 21:
                        keys.add(new Key(position[0], position[1], 18, 18));
                        break;
                    case 27: case 43:
                        doors.add(new Door(position[0], position[1], 18, 18, 0));
                        break;
                    case 28:
                        doors.add(new Door(position[0], position[1], 18, 18, 1));
                        break;
                    case 42:
                        levers.add(new Lever(position[0], position[1], 18, 18));
                        break;
                    case 52:
                        rect = new CollisionRect(position[0], position[1] - 9 + 6, 18, 6, 0);
                        collisions.add(rect);
                        break;
                    case 44:
                        rect = new CollisionRect(position[0] + 8, position[1] - 9, 2, 18, 0);
                        collisions.add(rect);
                        break;
                }
            }
        }
        return collisions;
    }

    private BoostPad getFromBoostPads(int[] position) {
        for (BoostPad boostPad : boostPads) {
            if (boostPad.x == position[0] && boostPad.y - 26 == position[1])
                return boostPad;
        }
        return null;
    }
    private void drawObject(Graphics g, int objectType, int[] position) {
        BufferedImage img = objectImages.get(objectType);
        if (img != null) {
            if (objectType == 12) {
                BoostPad boostPad = getFromBoostPads(position);
                if (boostPad != null) {
                    if (boostPad.activated)
                        g.drawImage(objectImages.get(32), position[0], position[1] - 9, null);
                    else
                        g.drawImage(img, position[0], position[1] - 9, null);
                } else {
                    boostPads.add(new BoostPad(position[0], position[1] + 26, 18, 10, false));
                }
            } else if (objectType == 27) {
                if (doorsShown)
                    g.drawImage(img, position[0], position[1] - 9, null);
            } else if (objectType == 22 || objectType == 23) {
                for (Diamond diamond : diamonds) {
                    if (diamond.x == position[0] && diamond.y == position[1] - 9) {
                        if (!diamond.collected)
                            g.drawImage(img, position[0], position[1] - 9, null);
                    }
                }
            } else if (objectType == 42) {
                for (Lever lever : levers) {
                    if (lever.x == position[0] && lever.y == position[1]) {
                        if (lever.activated)
                            g.drawImage(objectImages.get(53), position[0], position[1] - 9, null);
                        else
                            g.drawImage(img, position[0], position[1] - 9, null);
                    }
                }
            } else if (objectType == 28 || objectType == 43) {
                for (Door door : doors) {
                    if (door.x == position[0] && door.y == position[1]) {
                        if (door.visible)
                            g.drawImage(img, position[0], position[1] - 9, null);
                    }
                }
            } else if (objectType == 21) {
                for (Key key : keys) {
                    if (key.x == position[0] && key.y == position[1]) {
                        if (!key.collected)
                            g.drawImage(img, position[0], position[1] - 9, null);
                    }
                }
            } else
                g.drawImage(img, position[0], position[1] - 9, null);
        }
    }
}