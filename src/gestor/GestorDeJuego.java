package gestor;

import actores.Historial;
import actores.Jugador;
import actores.JugadorHumano;
import dominio.CatalogoPersonajes;
import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;
import dominio.ProximoTurno;
import persistencia.RepositorioMarcador;
import presentacion.Consola;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GestorDeJuego {

    private static final int MS_PAUSA_ESPECTADOR = 1500;
    private static final int JUGADAS_RECIENTES = 3;

    private final Jugador jugadorA;
    private final Jugador jugadorB;
    private final RepositorioMarcador repositorioMarcador;
    private final Historial historial;
    private final Random random;
    private final Scanner scanner;
    private final boolean modoEspectador;

    /** Sin pausas ni entrada de teclado: útil para simulaciones masivas. */
    public GestorDeJuego(Jugador jugadorA, Jugador jugadorB, RepositorioMarcador repositorioMarcador) {
        this(jugadorA, jugadorB, repositorioMarcador, null, false);
    }

    /**
     * @param scanner        se usa para esperar ENTER entre turnos cuando hay un humano jugando;
     *                       puede ser null si no hay pausa interactiva
     * @param modoEspectador true en Máquina vs Máquina: pausa temporizada en vez de ENTER
     */
    public GestorDeJuego(Jugador jugadorA, Jugador jugadorB, RepositorioMarcador repositorioMarcador,
                         Scanner scanner, boolean modoEspectador) {
        this.jugadorA = jugadorA;
        this.jugadorB = jugadorB;
        this.repositorioMarcador = repositorioMarcador;
        this.historial = new Historial();
        this.random = new Random();
        this.scanner = scanner;
        this.modoEspectador = modoEspectador;
    }

    public void iniciarPartida() {
        elegirPersonajesSecretos();

        List<Personaje> candidatosDeA = CatalogoPersonajes.generar();
        List<Personaje> candidatosDeB = CatalogoPersonajes.generar();
        int totalCatalogo = candidatosDeA.size();

        Consola.cargando("Sorteando quién empieza", 3);
        Jugador turnoActual = sortearJugadorInicial();
        Jugador rival = (turnoActual == jugadorA) ? jugadorB : jugadorA;
        Consola.info("Empieza " + turnoActual.getNombre());
        Consola.cuentaRegresiva();

        boolean hayGanador = false;
        int numeroTurno = 0;

        while (!hayGanador) {
            numeroTurno++;
            List<Personaje> candidatosDelRival = (rival == jugadorA) ? candidatosDeA : candidatosDeB;

            ProximoTurno resultado = jugarTurno(turnoActual, rival, candidatosDelRival, numeroTurno, totalCatalogo);

            if (rival == jugadorA) {
                candidatosDeA = resultado.candidatos();
            } else {
                candidatosDeB = resultado.candidatos();
            }

            if (resultado.hayGanador()) {
                gano(turnoActual, numeroTurno);
                hayGanador = true;
            } else {
                Jugador temp = turnoActual;
                turnoActual = rival;
                rival = temp;
            }
        }
    }

    private void elegirPersonajesSecretos() {
        List<Personaje> personajesDisponibles = CatalogoPersonajes.generar();
        jugadorA.elegirPersonajeSecreto(personajesDisponibles);
        jugadorB.elegirPersonajeSecreto(personajesDisponibles);
    }

    private Jugador sortearJugadorInicial() {
        return random.nextBoolean() ? jugadorA : jugadorB;
    }

    /**
     * Logging centralizado: todo lo que se muestra del proceso de una
     * partida sale de acá (vía Consola), no de las clases de Jugador.
     * Así el log es consistente sin importar si juega un humano o una IA.
     */
    private ProximoTurno jugarTurno(Jugador turnoActual, Jugador rival, List<Personaje> candidatosDelRival,
                                    int numeroTurno, int totalCatalogo) {
        Consola.encabezadoTurno(turnoActual.getNombre(), numeroTurno);
        Consola.historialReciente(ultimasJugadas());

        // Detalle puramente de presentación: la animación de "pensando" solo tiene
        // sentido cuando no hay un humano escribiendo. No afecta la lógica del juego.
        if (!(turnoActual instanceof JugadorHumano)) {
            Consola.pensando(turnoActual.getNombre());
        }

        Jugada jugada = turnoActual.decidirJugada(candidatosDelRival, historial);

        switch (jugada.getTipoJugada()) {
            case PREGUNTA -> {
                Pregunta<?> pregunta = jugada.getPregunta();
                Consola.pregunta(turnoActual.getNombre(), rival.getNombre(), pregunta);

                boolean respuesta = rival.responder(pregunta);
                Consola.respuesta(rival.getNombre(), respuesta);

                historial.registrar(turnoActual, pregunta, respuesta);

                List<Personaje> filtrados = pregunta.filtrar(candidatosDelRival, respuesta);
                Consola.candidatosRestantes(turnoActual.getNombre(), filtrados.size(), totalCatalogo);
                Consola.tablero(filtrados);

                pausar();
                return new ProximoTurno(false, filtrados);
            }
            case ADIVINANZA -> {
                Personaje personajeAdivinado = jugada.getPersonajeAdivinado();
                Consola.adivinanza(turnoActual.getNombre(), rival.getNombre(), personajeAdivinado);

                if (rival.esMiPersonajeSecreto(personajeAdivinado)) {
                    Consola.aciertoAdivinanza(personajeAdivinado);
                    return new ProximoTurno(true, candidatosDelRival);
                } else {
                    Consola.falloAdivinanza(rival.getNombre(), personajeAdivinado);
                    // Decisión de diseño (2/9): una adivinanza fallida es una certeza tan válida
                    // como una respuesta, así que el personaje se descarta de la lista del rival.
                    candidatosDelRival.remove(personajeAdivinado);
                    Consola.candidatosRestantes(turnoActual.getNombre(), candidatosDelRival.size(), totalCatalogo);
                    Consola.tablero(candidatosDelRival);
                    pausar();
                    return new ProximoTurno(false, candidatosDelRival);
                }
            }
            default -> throw new IllegalStateException("TipoJugada no soportado: " + jugada.getTipoJugada());
        }
    }

    private List<String> ultimasJugadas() {
        List<Historial.Entrada> jugadas = historial.getJugadas();
        int desde = Math.max(0, jugadas.size() - JUGADAS_RECIENTES);
        List<String> lineas = new ArrayList<>();
        for (Historial.Entrada entrada : jugadas.subList(desde, jugadas.size())) {
            lineas.add(entrada.toString());
        }
        return lineas;
    }

    /** Espectador: pausa temporizada. Humano: espera ENTER. Sin scanner: sin pausa. */
    private void pausar() {
        if (modoEspectador) {
            try {
                Thread.sleep(MS_PAUSA_ESPECTADOR);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } else if (scanner != null) {
            Consola.esperarEnter(scanner);
        }
    }

    private void gano(Jugador ganador, int turnos) {
        Consola.victoria(ganador.getNombre(), turnos);
        repositorioMarcador.registrarVictoria(ganador.getNombre());
    }
}