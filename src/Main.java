//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import dataAccessPackage.DBConnection;

import java.sql.Connection;


public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            if (conn != null) {
                System.out.println("Connected to the database successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}