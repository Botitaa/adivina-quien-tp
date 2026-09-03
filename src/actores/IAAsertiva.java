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

        if(personajesDisponibles.isEmpty()){
            throw new IllegalArgumentException("La lista de personajes disponibles no puede ser nula ni vacía");
        }

        if (personajeSecreto == null){
            personajeSecreto = personajesDisponibles.get(random.nextInt(personajesDisponibles.size()));
        }
    }

    @Override
    public Jugada decidirJugada(List<Personaje> candidatos, Historial historial) {

        if (candidatos.size() == 1) {
            return Jugada.deAdivinanza(candidatos.get(0));
        }

        Pregunta<?> mejorPregunta = elegirMejorPregunta(candidatos, historial);

        if (mejorPregunta == null){
            return Jugada.deAdivinanza(candidatos.get(0));
        }

        return Jugada.dePregunta(mejorPregunta);
    }

    private Pregunta<?> elegirMejorPregunta(List<Personaje> candidatos, Historial historial) {

        List<Pregunta<?>> preguntas = Pregunta.generarTodas();
        Pregunta<?> mejorPregunta = null;
        double menorRatio = Double.POSITIVE_INFINITY;

        for (int i = 0; i < preguntas.size(); i++) {
            if (historial.yaSePregunto(preguntas.get(i), this)){
                continue;
            } else {
                Pregunta<?> pregunta = preguntas.get(i);

                List<Personaje> cumplen = pregunta.filtrar(candidatos,true);
                List<Personaje> noCumplen = pregunta.filtrar(candidatos,false);

                double ratio = (double) Math.max(cumplen.size(),noCumplen.size())/ (double) Math.min(cumplen.size(), noCumplen.size());

                if (ratio< menorRatio){
                    menorRatio = ratio;
                    mejorPregunta = pregunta;
                }
            }
        }

        return mejorPregunta;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean responder(Pregunta<?> pregunta) {

        if (personajeSecreto == null){
            throw new IllegalStateException("todavia no hay personaje secreto");
        } else {
            return pregunta.evaluar(personajeSecreto);
        }

    }

    @Override
    public boolean esMiPersonajeSecreto(Personaje personaje) {

        if (personajeSecreto == null){
            throw new IllegalStateException("todavia no hay personaje secreto");
        } else {
            return personajeSecreto.getId() == personaje.getId();
        }
    }
}