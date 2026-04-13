package viewPackage.Search;

import controllerPackage.RestaurantController;
import viewPackage.MainFrame;
import viewPackage.Shared.Factories.ButtonFactory;
import viewPackage.Shared.Factories.LabelFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public abstract class AbstractSearchPanel extends JPanel {

    protected final MainFrame parentFrame;
    protected final RestaurantController restaurantController;

    protected JTable resultTable;
    protected DefaultTableModel tableModel;

    protected AbstractSearchPanel(MainFrame parentFrame, RestaurantController restaurantController) {
        this.parentFrame = parentFrame;
        this.restaurantController = restaurantController;

        buildInterface();
    }

    private void buildInterface() {
        setLayout(new BorderLayout());

        JLabel titleLabel = LabelFactory.createTitleLabel(getTitleText(), 20);

        JPanel searchPanel = buildSearchPanel();
        JScrollPane scrollPane = buildResultScrollPane();

        JPanel centerPanel = createCenterPanel(searchPanel, scrollPane);

        add(titleLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

    }

    private JScrollPane buildResultScrollPane() {
        tableModel = new DefaultTableModel(getColumnNames(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        resultTable = new JTable(tableModel);
        return new JScrollPane(resultTable);
    }

    protected JButton createSearchButton() {
        return ButtonFactory.createPrimaryButton("Search", e -> search());
    }

    protected JButton createBackButton() {
        return ButtonFactory.createPrimaryButton("Back", e -> parentFrame.showHomeView());
    }

    protected JPanel createCenterPanel(JPanel searchPanel, JScrollPane scrollPane) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    protected abstract String getTitleText();

    protected abstract String[] getColumnNames();

    protected abstract JPanel buildSearchPanel();

    protected abstract void search();
}