package gestor;

import actores.IAAsertiva;
import actores.IABasica;
import actores.Jugador;
import actores.JugadorHumano;
import persistencia.RegistroMarcador;
import persistencia.RepositorioMarcador;
import persistencia.RepositorioMarcadorArchivo;

import java.util.Scanner;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        RepositorioMarcador repositorioMarcador = new RepositorioMarcadorArchivo("marcador.txt");

        int modo = elegirModo(scanner);

        Jugador jugadorA;
        Jugador jugadorB;
        boolean pausarEntreTurnos;

        if (modo == 1) {
            System.out.println("Ingresa tu nombre: ");
            String nombreHumano = scanner.nextLine();
            jugadorA = new JugadorHumano(nombreHumano,scanner);
            jugadorB = elegirMaquina(scanner);
            pausarEntreTurnos = false;
        } else {
            jugadorA = new IABasica("Maquina Basica");
            jugadorB = new IAAsertiva("Maquina Asertiva");
            pausarEntreTurnos = true;
        }

        GestorDeJuego gestorDeJuego = new GestorDeJuego(jugadorA, jugadorB, repositorioMarcador, pausarEntreTurnos);
        gestorDeJuego.iniciarPartida();

        mostrarMarcador(repositorioMarcador);
    }
    private static void mostrarMarcador(RepositorioMarcador repositorioMarcador){
        System.out.println();
        System.out.println("==== Marcador ====");
        List<RegistroMarcador> registros = repositorioMarcador.obtenerTodos();
        for (RegistroMarcador registro:registros){
            System.out.println(registro.nombreJugador()+ ": " + registro.partidasGanadas()+ " victorias");
        }
    }
    private static Jugador elegirMaquina(Scanner scanner){
        while (true){
            System.out.println("Contra que maquina queres jugar?: ");
            System.out.println("1. Maquina basica");
            System.out.println("2. Maquina Asertiva (dificil)");
            System.out.print("Opcion: ");
            String entrada = scanner.nextLine();
            try{
                int opcion = Integer.parseInt(entrada.trim());
                if (opcion == 1){
                    return new IABasica("Maquina Basica");
                }
                if (opcion == 2){
                    return new IAAsertiva("Maquina Asertiva");
                }
                System.out.println("Ingresa 1 o 2.");
            } catch (NumberFormatException e) {
                System.out.println("No es un numero valido.");
            }
        }
    }
    private static int elegirModo(Scanner scanner){
        while (true){
            System.out.println("Elegi el modo de juego: ");
            System.out.println("1. Humano vs Maquina");
            System.out.println("2. Maquina vs Maquina");
            System.out.print("Opcion: ");
            String entrada = scanner.nextLine();
            try {
                int opcion = Integer.parseInt(entrada.trim());
                if (opcion == 1 || opcion == 2){
                    return opcion;
                }
                System.out.println("Ingresa 1 o 2.");
            } catch (NumberFormatException e) {
                System.out.println("No es un numero valido.");
            }
        }
    }
}