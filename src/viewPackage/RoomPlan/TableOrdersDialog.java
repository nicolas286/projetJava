package viewPackage.RoomPlan;

import controllerPackage.RestaurantController;
import exceptionPackage.BusinessException;
import modelPackage.entity.Order;
import modelPackage.entity.RestaurantTable;
import viewPackage.Orders.Dialogs.OrderLinesDialog;
import viewPackage.Shared.Factories.ButtonFactory;
import viewPackage.Shared.Factories.DialogUtils;
import viewPackage.Shared.Factories.LabelFactory;
import viewPackage.Shared.Components.TakeOrderDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TableOrdersDialog extends JDialog {

    private final RestaurantTable restaurantTable;
    private final RestaurantController restaurantController;
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public TableOrdersDialog(JFrame parent,
                             RestaurantTable restaurantTable,
                             RestaurantController restaurantController) {

        super(parent, "Orders of " + restaurantTable, true);

        if (restaurantController == null) {
            throw new IllegalArgumentException("RestaurantController cannot be null.");
        }

        this.restaurantTable = restaurantTable;
        this.restaurantController = restaurantController;

        buildInterface();
        loadOrders();
    }

    private void buildInterface() {
        setSize(850, 420);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JLabel titleLabel = LabelFactory.createTitleLabel("Orders of " + restaurantTable, 20);

        String[] columnNames = {"Id", "Date Ordered", "Date Completed", "Status", "Date Delivered"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buildSouthPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildSouthPanel() {
        JPanel panel = new JPanel();

        panel.add(ButtonFactory.createPrimaryButton("Take Order For This Table", e -> openTakeOrderDialog()));
        panel.add(ButtonFactory.createPrimaryButton("View Selected Order Lines", e -> openSelectedOrderLines()));
        panel.add(ButtonFactory.createPrimaryButton("Close", e -> dispose()));

        return panel;
    }

    private void loadOrders() {
        try {
            List<Order> orders = restaurantController.getOrdersByTableId(restaurantTable.getId());
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
            DialogUtils.showError(this, e.getMessage());
        }
    }

    private void openTakeOrderDialog() {
        TakeOrderDialog dialog = new TakeOrderDialog((JFrame) getParent(), restaurantController);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadOrders();
        }
    }

    private void openSelectedOrderLines() {
        int selectedRow = ordersTable.getSelectedRow();

        if (selectedRow < 0) {
            DialogUtils.showInfo(this, "Please select an order first.");
            return;
        }

        int orderId = (Integer) tableModel.getValueAt(selectedRow, 0);

        OrderLinesDialog dialog = new OrderLinesDialog((JFrame) getParent(), orderId, restaurantController);
        dialog.setVisible(true);
    }
}