package dominio;

import java.util.List;

public record ProximoTurno(boolean hayGanador, List<Personaje> candidatos) {
}