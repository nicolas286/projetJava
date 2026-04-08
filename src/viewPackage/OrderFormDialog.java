package viewPackage;

import controllerPackage.OrderController;
import exceptionPackage.BusinessException;
import modelPackage.Order;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class OrderFormDialog extends JDialog {

    private final OrderController orderController;
    private boolean saved;

    private JTextField idField;
    private JTextField dateOrderedField;
    private JTextField dateCompletedField;
    private JTextField statusField;
    private JTextField tableIdField;
    private JTextField dateDeliveredField;

    public OrderFormDialog(JFrame parent, OrderController orderController) {
        super(parent, "Add Order", true);
        this.orderController = orderController;
        this.saved = false;

        buildInterface();
    }

    private void buildInterface() {
        setSize(450, 300);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = new JTextField();
        dateOrderedField = new JTextField("2026-03-15T12:00");
        dateCompletedField = new JTextField();
        statusField = new JTextField();
        tableIdField = new JTextField();
        dateDeliveredField = new JTextField();

        formPanel.add(new JLabel("Id:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Date Ordered (yyyy-MM-ddTHH:mm):"));
        formPanel.add(dateOrderedField);

        formPanel.add(new JLabel("Date Completed (optional):"));
        formPanel.add(dateCompletedField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);

        formPanel.add(new JLabel("Table Id:"));
        formPanel.add(tableIdField);

        formPanel.add(new JLabel("Date Delivered (optional):"));
        formPanel.add(dateDeliveredField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveOrder());
        cancelButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveOrder() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            LocalDateTime dateOrdered = LocalDateTime.parse(dateOrderedField.getText().trim());

            LocalDateTime dateCompleted = null;
            if (!dateCompletedField.getText().trim().isEmpty()) {
                dateCompleted = LocalDateTime.parse(dateCompletedField.getText().trim());
            }

            String status = statusField.getText().trim();
            int tableId = Integer.parseInt(tableIdField.getText().trim());

            LocalDateTime dateDelivered = null;
            if (!dateDeliveredField.getText().trim().isEmpty()) {
                dateDelivered = LocalDateTime.parse(dateDeliveredField.getText().trim());
            }

            Order order = new Order(id, dateOrdered, dateCompleted, dateDelivered, status, tableId);
            orderController.addOrder(order);

            saved = true;
            dispose();
        } catch (BusinessException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Business error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format.", "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isSaved() {
        return saved;
    }
}