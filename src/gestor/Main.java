package gestor;

import dominio.CatalogoPersonajes;
import dominio.Personaje;

import java.util.List;

public class Main {


    public static void main(String[] args) {
        List<Personaje> personajes = CatalogoPersonajes.generar();

        for (Personaje p : personajes) {
            System.out.println(p);
        }
        System.out.println("Total: " + personajes.size());
    }
}