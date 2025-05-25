import org.w3c.dom.ranges.DocumentRange;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

class PlayerImage extends Canvas {
    private BufferedImage img;
    private int x, y;
    private String name = "idle";
    private int diamonds = 0;
    private int health = 6;

    Key key = null;

    Image fullHeart;
    Image halfHeart;
    Image emptyHeart;

    {
        try {
            fullHeart = ImageIO.read(new File("assets/tile_0044.png"));
            halfHeart = ImageIO.read(new File("assets/tile_0045.png"));
            emptyHeart = ImageIO.read(new File("assets/tile_0046.png"));
        } catch (IOException e) {

        }
    }

    public void removeHealth(int amount) {
        health -= amount;
    }

    public void setKey(Key key_) {
        key = key_;
    }

    public int getHealth() {
        return health;
    }

    public PlayerImage(int x, int y, String path) {
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.x = x;
        this.y = y;
    }

    public String getImage() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String path) {
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(img, x, y + 26, null);

        g2d.setColor(Color.blue);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Diamonds: " + diamonds, 15, 525);

        if (key != null) {
            g2d.setColor(Color.red);
            g2d.drawString("A key is being held!", 150, 525);
        }

        switch (health) {
            case 6:
                g2d.drawImage(fullHeart, 500, 15, null);
                g2d.drawImage(fullHeart, 520, 15, null);
                g2d.drawImage(fullHeart, 540, 15, null);
                break;
            case 5:
                g2d.drawImage(halfHeart, 500, 15, null);
                g2d.drawImage(fullHeart, 520, 15, null);
                g2d.drawImage(fullHeart, 540, 15, null);
                break;
            case 4:
                g2d.drawImage(emptyHeart, 500, 15, null);
                g2d.drawImage(fullHeart, 520, 15, null);
                g2d.drawImage(fullHeart, 540, 15, null);
                break;
            case 3:
                g2d.drawImage(emptyHeart, 500, 15, null);
                g2d.drawImage(halfHeart, 520, 15, null);
                g2d.drawImage(fullHeart, 540, 15, null);
                break;
            case 2:
                g2d.drawImage(emptyHeart, 500, 15, null);
                g2d.drawImage(emptyHeart, 520, 15, null);
                g2d.drawImage(fullHeart, 540, 15, null);
                break;
            case 1:
                g2d.drawImage(emptyHeart, 500, 15, null);
                g2d.drawImage(emptyHeart, 520, 15, null);
                g2d.drawImage(halfHeart, 540, 15, null);
                break;
            case 0:
                break;
        }
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x_) { x = x_; }
    public void setY(int y_) { y = y_; }

    public int getDiamonds() { return diamonds; }
    public void setDiamonds(int diamonds_) { diamonds = diamonds_; }
    public void addDiamond(int amount) {diamonds += amount; }
}

public class Player extends JPanel {
    private int speed;
    private int jumpSpeed;

    private int width;
    private int height;

    private double velocityX;
    private double velocityY;

    private double gravity = 0.25;
    private final double maxFallSpeed = 10;

    private boolean onGround = false;
    private int groundLevel;

    PlayerImage playerImage;

    ArrayList<CollisionRect> collisions;
    ArrayList<BoostPad> boostPads;

    private double walkAnimDelay = 0.1;
    private final double walkAnimDelayMax = 0.1;

    private double boostPadTimer = 0.25;
    private final double boostPadTimerMax = 0.25;
    private BoostPad boostPadOn = null;
    private int boostPadPower = 9;

    private ArrayList<Integer> cloudIndexes;
    private boolean cloudCollision = true;

    private final int hitboxOffsetY = 32;
    private final int hitboxHeight = 18;
    private final int hitboxWidth = 18;
    private final int renderOffsetY = 26;

    ArrayList<Pipe> pipes;
    ArrayList<Diamond> diamonds;
    ArrayList<Spike> spikes;
    ArrayList<Key> keys;
    ArrayList<Door> doors;
    ArrayList<Lever> levers;

    private boolean leverActivation = false;

    private int spawnX;
    private int spawnY;

    public int level;
    private Main mainFrame;

