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

        BannerPanel bannerPanel = new BannerPanel();

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(titleLabel, BorderLayout.NORTH);
        northPanel.add(bannerPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        JButton productsButton = new JButton("Products");
        JButton menusButton = new JButton("Menus");
        JButton roomPlanButton = new JButton("Room Plan");
        JButton ordersButton = new JButton("Orders");
        JButton search1Button = new JButton("Search 1");
        JButton search2Button = new JButton("Search 2");
        JButton search3Button = new JButton("Search 3");


        roomPlanButton.addActionListener(e -> parentFrame.showTablesView());
        ordersButton.addActionListener(e -> parentFrame.showOrdersView());
        search1Button.addActionListener(e -> parentFrame.showSearchOrdersByTableView());
        search2Button.addActionListener(e -> parentFrame.showSearchProductCategoryConstraintView());
        search3Button.addActionListener(e -> parentFrame.showSearchLotStorageProductView());

        productsButton.setEnabled(false);
        menusButton.setEnabled(false);


        centerPanel.add(productsButton);
        centerPanel.add(menusButton);
        centerPanel.add(roomPlanButton);
        centerPanel.add(ordersButton);
        centerPanel.add(search1Button);
        centerPanel.add(search2Button);
        centerPanel.add(search3Button);


        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}