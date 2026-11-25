import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Swing windows must be created in the special GUI thread (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
