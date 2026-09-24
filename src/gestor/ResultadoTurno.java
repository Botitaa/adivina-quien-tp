package gestor;

import actores.Jugador;
import dominio.Jugada;

public record ResultadoTurno(int numeroTurno, Jugador autor, Jugador rival, Jugada jugada,
                             boolean respuesta, boolean acierto, int candidatosRestantes) {

    public boolean esPregunta() {
        return jugada.getTipoJugada() == Jugada.TipoJugada.PREGUNTA;
    }

    public boolean terminoPartida() {
        return !esPregunta() && acierto;
    }
}
