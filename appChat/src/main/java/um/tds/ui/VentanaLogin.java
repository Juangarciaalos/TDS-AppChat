package um.tds.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import um.tds.controlador.Controlador;

/**
 * Ventana de inicio de sesión para la aplicación AppChat.
 * Permite a los usuarios ingresar su número de teléfono y contraseña.
 * Incluye opciones para registrarse o cancelar el inicio de sesión.
 */
public class VentanaLogin {

    private JFrame frame;
    private JTextField telefonoField;
    private JPasswordField contrasenaField;

    public VentanaLogin() {
        frame = new JFrame("AppChat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(34, 34, 34));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        frame.setContentPane(contentPane);

        GroupLayout gl = new GroupLayout(contentPane);
        contentPane.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        JLabel labelTelefono = new JLabel("Teléfono:");
        labelTelefono.setForeground(Color.WHITE);
        labelTelefono.setFont(labelTelefono.getFont().deriveFont(Font.BOLD, 14f));

        telefonoField = new JTextField();
        styleField(telefonoField);
        telefonoField.addActionListener(e -> submit());

        JLabel labelContrasena = new JLabel("Contraseña:");
        labelContrasena.setForeground(Color.WHITE);
        labelContrasena.setFont(labelContrasena.getFont().deriveFont(Font.BOLD, 14f));

        contrasenaField = new JPasswordField();
        styleField(contrasenaField);
        contrasenaField.addActionListener(e -> submit());

        JButton botonRegistro = new JButton("Registrar");
        JButton botonCancelar  = new JButton("Cancelar");
        JButton botonAceptar   = new JButton("Aceptar");

        styleButton(botonRegistro);
        styleButton(botonCancelar);
        styleButton(botonAceptar);

        botonCancelar.addActionListener(e -> clearFields());
        botonAceptar.addActionListener(e -> submit());
        botonRegistro.addActionListener(e -> {
            VentanaRegistro registro = new VentanaRegistro();
            registro.setVisible(true);
            frame.dispose();
        });

        gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(labelTelefono)
            .addComponent(telefonoField)
            .addComponent(labelContrasena)
            .addComponent(contrasenaField)
            .addGroup(gl.createSequentialGroup()
                .addComponent(botonRegistro)
                .addComponent(botonCancelar)
                .addComponent(botonAceptar))
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addComponent(labelTelefono)
            .addComponent(telefonoField,  GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(labelContrasena)
            .addComponent(contrasenaField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 20, Short.MAX_VALUE)
            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(botonRegistro)
                .addComponent(botonCancelar)
                .addComponent(botonAceptar))
        );
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(64, 64, 64));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(new LineBorder(Color.GRAY));
        field.setFont(field.getFont().deriveFont(14f));

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(new LineBorder(Color.WHITE, 2));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(new LineBorder(Color.GRAY));
            }
        });
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 12f));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(100, 100, 100));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(80, 80, 80));
            }
        });
    }

    private void clearFields() {
        telefonoField.setText("");
        contrasenaField.setText("");
    }

    private void submit() {
        String telefono = telefonoField.getText().trim();
        String pass = new String(contrasenaField.getPassword());

        if (telefono.isEmpty()){
            mostrarError("Falta por introducir un teléfono.");
            return;
        }
        
        if (pass.isEmpty()) {
            mostrarError("Falta por introducir una contraseña.");
            return;
        }

        boolean success = Controlador.getInstancia().iniciarSesion(Integer.parseInt(telefono), pass);

        if (success) {
            JOptionPane.showMessageDialog(frame, "Bienvenido a AppChat", "Login Exitoso", JOptionPane.INFORMATION_MESSAGE);
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
            frame.dispose();
        } else {
            mostrarError("Teléfono o contraseña incorrectos.");
        }
    }

    private void mostrarError(String mensaje) {
        Toolkit.getDefaultToolkit().beep();
        telefonoField.setBorder(new LineBorder(Color.RED, 2));
        contrasenaField.setBorder(new LineBorder(Color.RED, 2));
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaLogin login = new VentanaLogin();
            login.setVisible(true);
        });
    }
}
