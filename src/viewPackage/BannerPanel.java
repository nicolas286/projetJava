package viewPackage;

import javax.swing.*;
import java.awt.*;

public class BannerPanel extends JPanel {

    private String message = "Welcome to Cafe & Restaurant Management   ";
    private int offset = 0;

    public BannerPanel() {
        setPreferredSize(new Dimension(100, 50));
        setBackground(Color.WHITE);

        BannerThread thread = new BannerThread(this);
        thread.start();
    }

    public void moveText() {
        offset++;
        if (offset >= message.length()) {
            offset = 0;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 18));

        String displayed = message.substring(offset) + message.substring(0, offset);
        g.drawString(displayed, 20, 30);
    }
}