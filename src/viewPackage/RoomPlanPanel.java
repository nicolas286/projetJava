package viewPackage;

import controllerPackage.TableController;
import exceptionPackage.BusinessException;
import modelPackage.entity.RestaurantTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RoomPlanPanel extends JPanel {

    private final MainFrame parentFrame;
    private final TableController tableController;
    private JPanel gridPanel;

    public RoomPlanPanel(MainFrame parentFrame, TableController tableController) {
        this.parentFrame = parentFrame;
        this.tableController = tableController;

        buildInterface();
        loadTables();
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Room Plan", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        gridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(gridPanel);

        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> parentFrame.showHomeView());

        JPanel southPanel = new JPanel();
        southPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadTables() {
        try {
            List<RestaurantTable> tables = tableController.getAllTables();
            gridPanel.removeAll();

            for (RestaurantTable table : tables) {
                JButton tableButton = new JButton("<html><center>" +
                        "Table " + table.getId() +
                        "<br/>Capacity: " + table.getCapacity() +
                        "<br/>" + (table.isActive() ? "Active" : "Inactive") +
                        "</center></html>");

                if (!table.isActive()) {
                    tableButton.setEnabled(false);
                } else {
                    tableButton.addActionListener(e -> openTableOrders(table));
                }

                gridPanel.add(tableButton);
            }

            gridPanel.revalidate();
            gridPanel.repaint();

        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void openTableOrders(RestaurantTable table) {
        TableOrdersDialog dialog = new TableOrdersDialog(parentFrame, table);
        dialog.setVisible(true);
    }
}