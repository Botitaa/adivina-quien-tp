package GUI;

import javax.swing.*;

public class VentanaJuego extends JFrame {
    private JButton btnPreguntar;
    private JPanel panel1;
    private JButton btnAdivinar;

    public VentanaJuego() {
        setContentPane(panel1);
        setTitle("Adivina Quién");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
