package persistencia;

import java.util.List;
import java.util.Optional;

public interface RepositorioMarcador {
    Optional<RegistroMarcador> buscarPorNombre(String nombreJugador);

    void registrarVictoria(String nombreJugador);

    List<RegistroMarcador> obtenerTodos();

}
