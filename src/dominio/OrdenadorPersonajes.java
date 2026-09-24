package dominio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public final class OrdenadorPersonajes {

    private OrdenadorPersonajes() {
    }

    public static List<Personaje> ordenarPorId(List<Personaje> personajes) {
        return mergeSort(personajes, Comparator.comparingInt(Personaje::getId));
    }

    public static List<Personaje> mergeSort(List<Personaje> personajes, Comparator<Personaje> criterio) {
        validar(personajes, criterio);

        Personaje[] datos = personajes.toArray(new Personaje[0]);
        Personaje[] auxiliar = new Personaje[datos.length];
        ordenar(datos, auxiliar, 0, datos.length - 1, criterio);

        List<Personaje> resultado = new ArrayList<>(datos.length);
        for (Personaje p : datos) {
            resultado.add(p);
        }
        return resultado;
    }

    private static void validar(List<Personaje> personajes, Comparator<Personaje> criterio) {
        if (personajes == null) {
            throw new IllegalArgumentException("La lista de personajes no puede ser null");
        }
        if (criterio == null) {
            throw new IllegalArgumentException("El criterio de ordenamiento no puede ser null");
        }
        if (personajes.contains(null)) {
            throw new IllegalArgumentException("La lista no puede contener personajes null");
        }
    }

    // DIVIDE: parte el rango [desde, hasta] en dos mitades.
    // CONQUISTA: ordena cada mitad recursivamente (caso base: rango de 0 o 1 elementos).
    // COMBINA: mezcla las dos mitades ya ordenadas.


    private static void ordenar(Personaje[] datos, Personaje[] auxiliar, int desde, int hasta,
                                Comparator<Personaje> criterio) {
        if (desde >= hasta) {
            return;
        }
        int medio = desde + (hasta - desde) / 2;
        ordenar(datos, auxiliar, desde, medio, criterio);
        ordenar(datos, auxiliar, medio + 1, hasta, criterio);
        mezclar(datos, auxiliar, desde, medio, hasta, criterio);
    }

    private static void mezclar(Personaje[] datos, Personaje[] auxiliar, int desde, int medio, int hasta,
                                Comparator<Personaje> criterio) {
        System.arraycopy(datos, desde, auxiliar, desde, hasta - desde + 1);

        int izquierda = desde;
        int derecha = medio + 1;

        for (int destino = desde; destino <= hasta; destino++) {
            if (izquierda > medio) {
                datos[destino] = auxiliar[derecha++];
            } else if (derecha > hasta) {
                datos[destino] = auxiliar[izquierda++];
            } else if (criterio.compare(auxiliar[derecha], auxiliar[izquierda]) < 0) {
                datos[destino] = auxiliar[derecha++];   // estrictamente menor: gana la derecha
            } else {
                datos[destino] = auxiliar[izquierda++]; // empate: gana la izquierda (estabilidad)
            }
        }
    }
}