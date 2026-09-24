package GUI;

import persistencia.RepositorioMarcador;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;


public class VentanaMenu extends JFrame {
    private JPanel panelPrincipal;
    private JLabel lblTitulo;
    private JLabel lblPortada;
    private JPanel panelBotones;
    private JButton btnJugar;
    private JButton btnMarcador;
    private JButton btnSalir;

    private final RepositorioMarcador repositorioMarcador;

    public VentanaMenu(RepositorioMarcador repositorioMarcador) {
        this.repositorioMarcador = repositorioMarcador;
        EstiloGUI.configurarVentana(this, panelPrincipal, "Menú");
        EstiloGUI.hacerTransparente(panelBotones);

        EstiloGUI.estilizarTitulo(lblTitulo, ConstantesGUI.TEXTO_TITULO_MENU);
        cargarPortada();

        EstiloGUI.estilizarBoton(btnJugar, ConstantesGUI.TEXTO_BTN_JUGAR);
        EstiloGUI.estilizarBoton(btnMarcador, ConstantesGUI.TEXTO_BTN_MARCADOR);
        EstiloGUI.estilizarBoton(btnSalir, ConstantesGUI.TEXTO_BTN_SALIR);

        btnJugar.addActionListener(evento -> irASeleccionDeModo());
        btnMarcador.addActionListener(evento -> mostrarMarcador());
        btnSalir.addActionListener(evento -> salir());
    }

    private void cargarPortada() {
        ImageIcon portada = ImagenesGUI.cargarEscalada(ConstantesGUI.RUTA_IMAGEN_PORTADA,
                ConstantesGUI.ANCHO_IMAGEN_PORTADA, ConstantesGUI.ALTO_IMAGEN_PORTADA);
        lblPortada.setPreferredSize(new Dimension(ConstantesGUI.ANCHO_IMAGEN_PORTADA, ConstantesGUI.ALTO_IMAGEN_PORTADA));
        lblPortada.setHorizontalAlignment(SwingConstants.CENTER);
        if (portada != null) {
            lblPortada.setIcon(portada);
            lblPortada.setText(null);
        } else {
            EstiloGUI.estilizarTexto(lblPortada, ConstantesGUI.TEXTO_IMAGEN_PENDIENTE);
            lblPortada.setBorder(javax.swing.BorderFactory.createLineBorder(ConstantesGUI.COLOR_TEXTO_SECUNDARIO));
        }
    }

    private void irASeleccionDeModo() {
        new VentanaSelecionModo(repositorioMarcador).setVisible(true);
        dispose();
    }

    private void mostrarMarcador() {
        try {
            DefaultTableModel modelo = VentanaResultado.crearModeloMarcador(repositorioMarcador);
            if (modelo.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, ConstantesGUI.TEXTO_MARCADOR_VACIO,
                        ConstantesGUI.TEXTO_TITULO_MARCADOR, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            JTable tabla = new JTable(modelo);
            JScrollPane scroll = new JScrollPane(tabla);
            scroll.setPreferredSize(new Dimension(ConstantesGUI.ANCHO_IMAGEN_MODO, ConstantesGUI.ALTO_IMAGEN_MODO / 2));
            JOptionPane.showMessageDialog(this, scroll, ConstantesGUI.TEXTO_TITULO_MARCADOR, JOptionPane.PLAIN_MESSAGE);
        } catch (IllegalStateException e) {
            EstiloGUI.mostrarError(this, ConstantesGUI.TEXTO_ERROR_MARCADOR + e.getMessage());
        }
    }

    private void salir() {
        if (EstiloGUI.confirmar(this, ConstantesGUI.TEXTO_CONFIRMAR_SALIR)) {
            dispose();
        }
    }
}
