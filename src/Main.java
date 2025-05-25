import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JFrame implements KeyListener {
    private static boolean keyPressed = false;
    private static KeyEvent keyEvent = null;

    private Lobby lobby;
    private Game game;
    int TILE_SIZE, OBJECT_TILE_SIZE, WIDTH, HEIGHT;
    Thread gameThread;

    public void showLobby() {
        if (game != null) {
            remove(game);
            game = null;
        }
        lobby = new Lobby(this, WIDTH, HEIGHT);
        lobby.setBounds(0, 0, WIDTH, HEIGHT);
        add(lobby);
        revalidate();
        repaint();
    }

    public void loadLevel(int level) {
        if (lobby != null) {
            remove(lobby);
            lobby = null;
        }

        game = new Game(TILE_SIZE, OBJECT_TILE_SIZE, WIDTH, HEIGHT, level, this);
        add(game);
        game.setFocusable(true);
        game.requestFocusInWindow();
        game.addKeyListener(this);
        revalidate();
        repaint();

        gameThread = new Thread(() -> {
            while (game != null) {
                if (keyPressed) {
                    game.player.movement(keyEvent, true);
                } else {
                    if (keyEvent != null)
                        game.player.movement(keyEvent, false);
                }

                game.player.updatePhysics();
                game.player.repaint();

                try {
                    Thread.sleep(1000 / 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.start();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int TILE_SIZE = 24;
            int OBJECT_TILE_SIZE = 18;
            int WIDTH = 592;
            int HEIGHT = TILE_SIZE * 24 + 18;

            Main frame = new Main();
            frame.setTitle("Game");
            frame.setSize(WIDTH, HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLayout(null);

            frame.TILE_SIZE = TILE_SIZE;
            frame.OBJECT_TILE_SIZE = OBJECT_TILE_SIZE;
            frame.WIDTH = WIDTH;
            frame.HEIGHT = HEIGHT;

            frame.showLobby();
            frame.setVisible(true);
        });
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        keyPressed = true;
        keyEvent = e;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed = false;
        keyEvent = e;
    }
}