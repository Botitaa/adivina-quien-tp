package actores;

import dominio.Pregunta;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

//registra la secuencia de jugadas (autor,pregunta, respuesta)
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

        @Override
        public String toString() {
            String textoRespuesta;
            if (getRespuesta()) {
                textoRespuesta = "Si";
            } else {
                textoRespuesta = "No";
            }
            return getAutor().getNombre() + " pregunto |" + getPregunta() + "| Respuesta: " + textoRespuesta;
        }
    }
    private final List<Entrada> jugadas = new ArrayList<>();

    public void registrar(Jugador autor, Pregunta<?> pregunta, boolean respuesta) {
        jugadas.add(new Entrada(autor,pregunta,respuesta));
    }

    public List<Entrada> getJugadas() {
        return Collections.unmodifiableList(jugadas); //este metodo de collections hace que jugadas sea solo lectura (solo .get())
    }
    public List<Entrada> getJugadasDe(Jugador jugador){
        List<Entrada> resultado = new ArrayList<>();
        for (Entrada entrada:jugadas){
            if (entrada.getAutor().equals(jugador)){
                resultado.add(entrada);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public boolean yaSePregunto(Pregunta<?> pregunta, Jugador jugador) {
        for (Entrada entrada:jugadas){
            if (entrada.getAutor().equals(jugador) && entrada.getPregunta().equals(pregunta)){
                return true;
            }
        } return false;
    }
    public boolean estaVacio(){
        return jugadas.isEmpty();
    }
}
