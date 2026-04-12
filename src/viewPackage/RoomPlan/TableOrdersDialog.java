package viewPackage.RoomPlan;

import controllerPackage.OrderController;
import exceptionPackage.BusinessException;
import modelPackage.entity.Order;
import modelPackage.entity.RestaurantTable;
import viewPackage.Orders.Dialogs.OrderLinesDialog;
import viewPackage.Orders.Dialogs.TakeOrderDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TableOrdersDialog extends JDialog {

    private final RestaurantTable restaurantTable;
    private final OrderController orderController;
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public TableOrdersDialog(JFrame parent, RestaurantTable restaurantTable) {
        super(parent, "Orders of " + restaurantTable, true);
        this.restaurantTable = restaurantTable;
        this.orderController = new OrderController();

        buildInterface();
        loadOrders();
    }

    private void buildInterface() {
        setSize(850, 420);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Orders of " + restaurantTable, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        String[] columnNames = {"Id", "Date Ordered", "Date Completed", "Status", "Date Delivered"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        JButton takeOrderButton = new JButton("Take Order For This Table");
        JButton viewLinesButton = new JButton("View Selected Order Lines");
        JButton closeButton = new JButton("Close");

        takeOrderButton.addActionListener(e -> openTakeOrderDialog());
        viewLinesButton.addActionListener(e -> openSelectedOrderLines());
        closeButton.addActionListener(e -> dispose());

        JPanel southPanel = new JPanel();
        southPanel.add(takeOrderButton);
        southPanel.add(viewLinesButton);
        southPanel.add(closeButton);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        try {
            List<Order> orders = orderController.getOrdersByTableId(restaurantTable.getId());
            tableModel.setRowCount(0);

            for (Order order : orders) {
                Object[] row = {
                        order.getId(),
                        order.getDateOrdered(),
                        order.getDateCompleted(),
                        order.getStatus(),
                        order.getDateDelivered()
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

    private void openTakeOrderDialog() {
        TakeOrderDialog dialog = new TakeOrderDialog((JFrame) getParent(), restaurantTable);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadOrders();
        }
    }

    private void openSelectedOrderLines() {
        int selectedRow = ordersTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an order first.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int orderId = (Integer) tableModel.getValueAt(selectedRow, 0);

        OrderLinesDialog dialog = new OrderLinesDialog((JFrame) getParent(), orderId);
        dialog.setVisible(true);
    }
}