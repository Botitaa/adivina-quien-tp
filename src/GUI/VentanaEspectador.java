package GUI;

import GUI.VentanaConfigPartida.NivelMaquina;
import actores.Jugador;
import gestor.PartidaPorTurnos;
import gestor.ResultadoTurno;
import persistencia.RepositorioMarcador;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;



public class VentanaEspectador extends JFrame {
    private JPanel panelPrincipal;
    private JPanel panelEncabezado;
    private JLabel lblTitulo;
    private JLabel lblTurno;
    private JLabel lblNumeroTurno;
    private JButton btnVolverMenu;
    private JPanel panelColumnas;
    private JPanel panelMaquinaA;
    private JLabel lblNombreA;
    private JLabel lblRestantesA;
    private JProgressBar barraA;
    private JPanel contenedorTableroA;
    private JLabel lblPreguntasA;
    private JScrollPane scrollPreguntasA;
    private JList<String> listPreguntasA;
    private JPanel panelMaquinaB;
    private JLabel lblNombreB;
    private JLabel lblRestantesB;
    private JProgressBar barraB;
    private JPanel contenedorTableroB;
    private JLabel lblPreguntasB;
    private JScrollPane scrollPreguntasB;
    private JList<String> listPreguntasB;

    private final RepositorioMarcador repositorioMarcador;
    private final Jugador maquinaA;
    private final Jugador maquinaB;
    private final PartidaPorTurnos partida;
    private final PanelTablero tableroA;
    private final PanelTablero tableroB;
    private final DefaultListModel<String> modeloA = new DefaultListModel<>();
    private final DefaultListModel<String> modeloB = new DefaultListModel<>();
    private final Timer temporizador;

    public VentanaEspectador(RepositorioMarcador repositorioMarcador) {
        this.repositorioMarcador = repositorioMarcador;
        maquinaA = NivelMaquina.BASICA.crear();
        maquinaB = NivelMaquina.ASERTIVA.crear();
        partida = new PartidaPorTurnos(maquinaA, maquinaB, repositorioMarcador);
        partida.iniciar();

        EstiloGUI.configurarVentana(this, panelPrincipal, "Modo espectador");
        tableroA = new PanelTablero(partida.getCatalogo(), PanelTablero.Tamano.MEDIANA);
        tableroB = new PanelTablero(partida.getCatalogo(), PanelTablero.Tamano.MEDIANA);
        aplicarEstilos();
        btnVolverMenu.addActionListener(evento -> volverAlMenu());

        refrescar();
        temporizador = new Timer(ConstantesGUI.MS_PAUSA_ESPECTADOR, evento -> jugarTurno());
        temporizador.start();
    }

    private void aplicarEstilos() {
        EstiloGUI.hacerTransparente(panelEncabezado, panelColumnas, contenedorTableroA, contenedorTableroB);
        EstiloGUI.estilizarSubtitulo(lblTitulo, ConstantesGUI.TEXTO_TITULO_ESPECTADOR);
        EstiloGUI.estilizarSubtitulo(lblTurno, "");
        lblTurno.setForeground(ConstantesGUI.COLOR_ACENTO);
        lblTurno.setHorizontalAlignment(SwingConstants.CENTER);
        EstiloGUI.estilizarTexto(lblNumeroTurno, "");
        EstiloGUI.estilizarBotonPeligro(btnVolverMenu, ConstantesGUI.TEXTO_BTN_VOLVER_MENU);

        estilizarColumna(panelMaquinaA, lblNombreA, lblRestantesA, barraA, contenedorTableroA, tableroA,
                lblPreguntasA, listPreguntasA, scrollPreguntasA, modeloA, maquinaA);
        estilizarColumna(panelMaquinaB, lblNombreB, lblRestantesB, barraB, contenedorTableroB, tableroB,
                lblPreguntasB, listPreguntasB, scrollPreguntasB, modeloB, maquinaB);
    }

