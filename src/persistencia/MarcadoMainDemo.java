package persistencia;

import java.util.List;

public class MarcadoMainDemo {
    public static void main(String[] args){
        RepositorioMarcador repositorio = new RepositorioMarcadorArchivo("marcador.txt");

        repositorio.registrarVictoria("marcos");

        List<RegistroMarcador> todos = repositorio.obtenerTodos();
        System.out.println("Marcador actual:");
        for (RegistroMarcador registro:todos){
            System.out.println(registro.nombreJugador()+";"+registro.partidasGanadas());
        }

    }
}
