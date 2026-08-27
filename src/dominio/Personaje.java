package dominio;

import valores.Genero;
import valores.ColorPelo;

public interface Personaje {
    int getId();
    String getNombre();
    Genero getGenero();
    boolean esCalvo();
    boolean usaLentes();
    ColorPelo getColorPelo();
}