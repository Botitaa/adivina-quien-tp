package actores;

import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;
import java.util.List;
import java.util.Scanner;

public class JugadorHumano implements Jugador {
    private final String nombre;
    private final Scanner scanner;
    private  Personaje personajeSecreto;

    public JugadorHumano(String nombre, Scanner scanner) {
        this.nombre = nombre;
        this.scanner = scanner;
    }

    @Override
    public void elegirPersonajeSecreto(List<Personaje> personajesDisponible) {
        System.out.println(nombre + ", elegi tu personaje secreto:");
        for (int i = 0; i<personajesDisponible.size();i++){
            System.out.println((i+1)+". "+ personajesDisponible.get(i).getNombre());
        }

        int opcion = leerOpcion(personajesDisponible.size());
        asignarSecreto(personajesDisponible.get(opcion-1));
    }
    private void asignarSecreto(Personaje personaje){
        if (personaje == null){
            throw new IllegalArgumentException("El personaje secreto no puede ser null");
        }
        if (this.personajeSecreto != null){
            throw new IllegalStateException("El personaje secterto de " + nombre + "ya fue asignado, no puede reasignarse.");
        }
        this.personajeSecreto = personaje;
    }

    @Override
    public Jugada decidirJugada(List<Personaje> candidatos, Historial historial) {
        throw new UnsupportedOperationException("pdte d implementar");//pendiente de implementar
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean esMiPersonajeSecreto(Personaje personaje) {
        if (personajeSecreto == null){
            throw new IllegalStateException(nombre + " todavia no eligio su personaje secreto.");
        }
        if(personaje == null){
            throw new IllegalArgumentException("El personaje a comparar no puede ser null");
        }
        return personajeSecreto.getId() == personaje.getId();
    }

    @Override
    public boolean responder(Pregunta<?> pregunta) {
        if (personajeSecreto == null){
            throw new IllegalStateException(nombre + " todavia no eligio su personaje secreto.");
        }
        return pregunta.evaluar(personajeSecreto);
    }
    private int leerOpcion(int cantidad){
        while (true){
            System.out.println("Opcion: ");
            String entrada = scanner.nextLine();
            try {
                int opcion = Integer.parseInt(entrada.trim());
                if(opcion >= 1 && opcion <= cantidad) {
                    return opcion;
                }
                System.out.println("Ingresa un numero entre 1 y " + cantidad);
        }   catch (NumberFormatException e) {
                System.out.println("Eso no es un numero valido.");
            }
        }
    }
}
