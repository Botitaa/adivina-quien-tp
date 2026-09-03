package actores;

import dominio.Jugada;
import dominio.Personaje;
import dominio.Pregunta;
import presentacion.Consola;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JugadorHumano implements Jugador {
    private final String nombre;
    private final Scanner scanner;
    private Personaje personajeSecreto;

    public JugadorHumano(String nombre, Scanner scanner) {
        this.nombre = nombre;
        this.scanner = scanner;
    }

    @Override
    public void elegirPersonajeSecreto(List<Personaje> personajesDisponible) {
        Consola.limpiarPantalla();
        Consola.caja("ELEGÍ TU PERSONAJE SECRETO", List.of(
                nombre + ", este es tu tablero.",
                "La máquina va a intentar descubrir cuál elegiste.",
                "M/F = género · calvo/c-pelo · lentes/s-lent"));
        Consola.tablero(personajesDisponible);

        int opcion = leerOpcion("Tu personaje", personajesDisponible.size());
        asignarSecreto(personajesDisponible.get(opcion - 1));
        Consola.info("Listo. Tu secreto es " + personajeSecreto.getNombre() + ". Que nadie lo sepa.");
    }

    private void asignarSecreto(Personaje personaje) {
        if (personaje == null) {
            throw new IllegalArgumentException("El personaje secreto no puede ser null");
        }
        if (this.personajeSecreto != null) {
            throw new IllegalStateException("El personaje secreto de " + nombre + " ya fue asignado, no puede reasignarse.");
        }
        this.personajeSecreto = personaje;
    }

    @Override
    public Jugada decidirJugada(List<Personaje> candidatos, Historial historial) {
        Consola.tablero(candidatos);
        Consola.menu(nombre + ", ¿qué hacés? (" + candidatos.size() + " candidatos)", List.of(
                "Hacer una pregunta",
                "Arriesgar una adivinanza"));
        int opcion = leerOpcion("Acción", 2);

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

        Consola.listaPreguntas(disponibles);
        int opcion = leerOpcion("Pregunta", disponibles.size());
        return disponibles.get(opcion - 1);
    }

    private Personaje elegirPersonajeParaAdivinar(List<Personaje> candidatos) {
        Consola.info("Elegí el número de la carta que creés que es el secreto.");
        int opcion = leerOpcion("Adivinanza", candidatos.size());
        return candidatos.get(opcion - 1);
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean esMiPersonajeSecreto(Personaje personaje) {
        if (personajeSecreto == null) {
            throw new IllegalStateException(nombre + " todavía no eligió su personaje secreto.");
        }
        if (personaje == null) {
            throw new IllegalArgumentException("El personaje a comparar no puede ser null");
        }
        return personajeSecreto.getId() == personaje.getId();
    }

    @Override
    public boolean responder(Pregunta<?> pregunta) {
        if (personajeSecreto == null) {
            throw new IllegalStateException(nombre + " todavía no eligió su personaje secreto.");
        }
        return pregunta.evaluar(personajeSecreto);
    }

    private int leerOpcion(String etiqueta, int cantidad) {
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