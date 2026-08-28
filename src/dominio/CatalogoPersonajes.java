package dominio;

import valores.ColorPelo;
import valores.Genero;

import java.util.ArrayList;
import java.util.List;

public class CatalogoPersonajes {

    private static final int CANTIDAD_PERSONAJES = 23;

    private CatalogoPersonajes() {


    }

    public static List<Personaje> generar() {

        List<Personaje> personajes = new ArrayList<>();
        int id = 1;

        for (Genero genero: Genero.values()){
            for(ColorPelo colorPelo: ColorPelo.values()){

                for (boolean esCalvo: new boolean[]{true, false}){
                    for (boolean usaLentes: new boolean[]{true, false}){

                        if (id<=CANTIDAD_PERSONAJES) {
                            personajes.add(new PersonajeJuego(id,"Personaje: "+id,genero, esCalvo, usaLentes, colorPelo));
                        }

                        id++;

                    }

                }
            }
        }

        return personajes;
    }
}
