package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;


public final class ConstantesGUI {

    private ConstantesGUI() {
        // clase de constantes
    }

    // =====================================================================
    // Ventana
    // =====================================================================
    public static final String TITULO_VENTANA = "Adivina Quién";
    public static final int ANCHO_VENTANA = 1100;
    public static final int ALTO_VENTANA = 1000;
    public static final int ANCHO_MINIMO_VENTANA = 1000;
    public static final int ALTO_MINIMO_VENTANA = 700;

    // =====================================================================
    // Tiempos (milisegundos)
    // =====================================================================
    public static final int MS_TURNO_MAQUINA = 1200;
    public static final int MS_PAUSA_ESPECTADOR = 1500;
    public static final int MS_ANTES_DE_RESULTADO = 1200;

    // =====================================================================
    // Botones
    // =====================================================================
    public static final int ANCHO_BOTON = 200;
    public static final int ALTO_BOTON = 45;
    public static final int ANCHO_BOTON_CHICO = 140;
    public static final int ALTO_BOTON_CHICO = 34;

    // =====================================================================
    // Imágenes (rutas relativas al classpath: /imagenes = carpeta src/imagenes)
    // =====================================================================
    public static final String RUTA_IMAGEN_PORTADA = "/imagenes/portada.png";
    public static final String RUTA_IMAGEN_MODO_VS_MAQUINA = "/imagenes/modo_vs_maquina.png";
    public static final String RUTA_IMAGEN_MODO_ESPECTADOR = "/imagenes/modo_espectador.png";

    public static final int ANCHO_IMAGEN_PORTADA = 800;
    public static final int ALTO_IMAGEN_PORTADA = 500;
    public static final int ANCHO_IMAGEN_MODO = 300;
    public static final int ALTO_IMAGEN_MODO = 300;
    public static final int ESPACIADO_ENTRE_OPCIONES = 60;
    public static final int ANCHO_IMAGEN_SECRETO = 60;
    public static final int ALTO_IMAGEN_SECRETO = 75;
    public static final int ANCHO_IMAGEN_RESULTADO = 130;
    public static final int ALTO_IMAGEN_RESULTADO = 160;

    // =====================================================================
    // Tablero y cartas (8 columnas: 23 personajes = 3 filas)
    // =====================================================================
    public static final int COLUMNAS_TABLERO = 8;
    public static final int ESPACIO_ENTRE_CARTAS = 6;
    public static final int ESPACIO_ENTRE_CARTAS_CHICAS = 3;

    public static final int ANCHO_CARTA_GRANDE = 96;
    public static final int ALTO_CARTA_GRANDE = 124;
    public static final int ANCHO_CARTA_NORMAL = 78;
    public static final int ALTO_CARTA_NORMAL = 100;
    public static final int ANCHO_CARTA_MEDIANA = 50;
    public static final int ALTO_CARTA_MEDIANA = 62;
    public static final int ANCHO_CARTA_CHICA = 32;
    public static final int ALTO_CARTA_CHICA = 40;
    /** Alto que se le resta a la imagen de las cartas con texto, para el número. */
    public static final int ALTO_TEXTO_CARTA = 16;

    public static final int GROSOR_BORDE_CARTA = 1;
    public static final int GROSOR_BORDE_SELECCION = 3;
    public static final int GROSOR_CRUZ_DESCARTE = 3;
    /** Opacidad (0-255) del velo oscuro sobre las cartas descartadas. */
    public static final int ALFA_VELO_DESCARTE = 150;

    // =====================================================================
    // Espaciados y márgenes (en píxeles)
    // =====================================================================
    public static final int ESPACIADO_MEDIO = 10;
    public static final int ESPACIADO_GRANDE = 20;
    public static final int MARGEN_PANEL = 15;

