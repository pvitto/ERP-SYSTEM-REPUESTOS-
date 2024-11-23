import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Cambiar el Look and Feel a Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("No se pudo aplicar el tema Nimbus. Usando el tema por defecto.");
        }

        // Ejecutar la aplicación
        SwingUtilities.invokeLater(() -> {
            ControladorUsuario controladorUsuario = new ControladorUsuario();
            ControladorRepuesto controladorRepuesto = new ControladorRepuesto();
            new LoginView(controladorUsuario, controladorRepuesto).setVisible(true);
        });
    }
}
