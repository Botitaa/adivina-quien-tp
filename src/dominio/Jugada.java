package dominio;


public class Jugada {

    private final Pregunta<?> pregunta;
    private final TipoJugada tipoJugada;
    private final Personaje personajeAdivinado;

    public enum TipoJugada {
        ADIVINANZA,
        PREGUNTA
    }



    private Jugada(Pregunta<?> pregunta, TipoJugada tipoJugada, Personaje personajeAdivinado) {

    this.pregunta = pregunta;
    this.tipoJugada = tipoJugada;
    this.personajeAdivinado = personajeAdivinado;

    }

    public static Jugada dePregunta(Pregunta<?> pregunta){

        return new Jugada(pregunta, TipoJugada.PREGUNTA,null);
    }

    public static Jugada deAdivinanza(Personaje personajeAdivinado){

        return new Jugada(null, TipoJugada.ADIVINANZA, personajeAdivinado);
    }

    public Pregunta<?> getPregunta() {
        return pregunta;
    }

    public TipoJugada getTipoJugada() {
        return tipoJugada;
    }

    public Personaje getPersonajeAdivinado() {
        return personajeAdivinado;
    }


    @Override
    public String toString() {
        return switch (tipoJugada) {
            case PREGUNTA -> "Jugada[PREGUNTA] " + pregunta;
            case ADIVINANZA -> "Jugada[ADIVINANZA] personaje=" + personajeAdivinado;
        };
    }
}
