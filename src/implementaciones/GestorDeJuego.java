package implementaciones;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class GestorDeJuego {

    private List<PersonajeJuego> personajes;
    private final Random rnd;
    private boolean sorteado;

    public GestorDeJuego() {
        this.rnd = new Random();
        this.sorteado = false;
        crearPersonajes();
    }

    private void crearPersonajes() {
        this.personajes = new ArrayList<>();

        String[] nombres = {"Mateo", "Valentina", "Ignacio", "Camila", "Tomás", "Sofía", "Lucas"};

        for (int i = 0; i < nombres.length; i++) {
            personajes.add(new PersonajeJuego(i + 1, nombres[i]));
        }
    }

    public void sortearElegido() {
        int indice = rnd.nextInt(personajes.size());
        PersonajeJuego elegido = personajes.get(indice);
        elegido.elegir();
        this.sorteado = true;
    }

    public String adivinar(int numeroId) {
        if (!sorteado) {
            return "todavía no se sorteó al elegido, esperá el inicio del juego";
        }

        PersonajeJuego encontrado = null;

        for (PersonajeJuego p : personajes) {
            if (p.getId() == numeroId) {
                encontrado = p;
                break;
            }
        }

        if (encontrado == null) {
            return "ese id no existe, probá del 1 al 7";
        }

        if (encontrado.esElegido()) {
            return "¡Acertaste! Era: " + encontrado;
        } else {
            return encontrado.getNombre() + ": no es el elegido";
        }
    }

    public String comodin() {
        if (!sorteado) {
            return "todavía no se sorteó al elegido, esperá el inicio del juego";
        }

        int mitad = (personajes.size() + 1) / 2;

        int idElegido = 0;
        for (PersonajeJuego p : personajes) {
            if (p.esElegido()) {
                idElegido = p.getId();
                break;
            }
        }

        if (idElegido <= mitad) {
            return "El elegido está entre el ID 1 y el ID " + mitad;
        } else {
            return "El elegido está entre el ID " + (mitad + 1) + " y el ID " + personajes.size();
        }
    }

    public boolean esElElegido(int id) {
        for (PersonajeJuego p : personajes) {
            if (p.getId() == id) {
                return p.esElegido();
            }
        }
        return false;
    }
}