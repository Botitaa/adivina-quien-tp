package dominio;

import valores.Atributo;
import valores.ColorPelo;
import valores.Genero;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pregunta<T> {


    private final Atributo atributo;
    private final T valor;

    public Pregunta(Atributo atributo, T valor) {

        this.atributo = atributo;
        this.valor = valor;

    }

    public boolean evaluar(Personaje personaje){

        switch (this.atributo){
            case GENERO:
                return this.valor.equals(personaje.getGenero());
            case LENTES:
                return (Boolean) this.valor == personaje.usaLentes();
            case COLOR_PELO:
                return this.valor.equals(personaje.getColorPelo());
            case CALVICIE:
                return (Boolean) this.valor == personaje.esCalvo();
            default:
                throw new IllegalStateException("Atributo no soportado: " + this.atributo);
        }
    }

    public static List<Pregunta<?>> generarTodas() {
        List<Pregunta<?>> preguntas = new ArrayList<>();

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

    public List<Personaje> filtrar(List<Personaje> candidatos, boolean respuesta){


        List<Personaje> filtrado = new ArrayList<>();

        for (Personaje p: candidatos){
            if(evaluar(p) == respuesta){

                filtrado.add(p);
            }
        }

        return filtrado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pregunta<?> otra)) return false;
        return atributo == otra.atributo && valor.equals(otra.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(atributo, valor);
    }

    @Override
    public String toString() {
        switch (this.atributo) {
            case GENERO:
                return "¿Es de género " + valor + "?";
            case COLOR_PELO:
                return "¿Tiene el pelo " + valor + "?";
            case CALVICIE:
                return ((Boolean) valor) ? "¿Es calvo?" : "¿No es calvo?";
            case LENTES:
                return ((Boolean) valor) ? "¿Usa lentes?" : "¿No usa lentes?";
            default:
                throw new IllegalStateException("Atributo no soportado: " + this.atributo);
        }
    }
}