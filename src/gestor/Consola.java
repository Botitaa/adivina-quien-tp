package gestor;

import dominio.Personaje;
import dominio.Pregunta;

import java.util.List;

/**
 * Utilidades de presentación por consola: colores ANSI, layout de
 * candidatos, encabezados de turno. Centraliza el "cómo se ve" para que
 * GestorDeJuego solo decida "qué" imprimir, no el formato.
 *
 * Los colores ANSI funcionan en la consola de IntelliJ y en terminales
 * modernas (Windows Terminal, macOS, Linux). En el cmd.exe clásico de
 * Windows pueden no renderizarse y mostrar los códigos como texto crudo
 * — si eso pasa, avisame y armamos un fallback sin color.
 */
public final class Consola {

    private static final String RESET = "\u001B[0m";
    private static final String NEGRITA = "\u001B[1m";
    private static final String VERDE = "\u001B[32m";
    private static final String ROJO = "\u001B[31m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    private static final int COLUMNAS = 6;

    private Consola() {
        // clase de utilidades, no instanciable
    }

    public static void encabezadoTurno(String nombreJugador) {
        String linea = "=".repeat(40);
        System.out.println();
        System.out.println(CYAN + NEGRITA + linea + RESET);
        System.out.println(CYAN + NEGRITA + "  TURNO DE " + nombreJugador.toUpperCase() + RESET);
        System.out.println(CYAN + NEGRITA + linea + RESET);
    }

    public static void pregunta(String preguntador, String respondedor, Pregunta<?> pregunta) {
        System.out.println(AMARILLO + preguntador + " le pregunta a " + respondedor + ": " + RESET + pregunta);
    }

    public static void respuesta(String respondedor, boolean respuesta) {
        String texto = respuesta ? VERDE + "Sí" + RESET : ROJO + "No" + RESET;
        System.out.println(respondedor + " responde: " + texto);
    }

    public static void adivinanza(String adivinador, String rival, Personaje personajeAdivinado) {
        System.out.println(AMARILLO + adivinador + " arriesga: " + RESET
                + "¿" + rival + " es " + NEGRITA + personajeAdivinado.getNombre() + RESET + "?");
    }

    public static void aciertoAdivinanza(Personaje personajeAdivinado) {
        System.out.println(VERDE + NEGRITA + "¡Correcto! Era " + personajeAdivinado.getNombre() + "." + RESET);
    }

    public static void falloAdivinanza(String rival, Personaje personajeAdivinado) {
        System.out.println(ROJO + "No, " + rival + " no es " + personajeAdivinado.getNombre()
                + ". Se descarta de la lista." + RESET);
    }

    public static void candidatosRestantes(String nombreJugador, int cantidad) {
        System.out.println("Le quedan " + NEGRITA + cantidad + RESET + " candidatos posibles a "
                + nombreJugador + ".");
    }

    public static void victoria(String nombreGanador) {
        String linea = "*".repeat(40);
        System.out.println();
        System.out.println(VERDE + NEGRITA + linea + RESET);
        System.out.println(VERDE + NEGRITA + "  ¡" + nombreGanador.toUpperCase() + " GANÓ LA PARTIDA!" + RESET);
        System.out.println(VERDE + NEGRITA + linea + RESET);
    }

    /**
     * Muestra la lista de candidatos en filas fijas de 6, numerados,
     * para que sea fácil de leer de un vistazo en vez de un bloque
     * de texto corrido.
     */
    public static void listarCandidatos(List<Personaje> candidatos) {
        for (int i = 0; i < candidatos.size(); i++) {
            System.out.printf("%2d. %-15s", i + 1, candidatos.get(i).getNombre());
            if ((i + 1) % COLUMNAS == 0 || i == candidatos.size() - 1) {
                System.out.println();
            }
        }
    }
}