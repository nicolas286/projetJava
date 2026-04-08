package viewPackage;

import controllerPackage.OrderController;
import exceptionPackage.BusinessException;
import modelPackage.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderListPanel extends JPanel {

    private final MainFrame parentFrame;
    private final OrderController orderController;
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public OrderListPanel(MainFrame parentFrame, OrderController orderController) {
        this.parentFrame = parentFrame;
        this.orderController = orderController;

        buildInterface();
        loadOrders();
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Orders", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        String[] columnNames = {"Id", "Date Ordered", "Date Completed", "Status", "Table", "Date Delivered"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        JButton addButton = new JButton("Add Order");
        JButton takeOrderButton = new JButton("Take Order");
        JButton viewLinesButton = new JButton("View Selected Order Lines");
        JButton backButton = new JButton("Back to Home");

        addButton.addActionListener(e -> openAddDialog());
        takeOrderButton.addActionListener(e -> openTakeOrderDialog());
        viewLinesButton.addActionListener(e -> openSelectedOrderLines());
        backButton.addActionListener(e -> parentFrame.showHomeView());

        JPanel southPanel = new JPanel();
        southPanel.add(addButton);
        southPanel.add(takeOrderButton);
        southPanel.add(viewLinesButton);
        southPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        try {
            List<Order> orders = orderController.getAllOrders();
            tableModel.setRowCount(0);

            for (Order order : orders) {
                Object[] row = {
                        order.getId(),
                        order.getDateOrdered(),
                        order.getDateCompleted(),
                        order.getStatus(),
                        order.getTableId(),
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

    private void openAddDialog() {
        OrderFormDialog dialog = new OrderFormDialog(parentFrame, orderController);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadOrders();
        }
    }

    private void openTakeOrderDialog() {
        TakeOrderDialog dialog = new TakeOrderDialog(parentFrame);
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

        OrderLinesDialog dialog = new OrderLinesDialog(parentFrame, orderId);
        dialog.setVisible(true);
    }
}