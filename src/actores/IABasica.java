package actores;

import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IABasica implements Jugador {
    private final Random random = new Random();
    private final String nombre;
    private Personaje personajeSecreto;

    public IABasica(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void elegirPersonajeSecreto(List<Personaje> personajesDisponible) {
        int indice = random.nextInt(personajesDisponible.size());
        asignarSecreto(personajesDisponible.get(indice));
    }
    private void asignarSecreto(Personaje personaje){
        if(personaje == null){
            throw new IllegalArgumentException("El personaje no puede ser null");
        }
        if (this.personajeSecreto != null) {
            throw new IllegalStateException("El personaje secreto de " + nombre + " ya fue asignado y no puede reasignarse");
        }
        this.personajeSecreto = personaje;
    }

    @Override
    public Jugada decidirJugada(List<Personaje> candidatos, Historial historial) {
        if (candidatos.size() == 1) {
            return Jugada.deAdivinanza(candidatos.get(0));
        }
        List<Pregunta<?>> discriminantes = preguntasQueDiscriminan(candidatos);
        Pregunta<?> elegida = discriminantes.get(random.nextInt(discriminantes.size()));
        return Jugada.dePregunta(elegida);
    }
    //pregunta discrimina si entre los candiadatos actuales hay al menos uno q cumpla y otro q no.
    private List<Pregunta<?>> preguntasQueDiscriminan(List<Personaje> candidatos){
        List<Pregunta<?>> discriminantes = new ArrayList<>();
        for (Pregunta<?> pregunta:Pregunta.generarTodas()) {
            List<Personaje> cumplen = pregunta.filtrar(candidatos, true);
            List<Personaje> noCumplen = pregunta.filtrar(candidatos, false);
            if (!cumplen.isEmpty() && !noCumplen.isEmpty()) {
                discriminantes.add(pregunta);
            }
        }
        return discriminantes;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean responder(Pregunta<?> pregunta) {
        if (personajeSecreto == null) {
            throw new IllegalStateException(nombre + " todavia no eligio su personaje.");
        }
        return pregunta.evaluar(personajeSecreto);
    }

    @Override
    public boolean esMiPersonajeSecreto(Personaje personaje) {
        if (personajeSecreto == null) {
            throw new IllegalStateException(nombre + " todavia no eligio su personaje.");
        }
        if (personaje == null){
            throw new IllegalArgumentException("El personaje a comparar no puede ser null");
        }
        return personajeSecreto.getId() == personaje.getId();
    }
}
