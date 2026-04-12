package viewPackage;

import controllerPackage.OrderController;
import controllerPackage.RestaurantController;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import dataAccessPackage.core.DBConnection;
import viewPackage.Home.HomePanel;
import viewPackage.Orders.OrderListPanel;
import viewPackage.RoomPlan.RoomPlanPanel;
import viewPackage.Search.SearchLotStorageProductPanel;
import viewPackage.Search.SearchOrdersByTablePanel;
import viewPackage.Search.SearchProductCategoryConstraintPanel;

import java.sql.SQLException;

import javax.swing.*;

public class MainFrame extends JFrame {

    private final OrderController orderController;
    private final RestaurantController restaurantController;

    public MainFrame() {
        super("Cafe & Restaurant Management");

        this.orderController = new OrderController();
        this.restaurantController = new RestaurantController();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
  
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DBConnection.closeConnection();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Erreur lors de la fermeture de la connexion à la base de données.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        setContentPane(new HomePanel(this));
        setVisible(true);
    }

    public void showHomeView() {
        setContentPane(new HomePanel(this));
        revalidate();
        repaint();
    }

    public void showTablesView() {
        setContentPane(new RoomPlanPanel(this, orderController, restaurantController));
        revalidate();
        repaint();
    }

    public void showOrdersView() {
        setContentPane(new OrderListPanel(this, restaurantController));
        revalidate();
        repaint();
    }

    public void showSearchOrdersByTableView() {
        setContentPane(new SearchOrdersByTablePanel(this, restaurantController));
        revalidate();
        repaint();
    }

    public void showSearchProductCategoryConstraintView() {
        setContentPane(new SearchProductCategoryConstraintPanel(this, restaurantController));
        revalidate();
        repaint();
    }

    public void showSearchLotStorageProductView() {
        setContentPane(new SearchLotStorageProductPanel(this, restaurantController));
        revalidate();
        repaint();
    }
}
