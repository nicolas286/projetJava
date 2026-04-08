package viewPackage;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    private final MainFrame parentFrame;

    public HomePanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        buildInterface();
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Cafe & Restaurant Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        JButton productsButton = new JButton("Products");
        JButton menusButton = new JButton("Menus");
        JButton roomPlanButton = new JButton("Room Plan");
        JButton ordersButton = new JButton("Orders");

        roomPlanButton.addActionListener(e -> parentFrame.showTablesView());
        ordersButton.addActionListener(e -> parentFrame.showOrdersView());

        productsButton.setEnabled(false);
        menusButton.setEnabled(false);

        centerPanel.add(productsButton);
        centerPanel.add(menusButton);
        centerPanel.add(roomPlanButton);
        centerPanel.add(ordersButton);

        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}