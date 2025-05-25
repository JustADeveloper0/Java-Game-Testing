import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Lobby extends JPanel {
    private Main mainFrame;

    public Lobby(Main mainFrame, int width, int height) {
        this.mainFrame = mainFrame;
        setLayout(null);
        setBackground(new Color(240, 240, 240));

        JLabel instructionsLabel = new JLabel("<html>Instructions: <br> W: Jump <br> A: Move Left <br> D: Move Right <br> E: Interact with Levers <br> S: Fall on a Passable Tile</html>");
        instructionsLabel.setBounds(10, 10, 300, 110);
        instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionsLabel.setForeground(new Color(50, 50, 50));
        add(instructionsLabel);

        createButton("Level 1", (width / 2) - 160, (height / 2) - 135, 1);
        createButton("Level 2", (width / 2) - 50, (height / 2) - 135, 2);
        createButton("Level 3", (width / 2) + 60, (height / 2) - 135, 3);
        createButton("Level 4", (width / 2) - 110, (height / 2) - 25, 4);
        createButton("Level 5", (width / 2) + 10, (height / 2) - 25, 5);
    }

    private void createButton(String text, int x, int y, int level) {
        RoundedButton button = new RoundedButton(text);
        button.setBounds(x, y, 100, 100);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> mainFrame.loadLevel(level));
        add(button);
    }

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            repaint();
        }
    }
}