package viewPackage.Orders.Dialogs;

import controllerPackage.RestaurantController;
import exceptionPackage.BusinessException;
import modelPackage.entity.OrderLine;
import viewPackage.Shared.Factories.ButtonFactory;
import viewPackage.Shared.Factories.DialogUtils;
import viewPackage.Shared.Factories.LabelFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderLinesDialog extends JDialog {

    private final int orderId;
    private final RestaurantController restaurantController;
    private JTable linesTable;
    private DefaultTableModel tableModel;

    public OrderLinesDialog(JFrame parent, int orderId, RestaurantController restaurantController) {
        super(parent, "Order Lines - Order " + orderId, true);
        this.orderId = orderId;
        this.restaurantController = restaurantController;

        buildInterface();
        loadLines();
    }

    private void buildInterface() {
        setSize(700, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JLabel titleLabel = LabelFactory.createTitleLabel("Lines of order " + orderId, 20);

        String[] columnNames = {"Line No", "Product Name", "Unit Price", "Product", "Quantity", "Line Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        linesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(linesTable);

        JPanel southPanel = buildSouthPanel();

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private JPanel buildSouthPanel() {
        JPanel panel = new JPanel();
        panel.add(ButtonFactory.createPrimaryButton("Close", e -> dispose()));
        return panel;
    }

    private void loadLines() {
        try {
            List<OrderLine> lines = restaurantController.getOrderLinesByOrderId(orderId);
            tableModel.setRowCount(0);

            for (OrderLine line : lines) {
                Object[] row = {
                        line.getNumber(),
                        line.getNameSnapshot(),
                        line.getPriceSnapshot(),
                        line.getProduct().getName(),
                        line.getQuantity(),
                        line.getLineTotal()
                };
                tableModel.addRow(row);
            }
        } catch (BusinessException e) {
            DialogUtils.showError(this, e.getMessage());
        }
    }
}