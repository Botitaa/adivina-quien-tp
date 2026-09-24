package GUI;

import dominio.Personaje;
import valores.ColorPelo;
import valores.Genero;

import javax.swing.ImageIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class ImagenesGUI {

    private static final Map<String, ImageIcon> CACHE = new HashMap<>();

    private ImagenesGUI() {
    }

    public static String descripcion(Personaje personaje) {
        return "#" + personaje.getId() + " · " + textoGenero(personaje.getGenero())
                + (personaje.esCalvo() ? " · " + textoPelado(personaje.getGenero()) : " · con pelo")
                + (personaje.usaLentes() ? " · con anteojos" : " · sin anteojos")
                + " · " + textoColor(personaje.getColorPelo());
    }

    public static ImageIcon iconoPersonaje(Personaje personaje, int ancho, int alto) {
        return CACHE.computeIfAbsent("dibujo:" + personaje.getId() + ":" + ancho + "x" + alto,
                clave -> new ImageIcon(dibujarPersonaje(personaje, ancho, alto)));
    }

    public static ImageIcon cargarEscalada(String ruta, int ancho, int alto) {
        String clave = ruta + ":" + ancho + "x" + alto;
        if (CACHE.containsKey(clave)) {
            return CACHE.get(clave);
        }
        URL url = ImagenesGUI.class.getResource(ruta);
        ImageIcon icono = null;
        if (url != null) {
            Image escalada = new ImageIcon(url).getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            icono = new ImageIcon(escalada);
        }
        CACHE.put(clave, icono);
        return icono;
    }

    private static Image dibujarPersonaje(Personaje personaje, int ancho, int alto) {
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cabeza = Math.min(ancho, alto) / 2;
        int x = (ancho - cabeza) / 2;
        int y = alto / 6;
        Color pelo = colorDePelo(personaje.getColorPelo());
        boolean mujer = personaje.getGenero() == Genero.FEMENINO;

        g.setColor(mujer ? ConstantesGUI.COLOR_ROPA_MUJER : ConstantesGUI.COLOR_ROPA_HOMBRE);
        g.fillRoundRect(ancho / 6, y + cabeza, ancho * 2 / 3, alto, cabeza / 2, cabeza / 2);

        if (mujer && !personaje.esCalvo()) {
            g.setColor(pelo);
            g.fillRoundRect(x - cabeza / 6, y, cabeza + cabeza / 3, cabeza + cabeza / 2, cabeza / 2, cabeza / 2);
        }

        g.setColor(ConstantesGUI.COLOR_PIEL);
        g.fillOval(x, y, cabeza, cabeza);

        if (!personaje.esCalvo()) {
            g.setColor(pelo);
            g.fillArc(x, y - cabeza / 12, cabeza, cabeza * 2 / 3, 0, 180);
        }

        int cejaAncho = Math.max(3, cabeza / 4);
        int cejaAlto = Math.max(2, cabeza / 10);
        int cejaY = y + cabeza * 3 / 10;
        g.setColor(pelo);
        g.fillRect(x + cabeza / 5, cejaY, cejaAncho, cejaAlto);
        g.fillRect(x + cabeza - cabeza / 5 - cejaAncho, cejaY, cejaAncho, cejaAlto);

        if (personaje.usaLentes()) {
            int lente = Math.max(3, cabeza / 4);
            int ojosY = y + cabeza * 2 / 5;
            g.setColor(ConstantesGUI.COLOR_LENTES);
            g.setStroke(new BasicStroke(Math.max(1f, cabeza / 16f)));
            g.drawOval(x + cabeza / 5, ojosY, lente, lente);
            g.drawOval(x + cabeza - cabeza / 5 - lente, ojosY, lente, lente);
            g.drawLine(x + cabeza / 5 + lente, ojosY + lente / 2, x + cabeza - cabeza / 5 - lente, ojosY + lente / 2);
        }

        g.dispose();
        return imagen;
    }

    private static Color colorDePelo(ColorPelo colorPelo) {
        return switch (colorPelo) {
            case COLORADO -> ConstantesGUI.COLOR_PELO_COLORADO;
            case NEGRO -> ConstantesGUI.COLOR_PELO_NEGRO;
            case AMARILLO -> ConstantesGUI.COLOR_PELO_AMARILLO;
        };
    }

    private static String textoGenero(Genero genero) {
        return genero == Genero.FEMENINO ? "mujer" : "hombre";
    }

    private static String textoPelado(Genero genero) {
        return genero == Genero.FEMENINO ? "pelada" : "pelado";
    }

    private static String textoColor(ColorPelo colorPelo) {
        return switch (colorPelo) {
            case COLORADO -> "colorado";
            case NEGRO -> "morocho";
            case AMARILLO -> "rubio";
        };
    }
}