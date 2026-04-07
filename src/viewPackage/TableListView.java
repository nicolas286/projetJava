package viewPackage;

import controllerPackage.TableController;
import exceptionPackage.BusinessException;
import modelPackage.RestaurantTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TableListView extends JFrame {

    private final TableController tableController;
    private JTable table;
    private DefaultTableModel tableModel;

    public TableListView(TableController tableController) {
        this.tableController = tableController;

        setTitle("Restaurant Tables");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        loadTables();
    }

    private void initComponents() {
        String[] columnNames = {"Id", "Position X", "Position Y", "étage", "Capacité", "Active"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JLabel titleLabel = new JLabel("listes des tables dans le resto", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadTables() {
        try {
            List<RestaurantTable> tables = tableController.getAllTables();
            tableModel.setRowCount(0);

            for (RestaurantTable restaurantTable : tables) {
                Object[] row = {
                        restaurantTable.getId(),
                        restaurantTable.getPositionX(),
                        restaurantTable.getPositionY(),
                        restaurantTable.getFloor(),
                        restaurantTable.getCapacity(),
                        restaurantTable.isActive()
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