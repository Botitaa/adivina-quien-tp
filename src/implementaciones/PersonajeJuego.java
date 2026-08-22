package implementaciones;

public class PersonajeJuego implements interfaces.Personaje {

    private int id;
    private String nombre;
    private boolean elegido;

    public PersonajeJuego(int id, String nombre){
        this.id = id;
        this.nombre = nombre;
        this.elegido = false;
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
    public boolean esElegido() {
        return this.elegido;
    }

    @Override
    public void elegir() {
        this.elegido = true;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + nombre + " | Elegido: " + elegido;
    }
}