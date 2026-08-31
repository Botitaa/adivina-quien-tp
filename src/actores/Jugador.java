package actores;

import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;

import java.util.List;

public interface Jugador {

    void elegirPersonajeSecreto(List<Personaje> personajesDisponible);//elige una vez el personaje secreto entre los disp.

    Jugada decidirJugada(List<Personaje> candidatos, Historial historial); //decide si preguntar o adivinar

    String getNombre();

    boolean responder(Pregunta pregunta);//responde t/f si el pers secreto responde a la pregunta recibida

    boolean esMiPersonajeSecreto(Personaje personaje);
}
