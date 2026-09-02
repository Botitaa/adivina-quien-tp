package actores;

import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;
import java.util.ArrayList;
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
            System.out.println((i+1)+". "+ describir(personajesDisponible.get(i)));
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
        System.out.println("\n" + nombre + ", te quedan " + candidatos.size() + " candidatos posibles.");
        System.out.println(nombre + ", ¿qué querés hacer?");
        System.out.println("1. Preguntar");
        System.out.println("2. Adivinar");
        int opcion = leerOpcion(2);

        if (opcion == 1) {
            Pregunta<?> pregunta = elegirPregunta(historial);
            return Jugada.dePregunta(pregunta);
        } else {
            Personaje personajeElegido = elegirPersonajeParaAdivinar(candidatos);
            return Jugada.deAdivinanza(personajeElegido);
        }
    }

    private Pregunta<?> elegirPregunta(Historial historial) {
        List<Pregunta<?>> disponibles = new ArrayList<>();
        for (Pregunta<?> pregunta : Pregunta.generarTodas()) {
            if (!historial.yaSePregunto(pregunta, this)) {
                disponibles.add(pregunta);
            }
        }

        System.out.println(nombre + ", elegí tu pregunta:");
        for (int i = 0; i < disponibles.size(); i++) {
            System.out.println((i + 1) + ". " + disponibles.get(i));
        }

        int opcion = leerOpcion(disponibles.size());
        return disponibles.get(opcion - 1);
    }

    private Personaje elegirPersonajeParaAdivinar(List<Personaje> candidatos) {
        System.out.println(nombre + ", ¿quién creés que es el personaje secreto?");
        for (int i = 0; i < candidatos.size(); i++) {
            System.out.println((i + 1) + ". " + describir(candidatos.get(i)));
        }

        int opcion = leerOpcion(candidatos.size());
        return candidatos.get(opcion - 1);
    }

    private String describir(Personaje personaje) {
        String calvicie = personaje.esCalvo() ? "calvo" : "no calvo";
        String lentes = personaje.usaLentes() ? "usa lentes" : "no usa lentes";
        return personaje.getNombre() + " [" + personaje.getGenero() + ", " + calvicie + ", "
                + lentes + ", pelo " + personaje.getColorPelo() + "]";
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