package dominio;

import valores.ColorPelo;
import valores.Genero;

public class PersonajeJuego implements Personaje{

    private final int id;
    private final String nombre;
    private final Genero genero;
    private final boolean esCalvo;
    private final boolean usaLentes;
    private final ColorPelo colorPelo;

    public PersonajeJuego(int id, String nombre, Genero genero, boolean esCalvo, boolean usaLentes, ColorPelo colorPelo) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.esCalvo = esCalvo;
        this.usaLentes = usaLentes;
        this.colorPelo = colorPelo;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getNombre() {
        return this.nombre;
    }

    @Override
    public Genero getGenero() {
        return this.genero;
    }

    @Override
    public boolean esCalvo() {
        return this.esCalvo;
    }

    @Override
    public boolean usaLentes() {
        return this.usaLentes;
    }

    @Override
    public ColorPelo getColorPelo() {
        return this.colorPelo;
    }
}