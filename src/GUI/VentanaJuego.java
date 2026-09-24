package GUI;

import GUI.VentanaConfigPartida.NivelMaquina;
import actores.Jugador;
import actores.JugadorGUI;
import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class VentanaJuego extends JFrame {
    private JPanel panelPrincipal;
    private JPanel panelEncabezado;
    private JLabel lblTurno;
    private JLabel lblNumeroTurno;
    private JLabel lblMiSecreto;
    private JButton btnAbandonar;
    private JPanel panelTableroJugador;
    private JLabel lblTituloTablero;
    private JProgressBar barraJugador;
    private JPanel contenedorTableroJugador;
    private JLabel lblAviso;
    private JPanel panelInferior;
    private JPanel panelPreguntas;
    private JLabel lblTituloPreguntas;
    private JScrollPane scrollPreguntas;
    private JList<Pregunta<?>> listPreguntas;
    private JButton btnPreguntar;
    private JButton btnAdivinar;
    private JButton btnCancelarAdivinanza;
    private JPanel panelRespuestas;
    private JLabel lblTituloRespuestas;
    private JScrollPane scrollRespuestas;
    private JList<String> listRespuestas;
    private JPanel panelMaquina;
    private JLabel lblTituloMaquina;
    private JLabel lblRestantesMaquina;
    private JProgressBar barraMaquina;
    private JPanel contenedorTableroMaquina;

    private final RepositorioMarcador repositorioMarcador;
    private final String nombreJugador;
    private final NivelMaquina nivelMaquina;
    private final JugadorGUI humano;
    private final Jugador maquina;
    private final PartidaPorTurnos partida;
    private final PanelTablero tableroJugador;
    private final PanelTablero tableroMaquina;
    private final DefaultListModel<Pregunta<?>> modeloPreguntas = new DefaultListModel<>();
    private final DefaultListModel<String> modeloRespuestas = new DefaultListModel<>();
    private Timer temporizador;
    private boolean modoAdivinanza;

    public VentanaJuego(RepositorioMarcador repositorioMarcador, String nombreJugador,
                        NivelMaquina nivelMaquina, Personaje personajeSecreto) {
        this.repositorioMarcador = repositorioMarcador;
        this.nombreJugador = nombreJugador;
        this.nivelMaquina = nivelMaquina;

        humano = new JugadorGUI(nombreJugador);
        humano.prepararPersonajeSecreto(personajeSecreto);
        maquina = nivelMaquina.crear();
        partida = new PartidaPorTurnos(humano, maquina, repositorioMarcador);
        partida.iniciar();

        EstiloGUI.configurarVentana(this, panelPrincipal, nombreJugador + " vs " + maquina.getNombre());
        tableroJugador = new PanelTablero(partida.getCatalogo(), PanelTablero.Tamano.NORMAL);
        tableroMaquina = new PanelTablero(partida.getCatalogo(), PanelTablero.Tamano.CHICA);
        aplicarEstilos(personajeSecreto);
        conectarEventos();

        refrescar();
        if (partida.getTurnoActual() == maquina) {
            programarTurnoMaquina();
        } else {
            mostrarTurnoHumano(ConstantesGUI.TEXTO_AVISO_INICIO);
        }
    }

    private void aplicarEstilos(Personaje personajeSecreto) {
        EstiloGUI.hacerTransparente(panelEncabezado, panelTableroJugador, panelInferior,
                contenedorTableroJugador, contenedorTableroMaquina);
        EstiloGUI.estilizarSeccion(panelPreguntas);
        EstiloGUI.estilizarSeccion(panelRespuestas);
        EstiloGUI.estilizarSeccion(panelMaquina);
        contenedorTableroJugador.setBorder(null);
        contenedorTableroMaquina.setBorder(null);
        contenedorTableroJugador.add(tableroJugador, BorderLayout.CENTER);
        contenedorTableroMaquina.add(tableroMaquina, BorderLayout.CENTER);

        EstiloGUI.estilizarSubtitulo(lblTurno, "");
        EstiloGUI.estilizarTexto(lblNumeroTurno, "");
        EstiloGUI.estilizarTexto(lblMiSecreto, ConstantesGUI.TEXTO_TU_PERSONAJE);
        lblMiSecreto.setIcon(ImagenesGUI.iconoPersonaje(personajeSecreto,
                ConstantesGUI.ANCHO_IMAGEN_SECRETO, ConstantesGUI.ALTO_IMAGEN_SECRETO));
        lblMiSecreto.setToolTipText(ImagenesGUI.descripcion(personajeSecreto));
        EstiloGUI.estilizarBotonPeligro(btnAbandonar, ConstantesGUI.TEXTO_BTN_ABANDONAR);

        EstiloGUI.estilizarTexto(lblTituloTablero, "");
        EstiloGUI.estilizarBarra(barraJugador, ConstantesGUI.COLOR_EXITO);
        barraJugador.setMaximum(partida.getTotalPersonajes());
        EstiloGUI.estilizarTexto(lblAviso, "");
        lblAviso.setForeground(ConstantesGUI.COLOR_ACENTO);
        lblAviso.setHorizontalAlignment(SwingConstants.CENTER);

        EstiloGUI.estilizarSubtitulo(lblTituloPreguntas, ConstantesGUI.TEXTO_TITULO_PREGUNTAS);
        listPreguntas.setModel(modeloPreguntas);
        listPreguntas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        EstiloGUI.estilizarLista(listPreguntas, scrollPreguntas);
        EstiloGUI.estilizarBotonChico(btnPreguntar, ConstantesGUI.TEXTO_BTN_PREGUNTAR);
        EstiloGUI.estilizarBotonChico(btnAdivinar, ConstantesGUI.TEXTO_BTN_ADIVINAR);
        EstiloGUI.estilizarBotonChico(btnCancelarAdivinanza, ConstantesGUI.TEXTO_BTN_CANCELAR);
        btnCancelarAdivinanza.setVisible(false);

        EstiloGUI.estilizarSubtitulo(lblTituloRespuestas, ConstantesGUI.TEXTO_TITULO_RESPUESTAS);
        listRespuestas.setModel(modeloRespuestas);
        EstiloGUI.estilizarLista(listRespuestas, scrollRespuestas);

        EstiloGUI.estilizarSubtitulo(lblTituloMaquina, maquina.getNombre());
        EstiloGUI.estilizarTexto(lblRestantesMaquina, "");
        EstiloGUI.estilizarBarra(barraMaquina, ConstantesGUI.COLOR_ERROR);
        barraMaquina.setMaximum(partida.getTotalPersonajes());
        // En el tablero de la máquina se ve tu personaje: el que ella busca
        tableroMaquina.marcarObjetivo(personajeSecreto, ConstantesGUI.COLOR_BORDE_OBJETIVO,
                ConstantesGUI.TEXTO_TOOLTIP_OBJETIVO);
    }

    private void conectarEventos() {
        listPreguntas.addListSelectionListener(evento -> actualizarBotones());
        listPreguntas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evento) {
                if (evento.getClickCount() == 2 && btnPreguntar.isEnabled()) {
                    preguntar(); // doble clic = preguntar
                }
            }
        });
        btnPreguntar.addActionListener(evento -> preguntar());
        btnAdivinar.addActionListener(evento -> entrarModoAdivinanza());
        btnCancelarAdivinanza.addActionListener(evento -> salirModoAdivinanza(ConstantesGUI.TEXTO_AVISO_INICIO));
        btnAbandonar.addActionListener(evento -> abandonar());
    }

    // ------------------------------------------------------------------
    // Turno del humano
    // ------------------------------------------------------------------

    private void preguntar() {
        Pregunta<?> pregunta = listPreguntas.getSelectedValue();
        if (pregunta == null) {
            lblAviso.setText(ConstantesGUI.TEXTO_AVISO_ELEGIR_PREGUNTA);
            return;
        }
        jugarTurnoHumano(Jugada.dePregunta(pregunta));
    }

    private void entrarModoAdivinanza() {
        modoAdivinanza = true;
        tableroJugador.activarSeleccion(this::confirmarAdivinanza);
        btnCancelarAdivinanza.setVisible(true);
        lblAviso.setText(ConstantesGUI.TEXTO_AVISO_ADIVINAR);
        actualizarBotones();
    }

    private void salirModoAdivinanza(String aviso) {
        modoAdivinanza = false;
        tableroJugador.desactivarSeleccion();
        btnCancelarAdivinanza.setVisible(false);
        lblAviso.setText(aviso);
        actualizarBotones();
    }

    private void confirmarAdivinanza(Personaje personaje) {
        String mensaje = String.format(ConstantesGUI.TEXTO_CONFIRMAR_ADIVINANZA,
                maquina.getNombre(), ImagenesGUI.descripcion(personaje));
        if (EstiloGUI.confirmar(this, mensaje)) {
            salirModoAdivinanza("");
            jugarTurnoHumano(Jugada.deAdivinanza(personaje));
        }
    }

    private void jugarTurnoHumano(Jugada jugada) {
        humano.prepararJugada(jugada);
        procesar(partida.jugarTurno());
    }

    // ------------------------------------------------------------------
    // Turno de la máquina
    // ------------------------------------------------------------------

    private void programarTurnoMaquina() {
        lblTurno.setText(String.format(ConstantesGUI.TEXTO_PENSANDO, maquina.getNombre()));
        lblTurno.setForeground(ConstantesGUI.COLOR_TEXTO_SECUNDARIO);
        actualizarBotones();
        temporizador = new Timer(ConstantesGUI.MS_TURNO_MAQUINA, evento -> procesar(partida.jugarTurno()));
        temporizador.setRepeats(false);
        temporizador.start();
    }

    // ------------------------------------------------------------------
    // Después de cada turno
    // ------------------------------------------------------------------

    private void procesar(ResultadoTurno resultado) {
        agregarRespuesta(resultado);
        refrescar();
        if (resultado.terminoPartida()) {
            terminarPartida(resultado);
        } else if (partida.getTurnoActual() == maquina) {
            programarTurnoMaquina();
        } else {
            mostrarTurnoHumano(describir(resultado));
        }
    }

    private void mostrarTurnoHumano(String aviso) {
        lblTurno.setText(ConstantesGUI.TEXTO_TU_TURNO);
        lblTurno.setForeground(ConstantesGUI.COLOR_ACENTO);
        boolean sinPreguntas = modeloPreguntas.isEmpty();
        lblAviso.setText(sinPreguntas ? ConstantesGUI.TEXTO_AVISO_SIN_PREGUNTAS : aviso);
        actualizarBotones();
    }

    private void refrescar() {
        int total = partida.getTotalPersonajes();
        int misCandidatos = partida.getCandidatosDe(humano).size();
        int candidatosMaquina = partida.getCandidatosDe(maquina).size();

        lblNumeroTurno.setText(String.format(ConstantesGUI.TEXTO_NUMERO_TURNO, Math.max(1, partida.getNumeroTurno())));
        tableroJugador.marcarCandidatos(partida.getCandidatosDe(humano));
        tableroMaquina.marcarCandidatos(partida.getCandidatosDe(maquina));
        lblTituloTablero.setText(String.format(ConstantesGUI.TEXTO_TITULO_TABLERO_JUGADOR,
                maquina.getNombre(), misCandidatos, total));
        barraJugador.setValue(misCandidatos);
        barraJugador.setString(misCandidatos + " / " + total);
        lblRestantesMaquina.setText(String.format(ConstantesGUI.TEXTO_RESTANTES_MAQUINA, candidatosMaquina, total));
        barraMaquina.setValue(candidatosMaquina);
        barraMaquina.setString(candidatosMaquina + " / " + total);

        modeloPreguntas.clear();
        for (Pregunta<?> pregunta : partida.preguntasDisponiblesPara(humano)) {
            modeloPreguntas.addElement(pregunta);
        }
    }


    private void actualizarBotones() {
        boolean miTurno = !partida.estaTerminada() && partida.getTurnoActual() == humano;
        btnPreguntar.setEnabled(miTurno && !modoAdivinanza && listPreguntas.getSelectedValue() != null);
        btnAdivinar.setEnabled(miTurno && !modoAdivinanza);
        listPreguntas.setEnabled(miTurno && !modoAdivinanza);
    }

    private void agregarRespuesta(ResultadoTurno resultado) {
        modeloRespuestas.addElement(describir(resultado));
        listRespuestas.ensureIndexIsVisible(modeloRespuestas.size() - 1);
    }

    private String nombreDe(Jugador jugador) {
        return jugador == humano ? ConstantesGUI.TEXTO_VOS : jugador.getNombre();
    }

    private String describir(ResultadoTurno resultado) {
        String autor = nombreDe(resultado.autor());
        if (resultado.esPregunta()) {
            return String.format(ConstantesGUI.TEXTO_LINEA_PREGUNTA, autor, resultado.jugada().getPregunta(),
                    resultado.respuesta() ? ConstantesGUI.TEXTO_RESPUESTA_SI : ConstantesGUI.TEXTO_RESPUESTA_NO);
        }
        String formato = resultado.acierto()
                ? ConstantesGUI.TEXTO_LINEA_ADIVINANZA_ACERTADA : ConstantesGUI.TEXTO_LINEA_ADIVINANZA_FALLIDA;
        return String.format(formato, autor, "#" + resultado.jugada().getPersonajeAdivinado().getId());
    }


    private void terminarPartida(ResultadoTurno ultimo) {
        lblTurno.setText(describir(ultimo));
        lblAviso.setText("");
        actualizarBotones();
        temporizador = new Timer(ConstantesGUI.MS_ANTES_DE_RESULTADO, evento -> irAResultado());
        temporizador.setRepeats(false);
        temporizador.start();
    }

    private void irAResultado() {
        Runnable revancha = () -> new VentanaElegirSecreto(repositorioMarcador, nombreJugador, nivelMaquina).setVisible(true);
        new VentanaResultado(repositorioMarcador, partida, humano, false, revancha).setVisible(true);
        dispose();
    }

    private void abandonar() {
        if (EstiloGUI.confirmar(this, ConstantesGUI.TEXTO_CONFIRMAR_ABANDONAR)) {
            new VentanaMenu(repositorioMarcador).setVisible(true);
            dispose();
        }
    }

    @Override
    public void dispose() {
        if (temporizador != null) {
            temporizador.stop();
        }
        super.dispose();
    }
}
