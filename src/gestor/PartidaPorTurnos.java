package gestor;

import actores.Historial;
import actores.Jugador;
import dominio.CatalogoPersonajes;
import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;
import persistencia.RepositorioMarcador;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;


public class PartidaPorTurnos {

    private final Jugador jugadorA;
    private final Jugador jugadorB;
    private final RepositorioMarcador repositorioMarcador;
    private final Historial historial = new Historial();
    private final List<Personaje> catalogo = CatalogoPersonajes.generar();

    private final Map<Jugador, List<Personaje>> candidatosPorJugador = new HashMap<>();
    private final Random random = new Random();

    private Jugador turnoActual;
    private Jugador ganador;
    private int numeroTurno;
    private boolean iniciada;

    public PartidaPorTurnos(Jugador jugadorA, Jugador jugadorB, RepositorioMarcador repositorioMarcador) {
        if (jugadorA == null || jugadorB == null || repositorioMarcador == null) {
            throw new IllegalArgumentException("Los jugadores y el repositorio no pueden ser null");
        }
        if (jugadorA == jugadorB) {
            throw new IllegalArgumentException("Un jugador no puede jugar contra sí mismo");
        }
        this.jugadorA = jugadorA;
        this.jugadorB = jugadorB;
        this.repositorioMarcador = repositorioMarcador;
    }


    public void iniciar() {
        if (iniciada) {
            throw new IllegalStateException("La partida ya fue iniciada");
        }
        jugadorA.elegirPersonajeSecreto(catalogo);
        jugadorB.elegirPersonajeSecreto(catalogo);
        candidatosPorJugador.put(jugadorA, CatalogoPersonajes.generar());
        candidatosPorJugador.put(jugadorB, CatalogoPersonajes.generar());
        turnoActual = random.nextBoolean() ? jugadorA : jugadorB;
        iniciada = true;
        log("Nueva partida: " + jugadorA.getNombre() + " vs " + jugadorB.getNombre()
                + ". Empieza " + turnoActual.getNombre() + ".");
    }


    public ResultadoTurno jugarTurno() {
        verificarEnCurso();
        numeroTurno++;
        Jugador autor = turnoActual;
        Jugador rival = rivalDe(autor);
        Jugada jugada = autor.decidirJugada(candidatosPorJugador.get(autor), historial);

        ResultadoTurno resultado = switch (jugada.getTipoJugada()) {
            case PREGUNTA -> resolverPregunta(autor, rival, jugada);
            case ADIVINANZA -> resolverAdivinanza(autor, rival, jugada);
        };

        if (resultado.terminoPartida()) {
            ganador = autor;
            repositorioMarcador.registrarVictoria(autor.getNombre());
            log(autor.getNombre() + " GANA en " + numeroTurno + " turnos.");
        } else {
            turnoActual = rival;
        }
        return resultado;
    }

    private ResultadoTurno resolverPregunta(Jugador autor, Jugador rival, Jugada jugada) {
        Pregunta<?> pregunta = jugada.getPregunta();
        boolean respuesta = rival.responder(pregunta);
        historial.registrar(autor, pregunta, respuesta);

        List<Personaje> filtrados = pregunta.filtrar(candidatosPorJugador.get(autor), respuesta);
        candidatosPorJugador.put(autor, filtrados);

        log(autor.getNombre() + " pregunta " + pregunta + " -> " + (respuesta ? "SÍ" : "NO")
                + " | le quedan " + filtrados.size() + " candidatos");
        return new ResultadoTurno(numeroTurno, autor, rival, jugada, respuesta, false, filtrados.size());
    }

    private ResultadoTurno resolverAdivinanza(Jugador autor, Jugador rival, Jugada jugada) {
        Personaje adivinado = jugada.getPersonajeAdivinado();
        boolean acierto = rival.esMiPersonajeSecreto(adivinado);
        List<Personaje> candidatos = candidatosPorJugador.get(autor);
        if (!acierto) {
            candidatos.removeIf(personaje -> personaje.getId() == adivinado.getId());
        }
        log(autor.getNombre() + " arriesga #" + adivinado.getId() + " -> " + (acierto ? "ACIERTA" : "falla")
                + " | le quedan " + candidatos.size() + " candidatos");
        return new ResultadoTurno(numeroTurno, autor, rival, jugada, false, acierto, candidatos.size());
    }


    public List<Personaje> getCatalogo() {
        return Collections.unmodifiableList(catalogo);
    }


    public List<Personaje> getCandidatosDe(Jugador jugador) {
        verificarJugador(jugador);
        verificarIniciada();
        return Collections.unmodifiableList(candidatosPorJugador.get(jugador));
    }


    public List<Pregunta<?>> preguntasDisponiblesPara(Jugador jugador) {
        verificarJugador(jugador);
        List<Pregunta<?>> disponibles = new ArrayList<>();
        for (Pregunta<?> pregunta : Pregunta.generarTodas()) {
            if (!historial.yaSePregunto(pregunta, jugador)) {
                disponibles.add(pregunta);
            }
        }
        return disponibles;
    }


    public Personaje personajeSecretoDe(Jugador jugador) {
        verificarJugador(jugador);
        verificarIniciada();
        for (Personaje personaje : catalogo) {
            if (jugador.esMiPersonajeSecreto(personaje)) {
                return personaje;
            }
        }
        throw new IllegalStateException(jugador.getNombre() + " no tiene personaje secreto");
    }

    public Jugador rivalDe(Jugador jugador) {
        verificarJugador(jugador);
        return (jugador == jugadorA) ? jugadorB : jugadorA;
    }

    public Jugador getJugadorA() {
        return jugadorA;
    }

    public Jugador getJugadorB() {
        return jugadorB;
    }

    public Jugador getTurnoActual() {
        return turnoActual;
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public boolean estaTerminada() {
        return ganador != null;
    }

    public Optional<Jugador> getGanador() {
        return Optional.ofNullable(ganador);
    }

    public int getTotalPersonajes() {
        return catalogo.size();
    }



    private void verificarEnCurso() {
        verificarIniciada();
        if (estaTerminada()) {
            throw new IllegalStateException("La partida ya terminó");
        }
    }

    private void verificarIniciada() {
        if (!iniciada) {
            throw new IllegalStateException("La partida todavía no fue iniciada");
        }
    }

    private void verificarJugador(Jugador jugador) {
        if (jugador != jugadorA && jugador != jugadorB) {
            throw new IllegalArgumentException("El jugador no pertenece a esta partida");
        }
    }

    private void log(String mensaje) {
        System.out.println("[Turno " + numeroTurno + "] " + mensaje);
    }
}
