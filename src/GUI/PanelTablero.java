package GUI;

import dominio.Personaje;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PanelTablero extends JPanel {


    private final Map<Integer, CartaPersonaje> cartasPorId = new LinkedHashMap<>();
    private Consumer<Personaje> alSeleccionar;

    public PanelTablero(List<Personaje> personajes, Tamano tamano) {
        super(new GridLayout(0, ConstantesGUI.COLUMNAS_TABLERO, tamano.getEspacio(), tamano.getEspacio()));
        setOpaque(false);
        for (Personaje personaje : personajes) {
            CartaPersonaje carta = new CartaPersonaje(personaje, tamano);
            carta.addActionListener(evento -> cartaTocada(carta));
            cartasPorId.put(personaje.getId(), carta);
            add(carta);
        }
    }


    public void marcarCandidatos(Collection<Personaje> candidatos) {
        Set<Integer> idsCandidatos = new HashSet<>();
        for (Personaje candidato : candidatos) {
            idsCandidatos.add(candidato.getId());
        }
        for (CartaPersonaje carta : cartasPorId.values()) {
            carta.setDescartado(!idsCandidatos.contains(carta.getPersonaje().getId()));
        }
    }


    public void activarSeleccion(Consumer<Personaje> accion) {
        this.alSeleccionar = accion;
        cartasPorId.values().forEach(carta -> carta.setSeleccionable(true));
    }

    public void desactivarSeleccion() {
        this.alSeleccionar = null;
        cartasPorId.values().forEach(carta -> carta.setSeleccionable(false));
    }


    public void resaltar(Personaje personaje, Color color) {
        for (CartaPersonaje carta : cartasPorId.values()) {
            boolean esEsta = personaje != null && carta.getPersonaje().getId() == personaje.getId();
            carta.setResaltado(esEsta, color);
        }
    }


    public void marcarObjetivo(Personaje personaje, Color color, String tooltip) {
        resaltar(personaje, color);
        CartaPersonaje carta = cartasPorId.get(personaje.getId());
        if (carta != null) {
            carta.setToolTipText(tooltip + " — " + ImagenesGUI.descripcion(personaje));
        }
    }

    private void cartaTocada(CartaPersonaje carta) {
        if (alSeleccionar != null && !carta.isDescartado()) {
            alSeleccionar.accept(carta.getPersonaje());
        }
    }

    public enum Tamano {
        GRANDE(ConstantesGUI.ANCHO_CARTA_GRANDE, ConstantesGUI.ALTO_CARTA_GRANDE, true),
        NORMAL(ConstantesGUI.ANCHO_CARTA_NORMAL, ConstantesGUI.ALTO_CARTA_NORMAL, true),
        MEDIANA(ConstantesGUI.ANCHO_CARTA_MEDIANA, ConstantesGUI.ALTO_CARTA_MEDIANA, false),
        CHICA(ConstantesGUI.ANCHO_CARTA_CHICA, ConstantesGUI.ALTO_CARTA_CHICA, false);

        private final int ancho;
        private final int alto;
        private final boolean conNumero;

        Tamano(int ancho, int alto, boolean conNumero) {
            this.ancho = ancho;
            this.alto = alto;
            this.conNumero = conNumero;
        }

        public int getAncho() {
            return ancho;
        }

        public int getAlto() {
            return alto;
        }


        public boolean conNumero() {
            return conNumero;
        }

        public int getAltoImagen() {
            return conNumero ? alto - ConstantesGUI.ALTO_TEXTO_CARTA : alto;
        }

        public int getEspacio() {
            return conNumero ? ConstantesGUI.ESPACIO_ENTRE_CARTAS : ConstantesGUI.ESPACIO_ENTRE_CARTAS_CHICAS;
        }
    }


    private static class CartaPersonaje extends JButton {

        private final Personaje personaje;
        private boolean descartado;
        private boolean seleccionable;

        public CartaPersonaje(Personaje personaje, Tamano tamano) {
            this.personaje = personaje;
            setIcon(ImagenesGUI.iconoPersonaje(personaje, tamano.getAncho() - 2 * ConstantesGUI.GROSOR_BORDE_SELECCION,
                    tamano.getAltoImagen() - 2 * ConstantesGUI.GROSOR_BORDE_SELECCION));
            if (tamano.conNumero()) {
                setText("#" + personaje.getId());
                setFont(ConstantesGUI.FUENTE_CARTA);
                setVerticalTextPosition(SwingConstants.BOTTOM);
                setHorizontalTextPosition(SwingConstants.CENTER);
                setIconTextGap(0);
            }
            setToolTipText(ImagenesGUI.descripcion(personaje));
            setPreferredSize(new Dimension(tamano.getAncho(), tamano.getAlto()));
            setMargin(new java.awt.Insets(0, 0, 0, 0));
            setBackground(ConstantesGUI.COLOR_CARTA);
            setForeground(ConstantesGUI.COLOR_BORDE_CARTA);
            setContentAreaFilled(false);
            setOpaque(true);
            setFocusPainted(false);
            setRolloverEnabled(false);
            setResaltado(false, null);
        }

        public Personaje getPersonaje() {
            return personaje;
        }

        public boolean isDescartado() {
            return descartado;
        }

        public void setDescartado(boolean descartado) {
            this.descartado = descartado;
            actualizarCursor();
            repaint();
        }


        public void setSeleccionable(boolean seleccionable) {
            this.seleccionable = seleccionable;
            actualizarCursor();
        }


        public void setResaltado(boolean resaltado, java.awt.Color color) {
            if (resaltado && color != null) {
                setBorder(BorderFactory.createLineBorder(color, ConstantesGUI.GROSOR_BORDE_SELECCION));
            } else {
                int relleno = ConstantesGUI.GROSOR_BORDE_SELECCION - ConstantesGUI.GROSOR_BORDE_CARTA;
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(relleno, relleno, relleno, relleno),
                        BorderFactory.createLineBorder(ConstantesGUI.COLOR_BORDE_CARTA, ConstantesGUI.GROSOR_BORDE_CARTA)));
            }
        }

        private void actualizarCursor() {
            boolean tocable = seleccionable && !descartado;
            setCursor(Cursor.getPredefinedCursor(tocable ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!descartado) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(ConstantesGUI.COLOR_VELO_DESCARTE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            int margen = ConstantesGUI.GROSOR_BORDE_SELECCION * 2;
            g2.setColor(ConstantesGUI.COLOR_CRUZ_DESCARTE);
            g2.setStroke(new BasicStroke(ConstantesGUI.GROSOR_CRUZ_DESCARTE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(margen, margen, getWidth() - margen, getHeight() - margen);
            g2.drawLine(getWidth() - margen, margen, margen, getHeight() - margen);
            g2.dispose();
        }
    }
}
