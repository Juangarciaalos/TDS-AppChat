package um.tds.ui;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.toedter.calendar.JDateChooser;

import um.tds.clases.Usuario;
import um.tds.controlador.Controlador;

@SuppressWarnings("serial")
/**
 * Ventana de registro de usuario.
 * Permite al usuario ingresar sus datos y registrarse en la aplicación.
 */
public class VentanaRegistro extends JFrame {
    private JTextField nombreField;
    private JTextField apellidosField;
    private JTextField telefonoField;
    private JTextField correoField;
    private JPasswordField contraseñaField;
    private JPasswordField repetirContraseñaField;
    private JDateChooser dateChooser;
    private JTextArea estadoArea;
    private JScrollPane scrollSaludo;
    private JButton botonCancelar;
    private JButton botonAceptar;
    private JLabel imageLabel;
    private JButton btnSeleccionarImagen;
    private JButton togglePasswordBtn;
    private boolean passwordVisible = false;

    public VentanaRegistro() {
        setTitle("AppChat - Registro de Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        JPanel content = new JPanel();
        content.setBackground(new Color(34, 34, 34));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(content);

        nombreField = new JTextField();
        apellidosField = new JTextField();
        telefonoField = new JTextField();
        correoField = new JTextField();
        contraseñaField = new JPasswordField();
        repetirContraseñaField = new JPasswordField();

        estadoArea = new JTextArea(4, 20);
        estadoArea.setLineWrap(true);
        estadoArea.setWrapStyleWord(true);
        scrollSaludo = new JScrollPane(estadoArea);
        scrollSaludo.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));

