package um.tds.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class VentanaRegistro extends JFrame {

    private JTextField nombreField;
    private JTextField apellidosField;
    private JTextField telefonoField;
    private JPasswordField contraseñaField;
    private JPasswordField repetirContraseñaField;
    private JDateChooser dateChooser;
    private JTextArea saludoArea;
    private JScrollPane scrollSaludo;
    private JButton btnCancelar;
    private JButton btnAceptar;

    public VentanaRegistro() {
        setTitle("AppChat - Perfil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel content = new JPanel();
        content.setBackground(new Color(34, 34, 34));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(content);

        // Campos de texto
        nombreField      = new JTextField();
        apellidosField   = new JTextField();
        telefonoField    = new JTextField();
        contraseñaField  = new JPasswordField();
        repetirContraseñaField = new JPasswordField();

        saludoArea = new JTextArea(4, 20);
        saludoArea.setLineWrap(true);
        saludoArea.setWrapStyleWord(true);
        scrollSaludo = new JScrollPane(saludoArea);
        scrollSaludo.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));

        // Selector de fecha con JDateChooser
        dateChooser = new JDateChooser(new Date());
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setPreferredSize(new Dimension(120, 25));

        // Botones
        btnCancelar = new JButton("Cancelar");
        btnAceptar  = new JButton("Aceptar");

        // Estilos
        applyFieldStyle(nombreField);
        applyFieldStyle(apellidosField);
        applyFieldStyle(telefonoField);
        applyFieldStyle(contraseñaField);
        applyFieldStyle(repetirContraseñaField);
        applyTextAreaStyle(saludoArea, scrollSaludo);
        applyDateChooserStyle(dateChooser);
        applyButtonStyle(btnCancelar);
        applyButtonStyle(btnAceptar);

        btnCancelar.addActionListener(e -> clearAll());
        btnAceptar.addActionListener(e -> submit());

        // Labels
        JLabel lblNombre    = createLabel("Nombre:");
        JLabel lblApellidos = createLabel("Apellidos:");
        JLabel lblTelefono  = createLabel("Teléfono:");
        JLabel lblContra    = createLabel("Contraseña:");
        JLabel lblReContra  = createLabel("Repetir Contraseña:");
        JLabel lblFecha     = createLabel("Fecha:");
        JLabel lblSaludo    = createLabel("Saludo:");

        // Layout
        GroupLayout gl = new GroupLayout(content);
        content.setLayout(gl);
        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        // Horizontal
        gl.setHorizontalGroup(
            gl.createParallelGroup(GroupLayout.Alignment.CENTER)
              .addGroup(
                  gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lblNombre)
                        .addComponent(lblApellidos)
                        .addComponent(lblTelefono)
                        .addComponent(lblContra)
                        .addComponent(lblReContra)
                        .addComponent(lblFecha)
                        .addComponent(lblSaludo)
                    )
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nombreField)
                        .addComponent(apellidosField)
                        .addComponent(telefonoField, 150, 150, 200)
                        .addComponent(contraseñaField)
                        .addComponent(repetirContraseñaField)
                        .addComponent(dateChooser)
                        .addComponent(scrollSaludo)
                    )
              )
              .addGroup(gl.createSequentialGroup()
                  .addComponent(btnCancelar)
                  .addComponent(btnAceptar)
              )
        );

        // Vertical
        gl.setVerticalGroup(
            gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(nombreField)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblApellidos)
                    .addComponent(apellidosField)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTelefono)
                    .addComponent(telefonoField)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblContra)
                    .addComponent(contraseñaField)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReContra)
                    .addComponent(repetirContraseñaField)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFecha)
                    .addComponent(dateChooser)
                )
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(lblSaludo)
                    .addComponent(scrollSaludo)
                )
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 20, Short.MAX_VALUE)
                .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelar)
                    .addComponent(btnAceptar)
                )
        );
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 14f));
        return lbl;
    }

    private void applyFieldStyle(JTextField field) {
        field.setBackground(new Color(64, 64, 64));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setFont(field.getFont().deriveFont(14f));
    }

    private void applyTextAreaStyle(JTextArea area, JScrollPane scroll) {
        area.setBackground(new Color(64, 64, 64));
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setFont(area.getFont().deriveFont(14f));
        scroll.getViewport().setBackground(new Color(64, 64, 64));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
    }

    private void applyDateChooserStyle(JDateChooser chooser) {
        chooser.setBackground(new Color(64, 64, 64));
        chooser.setForeground(Color.WHITE);
        chooser.setFont(chooser.getFont().deriveFont(14f));
        chooser.getCalendarButton().setFocusPainted(false);
        chooser.getCalendarButton().setBackground(new Color(80, 80, 80));
        chooser.getCalendarButton().setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        chooser.getCalendarButton().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void applyButtonStyle(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 12f));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(100, 100, 100)); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(80, 80, 80)); }
        });
    }

    private void clearAll() {
        nombreField.setText("");
        apellidosField.setText("");
        telefonoField.setText("");
        contraseñaField.setText("");
        repetirContraseñaField.setText("");
        dateChooser.setDate(new Date());
        saludoArea.setText("");
    }

    private void submit() {
        String pass  = new String(contraseñaField.getPassword());
        String pass2 = new String(repetirContraseñaField.getPassword());
        if (!pass.equals(pass2)) {
            JOptionPane.showMessageDialog(this,
                "Las contraseñas no coinciden.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        Date fecha = dateChooser.getDate();
        JOptionPane.showMessageDialog(this,
            "Datos guardados:\n" +
            "Nombre: " + nombreField.getText() + "\n" +
            "Apellidos: " + apellidosField.getText() + "\n" +
            "Teléfono: " + telefonoField.getText() + "\n" +
            "Fecha: " + new java.text.SimpleDateFormat("dd/MM/yyyy").format(fecha) + "\n" +
            "Saludo: " + saludoArea.getText(),
            "OK",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Arranca en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaRegistro frame = new VentanaRegistro();
            frame.setVisible(true);
        });
    }
}