package um.tds.ui;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import um.tds.Utilidades.ConversorImagenes;
import um.tds.controlador.Controlador;

@SuppressWarnings("serial")
/**
 * Ventana para mostrar y editar el perfil del usuario actual.
 * Permite cambiar el estado y la foto de perfil.
 */
public class VentanaPerfil extends JFrame {

    private JTextArea estadoArea;
    private JLabel imageLabel;
    private JButton btnSeleccionar;
    private JButton btnEliminar;
    private JButton btnGuardar;
    private JButton btnVolver;
    private JLabel seleccionLabel;
    public VentanaPerfil() {
        setTitle("Perfil de Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null);

        JPanel content = new JPanel();
        content.setBackground(new Color(34, 34, 34));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(content);

        estadoArea = new JTextArea(4, 20);
        estadoArea.setText(Controlador.getInstancia().getUsuarioActual().getEstado());
        estadoArea.setLineWrap(true);
        estadoArea.setWrapStyleWord(true);
        estadoArea.setRows(10); 
        estadoArea.setPreferredSize(new Dimension(250, 150));
        estadoArea.setMaximumSize(new Dimension(250, 150));
        estadoArea.setMinimumSize(new Dimension(250, 150));
        JScrollPane estadoScroll = new JScrollPane(estadoArea);
        Dimension scrollSize = new Dimension(250, 150);
        estadoScroll.setPreferredSize(scrollSize);
        estadoScroll.setMaximumSize(scrollSize);
        estadoScroll.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));

        
        imageLabel = new JLabel(new ImageIcon(Controlador.getInstancia().getUsuarioActual().getFoto().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        imageLabel.setPreferredSize(new Dimension(80, 80));
        imageLabel.setToolTipText("src/main/resources/default-user.png");

        seleccionLabel = new JLabel("Selecciona una imagen o arrástrala aquí", SwingConstants.CENTER);
        seleccionLabel.setPreferredSize(new Dimension(250, 40));
        seleccionLabel.setOpaque(true);
        seleccionLabel.setBackground(new Color(64, 64, 64));
        seleccionLabel.setForeground(Color.WHITE);
        seleccionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        seleccionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        seleccionLabel.setVerticalAlignment(SwingConstants.CENTER);
		seleccionLabel.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        seleccionLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarImagen();
            }
        });

        JPanel dragPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        dragPanel.setBackground(new Color(34, 34, 34));
        dragPanel.add(imageLabel);
        dragPanel.add(Box.createHorizontalStrut(10));
        dragPanel.add(seleccionLabel);

        dragPanel.setDropTarget(new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked")
					List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (!droppedFiles.isEmpty()) {
                        File file = droppedFiles.get(0);
                        ImageIcon img = new ImageIcon(new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
                        imageLabel.setIcon(img);
                        imageLabel.setToolTipText(file.getAbsolutePath());
                        seleccionLabel.setVisible(false);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnSeleccionar = new JButton("Seleccionar Imagen");
        btnSeleccionar.addActionListener(e -> seleccionarImagen());
        applyButtonStyle(btnSeleccionar);

        btnEliminar = new JButton("Eliminar Imagen");
        applyButtonStyle(btnEliminar);
        btnEliminar.addActionListener(e -> {
            imageLabel.setIcon(null);
            seleccionLabel.setVisible(true);
        });

        btnGuardar = new JButton("Guardar Cambios");
        applyButtonStyle(btnGuardar);
        btnGuardar.addActionListener(e -> guardarCambios());

        btnVolver = new JButton("Volver");
        applyButtonStyle(btnVolver);
        btnVolver.addActionListener(e -> {
            dispose();
        });

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(34, 34, 34));

        formPanel.add(new JLabel("Estado:") {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }});
        formPanel.add(Box.createVerticalStrut(5));
        JPanel estadoPanel = new JPanel();
        estadoPanel.setLayout(new BorderLayout());
        estadoPanel.setMaximumSize(new Dimension(250, 70));
        estadoPanel.setPreferredSize(new Dimension(250, 70));
        estadoPanel.setMinimumSize(new Dimension(250, 70));
        estadoPanel.setBackground(new Color(34, 34, 34));
        estadoPanel.add(estadoScroll, BorderLayout.CENTER);
        formPanel.add(estadoPanel);        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(new JLabel("Imagen:") {{
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
        }});
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(dragPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(btnSeleccionar);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(btnEliminar);

        JPanel buttons = new JPanel();
        buttons.setBackground(new Color(34, 34, 34));
        buttons.add(btnVolver);
        buttons.add(btnGuardar);

        content.setLayout(new BorderLayout(0, 20));
        content.add(formPanel, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
    }

    private void seleccionarImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                ImageIcon img = new ImageIcon(new ImageIcon(file.getAbsolutePath()).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
                imageLabel.setIcon(img);
                imageLabel.setToolTipText(file.getAbsolutePath());
                seleccionLabel.setVisible(false);
            }
        }
    }

    private void guardarCambios() {
        String nuevoEstado = estadoArea.getText();
        String rutaImagen = imageLabel.getToolTipText();

        Controlador.getInstancia().cambiarEstado(nuevoEstado);

        if (rutaImagen != null && new File(rutaImagen).exists()) {
            File archivo = new File(rutaImagen);
            String imagenCodificada = ConversorImagenes.imageToBase64(archivo);
            Controlador.getInstancia().cambiarFotoPerfil(imagenCodificada);
        }

        JOptionPane.showMessageDialog(this, "Cambios guardados correctamente", "Perfil", JOptionPane.INFORMATION_MESSAGE);
    }



    private void applyButtonStyle(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
