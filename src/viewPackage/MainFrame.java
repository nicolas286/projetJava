package viewPackage;

import controllerPackage.OrderController;
import controllerPackage.TableController;

import javax.swing.*;

public class MainFrame extends JFrame {

    private final TableController tableController;
    private final OrderController orderController;

    public MainFrame() {
        super("Cafe & Restaurant Management");

        this.tableController = new TableController();
        this.orderController = new OrderController();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        setContentPane(new HomePanel(this));
        setVisible(true);
    }

    public void showHomeView() {
        setContentPane(new HomePanel(this));
        revalidate();
        repaint();
    }

    public void showTablesView() {
        setContentPane(new RoomPlanPanel(this, tableController));
        revalidate();
        repaint();
    }

    public void showOrdersView() {
        setContentPane(new OrderListPanel(this, orderController));
        revalidate();
        repaint();
    }

    public void showSearchOrdersByTableView() {
        setContentPane(new SearchOrdersByTablePanel(this));
        revalidate();
        repaint();
    }

    public void showSearchProductCategoryConstraintView() {
        setContentPane(new SearchProductCategoryConstraintPanel(this));
        revalidate();
        repaint();
    }

    public void showSearchLotStorageProductView() {
        setContentPane(new SearchLotStorageProductPanel(this));
        revalidate();
        repaint();
    }
}