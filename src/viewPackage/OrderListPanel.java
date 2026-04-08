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

        JButton backButton = new JButton("Back to Home");
        backButton.addActionListener(e -> parentFrame.showHomeView());

        JPanel southPanel = new JPanel();
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
}