    private void estilizarColumna(JPanel panel, JLabel nombre, JLabel restantes, JProgressBar barra,
                                  JPanel contenedor, PanelTablero tablero, JLabel tituloPreguntas,
                                  JList<String> lista, JScrollPane scroll, DefaultListModel<String> modelo,
                                  Jugador maquina) {
        EstiloGUI.estilizarSeccion(panel);
        EstiloGUI.estilizarSubtitulo(nombre, maquina.getNombre());
        EstiloGUI.estilizarTexto(restantes, "");
        EstiloGUI.estilizarBarra(barra, ConstantesGUI.COLOR_EXITO);
        barra.setMaximum(partida.getTotalPersonajes());
        contenedor.setBorder(null);
        contenedor.add(tablero, BorderLayout.CENTER);

        tablero.marcarObjetivo(partida.personajeSecretoDe(partida.rivalDe(maquina)),
                ConstantesGUI.COLOR_BORDE_OBJETIVO, ConstantesGUI.TEXTO_TOOLTIP_OBJETIVO);
        EstiloGUI.estilizarTexto(tituloPreguntas, ConstantesGUI.TEXTO_TITULO_PREGUNTAS);
        lista.setModel(modelo);
        EstiloGUI.estilizarLista(lista, scroll);
    }

    private void jugarTurno() {
        ResultadoTurno resultado = partida.jugarTurno();
        DefaultListModel<String> modelo = resultado.autor() == maquinaA ? modeloA : modeloB;
        JList<String> lista = resultado.autor() == maquinaA ? listPreguntasA : listPreguntasB;
        modelo.addElement(describir(resultado));
        lista.ensureIndexIsVisible(modelo.size() - 1);
        refrescar();

        if (resultado.terminoPartida()) {
            temporizador.stop();
            lblTurno.setText(String.format(ConstantesGUI.TEXTO_GANO, resultado.autor().getNombre()));
            Timer espera = new Timer(ConstantesGUI.MS_ANTES_DE_RESULTADO, evento -> irAResultado());
            espera.setRepeats(false);
            espera.start();
        }
    }

    private void refrescar() {
        int total = partida.getTotalPersonajes();
        lblNumeroTurno.setText(String.format(ConstantesGUI.TEXTO_NUMERO_TURNO, partida.getNumeroTurno() + 1));
        lblTurno.setText(String.format(ConstantesGUI.TEXTO_TURNO_DE, partida.getTurnoActual().getNombre()));
        refrescarColumna(maquinaA, tableroA, lblRestantesA, barraA, total);
        refrescarColumna(maquinaB, tableroB, lblRestantesB, barraB, total);
    }

    private void refrescarColumna(Jugador maquina, PanelTablero tablero, JLabel restantes, JProgressBar barra, int total) {
        int cantidad = partida.getCandidatosDe(maquina).size();
        tablero.marcarCandidatos(partida.getCandidatosDe(maquina));
        restantes.setText(String.format(ConstantesGUI.TEXTO_RESTANTES, cantidad, total));
        barra.setValue(cantidad);
        barra.setString(cantidad + " / " + total);
    }

    private String describir(ResultadoTurno resultado) {
        if (resultado.esPregunta()) {
            return resultado.jugada().getPregunta() + " → "
                    + (resultado.respuesta() ? ConstantesGUI.TEXTO_RESPUESTA_SI : ConstantesGUI.TEXTO_RESPUESTA_NO);
        }
        String formato = resultado.acierto()
                ? ConstantesGUI.TEXTO_LINEA_ADIVINANZA_ACERTADA : ConstantesGUI.TEXTO_LINEA_ADIVINANZA_FALLIDA;
        return String.format(formato, resultado.autor().getNombre(), "#" + resultado.jugada().getPersonajeAdivinado().getId());
    }

    private void irAResultado() {
        Runnable revancha = () -> new VentanaEspectador(repositorioMarcador).setVisible(true);
        new VentanaResultado(repositorioMarcador, partida, maquinaA, true, revancha).setVisible(true);
        dispose();
    }

    private void volverAlMenu() {
        temporizador.stop();
        if (EstiloGUI.confirmar(this, ConstantesGUI.TEXTO_CONFIRMAR_SALIR_ESPECTADOR)) {
            new VentanaMenu(repositorioMarcador).setVisible(true);
            dispose();
        } else {
            temporizador.start();
        }
    }

    @Override
    public void dispose() {
        temporizador.stop();
        super.dispose();
    }
}
