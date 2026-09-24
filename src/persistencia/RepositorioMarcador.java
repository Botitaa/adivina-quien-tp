package persistencia;

import java.util.List;

public interface RepositorioMarcador {
    void registrarVictoria(String nombreJugador);

    List<RegistroMarcador> obtenerTodos();

}
