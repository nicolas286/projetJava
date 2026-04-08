package viewPackage;

import controllerPackage.OrderController;
import controllerPackage.TableController;
import exceptionPackage.BusinessException;
import modelPackage.Order;
import modelPackage.RestaurantTable;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class OrderFormDialog extends JDialog {

    private final OrderController orderController;
    private final TableController tableController;
    private boolean saved;

    private JTextField idField;
    private JTextField dateOrderedField;
    private JTextField dateCompletedField;
    private JComboBox<String> statusComboBox;
    private JComboBox<RestaurantTable> tableComboBox;
    private JTextField dateDeliveredField;

    public OrderFormDialog(JFrame parent, OrderController orderController) {
        super(parent, "Add Order", true);
        this.orderController = orderController;
        this.tableController = new TableController();
        this.saved = false;

        buildInterface();
        loadTables();
    }

    private void buildInterface() {
        setSize(500, 320);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = new JTextField();
        dateOrderedField = new JTextField("2026-03-15T12:00");
        dateCompletedField = new JTextField();
        statusComboBox = new JComboBox<>(new String[]{
                "ORDERED", "IN_PREPARATION", "READY", "DELIVERED", "CANCELLED"
        });
        tableComboBox = new JComboBox<>();
        dateDeliveredField = new JTextField();

        formPanel.add(new JLabel("Id:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Date Ordered (yyyy-MM-ddTHH:mm):"));
        formPanel.add(dateOrderedField);

        formPanel.add(new JLabel("Date Completed (optional):"));
        formPanel.add(dateCompletedField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusComboBox);

        formPanel.add(new JLabel("Table:"));
        formPanel.add(tableComboBox);

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

    private void loadTables() {
        try {
            List<RestaurantTable> tables = tableController.getAllTables();
            DefaultComboBoxModel<RestaurantTable> comboBoxModel = new DefaultComboBoxModel<>();

            for (RestaurantTable table : tables) {
                comboBoxModel.addElement(table);
            }

            tableComboBox.setModel(comboBoxModel);

        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void saveOrder() {
        try {
            int id = 0;
            LocalDateTime dateOrdered = LocalDateTime.parse(dateOrderedField.getText().trim());

            LocalDateTime dateCompleted = null;
            if (!dateCompletedField.getText().trim().isEmpty()) {
                dateCompleted = LocalDateTime.parse(dateCompletedField.getText().trim());
            }

            String status = (String) statusComboBox.getSelectedItem();

            RestaurantTable selectedTable = (RestaurantTable) tableComboBox.getSelectedItem();
            if (selectedTable == null) {
                JOptionPane.showMessageDialog(this, "Please select a table.", "Validation error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime dateDelivered = null;
            if (!dateDeliveredField.getText().trim().isEmpty()) {
                dateDelivered = LocalDateTime.parse(dateDeliveredField.getText().trim());
            }

            Order order = new Order(id, dateOrdered, dateCompleted, dateDelivered, status, selectedTable.getId());
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