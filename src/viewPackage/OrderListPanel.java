package viewPackage;

import controllerPackage.OrderController;
import exceptionPackage.BusinessException;
import modelPackage.entity.Order;

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

        String[] columnNames = {"Id", "Date Ordered", "Date Completed", "Status", "Paid", "Table", "Date Delivered"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ordersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        JButton addButton = new JButton("Add Order");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton takeOrderButton = new JButton("Take Order");
        JButton viewLinesButton = new JButton("View Selected Order Lines");
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Home");

        addButton.addActionListener(e -> openAddDialog());
        editButton.addActionListener(e -> openEditDialog());
        deleteButton.addActionListener(e -> deleteSelectedOrder());
        takeOrderButton.addActionListener(e -> openTakeOrderDialog());
        viewLinesButton.addActionListener(e -> openSelectedOrderLines());
        refreshButton.addActionListener(e -> loadOrders());
        backButton.addActionListener(e -> parentFrame.showHomeView());

        JPanel southPanel = new JPanel();
        southPanel.add(addButton);
        southPanel.add(editButton);
        southPanel.add(deleteButton);
        southPanel.add(takeOrderButton);
        southPanel.add(viewLinesButton);
        southPanel.add(refreshButton);
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
                        order.isPaid(),
                        order.getTableId(),
                        order.getDateDelivered()
                };
                tableModel.addRow(row);
            }
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddDialog() {
        OrderFormDialog dialog = new OrderFormDialog(parentFrame, orderController);
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            loadOrders();
        }
    }

    private void openEditDialog() {
        int selectedRow = ordersTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order first.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int orderId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Order order = orderController.getOrderById(orderId);

            if (order == null) {
                JOptionPane.showMessageDialog(this, "Selected order not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            OrderFormDialog dialog = new OrderFormDialog(parentFrame, orderController, order);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                loadOrders();
            }
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedOrder() {
        int selectedRow = ordersTable.getSelectedRow();

        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an order first.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int orderId = (Integer) tableModel.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete order " + orderId + " ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                orderController.deleteOrder(orderId);
                loadOrders();
            } catch (BusinessException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
            JOptionPane.showMessageDialog(this, "Please select an order first.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int orderId = (Integer) tableModel.getValueAt(selectedRow, 0);

        OrderLinesDialog dialog = new OrderLinesDialog(parentFrame, orderId);
        dialog.setVisible(true);
    }
}