    public void updateCollisions(ArrayList<CollisionRect> collisionRects) {
        collisions.clear();
        collisions.addAll(collisionRects);
    }

    public void updateBoostPads(ArrayList<BoostPad> boostPads_) {
        boostPads.clear();
        boostPads.addAll(boostPads_);
    }

    public void updatePipes(ArrayList<Pipe> pipes_) {
        pipes.clear();
        pipes.addAll(pipes_);
    }

    public void updateDiamonds(ArrayList<Diamond> diamonds_) {
        diamonds.clear();
        diamonds.addAll(diamonds_);
    }

    public void updateSpikes(ArrayList<Spike> spikes_) {
        spikes.clear();
        spikes.addAll(spikes_);
    }

    public void updateKeys(ArrayList<Key> keys_) {
        keys.clear();
        keys.addAll(keys_);
    }

    public void updateDoors(ArrayList<Door> doors_) {
        doors.clear();
        doors.addAll(doors_);
    }

    public Player(String path, int width, int height, ArrayList<CollisionRect> collisionBoxes, ArrayList<BoostPad> boostPads, ArrayList<Pipe> pipes, ArrayList<Diamond> diamonds, ArrayList<Spike> spikes, ArrayList<Key> keys, ArrayList<Door> doors, ArrayList<Lever> levers, int speed, int jumpSpeed, int level, Main mainFrame) {
        setOpaque(false);

        switch (level) {
            case 1:
                spawnX = 15;
                spawnY = 380;
                break;
            case 2:
                spawnX = 5;
                spawnY = 300;
                break;
            case 3:
                spawnX = 5;
                spawnY = 25;
                break;
            case 4:
                spawnX = 5;
                spawnY = 425;
                break;
            case 5:
                spawnX = 5;
                spawnY = 375;
                break;
        }

        playerImage = new PlayerImage(spawnX, spawnY, path);

        this.spawnX = spawnX;
        this.spawnY = spawnY;

        this.width = width;
        this.height = height;

        this.speed = speed;
        this.jumpSpeed = jumpSpeed;

        this.collisions = collisionBoxes;
        this.boostPads = boostPads;

        this.pipes = pipes;
        this.diamonds = diamonds;
        this.spikes = spikes;
        this.keys = keys;
        this.doors = doors;
        this.levers = levers;

        this.level = level;
        this.mainFrame = mainFrame;

        if (this.keys.size() == 1 && this.doors.size() == 2) {
            for (Door door : doors) {
                if (door.type == 0)
                    this.keys.get(0).opens(door);
            }
        }

        if (this.pipes.size() == 2) {
            pipes.get(0).connectPipe(pipes.get(1));
            pipes.get(1).connectPipe(pipes.get(0));
        }

        if (this.level == 2) {
            ArrayList<Pipe> outPipe = new ArrayList<Pipe>();

            for (Pipe pipe : this.pipes) {
                if (pipe.direction == 2)
                    outPipe.add(pipe);
            }

            Collections.shuffle(outPipe);

            int index = 0;
            for (Pipe pipe : this.pipes) {
                if (pipe.direction != 2) {
                    pipe.connectPipe(outPipe.get(index));
                    index++;
                }
            }

            for (Door door : doors) {
                if (door.type == 0)
                    keys.get(0).door = door;
                door.visible = false;
            }
        }

        if (level == 4) {
            Pipe decisionPipe = null;
            for (Pipe pipe : pipes) {
                if (decisionPipe == null) {
                    if (pipe.direction == 2)
                        decisionPipe = pipe;
                } else {
                    if (pipe.direction == 2 && pipe.y < decisionPipe.y)
                        decisionPipe = pipe;
                }
            }

            for (Pipe pipe : pipes) {
                if (pipe.direction == 0 || pipe.direction == 1) {
                    pipe.connectPipe(decisionPipe);
                }
            }

            ArrayList<Pipe> outPipes = new ArrayList<>();

            for (Pipe pipe : this.pipes) {
                if (pipe.direction == 2 && pipe != decisionPipe)
                    outPipes.add(pipe);
            }

            Collections.shuffle(outPipes);

            int index = 0;
            for (Pipe pipe : this.pipes) {
                if (pipe.direction == 3) {
                    pipe.connectPipe(outPipes.get(index));
                    index++;
                }
            }
        }

        if (level == 5) {
            boostPadPower = 11;


            ArrayList<Pipe> outPipes = new ArrayList<>();

            for (Pipe pipe : this.pipes) {
                if (pipe.direction == 2)
                    outPipes.add(pipe);
            }

            Pipe exitPipe = null;
            for (Pipe pipe : pipes) {
                if (exitPipe == null) {
                    if (pipe.direction == 3)
                        exitPipe = pipe;
                } else {
                    if (pipe.direction == 3 && pipe.y < exitPipe.y) {
                        exitPipe = pipe;
                    }
                }
            }

            Pipe doorPipe = null;
            for (Pipe pipe : pipes) {
                if (doorPipe == null) {
                    if (pipe.direction == 2)
                        doorPipe = pipe;
                } else {
                    if (pipe.direction == 2 && pipe.y < doorPipe.y && pipe.x < doorPipe.x) {
                        doorPipe = pipe;
                    }
                }
            }

            Collections.shuffle(outPipes);

            int index = 0;
            for (Pipe pipe : this.pipes) {
                if (pipe.direction == 3 && pipe != exitPipe) {
                    pipe.connectPipe(outPipes.get(index));
                    index++;
                }
            }

            exitPipe.connectPipe(doorPipe);
        }

        groundLevel = 1000;
    }

