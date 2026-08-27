package dominio;

public interface Personaje {
    int getId(); //devuelve el id
    String getNombre(); // devuelve el nombre del personaje
    boolean esElegido(); // devuelve si es o no elegido
    void elegir(); //setea el personaje a adivinar
    String toString(); // documenta que toda implementación debe dar una representación legible
}