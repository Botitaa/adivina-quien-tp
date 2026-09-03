package presentacion;

import dominio.Personaje;
import dominio.Pregunta;
import valores.ColorPelo;
import valores.Genero;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Capa de presentación por consola, estilo "arcade retro": colores ANSI,
 * cajas con bordes, efecto de tipeo, animaciones de carga, tablero de
 * cartas con los personajes, barra de progreso y campanita de victoria.
 *
 * Centraliza el "cómo se ve" para que GestorDeJuego, Main y JugadorHumano
 * solo decidan "qué" mostrar, nunca el formato. Depende únicamente de
 * dominio y valores, así puede usarla cualquier capa superior sin ciclos.
 *
 * Los códigos ANSI funcionan en la consola de IntelliJ y en terminales
 * modernas (macOS, Linux, Windows Terminal). En cmd.exe clásico pueden
 * mostrarse como texto crudo.
 */
public final class Consola {

    // ---- códigos ANSI ----
    private static final String RESET = "\u001B[0m";
    private static final String NEGRITA = "\u001B[1m";
    private static final String TENUE = "\u001B[2m";
    private static final String VERDE = "\u001B[32m";
    private static final String ROJO = "\u001B[31m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String BLANCO = "\u001B[97m";
    private static final String CAMPANA = "\u0007";

    // ---- layout ----
    private static final int COLUMNAS = 6;
    private static final int ANCHO_CARTA = 14;
    private static final int ANCHO_CAJA = 52;
    private static final int ANCHO_BARRA = 30;

    // ---- tiempos (ms) ----
    private static final int MS_TIPEO = 18;
    private static final int MS_PUNTO_CARGA = 350;

    /** Permite apagar las animaciones (útil para simulaciones masivas IA vs IA). */
    private static boolean animaciones = true;

    private Consola() {
        // clase de utilidades, no instanciable
    }

    public static void setAnimaciones(boolean activas) {
        animaciones = activas;
    }

    // =====================================================================
    // Pantalla y utilidades básicas
    // =====================================================================

    public static void limpiarPantalla() {
        System.out.print("\u001B[H\u001B[2J");
        System.out.flush();
    }

    public static void saltoDeLinea() {
        System.out.println();
    }

    /** Efecto máquina de escribir, letra por letra. */
    public static void escribirLento(String texto) {
        if (!animaciones) {
            System.out.println(texto);
            return;
        }
        for (char c : texto.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            dormir(MS_TIPEO);
        }
        System.out.println();
    }

    /** "Cargando..." con puntitos que aparecen de a uno. */
    public static void cargando(String texto, int puntos) {
        System.out.print(TENUE + texto + RESET);
        System.out.flush();
        for (int i = 0; i < puntos; i++) {
            dormir(MS_PUNTO_CARGA);
            System.out.print(TENUE + "." + RESET);
            System.out.flush();
        }
        System.out.println();
    }

    public static void esperarEnter(Scanner scanner) {
        System.out.print(TENUE + "\n[ Presioná ENTER para continuar ]" + RESET);
        scanner.nextLine();
    }

    public static void prompt(String texto) {
        System.out.print(CYAN + NEGRITA + texto + " > " + RESET);
        System.out.flush();
    }

    public static void error(String mensaje) {
        System.out.println(ROJO + "  ✖ " + mensaje + RESET);
    }

    public static void info(String mensaje) {
        System.out.println(TENUE + "  » " + mensaje + RESET);
    }

    // =====================================================================
    // Cajas y menús
    // =====================================================================

    public static void caja(String titulo, List<String> lineas) {
        String borde = "═".repeat(ANCHO_CAJA - 2);
        System.out.println(CYAN + "╔" + borde + "╗" + RESET);
        if (titulo != null && !titulo.isBlank()) {
            System.out.println(CYAN + "║" + RESET + NEGRITA + centrar(titulo, ANCHO_CAJA - 2) + RESET + CYAN + "║" + RESET);
            System.out.println(CYAN + "╠" + borde + "╣" + RESET);
        }
        for (String linea : lineas) {
            System.out.println(CYAN + "║" + RESET + " " + rellenar(linea, ANCHO_CAJA - 3) + CYAN + "║" + RESET);
        }
        System.out.println(CYAN + "╚" + borde + "╝" + RESET);
    }

    public static void menu(String titulo, List<String> opciones) {
        System.out.println();
        String borde = "═".repeat(ANCHO_CAJA - 2);
        System.out.println(CYAN + "╔" + borde + "╗" + RESET);
        System.out.println(CYAN + "║" + RESET + NEGRITA + centrar(titulo, ANCHO_CAJA - 2) + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╠" + borde + "╣" + RESET);
        for (int i = 0; i < opciones.size(); i++) {
            String texto = AMARILLO + NEGRITA + " [" + (i + 1) + "] " + RESET + opciones.get(i);
            System.out.println(CYAN + "║" + RESET + rellenar(texto, ANCHO_CAJA - 2, longitudVisible(texto)) + CYAN + "║" + RESET);
        }
        System.out.println(CYAN + "╚" + borde + "╝" + RESET);
    }

    // =====================================================================
    // Pantallas de inicio y fin
    // =====================================================================

    public static void pantallaTitulo(Scanner scanner) {
        limpiarPantalla();
        String[] logo = {
                "    _    ____ ___ __     __ ___  _   _    _    ",
                "   / \\  |  _ \\_ _|\\ \\   / /|_ _|| \\ | |  / \\   ",
                "  / _ \\ | | | | |  \\ \\ / /  | | |  \\| | / _ \\  ",
                " / ___ \\| |_| | |   \\ V /   | | | |\\  |/ ___ \\ ",
                "/_/   \\_\\____/___|   \\_/   |___||_| \\_/_/   \\_\\",
                "",
                "       ___   _   _  ___  _____  _   _         ",
                "      / _ \\ | | | ||_ _|| ____|| \\ | |  ___   ",
                "     | | | || | | | | | |  _|  |  \\| | |__ \\  ",
                "     | |_| || |_| | | | | |___ | |\\  |   / /  ",
                "      \\__\\_\\ \\___/ |___||_____||_| \\_|  (_)   ",
        };
        System.out.println();
        for (String linea : logo) {
            System.out.println(MAGENTA + NEGRITA + linea + RESET);
            dormir(60);
        }
        System.out.println();
        System.out.println(TENUE + centrar("Programación 3 · UADE · edición consola", ANCHO_CAJA) + RESET);
        System.out.println();
        escribirLento(AMARILLO + "  23 personajes. 9 preguntas. Un solo secreto." + RESET);
        escribirLento(AMARILLO + "  ¿Podés descubrirlo antes que la máquina?" + RESET);
        esperarEnter(scanner);
    }

    public static void cuentaRegresiva() {
        System.out.println();
        for (int i = 3; i >= 1; i--) {
            System.out.println(NEGRITA + AMARILLO + "   " + i + "..." + RESET);
            dormir(500);
        }
        escribirLento(VERDE + NEGRITA + "   ¡QUE EMPIECE LA PARTIDA!" + RESET);
        dormir(600);
    }

    public static void victoria(String nombreGanador, int turnos) {
        System.out.print(CAMPANA);
        System.out.println();
        String borde = "★".repeat(ANCHO_CAJA - 2);
        System.out.println(VERDE + NEGRITA + "╔" + borde + "╗" + RESET);
        System.out.println(VERDE + NEGRITA + "║" + centrar("¡" + nombreGanador.toUpperCase() + " GANÓ LA PARTIDA!", ANCHO_CAJA - 2) + "║" + RESET);
        System.out.println(VERDE + NEGRITA + "║" + centrar("en " + turnos + " turnos", ANCHO_CAJA - 2) + "║" + RESET);
        System.out.println(VERDE + NEGRITA + "╚" + borde + "╝" + RESET);
        dormir(800);
    }

    public static void marcador(Map<String, Integer> victoriasPorJugador) {
        System.out.println();
        String borde = "═".repeat(ANCHO_CAJA - 2);
        System.out.println(MAGENTA + "╔" + borde + "╗" + RESET);
        System.out.println(MAGENTA + "║" + RESET + NEGRITA + centrar("★  SALÓN DE LA FAMA  ★", ANCHO_CAJA - 2) + RESET + MAGENTA + "║" + RESET);
        System.out.println(MAGENTA + "╠" + borde + "╣" + RESET);
        if (victoriasPorJugador.isEmpty()) {
            System.out.println(MAGENTA + "║" + RESET + centrar("(todavía no hay partidas ganadas)", ANCHO_CAJA - 2) + MAGENTA + "║" + RESET);
        }
        for (Map.Entry<String, Integer> entrada : victoriasPorJugador.entrySet()) {
            String barra = "█".repeat(Math.min(entrada.getValue(), 20));
            String linea = String.format(" %-18s %3d  %s", entrada.getKey(), entrada.getValue(), AMARILLO + barra + RESET);
            System.out.println(MAGENTA + "║" + RESET + rellenar(linea, ANCHO_CAJA - 2, longitudVisible(linea)) + MAGENTA + "║" + RESET);
        }
        System.out.println(MAGENTA + "╚" + borde + "╝" + RESET);
    }

    public static void despedida() {
        System.out.println();
        escribirLento(TENUE + "  Gracias por jugar. Hasta la próxima." + RESET);
        System.out.println();
    }

    // =====================================================================
    // Turnos y eventos de partida
    // =====================================================================

    public static void encabezadoTurno(String nombreJugador, int numeroTurno) {
        limpiarPantalla();
        String borde = "═".repeat(ANCHO_CAJA - 2);
        System.out.println(CYAN + NEGRITA + "╔" + borde + "╗" + RESET);
        System.out.println(CYAN + NEGRITA + "║" + centrar("TURNO " + numeroTurno + "  ·  " + nombreJugador.toUpperCase(), ANCHO_CAJA - 2) + "║" + RESET);
        System.out.println(CYAN + NEGRITA + "╚" + borde + "╝" + RESET);
    }

    public static void historialReciente(List<String> lineas) {
        if (lineas.isEmpty()) {
            return;
        }
        System.out.println(TENUE + "  Últimas jugadas:" + RESET);
        for (String linea : lineas) {
            System.out.println(TENUE + "   · " + linea + RESET);
        }
        System.out.println();
    }

    public static void pensando(String nombreJugador) {
        cargando(nombreJugador + " está pensando", 3);
    }

    public static void pregunta(String preguntador, String respondedor, Pregunta<?> pregunta) {
        escribirLento(AMARILLO + NEGRITA + preguntador + RESET + " le pregunta a " + NEGRITA + respondedor + RESET
                + ": " + BLANCO + pregunta + RESET);
    }

    public static void respuesta(String respondedor, boolean respuesta) {
        dormir(400);
        String texto = respuesta
                ? VERDE + NEGRITA + "  ✔  SÍ  " + RESET
                : ROJO + NEGRITA + "  ✘  NO  " + RESET;
        System.out.println(NEGRITA + respondedor + RESET + " responde: " + texto);
    }

    public static void adivinanza(String adivinador, String rival, Personaje personajeAdivinado) {
        escribirLento(AMARILLO + NEGRITA + adivinador + RESET + " arriesga: ¿" + rival + " es "
                + BLANCO + NEGRITA + personajeAdivinado.getNombre() + RESET + "?");
        cargando("  Verificando", 3);
    }

    public static void aciertoAdivinanza(Personaje personajeAdivinado) {
        System.out.println(VERDE + NEGRITA + "  ✔ ¡CORRECTO! Era " + personajeAdivinado.getNombre() + "." + RESET);
    }

    public static void falloAdivinanza(String rival, Personaje personajeAdivinado) {
        System.out.println(ROJO + NEGRITA + "  ✘ No. " + RESET + ROJO + rival + " no es "
                + personajeAdivinado.getNombre() + ". Queda descartado." + RESET);
    }

    /** Barra de progreso de candidatos restantes sobre el total del catálogo. */
    public static void candidatosRestantes(String nombreJugador, int restantes, int total) {
        int llenos = (int) Math.round((double) restantes / total * ANCHO_BARRA);
        String barra = VERDE + "█".repeat(llenos) + RESET + TENUE + "░".repeat(ANCHO_BARRA - llenos) + RESET;
        System.out.println();
        System.out.println("  " + NEGRITA + nombreJugador + RESET + " · candidatos: [" + barra + "] "
                + NEGRITA + restantes + RESET + "/" + total);
    }

    // =====================================================================
    // Tablero de cartas
    // =====================================================================

    /**
     * Muestra los personajes como cartas, 6 por fila, numeradas con la
     * opción a ingresar (1..n) y el id real del personaje. Cada carta
     * resume los cuatro atributos y pinta el color de pelo.
     */
    public static void tablero(List<Personaje> personajes) {
        System.out.println();
        for (int desde = 0; desde < personajes.size(); desde += COLUMNAS) {
            int hasta = Math.min(desde + COLUMNAS, personajes.size());
            List<Personaje> fila = personajes.subList(desde, hasta);

            StringBuilder arriba = new StringBuilder();
            StringBuilder l1 = new StringBuilder();
            StringBuilder l2 = new StringBuilder();
            StringBuilder l3 = new StringBuilder();
            StringBuilder abajo = new StringBuilder();

            for (int i = 0; i < fila.size(); i++) {
                Personaje p = fila.get(i);
                int opcion = desde + i + 1;

                arriba.append("┌").append("─".repeat(ANCHO_CARTA)).append("┐ ");
                l1.append("│").append(rellenar(lineaCabecera(opcion, p), ANCHO_CARTA, longitudVisible(lineaCabecera(opcion, p)))).append("│ ");
                l2.append("│").append(rellenar(lineaCuerpo(p), ANCHO_CARTA)).append("│ ");
                l3.append("│").append(rellenar(lineaPelo(p), ANCHO_CARTA, longitudVisible(lineaPelo(p)))).append("│ ");
                abajo.append("└").append("─".repeat(ANCHO_CARTA)).append("┘ ");
            }
            System.out.println(TENUE + arriba + RESET);
            System.out.println(l1);
            System.out.println(l2);
            System.out.println(l3);
            System.out.println(TENUE + abajo + RESET);
        }
    }

    private static String lineaCabecera(int opcion, Personaje p) {
        String simboloGenero = (p.getGenero() == Genero.MASCULINO) ? "M" : "F";
        return AMARILLO + NEGRITA + String.format("[%2d]", opcion) + RESET + " #" + p.getId() + "  " + simboloGenero;
    }

    private static String lineaCuerpo(Personaje p) {
        String calvicie = p.esCalvo() ? "calvo" : "c/pelo";
        String lentes = p.usaLentes() ? "lentes" : "s/lent";
        return String.format("%-6s %-6s", calvicie, lentes);
    }

    private static String lineaPelo(Personaje p) {
        ColorPelo color = p.getColorPelo();
        String ansi;
        switch (color) {
            case COLORADO -> ansi = ROJO;
            case AMARILLO -> ansi = AMARILLO;
            default -> ansi = BLANCO;
        }
        return ansi + "pelo " + color.name().toLowerCase() + RESET;
    }

    /** Lista de preguntas disponibles, numeradas, estilo menú. */
    public static void listaPreguntas(List<Pregunta<?>> preguntas) {
        System.out.println();
        for (int i = 0; i < preguntas.size(); i++) {
            System.out.println("  " + AMARILLO + NEGRITA + String.format("[%d]", i + 1) + RESET + " " + preguntas.get(i));
        }
    }

    // =====================================================================
    // Helpers privados
    // =====================================================================

    private static void dormir(int ms) {
        if (!animaciones) {
            return;
        }
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static String centrar(String texto, int ancho) {
        int visible = longitudVisible(texto);
        if (visible >= ancho) {
            return texto;
        }
        int izquierda = (ancho - visible) / 2;
        int derecha = ancho - visible - izquierda;
        return " ".repeat(izquierda) + texto + " ".repeat(derecha);
    }

    private static String rellenar(String texto, int ancho) {
        return rellenar(texto, ancho, texto.length());
    }

    /** Rellena con espacios usando la longitud visible (sin contar códigos ANSI). */
    private static String rellenar(String texto, int ancho, int longitudVisible) {
        if (longitudVisible >= ancho) {
            return texto;
        }
        return texto + " ".repeat(ancho - longitudVisible);
    }

    private static int longitudVisible(String texto) {
        return texto.replaceAll("\u001B\\[[0-9;]*m", "").length();
    }
}