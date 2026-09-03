package actores;

import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;

import java.util.List;
import java.util.Random;

public class IAAsertiva implements Jugador {

    private final Random random = new Random();
    private final String nombre;
    private Personaje personajeSecreto;

    public IAAsertiva(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void elegirPersonajeSecreto(List<Personaje> personajesDisponibles) {
        // validar lista no nula/vacía
        // validar que personajeSecreto todavía no fue asignado
        // elegir uno al azar con random.nextInt(...) y asignarlo
    }

    @Override
    public Jugada decidirJugada(List<Personaje> candidatos, Historial historial) {
        // si candidatos.size() == 1 -> Jugada.deAdivinanza(...)
        // si no, buscar la mejor pregunta (ver elegirMejorPregunta)
        // si no hay ninguna discriminante sin hacer -> Jugada.deAdivinanza(candidatos.get(0))
        // si hay -> Jugada.dePregunta(...)
        return null;
    }

    // Recorre Pregunta.generarTodas(), descarta las ya hechas por "this" (historial.yaSePregunto),
    // calcula ratio = max(cumplen,noCumplen)/(double)min(cumplen,noCumplen) para las que sí discriminan,
    // devuelve la de menor ratio (o null si no queda ninguna).
    private Pregunta<?> elegirMejorPregunta(List<Personaje> candidatos, Historial historial) {
        return null;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean responder(Pregunta<?> pregunta) {
        // evaluar la pregunta contra personajeSecreto (validar que no sea null antes)
        return false;
    }

    @Override
    public boolean esMiPersonajeSecreto(Personaje personaje) {
        // comparar personajeSecreto.getId() con personaje.getId() (validar nulls antes)
        return false;
    }
}