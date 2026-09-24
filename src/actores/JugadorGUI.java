package actores;

import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;

import java.util.List;

public class JugadorGUI implements Jugador {

    private final String nombre;
    private Personaje personajeSecreto;
    private Personaje secretoPreparado;
    private Jugada jugadaPreparada;

    public JugadorGUI(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public void prepararPersonajeSecreto(Personaje personaje) {
        if (personaje == null) {
            throw new IllegalArgumentException("El personaje secreto no puede ser null");
        }
        this.secretoPreparado = personaje;
    }


    public void prepararJugada(Jugada jugada) {
        if (jugada == null) {
            throw new IllegalArgumentException("La jugada no puede ser null");
        }
        this.jugadaPreparada = jugada;
    }

    @Override
    public void elegirPersonajeSecreto(List<Personaje> personajesDisponible) {
        if (personajeSecreto != null) {
            throw new IllegalStateException("El personaje secreto de " + nombre + " ya fue asignado");
        }
        if (secretoPreparado == null) {
            throw new IllegalStateException("La interfaz todavía no preparó el personaje secreto de " + nombre);
        }

        for (Personaje personaje : personajesDisponible) {
            if (personaje.getId() == secretoPreparado.getId()) {
                personajeSecreto = personaje;
                return;
            }
        }
        throw new IllegalArgumentException("El personaje elegido no está entre los disponibles");
    }

    @Override
    public Jugada decidirJugada(List<Personaje> candidatos, Historial historial) {
        if (jugadaPreparada == null) {
            throw new IllegalStateException("La interfaz todavía no preparó la jugada de " + nombre);
        }
        Jugada jugada = jugadaPreparada;
        jugadaPreparada = null;
        return jugada;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean responder(Pregunta<?> pregunta) {
        if (personajeSecreto == null) {
            throw new IllegalStateException(nombre + " todavía no eligió su personaje secreto.");
        }
        return pregunta.evaluar(personajeSecreto);
    }

    @Override
    public boolean esMiPersonajeSecreto(Personaje personaje) {
        if (personajeSecreto == null) {
            throw new IllegalStateException(nombre + " todavía no eligió su personaje secreto.");
        }
        if (personaje == null) {
            throw new IllegalArgumentException("El personaje a comparar no puede ser null");
        }
        return personajeSecreto.getId() == personaje.getId();
    }
}
