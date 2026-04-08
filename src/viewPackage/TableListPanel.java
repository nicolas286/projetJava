package viewPackage;

import controllerPackage.TableController;
import exceptionPackage.BusinessException;
import modelPackage.RestaurantTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TableListPanel extends JPanel {

    private final MainFrame parentFrame;
    private final TableController tableController;
    private JTable tablesTable;
    private DefaultTableModel tableModel;

    public TableListPanel(MainFrame parentFrame, TableController tableController) {
        this.parentFrame = parentFrame;
        this.tableController = tableController;

        buildInterface();
        loadTables();
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Room Plan - Restaurant Tables", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        String[] columnNames = {"Id", "Position X", "Position Y", "Floor", "Capacity", "Active"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablesTable);

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
            tableModel.setRowCount(0);

            for (RestaurantTable table : tables) {
                Object[] row = {
                        table.getId(),
                        table.getPositionX(),
                        table.getPositionY(),
                        table.getFloor(),
                        table.getCapacity(),
                        table.isActive()
                };
                tableModel.addRow(row);
            }
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}