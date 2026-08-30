package persistencia;

import javax.print.DocFlavor;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioMarcadorArchivo implements RepositorioMarcador {
    private static final String DELIMITADOR = ";";
    private final Path archivo;

    public RepositorioMarcadorArchivo(String rutaArchivo) {
        this.archivo = Path.of(rutaArchivo);
        crearArchivoSiNoExiste();
    }

    private void crearArchivoSiNoExiste() {
        if (!Files.exists(archivo)) {
            try {
                Files.createFile(archivo);
            } catch (IOException e) {
                throw new IllegalStateException("No se pudo crear el archivo de marcador: " + archivo, e);
            }
        }
    }

    @Override
    public Optional<RegistroMarcador> buscarPorNombre(String nombreJugador) {
        List<RegistroMarcador> registros = leerTodos();

        for (RegistroMarcador registro: registros){
            if (registro.nombreJugador().equals(nombreJugador)) {
                return Optional.of(registro);
            }
        }
        return Optional.empty();
    }

    @Override
    public void registrarVictoria(String nombreJugador) {
        List<RegistroMarcador> registros = leerTodos();
        List<RegistroMarcador> actualizados = new ArrayList<>();
        boolean existia = false;

        for (RegistroMarcador registro : registros) {
            if (registro.nombreJugador().equals(nombreJugador)) {
                actualizados.add(new RegistroMarcador(nombreJugador, registro.partidasGanadas() + 1));
                existia = true;
            } else {
                actualizados.add(registro);
            }
        }
        if (!existia) {
            actualizados.add(new RegistroMarcador(nombreJugador, 1));
        }
        escribirTodos(actualizados);
    }

    @Override
    public List<RegistroMarcador> obtenerTodos() {
        return leerTodos();
    }

    private List<RegistroMarcador> leerTodos() {
        List<RegistroMarcador> registros = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(archivo.toFile()))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] partes = linea.split(DELIMITADOR);
                String nombre = partes[0];
                int partidasGanadas = Integer.parseInt(partes[1]);
                registros.add(new RegistroMarcador(nombre, partidasGanadas));
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo de marcador: " + archivo, e);
        }
        return registros;
    }

    private void escribirTodos(List<RegistroMarcador> registros) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo.toFile()))) {
            for (RegistroMarcador registro:registros) {
                escritor.write(registro.nombreJugador() + DELIMITADOR + registro.partidasGanadas());
                escritor.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo escribir el archivo de marcador: " + archivo, e);
        }
    }
}
