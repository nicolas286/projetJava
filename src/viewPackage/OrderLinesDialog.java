package viewPackage;

import controllerPackage.OrderLineController;
import exceptionPackage.BusinessException;
import modelPackage.entity.OrderLine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderLinesDialog extends JDialog {

    private final int orderId;
    private final OrderLineController orderLineController;
    private JTable linesTable;
    private DefaultTableModel tableModel;

    public OrderLinesDialog(JFrame parent, int orderId) {
        super(parent, "Order Lines - Order " + orderId, true);
        this.orderId = orderId;
        this.orderLineController = new OrderLineController();

        buildInterface();
        loadLines();
    }

    private void buildInterface() {
        setSize(700, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Lines of order " + orderId, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        String[] columnNames = {"Line No", "Product Name", "Unit Price", "Product Id", "Quantity", "Line Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        linesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(linesTable);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel southPanel = new JPanel();
        southPanel.add(closeButton);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadLines() {
        try {
            List<OrderLine> lines = orderLineController.getLinesByOrderId(orderId);
            tableModel.setRowCount(0);

            for (OrderLine line : lines) {
                Object[] row = {
                        line.getNumber(),
                        line.getNameSnapshot(),
                        line.getPriceSnapshot(),
                        line.getProductId(),
                        line.getQuantity(),
                        line.getLineTotal()
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