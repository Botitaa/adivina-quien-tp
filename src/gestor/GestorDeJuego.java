package gestor;

import actores.Historial;
import actores.Jugador;
import dominio.CatalogoPersonajes;
import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;
import dominio.ProximoTurno;
import persistencia.RepositorioMarcador;

import java.util.List;
import java.util.Random;

public class GestorDeJuego {

    private final Jugador jugadorA;
    private final Jugador jugadorB;
    private final RepositorioMarcador repositorioMarcador;
    private final Historial historial;
    private final Random random;
    private final boolean pausarEntreTurnos;

    public GestorDeJuego(Jugador jugadorA, Jugador jugadorB, RepositorioMarcador repositorioMarcador) {
        this(jugadorA, jugadorB, repositorioMarcador, false);
    }

    public GestorDeJuego(Jugador jugadorA, Jugador jugadorB, RepositorioMarcador repositorioMarcador,
                         boolean pausarEntreTurnos) {
        this.jugadorA = jugadorA;
        this.jugadorB = jugadorB;
        this.repositorioMarcador = repositorioMarcador;
        this.historial = new Historial();
        this.random = new Random();
        this.pausarEntreTurnos = pausarEntreTurnos;
    }

    public void iniciarPartida() {
        elegirPersonajesSecretos();

        List<Personaje> candidatosDeA = CatalogoPersonajes.generar();
        List<Personaje> candidatosDeB = CatalogoPersonajes.generar();

        Jugador turnoActual = sortearJugadorInicial();
        Jugador rival = (turnoActual == jugadorA) ? jugadorB : jugadorA;

        boolean hayGanador = false;

        while (!hayGanador) {
            List<Personaje> candidatosDelRival = (rival == jugadorA) ? candidatosDeA : candidatosDeB;

            ProximoTurno resultado = jugarTurno(turnoActual, rival, candidatosDelRival);

            if (rival == jugadorA) {
                candidatosDeA = resultado.candidatos();
            } else {
                candidatosDeB = resultado.candidatos();
            }

            if (resultado.hayGanador()) {
                gano(turnoActual);
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
     * Logging centralizado: todo lo que se imprime del proceso de una
     * partida vive acá, no en las clases de Jugador. Así el log es
     * consistente sin importar si juega un humano o cualquier IA.
     */
    private ProximoTurno jugarTurno(Jugador turnoActual, Jugador rival, List<Personaje> candidatosDelRival) {
        Consola.encabezadoTurno(turnoActual.getNombre());

        Jugada jugada = turnoActual.decidirJugada(candidatosDelRival, historial);

        switch (jugada.getTipoJugada()) {
            case PREGUNTA -> {
                Pregunta<?> pregunta = jugada.getPregunta();
                Consola.pregunta(turnoActual.getNombre(), rival.getNombre(), pregunta);

                boolean respuesta = rival.responder(pregunta);
                Consola.respuesta(rival.getNombre(), respuesta);

                historial.registrar(turnoActual, pregunta, respuesta);

                List<Personaje> filtrados = pregunta.filtrar(candidatosDelRival, respuesta);
                Consola.candidatosRestantes(turnoActual.getNombre(), filtrados.size());
                Consola.listarCandidatos(filtrados);

                pausar();
                return new ProximoTurno(false, filtrados);
            }
            case ADIVINANZA -> {
                Personaje personajeAdivinado = jugada.getPersonajeAdivinado();
                Consola.adivinanza(turnoActual.getNombre(), rival.getNombre(), personajeAdivinado);

                if (rival.esMiPersonajeSecreto(personajeAdivinado)) {
                    Consola.aciertoAdivinanza(personajeAdivinado);
                    pausar();
                    return new ProximoTurno(true, candidatosDelRival);
                } else {
                    Consola.falloAdivinanza(rival.getNombre(), personajeAdivinado);
                    candidatosDelRival.remove(personajeAdivinado);
                    Consola.candidatosRestantes(turnoActual.getNombre(), candidatosDelRival.size());
                    Consola.listarCandidatos(candidatosDelRival);
                    pausar();
                    return new ProximoTurno(false, candidatosDelRival);
                }
            }
            default -> throw new IllegalStateException("TipoJugada no soportado: " + jugada.getTipoJugada());
        }
    }

    private void pausar() {
        if (!pausarEntreTurnos) {
            return;
        }
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void gano(Jugador ganador) {
        Consola.victoria(ganador.getNombre());
        repositorioMarcador.registrarVictoria(ganador.getNombre());
    }
}