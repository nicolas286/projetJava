package viewPackage.RoomPlan;

import controllerPackage.RestaurantController;
import exceptionPackage.BusinessException;
import modelPackage.entity.RestaurantTable;
import viewPackage.MainFrame;
import viewPackage.Shared.Factories.ButtonFactory;
import viewPackage.Shared.Factories.DialogUtils;
import viewPackage.Shared.Factories.LabelFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RoomPlanPanel extends JPanel {

    private final MainFrame parentFrame;
    private final RestaurantController restaurantController;
    private JPanel gridPanel;

    public RoomPlanPanel(MainFrame parentFrame, RestaurantController restaurantController) {
        this.parentFrame = parentFrame;
        this.restaurantController = restaurantController;

        buildInterface();
        loadTables();
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        JLabel titleLabel = LabelFactory.createTitleLabel("Room Plan");

        gridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(gridPanel);

        JPanel southPanel = buildSouthPanel();

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel buildSouthPanel() {
        JPanel panel = new JPanel();
        panel.add(ButtonFactory.createPrimaryButton("Back to Home", e -> parentFrame.showHomeView()));
        return panel;
    }

    private void loadTables() {
        try {
            List<RestaurantTable> tables = restaurantController.getAllTables();
            gridPanel.removeAll();

            for (RestaurantTable table : tables) {
                gridPanel.add(createTableButton(table));
            }

            gridPanel.revalidate();
            gridPanel.repaint();

        } catch (BusinessException e) {
            DialogUtils.showError(this, e.getMessage());
        }
    }

    private JButton createTableButton(RestaurantTable table) {
        JButton button = ButtonFactory.createButton(buildTableButtonText(table));

        if (!table.isActive()) {
            button.setEnabled(false);
        } else {
            button.addActionListener(e -> openTableOrders(table));
        }

        return button;
    }

    private String buildTableButtonText(RestaurantTable table) {
        return "<html><center>" +
                "Table " + table.getId() +
                "<br/>Capacity: " + table.getCapacity() +
                "<br/>" + (table.isActive() ? "Active" : "Inactive") +
                "</center></html>";
    }

    private void openTableOrders(RestaurantTable table) {
        TableOrdersDialog dialog = new TableOrdersDialog(parentFrame, table, restaurantController);
        dialog.setVisible(true);
    }
}