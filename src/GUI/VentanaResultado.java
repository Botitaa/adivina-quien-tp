package GUI;

import actores.Jugador;
import dominio.Personaje;
import gestor.PartidaPorTurnos;
import persistencia.RepositorioMarcador;
import persistencia.RegistroMarcador;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.Color;


public class VentanaResultado extends JFrame {
    private JPanel panelPrincipal;
    private JLabel lblResultado;
    private JLabel lblDetalle;
    private JPanel panelSecretos;
    private JLabel lblSecretoIzquierda;
    private JLabel lblSecretoDerecha;
    private JLabel lblTituloMarcador;
    private JScrollPane scrollMarcador;
    private JTable tablaMarcador;
    private JPanel panelBotones;
    private JButton btnMenu;
    private JButton btnRevancha;
    private JButton btnSalir;

    private final RepositorioMarcador repositorioMarcador;
    private final Runnable revancha;


    public VentanaResultado(RepositorioMarcador repositorioMarcador, PartidaPorTurnos partida,
                            Jugador izquierda, boolean modoEspectador, Runnable revancha) {
        this.repositorioMarcador = repositorioMarcador;
        this.revancha = revancha;
        EstiloGUI.configurarVentana(this, panelPrincipal, "Resultado");
        EstiloGUI.hacerTransparente(panelSecretos, panelBotones);

        Jugador derecha = partida.rivalDe(izquierda);
        Jugador ganador = partida.getGanador()
                .orElseThrow(() -> new IllegalStateException("La partida todavía no terminó"));
        mostrarTitulo(ganador, izquierda, modoEspectador);
        EstiloGUI.estilizarTexto(lblDetalle, String.format(ConstantesGUI.TEXTO_DETALLE_TURNOS, partida.getNumeroTurno()));

        String etiquetaIzquierda = modoEspectador
                ? String.format(ConstantesGUI.TEXTO_PERSONAJE_DE, izquierda.getNombre())
                : ConstantesGUI.TEXTO_TU_PERSONAJE;
        mostrarSecreto(lblSecretoIzquierda, partida.personajeSecretoDe(izquierda), etiquetaIzquierda);
        mostrarSecreto(lblSecretoDerecha, partida.personajeSecretoDe(derecha),
                String.format(ConstantesGUI.TEXTO_PERSONAJE_DE, derecha.getNombre()));

        EstiloGUI.estilizarSubtitulo(lblTituloMarcador, ConstantesGUI.TEXTO_TITULO_MARCADOR);
        cargarMarcador();

        EstiloGUI.estilizarBoton(btnMenu, ConstantesGUI.TEXTO_BTN_VOLVER_MENU);
        EstiloGUI.estilizarBoton(btnRevancha, ConstantesGUI.TEXTO_BTN_REVANCHA);
        EstiloGUI.estilizarBoton(btnSalir, ConstantesGUI.TEXTO_BTN_SALIR);
        btnMenu.addActionListener(evento -> volverAlMenu());
        btnRevancha.addActionListener(evento -> jugarRevancha());
        btnSalir.addActionListener(evento -> salir());
    }

    private void mostrarTitulo(Jugador ganador, Jugador izquierda, boolean modoEspectador) {
        String texto;
        Color color;
        if (modoEspectador) {
            texto = String.format(ConstantesGUI.TEXTO_GANO, ganador.getNombre());
            color = ConstantesGUI.COLOR_ACENTO;
        } else if (ganador == izquierda) {
            texto = String.format(ConstantesGUI.TEXTO_GANASTE, izquierda.getNombre());
            color = ConstantesGUI.COLOR_EXITO;
        } else {
            texto = String.format(ConstantesGUI.TEXTO_PERDISTE, ganador.getNombre());
            color = ConstantesGUI.COLOR_ERROR;
        }
        EstiloGUI.estilizarTitulo(lblResultado, texto);
        lblResultado.setForeground(color);
    }

    private void mostrarSecreto(JLabel etiqueta, Personaje personaje, String texto) {
        EstiloGUI.estilizarTexto(etiqueta, texto + " (#" + personaje.getId() + ")");
        etiqueta.setIcon(ImagenesGUI.iconoPersonaje(personaje,
                ConstantesGUI.ANCHO_IMAGEN_RESULTADO, ConstantesGUI.ALTO_IMAGEN_RESULTADO));
        etiqueta.setToolTipText(ImagenesGUI.descripcion(personaje));
        etiqueta.setVerticalTextPosition(SwingConstants.BOTTOM);
        etiqueta.setHorizontalTextPosition(SwingConstants.CENTER);
        etiqueta.setBorder(javax.swing.BorderFactory.createEmptyBorder(
                0, ConstantesGUI.ESPACIADO_GRANDE, 0, ConstantesGUI.ESPACIADO_GRANDE));
    }

    private void cargarMarcador() {
        try {
            tablaMarcador.setModel(crearModeloMarcador(repositorioMarcador));
        } catch (IllegalStateException e) {
            EstiloGUI.mostrarError(this, ConstantesGUI.TEXTO_ERROR_MARCADOR + e.getMessage());
        }
        EstiloGUI.estilizarTabla(tablaMarcador, scrollMarcador);
    }

    private void volverAlMenu() {
        new VentanaMenu(repositorioMarcador).setVisible(true);
        dispose();
    }

    private void jugarRevancha() {
        revancha.run();
        dispose();
    }

    private void salir() {
        if (EstiloGUI.confirmar(this, ConstantesGUI.TEXTO_CONFIRMAR_SALIR)) {
            dispose();
        }
    }


    static DefaultTableModel crearModeloMarcador(RepositorioMarcador repositorio) {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{ConstantesGUI.TEXTO_COLUMNA_JUGADOR, ConstantesGUI.TEXTO_COLUMNA_VICTORIAS}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false; // solo lectura
            }
        };
        List<RegistroMarcador> registros = new ArrayList<>(repositorio.obtenerTodos());
        registros.sort(Comparator.comparingInt(RegistroMarcador::partidasGanadas).reversed());
        for (RegistroMarcador registro : registros) {
            modelo.addRow(new Object[]{registro.nombreJugador(), registro.partidasGanadas()});
        }
        return modelo;
    }
}
