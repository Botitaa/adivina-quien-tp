package gestor;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorDeJuego gestor = new GestorDeJuego();

        System.out.println("=================================");
        System.out.println(" ¡Bienvenido al juego de Adivinar el Personaje!");
        System.out.println("=================================");
        System.out.println("Hay 7 personajes (ID 1 al 7). La máquina eligió uno en secreto.\n");

        gestor.sortearElegido();

        boolean acerto = false;
        int intentos = 0;
        int opcion;

        do {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1) Adivinar un personaje por ID");
            System.out.println("2) Pedir comodín");
            System.out.println("0) Salir");
            System.out.print("Elegí una opción: ");

            opcion = leerEntero(scanner, -1);

            switch (opcion) {
                case 1:
                    System.out.print("Ingresá el ID (1-7): ");
                    int id = leerEntero(scanner, -1);
                    if (id == -1) {
                        System.out.println("Eso no es un número válido.");
                        break;
                    }

                    intentos++;
                    String resultado = gestor.adivinar(id);
                    System.out.println(resultado);

                    if (gestor.esElElegido(id)) {
                        acerto = true;
                        System.out.println("Lo lograste en " + intentos + " intento(s).");
                    }
                    break;

                case 2:
                    System.out.println(gestor.comodin());
                    break;

                case 0:
                    System.out.println("¡Gracias por jugar!");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0 && !acerto);

        scanner.close();
    }

    private static int leerEntero(Scanner scanner, int valorSiError) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return valorSiError;
        }
    }
}