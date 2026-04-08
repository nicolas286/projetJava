package viewPackage;

import controllerPackage.TakeOrderController;
import exceptionPackage.BusinessException;
import modelPackage.Product;
import modelPackage.TakeOrderLine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TakeOrderDialog extends JDialog {

    private final TakeOrderController takeOrderController;
    private boolean saved;

    private JTextField orderIdField;
    private JTextField tableIdField;
    private JComboBox<Product> productComboBox;
    private JSpinner quantitySpinner;

    private JTable linesTable;
    private DefaultTableModel linesTableModel;

    private final List<TakeOrderLine> lines;

    public TakeOrderDialog(JFrame parent) {
        super(parent, "Take Order", true);
        this.takeOrderController = new TakeOrderController();
        this.saved = false;
        this.lines = new ArrayList<>();

        buildInterface();
        loadProducts();
    }

    private void buildInterface() {
        setSize(750, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        orderIdField = new JTextField();
        tableIdField = new JTextField();
        productComboBox = new JComboBox<>();
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));

        formPanel.add(new JLabel("Order Id:"));
        formPanel.add(orderIdField);
        formPanel.add(new JLabel("Table Id:"));
        formPanel.add(tableIdField);

        formPanel.add(new JLabel("Product:"));
        formPanel.add(productComboBox);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantitySpinner);

        String[] columnNames = {"Product", "Unit price", "Quantity", "Line total"};
        linesTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        linesTable = new JTable(linesTableModel);
        JScrollPane scrollPane = new JScrollPane(linesTable);

        JButton addLineButton = new JButton("Add line");
        JButton removeLineButton = new JButton("Remove selected line");
        JButton confirmButton = new JButton("Confirm order");
        JButton cancelButton = new JButton("Cancel");

        addLineButton.addActionListener(e -> addLine());
        removeLineButton.addActionListener(e -> removeSelectedLine());
        confirmButton.addActionListener(e -> confirmOrder());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addLineButton);
        buttonPanel.add(removeLineButton);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadProducts() {
        try {
            List<Product> products = takeOrderController.getAvailableProducts();
            DefaultComboBoxModel<Product> comboBoxModel = new DefaultComboBoxModel<>();

            for (Product product : products) {
                comboBoxModel.addElement(product);
            }

            productComboBox.setModel(comboBoxModel);

        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addLine() {
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();

        if (selectedProduct == null) {
            JOptionPane.showMessageDialog(this, "Please select a product.", "Validation error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TakeOrderLine line = new TakeOrderLine(selectedProduct, quantity);
        lines.add(line);

        Object[] row = {
                selectedProduct.getName(),
                selectedProduct.getPrice(),
                quantity,
                line.getLineTotal()
        };
        linesTableModel.addRow(row);
    }

    private void removeSelectedLine() {
        int selectedRow = linesTable.getSelectedRow();
        if (selectedRow >= 0) {
            lines.remove(selectedRow);
            linesTableModel.removeRow(selectedRow);
        }
    }

    private void confirmOrder() {
        try {
            int orderId = Integer.parseInt(orderIdField.getText().trim());
            int tableId = Integer.parseInt(tableIdField.getText().trim());

            takeOrderController.takeOrder(orderId, tableId, lines);

            saved = true;
            JOptionPane.showMessageDialog(
                    this,
                    "Order created successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();

        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Business error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Order id and table id must be valid integers.",
                    "Validation error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isSaved() {
        return saved;
    }
}