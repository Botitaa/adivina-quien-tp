package GUI;

import persistencia.RepositorioMarcador;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;


public class VentanaSelecionModo extends JFrame {
    private JPanel panelPrincipal;
    private JLabel lblTitulo;
    private JLabel lblImagenVsMaquina;
    private JLabel lblImagenEspectador;
    private JButton btnVsMaquina;
    private JButton btnEspectador;
    private JButton btnVolver;

    private final RepositorioMarcador repositorioMarcador;

    public VentanaSelecionModo(RepositorioMarcador repositorioMarcador) {
        this.repositorioMarcador = repositorioMarcador;
        EstiloGUI.configurarVentana(this, panelPrincipal, "Modo de juego");

        EstiloGUI.estilizarTitulo(lblTitulo, ConstantesGUI.TEXTO_TITULO_SELECCION_MODO);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        cargarImagen(lblImagenVsMaquina, ConstantesGUI.RUTA_IMAGEN_MODO_VS_MAQUINA);
        cargarImagen(lblImagenEspectador, ConstantesGUI.RUTA_IMAGEN_MODO_ESPECTADOR);

        EstiloGUI.estilizarBoton(btnVsMaquina, ConstantesGUI.TEXTO_BTN_VS_MAQUINA);
        EstiloGUI.estilizarBoton(btnEspectador, ConstantesGUI.TEXTO_BTN_ESPECTADOR);
        EstiloGUI.estilizarBotonChico(btnVolver, ConstantesGUI.TEXTO_BTN_VOLVER);

        btnVsMaquina.addActionListener(evento -> irAConfiguracion());
        btnEspectador.addActionListener(evento -> irAModoEspectador());
        btnVolver.addActionListener(evento -> volverAlMenu());
    }


    private void cargarImagen(JLabel etiqueta, String ruta) {
        int margen = ConstantesGUI.ESPACIADO_ENTRE_OPCIONES / 2;
        Dimension tamano = ConstantesGUI.tamanoImagenModo();
        etiqueta.setPreferredSize(new Dimension(tamano.width + ConstantesGUI.ESPACIADO_ENTRE_OPCIONES, tamano.height));
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);

        ImageIcon icono = ImagenesGUI.cargarEscalada(ruta, tamano.width, tamano.height);
        if (icono != null) {
            etiqueta.setIcon(icono);
            etiqueta.setText(null);
            etiqueta.setBorder(BorderFactory.createEmptyBorder(0, margen, 0, margen));
        } else {
            EstiloGUI.estilizarTexto(etiqueta, ConstantesGUI.TEXTO_IMAGEN_PENDIENTE);
            etiqueta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(0, margen, 0, margen),
                    BorderFactory.createLineBorder(ConstantesGUI.COLOR_TEXTO_SECUNDARIO)));
        }
    }

    private void irAConfiguracion() {
        new VentanaConfigPartida(repositorioMarcador).setVisible(true);
        dispose();
    }

    private void irAModoEspectador() {
        new VentanaEspectador(repositorioMarcador).setVisible(true);
        dispose();
    }

    private void volverAlMenu() {
        new VentanaMenu(repositorioMarcador).setVisible(true);
        dispose();
    }
}