    // =====================================================================
    // Colores generales
    // =====================================================================
    public static final Color COLOR_FONDO = new Color(30, 30, 40);
    public static final Color COLOR_FONDO_PANEL = new Color(45, 45, 60);
    public static final Color COLOR_TEXTO = Color.WHITE;
    public static final Color COLOR_TEXTO_SECUNDARIO = new Color(170, 170, 185);
    public static final Color COLOR_ACENTO = new Color(255, 200, 60);
    public static final Color COLOR_EXITO = new Color(80, 200, 120);
    public static final Color COLOR_ERROR = new Color(230, 80, 80);

    // Botones
    public static final Color COLOR_BOTON = new Color(70, 110, 200);
    public static final Color COLOR_BOTON_PELIGRO = new Color(170, 60, 60);
    public static final Color COLOR_TEXTO_BOTON = Color.BLACK;

    // Cartas
    public static final Color COLOR_CARTA = new Color(240, 240, 235);
    public static final Color COLOR_BORDE_CARTA = new Color(20, 20, 30);
    public static final Color COLOR_VELO_DESCARTE = new Color(20, 20, 30, ALFA_VELO_DESCARTE);
    public static final Color COLOR_CRUZ_DESCARTE = COLOR_ERROR;
    public static final Color COLOR_BORDE_SELECCION = COLOR_ACENTO;
    public static final Color COLOR_BORDE_OBJETIVO = COLOR_EXITO;

    // Dibujo de reemplazo de personajes (mientras no haya imagen)
    public static final Color COLOR_PIEL = new Color(235, 200, 170);
    public static final Color COLOR_ROPA_HOMBRE = new Color(90, 130, 200);
    public static final Color COLOR_ROPA_MUJER = new Color(200, 100, 150);
    public static final Color COLOR_LENTES = new Color(30, 30, 30);
    public static final Color COLOR_PELO_COLORADO = new Color(200, 80, 40);
    public static final Color COLOR_PELO_NEGRO = new Color(25, 25, 25);
    public static final Color COLOR_PELO_AMARILLO = new Color(245, 210, 70);

    // =====================================================================
    // Fuentes (familia lógica "SansSerif": existe en todos los sistemas)
    // =====================================================================
    public static final String FAMILIA_FUENTE = "SansSerif";
    public static final Font FUENTE_TITULO = new Font(FAMILIA_FUENTE, Font.BOLD, 36);
    public static final Font FUENTE_SUBTITULO = new Font(FAMILIA_FUENTE, Font.BOLD, 20);
    public static final Font FUENTE_TEXTO = new Font(FAMILIA_FUENTE, Font.PLAIN, 15);
    public static final Font FUENTE_BOTON = new Font(FAMILIA_FUENTE, Font.BOLD, 15);
    public static final Font FUENTE_CARTA = new Font(FAMILIA_FUENTE, Font.BOLD, 11);
    public static final Font FUENTE_CHICA = new Font(FAMILIA_FUENTE, Font.PLAIN, 12);

    // =====================================================================
    // Nombres de las máquinas
    // =====================================================================
    public static final String NOMBRE_MAQUINA_BASICA = "Máquina Básica";
    public static final String NOMBRE_MAQUINA_ASERTIVA = "Máquina Asertiva";
    public static final String DESCRIPCION_MAQUINA_BASICA = "Pregunta al azar entre las preguntas que sirven. Más fácil.";
    public static final String DESCRIPCION_MAQUINA_ASERTIVA = "Greedy + D&C: elige la pregunta que corta más parejo. Difícil.";

    // =====================================================================
    // Textos: menú
    // =====================================================================
    public static final String TEXTO_TITULO_MENU = "ADIVINA QUIÉN";
    public static final String TEXTO_BTN_JUGAR = "Jugar";
    public static final String TEXTO_BTN_MARCADOR = "Marcador";
    public static final String TEXTO_BTN_SALIR = "Salir";
    public static final String TEXTO_CONFIRMAR_SALIR = "¿Seguro que querés salir del juego?";

