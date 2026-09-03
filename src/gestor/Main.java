package gestor;

import actores.IAAsertiva;
import actores.IABasica;
import actores.Jugador;
import actores.JugadorHumano;
import persistencia.RegistroMarcador;
import persistencia.RepositorioMarcador;
import persistencia.RepositorioMarcadorArchivo;
import presentacion.Consola;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final int MODO_HUMANO_VS_MAQUINA = 1;
    private static final int MODO_MAQUINA_VS_MAQUINA = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RepositorioMarcador repositorioMarcador = new RepositorioMarcadorArchivo("marcador.txt");

        Consola.pantallaTitulo(scanner);

        boolean seguirJugando = true;
        while (seguirJugando) {
            jugarUnaPartida(scanner, repositorioMarcador);
            mostrarMarcador(repositorioMarcador);
            seguirJugando = preguntarRevancha(scanner);
        }

        Consola.despedida();
    }

    private static void jugarUnaPartida(Scanner scanner, RepositorioMarcador repositorioMarcador) {
        Consola.limpiarPantalla();
        int modo = elegirModo(scanner);

        Jugador jugadorA;
        Jugador jugadorB;
        boolean modoEspectador;

        if (modo == MODO_HUMANO_VS_MAQUINA) {
            Consola.prompt("¿Cómo te llamás?");
            String nombreHumano = scanner.nextLine().trim();
            if (nombreHumano.isEmpty()) {
                nombreHumano = "Jugador";
            }
            jugadorA = new JugadorHumano(nombreHumano, scanner);
            jugadorB = elegirMaquina(scanner);
            modoEspectador = false;
        } else {
            jugadorA = new IABasica("Máquina Básica");
            jugadorB = new IAAsertiva("Máquina Asertiva");
            modoEspectador = true;
            Consola.info("Modo espectador: sentate y mirá cómo se pelean las dos IA.");
        }

        GestorDeJuego gestorDeJuego = new GestorDeJuego(jugadorA, jugadorB, repositorioMarcador, scanner, modoEspectador);
        gestorDeJuego.iniciarPartida();
    }

    private static void mostrarMarcador(RepositorioMarcador repositorioMarcador) {
        Map<String, Integer> victoriasPorJugador = new LinkedHashMap<>();
        List<RegistroMarcador> registros = repositorioMarcador.obtenerTodos();
        for (RegistroMarcador registro : registros) {
            victoriasPorJugador.put(registro.nombreJugador(), registro.partidasGanadas());
        }
        Consola.marcador(victoriasPorJugador);
    }

    private static boolean preguntarRevancha(Scanner scanner) {
        while (true) {
            Consola.prompt("¿Otra partida? (s/n)");
            String entrada = scanner.nextLine().trim().toLowerCase();
            if (entrada.equals("s") || entrada.equals("si") || entrada.equals("sí")) {
                return true;
            }
            if (entrada.equals("n") || entrada.equals("no")) {
                return false;
            }
            Consola.error("Respondé s o n.");
        }
    }

    private static Jugador elegirMaquina(Scanner scanner) {
        Consola.menu("¿CONTRA QUIÉN JUGÁS?", List.of(
                "Máquina Básica   (pregunta al azar)",
                "Máquina Asertiva (Greedy + D&C, difícil)"));
        int opcion = leerOpcion(scanner, "Rival", 2);
        return (opcion == 1) ? new IABasica("Máquina Básica") : new IAAsertiva("Máquina Asertiva");
    }

    private static int elegirModo(Scanner scanner) {
        Consola.menu("MODO DE JUEGO", List.of(
                "Humano vs Máquina",
                "Máquina vs Máquina (modo espectador)"));
        return leerOpcion(scanner, "Modo", 2);
    }

    private static int leerOpcion(Scanner scanner, String etiqueta, int cantidad) {
        while (true) {
            Consola.prompt(etiqueta + " [1-" + cantidad + "]");
            String entrada = scanner.nextLine();
            try {
                int opcion = Integer.parseInt(entrada.trim());
                if (opcion >= 1 && opcion <= cantidad) {
                    return opcion;
                }
                Consola.error("Ingresá un número entre 1 y " + cantidad + ".");
            } catch (NumberFormatException e) {
                Consola.error("Eso no es un número válido.");
            }
        }
    }
}