package gestor;

import dominio.CatalogoPersonajes;
import dominio.Personaje;
import dominio.PersonajeJuego;
import dominio.Pregunta;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Personaje> personajesRaw = CatalogoPersonajes.generar();

        // Cast a PersonajeJuego porque evaluar() pide ese tipo puntual
        List<PersonajeJuego> personajes = personajesRaw.stream()
                .map(p -> (PersonajeJuego) p)
                .toList();

        System.out.println("=== Verificación exhaustiva: evaluar() sobre los 23 personajes ===");

        List<Pregunta<?>> preguntas = Pregunta.generarTodas();

        for (Pregunta<?> pregunta : preguntas) {
            int cantidadTrue = 0;
            int cantidadFalse = 0;

            for (PersonajeJuego personaje : personajes) {
                boolean resultado = pregunta.evaluar(personaje);
                if (resultado) {
                    cantidadTrue++;
                } else {
                    cantidadFalse++;
                }
            }

            System.out.printf("%-30s -> true: %2d | false: %2d | total: %2d%n",
                    pregunta, cantidadTrue, cantidadFalse, cantidadTrue + cantidadFalse);
        }
    }
}