    // =====================================================================
    // Textos: selección de modo
    // =====================================================================
    public static final String TEXTO_TITULO_SELECCION_MODO = "ELEGÍ EL MODO DE JUEGO";
    public static final String TEXTO_BTN_VS_MAQUINA = "Jugador vs Máquina";
    public static final String TEXTO_BTN_ESPECTADOR = "Modo espectador";
    public static final String TEXTO_IMAGEN_PENDIENTE = "Imagen";

    // =====================================================================
    // Textos: configuración de la partida
    // =====================================================================
    public static final String TEXTO_TITULO_CONFIGURACION = "CONFIGURÁ LA PARTIDA";
    public static final String TEXTO_LABEL_NOMBRE = "Tu nombre";
    public static final String TEXTO_LABEL_RIVAL = "Tu rival";
    public static final String TEXTO_BTN_CONTINUAR = "Continuar";
    public static final String TEXTO_BTN_VOLVER = "Volver";
    public static final String TEXTO_ERROR_NOMBRE_VACIO = "El nombre no puede estar vacío.";
    public static final String TEXTO_ERROR_NOMBRE_INVALIDO = "El nombre no puede contener ';'.";
    public static final String TEXTO_ERROR_NOMBRE_LARGO = "El nombre puede tener hasta 20 caracteres.";
    public static final String TEXTO_ERROR_NOMBRE_RESERVADO = "Ese nombre es el de una máquina, elegí otro.";
    public static final int LARGO_MAXIMO_NOMBRE = 20;
    public static final String CARACTER_PROHIBIDO_NOMBRE = ";";

    // =====================================================================
    // Textos: elegir personaje secreto
    // =====================================================================
    public static final String TEXTO_TITULO_ELEGIR_SECRETO = "ELEGÍ TU PERSONAJE SECRETO";
    public static final String TEXTO_INSTRUCCION_SECRETO = "Tocá una carta. La máquina va a intentar descubrir cuál elegiste.";
    public static final String TEXTO_SIN_SELECCION = "Todavía no elegiste ningún personaje.";
    public static final String TEXTO_ELEGIDO = "Elegido: ";
    public static final String TEXTO_BTN_AL_AZAR = "Al azar";
    public static final String TEXTO_BTN_CONFIRMAR = "Confirmar";
    public static final String TEXTO_CONFIRMAR_SECRETO = "¿Confirmás a %s como tu personaje secreto?\nDespués no lo vas a poder cambiar.";

    // =====================================================================
    // Textos: juego (vista jugador)
    // =====================================================================
    public static final String TEXTO_TU_TURNO = "Tu turno";
    public static final String TEXTO_PENSANDO = "%s está pensando...";
    public static final String TEXTO_NUMERO_TURNO = "Turno %d";
    public static final String TEXTO_TU_PERSONAJE = "Tu personaje";
    public static final String TEXTO_TITULO_TABLERO_JUGADOR = "Candidatos para el personaje de %s: %d de %d";
    public static final String TEXTO_TITULO_PREGUNTAS = "Preguntas";
    public static final String TEXTO_TITULO_RESPUESTAS = "Respuestas";
    public static final String TEXTO_RESTANTES_MAQUINA = "Le quedan %d de %d para adivinar el tuyo";
    public static final String TEXTO_BTN_PREGUNTAR = "Preguntar";
    public static final String TEXTO_BTN_ADIVINAR = "Adivinar";
    public static final String TEXTO_BTN_CANCELAR = "Cancelar";
    public static final String TEXTO_BTN_ABANDONAR = "Abandonar";
    public static final String TEXTO_AVISO_INICIO = "Elegí una pregunta o arriesgá con \"Adivinar\".";
    public static final String TEXTO_AVISO_ELEGIR_PREGUNTA = "Elegí una pregunta de la lista.";
    public static final String TEXTO_AVISO_ADIVINAR = "Tocá la carta del personaje que creés que tiene la máquina.";
    public static final String TEXTO_AVISO_SIN_PREGUNTAS = "Ya hiciste todas las preguntas: ¡arriesgá con \"Adivinar\"!";
    public static final String TEXTO_CONFIRMAR_ADIVINANZA = "¿Arriesgás que el personaje de %s es %s?\nSi fallás, pasa el turno.";
    public static final String TEXTO_CONFIRMAR_ABANDONAR = "¿Seguro que querés abandonar la partida?\nNo cuenta para el marcador.";
    public static final String TEXTO_VOS = "Vos";
    public static final String TEXTO_RESPUESTA_SI = "SÍ";
    public static final String TEXTO_RESPUESTA_NO = "NO";
    public static final String TEXTO_LINEA_PREGUNTA = "%s: %s → %s";
    public static final String TEXTO_LINEA_ADIVINANZA_FALLIDA = "%s arriesgó %s → no era";
    public static final String TEXTO_LINEA_ADIVINANZA_ACERTADA = "%s arriesgó %s → ¡ERA!";
    public static final String TEXTO_TOOLTIP_OBJETIVO = "Este es el personaje que busca";

