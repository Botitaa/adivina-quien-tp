package GUI;

import actores.IAAsertiva;
import actores.IABasica;
import actores.Jugador;
import persistencia.RepositorioMarcador;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class VentanaConfigPartida extends JFrame {
    private JPanel panelPrincipal;
    private JLabel lblTitulo;
    private JPanel panelFormulario;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JLabel lblRival;
    private JRadioButton rbBasica;
    private JRadioButton rbAsertiva;
    private JLabel lblDescripcionRival;
    private JLabel lblError;
    private JPanel panelBotones;
    private JButton btnVolver;
    private JButton btnContinuar;

    private final RepositorioMarcador repositorioMarcador;

    public VentanaConfigPartida(RepositorioMarcador repositorioMarcador) {
        this.repositorioMarcador = repositorioMarcador;
        EstiloGUI.configurarVentana(this, panelPrincipal, "Configuración");
        EstiloGUI.hacerTransparente(panelFormulario, panelBotones);

        EstiloGUI.estilizarTitulo(lblTitulo, ConstantesGUI.TEXTO_TITULO_CONFIGURACION);
        EstiloGUI.estilizarSubtitulo(lblNombre, ConstantesGUI.TEXTO_LABEL_NOMBRE);
        EstiloGUI.estilizarCampo(txtNombre);
        EstiloGUI.estilizarSubtitulo(lblRival, ConstantesGUI.TEXTO_LABEL_RIVAL);
        EstiloGUI.estilizarOpcion(rbBasica, NivelMaquina.BASICA.getNombre());
        EstiloGUI.estilizarOpcion(rbAsertiva, NivelMaquina.ASERTIVA.getNombre());
        EstiloGUI.estilizarTexto(lblDescripcionRival, "");
        EstiloGUI.estilizarTexto(lblError, " ");
        lblError.setForeground(ConstantesGUI.COLOR_ERROR);


        ButtonGroup grupoRival = new ButtonGroup();
        grupoRival.add(rbBasica);
        grupoRival.add(rbAsertiva);
        rbAsertiva.setSelected(true);
        actualizarDescripcion();

        EstiloGUI.estilizarBoton(btnVolver, ConstantesGUI.TEXTO_BTN_VOLVER);
        EstiloGUI.estilizarBoton(btnContinuar, ConstantesGUI.TEXTO_BTN_CONTINUAR);

        rbBasica.addActionListener(evento -> actualizarDescripcion());
        rbAsertiva.addActionListener(evento -> actualizarDescripcion());
        txtNombre.addActionListener(evento -> continuar()); // ENTER en el campo = Continuar
        btnContinuar.addActionListener(evento -> continuar());
        btnVolver.addActionListener(evento -> volver());
        getRootPane().setDefaultButton(btnContinuar);
    }

    private NivelMaquina nivelElegido() {
        return rbBasica.isSelected() ? NivelMaquina.BASICA : NivelMaquina.ASERTIVA;
    }

    private void actualizarDescripcion() {
        lblDescripcionRival.setText(nivelElegido().getDescripcion());
    }


    private String validarNombre(String nombre) {
        if (nombre.isEmpty()) {
            return ConstantesGUI.TEXTO_ERROR_NOMBRE_VACIO;
        }
        if (nombre.contains(ConstantesGUI.CARACTER_PROHIBIDO_NOMBRE)) {
            return ConstantesGUI.TEXTO_ERROR_NOMBRE_INVALIDO;
        }
        if (nombre.length() > ConstantesGUI.LARGO_MAXIMO_NOMBRE) {
            return ConstantesGUI.TEXTO_ERROR_NOMBRE_LARGO;
        }
        if (NivelMaquina.esNombreDeMaquina(nombre)) {
            return ConstantesGUI.TEXTO_ERROR_NOMBRE_RESERVADO;
        }
        return null;
    }

    private void continuar() {
        String nombre = txtNombre.getText().trim();
        String error = validarNombre(nombre);
        if (error != null) {
            lblError.setText(error);
            txtNombre.requestFocusInWindow();
            return;
        }
        new VentanaElegirSecreto(repositorioMarcador, nombre, nivelElegido()).setVisible(true);
        dispose();
    }

    private void volver() {
        new VentanaSelecionModo(repositorioMarcador).setVisible(true);
        dispose();
    }


    public enum NivelMaquina {
        BASICA(ConstantesGUI.NOMBRE_MAQUINA_BASICA, ConstantesGUI.DESCRIPCION_MAQUINA_BASICA),
        ASERTIVA(ConstantesGUI.NOMBRE_MAQUINA_ASERTIVA, ConstantesGUI.DESCRIPCION_MAQUINA_ASERTIVA);

        private final String nombre;
        private final String descripcion;

        NivelMaquina(String nombre, String descripcion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
        }


        public Jugador crear() {
            return switch (this) {
                case BASICA -> new IABasica(nombre);
                case ASERTIVA -> new IAAsertiva(nombre);
            };
        }

        public String getNombre() {
            return nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }


        public static boolean esNombreDeMaquina(String nombre) {
            for (NivelMaquina nivel : values()) {
                if (nivel.nombre.equalsIgnoreCase(nombre.trim())) {
                    return true;
                }
            }
            return false;
        }
    }
}
