package viewPackage.Home;

import viewPackage.Home.Banner.BannerPanel;
import viewPackage.MainFrame;
import viewPackage.Shared.Factories.ButtonFactory;
import viewPackage.Shared.Factories.LabelFactory;

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

        add(buildNorthPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel buildNorthPanel() {
        return createNorthPanel(
                LabelFactory.createTitleLabel("Cafe & Restaurant Management"),
                new BannerPanel()
        );
    }

    private JPanel buildCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        panel.add(ButtonFactory.createDisabledButton("Products"));
        panel.add(ButtonFactory.createDisabledButton("Menus"));
        panel.add(ButtonFactory.createPrimaryButton("Room Plan", e -> parentFrame.showTablesView()));
        panel.add(ButtonFactory.createPrimaryButton("Orders", e -> parentFrame.showOrdersView()));
        panel.add(ButtonFactory.createPrimaryButton("Search 1", e -> parentFrame.showSearchOrdersByTableView()));
        panel.add(ButtonFactory.createPrimaryButton("Search 2", e -> parentFrame.showSearchProductCategoryConstraintView()));
        panel.add(ButtonFactory.createPrimaryButton("Search 3", e -> parentFrame.showSearchLotStorageProductView()));

        return panel;
    }

    private JPanel createNorthPanel(JComponent top, JComponent bottom) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(top, BorderLayout.NORTH);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

}