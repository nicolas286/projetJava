package viewPackage.Shared.Factories;

import javax.swing.*;
import java.awt.*;

public final class DialogUtils {

    private DialogUtils() {
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showValidationError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Validation error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void showTechnicalError(Component parent) {
        JOptionPane.showMessageDialog(
                parent,
                "Une erreur technique est survenue. Veuillez réessayer.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Information",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showWarning(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Warning",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static boolean confirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(
                parent,
                message,
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }
}