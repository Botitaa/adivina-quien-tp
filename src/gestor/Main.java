package gestor;

import actores.Jugador;
import actores.JugadorHumano;
import persistencia.RepositorioMarcador;
import persistencia.RepositorioMarcadorArchivo;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Jugador jugadorA = new JugadorHumano("Agustín", scanner);
        Jugador jugadorB = new JugadorHumano("Marcos", scanner);

        RepositorioMarcador repositorioMarcador = new RepositorioMarcadorArchivo("marcador.txt");

        GestorDeJuego gestorDeJuego = new GestorDeJuego(jugadorA, jugadorB, repositorioMarcador);
        gestorDeJuego.iniciarPartida();
    }
}