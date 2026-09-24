package actores;

import dominio.Pregunta;

import java.util.ArrayList;
import java.util.List;

/** Registra la secuencia de preguntas de la partida (autor, pregunta, respuesta). */
public class Historial {

    public static class Entrada {
        private final Jugador autor;
        private final Pregunta<?> pregunta;
        private final boolean respuesta;

        public Entrada(Jugador autor, Pregunta<?> pregunta, boolean respuesta) {
            this.autor = autor;
            this.pregunta = pregunta;
            this.respuesta = respuesta;
        }

        public Jugador getAutor() {
            return autor;
        }

        public Pregunta<?> getPregunta() {
            return pregunta;
        }

        public boolean getRespuesta() {
            return respuesta;
        }
    }

    private final List<Entrada> jugadas = new ArrayList<>();

    public void registrar(Jugador autor, Pregunta<?> pregunta, boolean respuesta) {
        jugadas.add(new Entrada(autor, pregunta, respuesta));
    }

    /** O(n): recorre las jugadas comparando con equals() de Pregunta (por atributo y valor). */
    public boolean yaSePregunto(Pregunta<?> pregunta, Jugador jugador) {
        for (Entrada entrada : jugadas) {
            if (entrada.getAutor().equals(jugador) && entrada.getPregunta().equals(pregunta)) {
                return true;
            }
        }
        return false;
    }
}
