import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Game extends JLayeredPane {
    public Player player;
    public int level;

    public Game(int tileSize, int objectTileSize, int width, int height, int level, Main mainFrame) {
        setLayout(null);
        setBounds(0, 0, width, height);

        this.level = level;

        try {
            Lobby lobby = new Lobby(mainFrame, width, height);
            Background background = new Background(tileSize, width, height, this.level);
            Objects objects = new Objects(objectTileSize, width, height, this.level);

            ArrayList<CollisionRect> collisionBoxes = objects.getCollisions();
            ArrayList<BoostPad> boostPads = objects.getBoostPads();
            ArrayList<Pipe> pipes = objects.getPipes();
            ArrayList<Diamond> diamonds = objects.getDiamonds();
            ArrayList<Spike> spikes = objects.getSpikes();
            ArrayList<Key> keys = objects.getKeys();
            ArrayList<Door> doors = objects.getDoors();
            ArrayList<Lever> levers = objects.getLevers();
            int spawnX = 35;
            int spawnY = -50;

            player = new Player("assets/ctile_0000.png",
                    18, 18,
                    collisionBoxes, boostPads, pipes, diamonds, spikes, keys, doors, levers,
                    2, 6, this.level, mainFrame);

            background.setBounds(0, 0, width, height);
            objects.setBounds(0, 0, width, height);
            player.setBounds(0, 0, width, height);

            JButton backButton = new JButton("Back to Lobby");
            backButton.setFont(new Font("Arial", Font.BOLD, 10));
            backButton.setBounds(10, 10, 110, 15);
            backButton.addActionListener(e -> {
                mainFrame.showLobby();
            });

            add(background, Integer.valueOf(0));
            add(objects, Integer.valueOf(1));
            add(player, Integer.valueOf(2));
            add(backButton, Integer.valueOf(3));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}