        dateChooser = new JDateChooser(new Date());
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(120, 25));

        botonCancelar = new JButton("Cancelar");
        botonAceptar = new JButton("Aceptar");

        applyFieldStyle(nombreField);
        applyFieldStyle(apellidosField);
        applyFieldStyle(telefonoField);
        applyFieldStyle(correoField);
        applyFieldStyle(contraseñaField);
        applyFieldStyle(repetirContraseñaField);
        applyTextAreaStyle(estadoArea, scrollSaludo);
        applyDateChooserStyle(dateChooser);
        applyButtonStyle(botonCancelar);
        applyButtonStyle(botonAceptar);

        botonCancelar.addActionListener(e -> clearAll());
        botonAceptar.addActionListener(e -> submit());

        imageLabel = new JLabel(new ImageIcon(new ImageIcon("src/main/resources/default-user.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());

        togglePasswordBtn = new JButton("👁");
        togglePasswordBtn.setFocusPainted(false);
        togglePasswordBtn.addActionListener(e -> togglePasswordVisibility());

        JLabel labelNombre = createLabel("Nombre:");
        JLabel labelApellidos = createLabel("Apellidos:");
        JLabel labelTelefono = createLabel("Teléfono:");
        JLabel labelCorreo = createLabel("Correo:");
        JLabel labelContraseña = createLabel("Contraseña:");
        JLabel labelReContraseña = createLabel("Repetir Contraseña:");
        JLabel labelFecha = createLabel("Fecha:");
        JLabel labelEstado = createLabel("Estado:");
        JLabel labelImagen = createLabel("Imagen:");

        GroupLayout gl = new GroupLayout(content);
        content.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(
            gl.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(labelNombre)
                        .addComponent(labelApellidos)
                        .addComponent(labelTelefono)
                        .addComponent(labelCorreo)
                        .addComponent(labelContraseña)
                        .addComponent(labelReContraseña)
                        .addComponent(labelFecha)
                        .addComponent(labelEstado)
                        .addComponent(labelImagen)
                    )
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nombreField)
                        .addComponent(apellidosField)
                        .addComponent(telefonoField)
                        .addComponent(correoField)
                        .addComponent(contraseñaField)
                        .addComponent(repetirContraseñaField)
                        .addComponent(dateChooser)
                        .addComponent(scrollSaludo)
                        .addComponent(imageLabel)
                    )
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(togglePasswordBtn)
                        .addComponent(btnSeleccionarImagen)
                    )
                )
                .addGroup(gl.createSequentialGroup()
                    .addComponent(botonCancelar)
                    .addComponent(botonAceptar)
                )
        );

        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelNombre)
                    .addComponent(nombreField)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelApellidos)
                    .addComponent(apellidosField)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTelefono)
                    .addComponent(telefonoField)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(labelCorreo)
					.addComponent(correoField)
				)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelContraseña)
                    .addComponent(contraseñaField)
                    .addComponent(togglePasswordBtn)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelReContraseña)
                    .addComponent(repetirContraseñaField)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelFecha)
                    .addComponent(dateChooser)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(labelEstado)
                    .addComponent(scrollSaludo)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(labelImagen)
                    .addComponent(imageLabel)
                    .addComponent(btnSeleccionarImagen)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(botonCancelar)
                    .addComponent(botonAceptar)
                )
        );
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }

    private void applyFieldStyle(JTextField field) {
        field.setBackground(new Color(64, 64, 64));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void applyTextAreaStyle(JTextArea area, JScrollPane scroll) {
        area.setBackground(new Color(64, 64, 64));
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        scroll.getViewport().setBackground(new Color(64, 64, 64));
    }

    private void applyDateChooserStyle(JDateChooser chooser) {
        chooser.setBackground(new Color(64, 64, 64));
        chooser.setForeground(Color.WHITE);
        chooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chooser.getCalendarButton().setBackground(new Color(80, 80, 80));
    }

    private void applyButtonStyle(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void clearAll() {
        nombreField.setText("");
        apellidosField.setText("");
        telefonoField.setText("");
        contraseñaField.setText("");
        repetirContraseñaField.setText("");
        dateChooser.setDate(new Date());
        estadoArea.setText("");
        imageLabel.setIcon(null);
    }

    private void submit() {
        String nombre = nombreField.getText().trim();
        String apellidos = apellidosField.getText().trim();
        String telefono = telefonoField.getText().trim();
        String correo = correoField.getText().trim();
        String pass1 = new String(contraseñaField.getPassword());
        String pass2 = new String(repetirContraseñaField.getPassword());
        String estado = estadoArea.getText().trim();

        if (nombre.isEmpty() || apellidos.isEmpty() || telefono.isEmpty() || correo.isEmpty() ||pass1.isEmpty() || pass2.isEmpty() || dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben estar completos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pass1.equals(pass2)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        registrar(nombre, apellidos, telefono, estado, pass1, correo, dateChooser.getDate());
    }

    private void registrar(String nombre, String apellido, String telefono, String estado, String contraseña, String correo, Date fechaNacimiento) {
    	LocalDate fechaNac = dateChooser.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
    	boolean exito = false;
    	if (estado.isEmpty()) {
        	exito = Controlador.getInstancia().registrarUsuario(nombre, apellido, Integer.parseInt(telefono), Usuario.ESTADO_BASE ,contraseña, correo, fechaNac, LocalDate.now());

		} else {
			exito = Controlador.getInstancia().registrarUsuario(nombre, apellido, Integer.parseInt(telefono), estado, contraseña, correo, fechaNac, LocalDate.now());
		}
    	
		JOptionPane.showMessageDialog(VentanaRegistro.this, exito ? "Usuario creado correctamente" : "El usuario ya existe");
		if (exito) {
			VentanaLogin ventanaLogin = new VentanaLogin();
			ventanaLogin.setVisible(true);
			dispose();
		}

	}
    
    private void seleccionarImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                imageLabel.setIcon(new ImageIcon(new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
            }
        }
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        char echo = passwordVisible ? (char) 0 : '•';
        contraseñaField.setEchoChar(echo);
        repetirContraseñaField.setEchoChar(echo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaRegistro().setVisible(true);
        });
    }
}
