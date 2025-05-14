package um.tds.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VentanaLogin extends JFrame {

    private JTextField telefonoField;
    private JPasswordField contraseñaField;

    public VentanaLogin() {
        setTitle("AppChat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(34, 34, 34));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        // Layout
        GroupLayout gl = new GroupLayout(contentPane);
        contentPane.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        // Labels y campos
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setForeground(Color.WHITE);
        lblTelefono.setFont(lblTelefono.getFont().deriveFont(Font.BOLD, 14f));

        telefonoField = new JTextField();
        styleField(telefonoField);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setForeground(Color.WHITE);
        lblContraseña.setFont(lblContraseña.getFont().deriveFont(Font.BOLD, 14f));

        contraseñaField = new JPasswordField();
        styleField(contraseñaField);

        // Botones
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar  = new JButton("Cancelar");
        JButton btnAceptar   = new JButton("Aceptar");

        styleButton(btnRegistrar);
        styleButton(btnCancelar);
        styleButton(btnAceptar);

        // Acción de botones
        btnCancelar.addActionListener(e -> clearFields());
        btnAceptar.addActionListener(e -> submit());
        btnRegistrar.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Funcionalidad de registro aún no implementada.", "Registrar", 
            JOptionPane.INFORMATION_MESSAGE));

        // Definición del layout
        gl.setHorizontalGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
            .addComponent(lblTelefono)
            .addComponent(telefonoField)
            .addComponent(lblContraseña)
            .addComponent(contraseñaField)
            .addGroup(gl.createSequentialGroup()
                .addComponent(btnRegistrar)
                .addComponent(btnCancelar)
                .addComponent(btnAceptar))
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
            .addComponent(lblTelefono)
            .addComponent(telefonoField,  GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(lblContraseña)
            .addComponent(contraseñaField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 20, Short.MAX_VALUE)
            .addGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(btnRegistrar)
                .addComponent(btnCancelar)
                .addComponent(btnAceptar))
        );
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(64, 64, 64));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setFont(field.getFont().deriveFont(14f));
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
        contraseñaField.setText("");
    }

    private void submit() {
        String telefono = telefonoField.getText().trim();
        String pass = new String(contraseñaField.getPassword());
        // Aquí iría la lógica real de login...
        JOptionPane.showMessageDialog(this, 
            "Teléfono: " + telefono + "\nContraseña: " + pass, 
            "Intento de login", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Arranca en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaLogin frame = new VentanaLogin();
            frame.setVisible(true);
        });
    }
}

