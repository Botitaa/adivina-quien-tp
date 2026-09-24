package GUI;

import GUI.VentanaConfigPartida.NivelMaquina;
import dominio.CatalogoPersonajes;
import dominio.Personaje;
import persistencia.RepositorioMarcador;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Random;


public class VentanaElegirSecreto extends JFrame {
    private JPanel panelPrincipal;
    private JLabel lblTitulo;
    private JLabel lblInstruccion;
    private JPanel contenedorTablero;
    private JLabel lblSeleccion;
    private JPanel panelBotones;
    private JButton btnVolver;
    private JButton btnAlAzar;
    private JButton btnConfirmar;

    private final RepositorioMarcador repositorioMarcador;
    private final String nombreJugador;
    private final NivelMaquina nivelMaquina;
    private final List<Personaje> personajes = CatalogoPersonajes.generar();
    private final PanelTablero tablero;
    private Personaje seleccionado;

    public VentanaElegirSecreto(RepositorioMarcador repositorioMarcador, String nombreJugador, NivelMaquina nivelMaquina) {
        this.repositorioMarcador = repositorioMarcador;
        this.nombreJugador = nombreJugador;
        this.nivelMaquina = nivelMaquina;
        EstiloGUI.configurarVentana(this, panelPrincipal, "Elegí tu personaje");
        EstiloGUI.hacerTransparente(contenedorTablero, panelBotones);
        contenedorTablero.setBorder(null);

        EstiloGUI.estilizarTitulo(lblTitulo, ConstantesGUI.TEXTO_TITULO_ELEGIR_SECRETO);
        EstiloGUI.estilizarTexto(lblInstruccion, ConstantesGUI.TEXTO_INSTRUCCION_SECRETO);
        EstiloGUI.estilizarSubtitulo(lblSeleccion, ConstantesGUI.TEXTO_SIN_SELECCION);
        lblSeleccion.setHorizontalTextPosition(SwingConstants.RIGHT);

        tablero = new PanelTablero(personajes, PanelTablero.Tamano.GRANDE);
        tablero.activarSeleccion(this::seleccionar);
        contenedorTablero.add(tablero, BorderLayout.CENTER);

        EstiloGUI.estilizarBoton(btnVolver, ConstantesGUI.TEXTO_BTN_VOLVER);
        EstiloGUI.estilizarBoton(btnAlAzar, ConstantesGUI.TEXTO_BTN_AL_AZAR);
        EstiloGUI.estilizarBoton(btnConfirmar, ConstantesGUI.TEXTO_BTN_CONFIRMAR);
        btnConfirmar.setEnabled(false); // hasta que elija una carta

        btnAlAzar.addActionListener(evento -> seleccionar(personajes.get(new Random().nextInt(personajes.size()))));
        btnConfirmar.addActionListener(evento -> confirmar());
        btnVolver.addActionListener(evento -> volver());
    }

    private void seleccionar(Personaje personaje) {
        seleccionado = personaje;
        tablero.resaltar(personaje, ConstantesGUI.COLOR_BORDE_SELECCION);
        lblSeleccion.setIcon(ImagenesGUI.iconoPersonaje(personaje,
                ConstantesGUI.ANCHO_IMAGEN_SECRETO, ConstantesGUI.ALTO_IMAGEN_SECRETO));
        lblSeleccion.setText(ConstantesGUI.TEXTO_ELEGIDO + ImagenesGUI.descripcion(personaje));
        btnConfirmar.setEnabled(true);
    }

    private void confirmar() {
        String mensaje = String.format(ConstantesGUI.TEXTO_CONFIRMAR_SECRETO, ImagenesGUI.descripcion(seleccionado));
        if (EstiloGUI.confirmar(this, mensaje)) {
            new VentanaJuego(repositorioMarcador, nombreJugador, nivelMaquina, seleccionado).setVisible(true);
            dispose();
        }
    }

    private void volver() {
        new VentanaConfigPartida(repositorioMarcador).setVisible(true);
        dispose();
    }
}
