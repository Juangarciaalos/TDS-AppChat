package um.tds.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import um.tds.controlador.Controlador;

@SuppressWarnings("serial")
/*
 * Ventana para añadir un nuevo contacto.
 * Permite introducir el nombre y el teléfono del contacto.
 * Valida la entrada y muestra mensajes de error o éxito.
 */
public class VentanaAddContacto extends JFrame {

    private static final String ERROR_TLF = "El teléfono debe ser un número";
    private static final String ERROR_NOMBRE = "Falta por introducir un nombre para el contacto";
    private static final String ERROR_TLF_NOMBRE = "Falta por introducir un teléfono y un nombre para el contacto";
    private static final String ERROR_AL_AÑADIR = "El contacto ya está añadido, el usuario no existe, o eres tu mismo";
    private static final String AÑADIDO_CORRECTAMENTE = "Contacto añadido correctamente";

    private JTextField txtTelefono;
    private JTextField txtNombre;

    public VentanaAddContacto() {
        setTitle("AppChat - Añadir Contacto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setBackground(new Color(34, 34, 34));

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(34, 34, 34));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        JLabel labelTitulo = new JLabel("Añadir nuevo contacto");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(labelTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setBackground(new Color(34, 34, 34));

        GridBagConstraints gbcNombreLabel = new GridBagConstraints();
        gbcNombreLabel.insets = new Insets(10, 10, 10, 10);
        gbcNombreLabel.gridx = 0;
        gbcNombreLabel.gridy = 0;
        gbcNombreLabel.anchor = GridBagConstraints.EAST;
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNombre.setForeground(Color.WHITE);
        panelCentro.add(lblNombre, gbcNombreLabel);

        GridBagConstraints gbcNombreField = new GridBagConstraints();
        gbcNombreField.insets = new Insets(10, 10, 10, 10);
        gbcNombreField.gridx = 1;
        gbcNombreField.gridy = 0;
        gbcNombreField.anchor = GridBagConstraints.WEST;
        txtNombre = new JTextField(20);
        styleField(txtNombre);
        panelCentro.add(txtNombre, gbcNombreField);

        GridBagConstraints gbcTelefonoLabel = new GridBagConstraints();
        gbcTelefonoLabel.insets = new Insets(10, 10, 10, 10);
        gbcTelefonoLabel.gridx = 0;
        gbcTelefonoLabel.gridy = 1;
        gbcTelefonoLabel.anchor = GridBagConstraints.EAST;
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTelefono.setForeground(Color.WHITE);
        panelCentro.add(lblTelefono, gbcTelefonoLabel);

        GridBagConstraints gbcTelefonoField = new GridBagConstraints();
        gbcTelefonoField.insets = new Insets(10, 10, 10, 10);
        gbcTelefonoField.gridx = 1;
        gbcTelefonoField.gridy = 1;
        gbcTelefonoField.anchor = GridBagConstraints.WEST;
        txtTelefono = new JTextField(20);
        styleField(txtTelefono);
        panelCentro.add(txtTelefono, gbcTelefonoField);

        contentPane.add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(34, 34, 34));
        JButton btnAceptar = new JButton("Aceptar");
        JButton btnCancelar = new JButton("Cancelar");
        styleButton(btnAceptar);
        styleButton(btnCancelar);

        btnAceptar.addActionListener(this::accionAceptar);
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);

        contentPane.add(panelBotones, BorderLayout.SOUTH);
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(64, 64, 64));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(100, 100, 100)); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(new Color(80, 80, 80)); }
        });
    }

    private void accionAceptar(ActionEvent e) {
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();

        String error = validarEntrada(telefono, nombre);
        if (!error.isEmpty()) {
            Toolkit.getDefaultToolkit().beep();
            mostrarError(error);
            return;
        }

        boolean success = Controlador.getInstancia().añadirContacto(nombre, telefono);
        if (success) {
            JOptionPane.showMessageDialog(this, AÑADIDO_CORRECTAMENTE, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            Toolkit.getDefaultToolkit().beep();
            mostrarError(ERROR_AL_AÑADIR);
        }
    }

    private String validarEntrada(String tlf, String nombre) {
        if (tlf.isEmpty() && nombre.isEmpty()) return ERROR_TLF_NOMBRE;
        if (tlf.isEmpty()) return ERROR_TLF;
        if (!tlf.matches("\\d+")) return ERROR_TLF;
        if (nombre.isEmpty()) return ERROR_NOMBRE;
        return "";
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void setTelefono(String numero) {
        txtTelefono.setText(numero);
    }
}
