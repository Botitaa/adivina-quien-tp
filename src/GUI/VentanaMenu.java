package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class VentanaMenu extends JFrame {
    private JLabel Titulo;
    private JButton btnJugar;
    private JButton btnEspectador;
    private JButton btnSalir;
    private JPanel menu;
    private JPanel panelBtn;
    private JFrame ventana = new JFrame();
    private static final int ANCHO_VENTANA = 1920;
    private static final int ALTO_VENTANA = 1080;
    private static final int ANCHO_BTN = 500;
    private static final int ALTO_BTN = 100;

    public VentanaMenu() {
        super("Adivina Quien - Menu");
        setContentPane(menu);
        menu.setPreferredSize(new Dimension(ANCHO_VENTANA, ALTO_VENTANA));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        btnJugar.setPreferredSize(new Dimension(ANCHO_BTN, ALTO_BTN));
        btnEspectador.setPreferredSize(new Dimension(ANCHO_BTN, ALTO_BTN));
        btnSalir.setPreferredSize(new Dimension(ANCHO_BTN, ALTO_BTN));

        btnJugar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaJuego juego = new VentanaJuego();
                juego.setVisible(true);
                dispose();
            }
        });
        btnEspectador.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(VentanaMenu.this, "Todavia no implementado");
            }
        });
        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        pack();
        setLocationRelativeTo(null);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaMenu().setVisible(true));
    }
}




