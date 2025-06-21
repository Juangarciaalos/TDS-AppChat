package um.tds.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import um.tds.clases.Contacto;
import um.tds.clases.ContactoIndividual;
import um.tds.clases.Mensaje;
import um.tds.clases.Usuario;
import um.tds.controlador.Controlador;

@SuppressWarnings("serial")
/**
 * Clase que renderiza cada celda de la lista de contactos.
 */
public class ContactCellRenderer extends JPanel implements ListCellRenderer<Contacto> {

    private JLabel nameLabel;
    private JLabel mensajeLabel;
    private JLabel imageLabel;
    private JButton btnAdd;

    public ContactCellRenderer() {
        setLayout(new GridBagLayout());
        setOpaque(true);
        setBorder(new EmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(75, 75));
        add(imageLabel, gbc);

        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);

        mensajeLabel = new JLabel();
        mensajeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mensajeLabel.setForeground(Color.LIGHT_GRAY);
        mensajeLabel.setMaximumSize(new Dimension(280, 18));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(nameLabel);
        textPanel.add(mensajeLabel);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        add(textPanel, gbc);
        
        ImageIcon iconAdd = new ImageIcon("src/main/resources/add-user.png");
        Image imgAdd = iconAdd.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btnAdd = new JButton(new ImageIcon(imgAdd));
        btnAdd.setPreferredSize(new Dimension(30, 30));
        btnAdd.setToolTipText("Añadir contacto");
        styleIconButton(btnAdd);

        gbc.gridx = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        add(btnAdd, gbc);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> list, Contacto value, int index,
                                                  boolean isSelected, boolean cellHasFocus) {
        nameLabel.setText(value.getNombre());
        Usuario actual = Controlador.getInstancia().getUsuarioActual();
        ArrayList<Mensaje> mensajes = new ArrayList<>(value.getAllMensajes(actual));
        if (!mensajes.isEmpty()) {
            mensajes.sort(Comparator.comparing(Mensaje::getHoraEnvio));
            Mensaje ultimo = mensajes.get(mensajes.size() - 1);
            String preview;
            if (ultimo.getTexto().isBlank()) {
            	preview = "Emoji";
            }else {
            	preview = ultimo.getTexto();
            }
            
            mensajeLabel.setText(preview.length() <= 30 ? preview : preview.substring(0, 27) + "...");
        } else {
            mensajeLabel.setText("Sin mensajes");
        }
        imageLabel.setIcon(new ImageIcon(value.getFoto()));

        btnAdd.setVisible(false); 

        if (value instanceof ContactoIndividual) {
            ContactoIndividual ci = (ContactoIndividual) value;
            boolean noAgregado = !ci.isContactoAgregado();

            if (noAgregado) {
                btnAdd.setVisible(true);
                limpiarListeners(btnAdd); 
                btnAdd.addActionListener(e -> {
                    VentanaAddContacto ventana = new VentanaAddContacto();
                    ventana.setTelefono(String.valueOf(ci.getNumeroTelefono())); 
                    ventana.setVisible(true);
                });
            }
        }

        setBackground(isSelected ? new Color(64, 64, 64) : new Color(34, 34, 34));
        return this;
    }

    private void styleIconButton(JButton btn) {
        btn.setBackground(new Color(238, 238, 238));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(100, 100, 100)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(new Color(238, 238, 238)); }
        });
    }

    private void limpiarListeners(JButton button) {
        for (ActionListener al : button.getActionListeners()) {
            button.removeActionListener(al);
        }
    }
    
    public Rectangle getBotonBounds(JList<?> list) {
        this.doLayout();
        return btnAdd.isVisible() ? btnAdd.getBounds() : new Rectangle();
    }

}