    public void movement(KeyEvent keyEvent, boolean keyPressed) {
        int keyCode = keyEvent.getKeyCode();

        if (keyPressed) {
            if (keyCode == KeyEvent.VK_W && onGround) {
                velocityY = jumpSpeed;
                onGround = false;
            }

            if (keyCode == KeyEvent.VK_A) {
                velocityX = -speed;
            } else if (keyCode == KeyEvent.VK_D) {
                velocityX = speed;
            }

            if (keyCode == KeyEvent.VK_S) {
                cloudCollision = false;
            }

            if (keyCode == keyEvent.VK_E) {
                leverActivation = true;
            }

        } else {
            if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_D) {
                velocityX = 0;
            }

            if (keyCode == KeyEvent.VK_S) {
                cloudCollision = true;
            }

            if (keyCode == keyEvent.VK_E) {
                leverActivation = false;
            }
        }
    }

    public void updatePhysics() {
        for (Lever lever : levers) {
            if (lever.inProximity(playerImage.getX(), playerImage.getY())) {
                if (leverActivation) {
                    lever.activated = true;

                    if (level == 2) {
                        for (Door door : doors) {
                            door.visible = true;
                        }
                    }

                    if (level == 3) {
                        gravity = 0.05;
                    }
                }
            }
        }

        for (Door door : doors) {
            if (door.type == 0 && door.visible) {
                if (door.inProximity(playerImage.getX(), playerImage.getY())) {
                    if (playerImage.key != null) {
                        for (Key key : keys) {
                            if (playerImage.key == key && key.door == door && !door.used) {
                                velocityX = 0;
                                velocityY = 0;
                                mainFrame.showLobby();
                                door.used = true;
                            }
                        }
                    }
                }
            }
        }

        for (Key key : keys) {
            if (key.inProximity(playerImage.getX(), playerImage.getY())) {
                playerImage.setKey(key);
                key.collected = true;
            }
        }

        for (Spike spike : spikes) {
            if (spike.inProximity(playerImage.getX(), playerImage.getY())) {
                playerImage.setX(spawnX);
                playerImage.setY(spawnY);
                playerImage.removeHealth(1);
                gravity = 0.25;

                for (Lever lever : levers) {
                    lever.activated = false;
                }

                for (Key key : keys) {
                    key.collected = false;
                }

                for (Diamond diamond : diamonds) {
                    diamond.collected = false;
                }

                playerImage.setDiamonds(0);
                playerImage.key = null;

                if (playerImage.getHealth() <= 0) {
                    velocityX = 0;
                    velocityY = 0;
                    mainFrame.showLobby();
                }
            }
        }

        for (Diamond diamond : diamonds) {
            if (diamond.inProximity(playerImage.getX(), playerImage.getY())) {
                diamond.collected = true;
                playerImage.addDiamond(1);
            }
        }

        for (Pipe pipe : pipes) {
            if (pipe.direction != 2) {
                int[] vector = pipe.inProximity(playerImage.getX(), playerImage.getY());
                if (vector != null) {
                    playerImage.setX(vector[0]);
                    playerImage.setY(vector[1] - 32);
                    velocityY = 0;
                }
            }
        }

        if (boostPadTimer <= 0) {
            velocityY += boostPadPower;
            if (boostPadOn != null)
                boostPadOn.activated = true;
            boostPadTimer = boostPadTimerMax;
        }

        boolean onOnePad = false;

        Rectangle playerRect = new Rectangle(
                playerImage.getX() + 2,
                playerImage.getY() + hitboxOffsetY,
                hitboxWidth,
                hitboxHeight
        );

        for (BoostPad boostPad : boostPads) {
            if (boostPad.activated)
                boostPad.timer();

            if (boostPad.inProximity(playerRect)) {
                boostPadTimer -= 0.01;
                boostPadOn = boostPad;
                onOnePad = true;
            }
        }

        if (boostPadTimer != boostPadTimerMax && !onOnePad) {
            boostPadTimer = boostPadTimerMax;
            boostPadOn = null;
        }

        if (velocityX != 0) {
            if (playerImage.getImage().equals("idle") && walkAnimDelay <= 0) {
                playerImage.setImage("assets/ctile_0001.png");
                playerImage.setName("run");
                walkAnimDelay = walkAnimDelayMax;
            } else if (walkAnimDelay <= 0) {
                playerImage.setImage("assets/ctile_0000.png");
                playerImage.setName("idle");
                walkAnimDelay = walkAnimDelayMax;
            }
        } else {
            if (walkAnimDelay <= 0) {
                playerImage.setImage("assets/ctile_0000.png");
                playerImage.setName("idle");
                walkAnimDelay = walkAnimDelayMax;
            }
        }

        walkAnimDelay -= 0.01;

        velocityY -= gravity;
        if (velocityY < -maxFallSpeed)
            velocityY = -maxFallSpeed;

        int nextX = (int)(playerImage.getX() + velocityX);
        int nextY = (int)(playerImage.getY() - velocityY);

        boolean collidedX = false;
        boolean collidedY = false;

        for (CollisionRect rect : collisions) {
            int bx = rect.getRectangle().x - 2;
            int by = rect.getRectangle().y;
            int bw = rect.getRectangle().width;
            int bh = rect.getRectangle().height;

            float playerFeetY = playerImage.getY() + height;

            Rectangle futureX = new Rectangle(nextX, playerImage.getY(), width, height);
            Rectangle block = new Rectangle(bx, by, bw, bh);

            Rectangle blockBounds = block.getBounds();
            if (futureX.intersects(blockBounds)) {
                boolean verticalOverlap = futureX.y + futureX.height > blockBounds.y + 1 &&
                        futureX.y < blockBounds.y + blockBounds.height - 1;
                if (verticalOverlap) {
                    collidedX = true;
                    break;
                }
            }


            Rectangle futureY = new Rectangle(
                    playerImage.getX() + 2,
                    nextY + hitboxOffsetY,
                    hitboxWidth,
                    hitboxHeight
            );

            if (futureY.intersects(block)) {
                if (rect.getType() == 2) {
                    if (cloudCollision) {
                        if (playerImage.getY() <= by + height && velocityY > 0) {
                            groundLevel = by;
                        } else if (velocityY < 0 && playerImage.getY() <= by + height) {
                            collidedY = true;
                            groundLevel = by;
                        }
                    }
                } else {
                    collidedY = true;
                    if (velocityY < 0) {
                        groundLevel = by;
                    }
                }
            }
        }

        if (!collidedX) {
            playerImage.setX(nextX);
        } else {
            velocityX = 0;
        }

        if (!collidedY) {
            playerImage.setY(nextY);
            onGround = false;
        } else {
            if (velocityY < 0) {
                playerImage.setY(groundLevel - hitboxHeight - hitboxOffsetY);
                onGround = true;
            }
            velocityY = 0;
        }
    }

    @Override
    public void paint(Graphics g) {
        playerImage.render(g);
    }
}