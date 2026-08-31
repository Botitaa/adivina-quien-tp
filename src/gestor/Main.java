package gestor;

import dominio.CatalogoPersonajes;
import dominio.Jugada;
import dominio.Personaje;
import dominio.PersonajeJuego;
import dominio.Pregunta;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Personaje> personajes = CatalogoPersonajes.generar();

        System.out.println("=== Catálogo de personajes ===");
        for (Personaje p : personajes) {
            System.out.println(p);
        }
        System.out.println("Total personajes: " + personajes.size());

        System.out.println("\n=== Catálogo de preguntas ===");
        List<Pregunta<?>> preguntas = Pregunta.generarTodas();
        System.out.println("Total de preguntas: " + preguntas.size());
        for (Pregunta<?> p : preguntas) {
            System.out.println(p);
        }

        System.out.println("\n=== Verificación exhaustiva: evaluar() sobre los 23 personajes ===");
        for (Pregunta<?> pregunta : preguntas) {
            int cantidadTrue = 0;
            int cantidadFalse = 0;

            for (Personaje p : personajes) {
                PersonajeJuego personaje = (PersonajeJuego) p;
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

        System.out.println("\n=== Verificación de filtrar() ===");
        for (Pregunta<?> pregunta : preguntas) {
            List<Personaje> sobrevivenTrue = pregunta.filtrar(personajes, true);
            List<Personaje> sobrevivenFalse = pregunta.filtrar(personajes, false);

            boolean sumaCorrecta = (sobrevivenTrue.size() + sobrevivenFalse.size()) == personajes.size();
            boolean sinSuperposicion = sobrevivenTrue.stream().noneMatch(sobrevivenFalse::contains);

            System.out.printf("%-30s -> filtrar(true): %2d | filtrar(false): %2d | suma OK: %b | sin superposición: %b%n",
                    pregunta, sobrevivenTrue.size(), sobrevivenFalse.size(), sumaCorrecta, sinSuperposicion);
        }

        System.out.println("\n=== Verificación de Jugada ===");

        // Caso 1: jugada de tipo PREGUNTA
        Pregunta<?> preguntaElegida = preguntas.get(0);
        Jugada jugadaPregunta = Jugada.dePregunta(preguntaElegida);

        System.out.println("Jugada creada: " + jugadaPregunta);
        System.out.println("  tipoJugada: " + jugadaPregunta.getTipoJugada());
        System.out.println("  pregunta: " + jugadaPregunta.getPregunta());
        System.out.println("  personajeAdivinado (debe ser null): " + jugadaPregunta.getPersonajeAdivinado());

        // Caso 2: jugada de tipo ADIVINANZA
        Personaje personajeElegido = personajes.get(0);
        Jugada jugadaAdivinanza = Jugada.deAdivinanza(personajeElegido);

        System.out.println("\nJugada creada: " + jugadaAdivinanza);
        System.out.println("  tipoJugada: " + jugadaAdivinanza.getTipoJugada());
        System.out.println("  pregunta (debe ser null): " + jugadaAdivinanza.getPregunta());
        System.out.println("  personajeAdivinado: " + jugadaAdivinanza.getPersonajeAdivinado());

        // Simulación de cómo GestorDeJuego va a ramificar según el tipo
        System.out.println("\n=== Simulación de switch tipo GestorDeJuego ===");
        for (Jugada j : List.of(jugadaPregunta, jugadaAdivinanza)) {
            switch (j.getTipoJugada()) {
                case PREGUNTA -> System.out.println("GestorDeJuego reenvía la pregunta: " + j.getPregunta());
                case ADIVINANZA -> System.out.println("GestorDeJuego verifica el personaje: " + j.getPersonajeAdivinado());
            }
        }
    }
}