    // =====================================================================
    // Textos: modo espectador
    // =====================================================================
    public static final String TEXTO_TITULO_ESPECTADOR = "MODO ESPECTADOR";
    public static final String TEXTO_TURNO_DE = "Turno de: %s";
    public static final String TEXTO_RESTANTES = "Le quedan %d de %d candidatos";
    public static final String TEXTO_BTN_VOLVER_MENU = "Volver al menú";
    public static final String TEXTO_CONFIRMAR_SALIR_ESPECTADOR = "¿Volver al menú? La partida se corta y no cuenta para el marcador.";

    // =====================================================================
    // Textos: resultado y marcador
    // =====================================================================
    public static final String TEXTO_GANASTE = "¡Ganaste, %s!";
    public static final String TEXTO_PERDISTE = "Perdiste contra %s";
    public static final String TEXTO_GANO = "¡Ganó %s!";
    public static final String TEXTO_DETALLE_TURNOS = "La partida duró %d turnos.";
    public static final String TEXTO_PERSONAJE_DE = "Personaje de %s";
    public static final String TEXTO_BTN_REVANCHA = "Otra partida";
    public static final String TEXTO_TITULO_MARCADOR = "MARCADOR";
    public static final String TEXTO_COLUMNA_JUGADOR = "Jugador";
    public static final String TEXTO_COLUMNA_VICTORIAS = "Victorias";
    public static final String TEXTO_MARCADOR_VACIO = "Todavía no hay partidas registradas.";
    public static final String TEXTO_ERROR_MARCADOR = "No se pudo leer el marcador: ";

    // =====================================================================
    // Textos: diálogos
    // =====================================================================
    public static final String TEXTO_TITULO_CONFIRMACION = "Confirmar";
    public static final String TEXTO_TITULO_ERROR = "Error";

    // =====================================================================
    // Tamaños compuestos: métodos, no constantes (Dimension es mutable)
    // =====================================================================
    public static Dimension tamanoVentana() {
        return new Dimension(ANCHO_VENTANA, ALTO_VENTANA);
    }

    public static Dimension tamanoMinimoVentana() {
        return new Dimension(ANCHO_MINIMO_VENTANA, ALTO_MINIMO_VENTANA);
    }

    public static Dimension tamanoBoton() {
        return new Dimension(ANCHO_BOTON, ALTO_BOTON);
    }

    public static Dimension tamanoBotonChico() {
        return new Dimension(ANCHO_BOTON_CHICO, ALTO_BOTON_CHICO);
    }

    public static Dimension tamanoImagenModo() {
        return new Dimension(ANCHO_IMAGEN_MODO, ALTO_IMAGEN_MODO);
    }
}
