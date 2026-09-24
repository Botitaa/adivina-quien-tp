package GUI;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Component;


public final class EstiloGUI {

    private EstiloGUI() {

    }


    public static void configurarVentana(JFrame ventana, JPanel panelPrincipal, String subtitulo) {
        ventana.setContentPane(panelPrincipal);
        ventana.setTitle(ConstantesGUI.TITULO_VENTANA + " - " + subtitulo);
        ventana.setSize(ConstantesGUI.tamanoVentana());
        ventana.setMinimumSize(ConstantesGUI.tamanoMinimoVentana());
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        panelPrincipal.setBackground(ConstantesGUI.COLOR_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(
                ConstantesGUI.MARGEN_PANEL, ConstantesGUI.MARGEN_PANEL,
                ConstantesGUI.MARGEN_PANEL, ConstantesGUI.MARGEN_PANEL));
    }

    public static void estilizarBoton(JButton boton, String texto) {
        boton.setText(texto);
        boton.setFont(ConstantesGUI.FUENTE_BOTON);
        boton.setPreferredSize(ConstantesGUI.tamanoBoton());
        boton.setBackground(ConstantesGUI.COLOR_BOTON);
        boton.setForeground(ConstantesGUI.COLOR_TEXTO_BOTON);
        boton.setFocusPainted(false);
    }

    public static void estilizarBotonChico(JButton boton, String texto) {
        estilizarBoton(boton, texto);
        boton.setPreferredSize(ConstantesGUI.tamanoBotonChico());
    }


    public static void estilizarBotonPeligro(JButton boton, String texto) {
        estilizarBotonChico(boton, texto);
        boton.setBackground(ConstantesGUI.COLOR_BOTON_PELIGRO);
        boton.setForeground(ConstantesGUI.COLOR_TEXTO);
    }

    public static void estilizarTitulo(JLabel etiqueta, String texto) {
        etiqueta.setText(texto);
        etiqueta.setFont(ConstantesGUI.FUENTE_TITULO);
        etiqueta.setForeground(ConstantesGUI.COLOR_TEXTO);
    }

    public static void estilizarSubtitulo(JLabel etiqueta, String texto) {
        etiqueta.setText(texto);
        etiqueta.setFont(ConstantesGUI.FUENTE_SUBTITULO);
        etiqueta.setForeground(ConstantesGUI.COLOR_TEXTO);
    }

    public static void estilizarTexto(JLabel etiqueta, String texto) {
        etiqueta.setText(texto);
        etiqueta.setFont(ConstantesGUI.FUENTE_TEXTO);
        etiqueta.setForeground(ConstantesGUI.COLOR_TEXTO_SECUNDARIO);
    }


    public static void hacerTransparente(JComponent... componentes) {
        for (JComponent componente : componentes) {
            componente.setOpaque(false);
        }
    }


    public static void estilizarSeccion(JPanel panel) {
        panel.setBackground(ConstantesGUI.COLOR_FONDO_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(
                ConstantesGUI.ESPACIADO_MEDIO, ConstantesGUI.ESPACIADO_MEDIO,
                ConstantesGUI.ESPACIADO_MEDIO, ConstantesGUI.ESPACIADO_MEDIO));
    }

    public static void estilizarLista(JList<?> lista, JScrollPane scroll) {
        lista.setFont(ConstantesGUI.FUENTE_CHICA);
        lista.setBackground(ConstantesGUI.COLOR_FONDO);
        lista.setForeground(ConstantesGUI.COLOR_TEXTO);
        lista.setSelectionBackground(ConstantesGUI.COLOR_BOTON);
        lista.setSelectionForeground(ConstantesGUI.COLOR_TEXTO);
        scroll.setBorder(BorderFactory.createLineBorder(ConstantesGUI.COLOR_TEXTO_SECUNDARIO));
    }

    public static void estilizarTabla(JTable tabla, JScrollPane scroll) {
        tabla.setFont(ConstantesGUI.FUENTE_TEXTO);
        tabla.setRowHeight(ConstantesGUI.ALTO_BOTON_CHICO - ConstantesGUI.ESPACIADO_MEDIO);
        tabla.setBackground(ConstantesGUI.COLOR_FONDO_PANEL);
        tabla.setForeground(ConstantesGUI.COLOR_TEXTO);
        tabla.getTableHeader().setFont(ConstantesGUI.FUENTE_BOTON);
        tabla.setFillsViewportHeight(true);
        scroll.getViewport().setBackground(ConstantesGUI.COLOR_FONDO_PANEL);
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setFont(ConstantesGUI.FUENTE_TEXTO);
        campo.setColumns(ConstantesGUI.LARGO_MAXIMO_NOMBRE);
    }

    public static void estilizarOpcion(JRadioButton opcion, String texto) {
        opcion.setText(texto);
        opcion.setFont(ConstantesGUI.FUENTE_TEXTO);
        opcion.setForeground(ConstantesGUI.COLOR_TEXTO);
        opcion.setOpaque(false);
        opcion.setFocusPainted(false);
    }

    public static void estilizarBarra(JProgressBar barra, Color color) {
        barra.setForeground(color);
        barra.setBackground(ConstantesGUI.COLOR_FONDO);
        barra.setStringPainted(true);
        barra.setFont(ConstantesGUI.FUENTE_CHICA);
    }


    public static boolean confirmar(Component padre, String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(padre, mensaje,
                ConstantesGUI.TEXTO_TITULO_CONFIRMACION, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return opcion == JOptionPane.YES_OPTION;
    }

    public static void mostrarError(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, ConstantesGUI.TEXTO_TITULO_ERROR, JOptionPane.ERROR_MESSAGE);
    }
}
