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

    public GestorDeJuego(Jugador jugadorA, Jugador jugadorB, RepositorioMarcador repositorioMarcador) {
        this.jugadorA = jugadorA;
        this.jugadorB = jugadorB;
        this.repositorioMarcador = repositorioMarcador;
        this.historial = new Historial();
        this.random = new Random();
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

    private ProximoTurno jugarTurno(Jugador turnoActual, Jugador rival, List<Personaje> candidatosDelRival) {
        Jugada jugada = turnoActual.decidirJugada(candidatosDelRival, historial);

        switch (jugada.getTipoJugada()) {
            case PREGUNTA -> {
                Pregunta<?> pregunta = jugada.getPregunta();
                boolean respuesta = rival.responder(pregunta);
                System.out.println(rival.getNombre() + " responde: " + (respuesta ? "Sí" : "No"));
                historial.registrar(turnoActual, pregunta, respuesta);
                List<Personaje> filtrados = pregunta.filtrar(candidatosDelRival, respuesta);
                System.out.println("Le quedan " + filtrados.size() + " candidatos posibles a " + turnoActual.getNombre() + ".");
                return new ProximoTurno(false, filtrados);
            }
            case ADIVINANZA -> {
                Personaje personajeAdivinado = jugada.getPersonajeAdivinado();
                if (rival.esMiPersonajeSecreto(personajeAdivinado)) {
                    return new ProximoTurno(true, candidatosDelRival);
                } else {
                    System.out.println("No, " + rival.getNombre() + " no es " + personajeAdivinado.getNombre() + ". Se descarta de la lista.");
                    candidatosDelRival.remove(personajeAdivinado);
                    return new ProximoTurno(false, candidatosDelRival);
                }
            }
            default -> throw new IllegalStateException("TipoJugada no soportado: " + jugada.getTipoJugada());
        }
    }

    private void gano(Jugador ganador) {
        System.out.println("¡" + ganador.getNombre() + " ganó la partida!");
        repositorioMarcador.registrarVictoria(ganador.getNombre());
    }
}