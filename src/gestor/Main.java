package gestor;

import GUI.VentanaMenu;
import persistencia.RepositorioMarcador;
import persistencia.RepositorioMarcadorArchivo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Punto de entrada del juego (interfaz gráfica con Swing).
 */
public class Main {

    private static final String RUTA_MARCADOR = "marcador.txt";

    public static void main(String[] args) {
        usarLookAndFeelMultiplataforma();
        RepositorioMarcador repositorioMarcador = new RepositorioMarcadorArchivo(RUTA_MARCADOR);
        // Swing tiene que crearse en su propio hilo (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> new VentanaMenu(repositorioMarcador).setVisible(true));
    }

    /**
     * El look and feel de macOS y Windows ignora los colores de fondo de los
     * botones. "Metal" (el multiplataforma de Java) sí los respeta y se ve
     * igual en todas las compus.
     */
    private static void usarLookAndFeelMultiplataforma() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            System.err.println("No se pudo aplicar el look and feel, se usa el del sistema: " + e.getMessage());
        }
    }
}
