package um.tds.ui;

import um.tds.Utilidades.ConversorImagenes;
import um.tds.clases.ContactoIndividual;
import um.tds.clases.Grupo;
import um.tds.controlador.Controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
/**
 * Ventana para editar un grupo de chat.
 * Permite ingresar el nombre, descripción, seleccionar contactos y una imagen.
 */
public class VentanaEditarGrupo extends JFrame {
    private JTextField nombreField;
    private JTextArea descripcionArea;
    private JLabel imageLabel;
    private DefaultListModel<ContactoIndividual> modeloDisponibles;
    private DefaultListModel<ContactoIndividual> modeloSeleccionados;
    private Image imagenSeleccionada;
    private VentanaPrincipal ventanaPrincipal;

    public VentanaEditarGrupo(Grupo grupo, VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        setTitle("AppChat - Crear Grupo");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(34, 34, 34));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(new Color(34, 34, 34));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNombre = crearLabel("Nombre del grupo:");
        gbc.gridx = 0; gbc.gridy = 0;
        centro.add(lblNombre, gbc);

        nombreField = crearTextField();
        nombreField.setText(grupo.getNombre());
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centro.add(nombreField, gbc);

        JLabel lblDescripcion = crearLabel("Descripción:");
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        centro.add(lblDescripcion, gbc);

        descripcionArea = new JTextArea(3, 20);
        descripcionArea.setBackground(new Color(64, 64, 64));
        descripcionArea.setForeground(Color.WHITE);
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        descripcionArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        descripcionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descripcionArea.setText(grupo.getEstado() != null ? grupo.getEstado() : "");
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centro.add(new JScrollPane(descripcionArea), gbc);

        JLabel lblImagen = crearLabel("Imagen:");
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        centro.add(lblImagen, gbc);

        JButton btnSeleccionarImagen = crearBoton("Seleccionar Imagen");
        gbc.gridx = 1; gbc.gridy = 2;
        centro.add(btnSeleccionarImagen, gbc);

        imagenSeleccionada = grupo.getFoto();
        imageLabel = new JLabel(new ImageIcon(imagenSeleccionada.getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        gbc.gridx = 1; gbc.gridy = 3;
        centro.add(imageLabel, gbc);

        mainPanel.add(centro, BorderLayout.CENTER);

        modeloDisponibles = new DefaultListModel<>();
        modeloSeleccionados = new DefaultListModel<>();

        for (var c : Controlador.getInstancia().getUsuarioActual().getContactos()) {
            if (c instanceof ContactoIndividual) {
            	ContactoIndividual contacto = (ContactoIndividual) c;
            	if (contacto.isContactoAgregado()) {
            		modeloDisponibles.addElement(contacto);
            	}
            }
        }
        
        for (var c : grupo.getParticipantes()) {
			if (c instanceof ContactoIndividual) {
				modeloSeleccionados.addElement((ContactoIndividual) c);
				if (modeloDisponibles.contains(c)) {
					modeloDisponibles.removeElement(c);
				}
			}
		}

        JList<ContactoIndividual> listDisponibles = new JList<>(modeloDisponibles);
        listDisponibles.setCellRenderer(new ContactCellRenderer());
        listDisponibles.setBackground(new Color(44, 44, 44));
        listDisponibles.setForeground(Color.WHITE);
        listDisponibles.setFixedCellHeight(75);

        JList<ContactoIndividual> listSeleccionados = new JList<>(modeloSeleccionados);
        listSeleccionados.setCellRenderer(new ContactCellRenderer());
        listSeleccionados.setBackground(new Color(44, 44, 44));
        listSeleccionados.setForeground(Color.WHITE);
        listSeleccionados.setFixedCellHeight(75);

        JScrollPane scrollDisponibles = new JScrollPane(listDisponibles);
        scrollDisponibles.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Contactos disponibles", TitledBorder.LEFT, TitledBorder.TOP, null, Color.WHITE));
        scrollDisponibles.getViewport().setBackground(new Color(34, 34, 34));

        JScrollPane scrollSeleccionados = new JScrollPane(listSeleccionados);
        scrollSeleccionados.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Contactos seleccionados", TitledBorder.LEFT, TitledBorder.TOP, null, Color.WHITE));
        scrollSeleccionados.getViewport().setBackground(new Color(34, 34, 34));

        JPanel listasPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        listasPanel.setBackground(new Color(34, 34, 34));
        scrollDisponibles.setPreferredSize(new Dimension(0, 200));
        scrollSeleccionados.setPreferredSize(new Dimension(0, 200));

        listasPanel.add(scrollDisponibles);
        listasPanel.add(scrollSeleccionados);

        JPanel contenedorListas = new JPanel(new BorderLayout());
        contenedorListas.setBackground(new Color(34, 34, 34));
        contenedorListas.add(listasPanel, BorderLayout.CENTER);

        mainPanel.add(contenedorListas, BorderLayout.NORTH);


        JPanel acciones = new JPanel();
        acciones.setBackground(new Color(34, 34, 34));
        JButton btnAgregar = crearBoton("→");
        JButton btnQuitar = crearBoton("←");
        JButton btnCrear = crearBoton("Editar Grupo");

        acciones.add(btnAgregar);
        acciones.add(btnQuitar);
        acciones.add(btnCrear);
        mainPanel.add(acciones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> moverSeleccion(listDisponibles, modeloDisponibles, modeloSeleccionados));
        btnQuitar.addActionListener(e -> moverSeleccion(listSeleccionados, modeloSeleccionados, modeloDisponibles));
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        btnCrear.addActionListener(e -> editarGrupo(grupo, e));
    }

    private void seleccionarImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                imagenSeleccionada = new ImageIcon(file.getAbsolutePath()).getImage();
                imageLabel.setIcon(new ImageIcon(imagenSeleccionada.getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
            }
        }
    }

    private void moverSeleccion(JList<ContactoIndividual> lista, DefaultListModel<ContactoIndividual> from, DefaultListModel<ContactoIndividual> to) {
        var selected = lista.getSelectedValue();
        if (selected != null) {
            from.removeElement(selected);
            to.addElement(selected);
        }
    }

    private void editarGrupo(Grupo grupo, ActionEvent e) {
        String nombre = nombreField.getText().trim();
        String descripcion = descripcionArea.getText().trim();
        List<ContactoIndividual> participantes = new ArrayList<>();
        modeloSeleccionados.elements().asIterator().forEachRemaining(participantes::add);

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del grupo es obligatorio.");
            return;
        }
        if (participantes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe añadir al menos un contacto al grupo.");
            return;
        }

        String foto = ConversorImagenes.imageToBase64(imagenSeleccionada);

        Controlador.getInstancia().editarGrupo(grupo, nombre, foto, descripcion, participantes);
        JOptionPane.showMessageDialog(this, "Grupo editado correctamente.");
        ventanaPrincipal.actualizarListaContactos(); 
        ventanaPrincipal.irAConversacion(grupo);     
        dispose();                                   

    }

    private JLabel crearLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }

    private JTextField crearTextField() {
        JTextField field = new JTextField(20);
        field.setBackground(new Color(64, 64, 64));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JButton crearBoton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
