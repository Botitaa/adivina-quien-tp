package dominio;

import valores.Atributo;
import valores.ColorPelo;
import valores.Genero;

import java.util.ArrayList;
import java.util.List;

public class Pregunta<T> {


    private final Atributo atributo;
    private final T valor;

    public Pregunta(Atributo atributo, T valor) {

        this.atributo = atributo;
        this.valor = valor;

    }

    public boolean evaluar(PersonajeJuego personajeJuego){


        switch (this.atributo){
            case GENERO:
                return this.valor.equals(personajeJuego.getGenero());
            case LENTES:
                return (Boolean) this.valor == personajeJuego.usaLentes();
            case COLOR_PELO:
                return this.valor.equals(personajeJuego.getColorPelo());
            case CALVICIE:
                return (Boolean) this.valor == personajeJuego.esCalvo();
            default:
                throw new IllegalStateException("Atributo no soportado: " + this.atributo);
        }



    }

    public static List<Pregunta> generarTodas() {
        List<Pregunta> preguntas = new ArrayList<>();

        for (Genero g : Genero.values()) {
            preguntas.add(new Pregunta<>(Atributo.GENERO, g));
        }
        for (ColorPelo c : ColorPelo.values()) {
            preguntas.add(new Pregunta<>(Atributo.COLOR_PELO, c));
        }
        for (Boolean b : List.of(true, false)) {
            preguntas.add(new Pregunta<>(Atributo.CALVICIE, b));
        }
        for (Boolean b : List.of(true, false)) {
            preguntas.add(new Pregunta<>(Atributo.LENTES, b));
        }

        return preguntas;
    }

    @Override
    public String toString() {
        return "Pregunta{" + atributo + "=" + valor + "}";
    }
}
