package actores;

import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;

import java.util.List;

public interface Jugador {

    void elegirPersonajeSecreto(List<Personaje> personajesDisponible);//elige una vez el personaje secreto entre los disp.

    Jugada decidirJugada(List<Personaje> candidatos, Historial historial); //edcide si preguntar o adivinar

    void recibirRespuesta(Pregunta pregunta, boolean respuesta); //recibe el resultado de la ultima pregunta hecha para actualizar su estado

    String getNombre();

    boolean responder(Pregunta pregunta);//responde t/f si el pers secreto responde a la pregunta recibida
}
