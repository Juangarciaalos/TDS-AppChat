package um.tds.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import um.tds.clases.Descuento;
import um.tds.controlador.Controlador;

@SuppressWarnings("serial")
/**
 * Ventana para que el usuario pueda seleccionar un descuento y pagar la suscripción premium.
 * Muestra una lista desplegable con los descuentos disponibles y el precio final después de aplicar el descuento.
 */
public class VentanaPremium extends JFrame {

    private static final double PRECIO_PREMIUM = 25;

    private JComboBox<Descuento> comboDescuentos;
    private JLabel lblPrecio;

    public VentanaPremium(List<Descuento> descuentos) {
        setTitle("Premium - AppChat");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(34, 34, 34));

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(new Color(34, 34, 34));

        JLabel lblSelecciona = new JLabel("Selecciona uno de tus descuentos aplicables:");
        lblSelecciona.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSelecciona.setForeground(Color.WHITE);
        lblSelecciona.setAlignmentX(Component.CENTER_ALIGNMENT);

        comboDescuentos = new JComboBox<>();
        comboDescuentos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Descuento) {
                    setText(((Descuento) value).getNombre());
                }
                return this;
            }
        });
        comboDescuentos.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboDescuentos.setMaximumSize(new Dimension(300, 30));
        comboDescuentos.setBackground(new Color(64, 64, 64));
        comboDescuentos.setForeground(Color.WHITE);

        for (Descuento d : descuentos) {
            comboDescuentos.addItem(d);
        }

        lblPrecio = new JLabel("Precio final: " + descuentos.get(0).getDescuento(PRECIO_PREMIUM) + "€");
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblPrecio.setForeground(Color.WHITE);
        lblPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);

        comboDescuentos.addActionListener(this::actualizarPrecio);

        centro.add(lblSelecciona);
        centro.add(Box.createVerticalStrut(10));
        centro.add(comboDescuentos);
        centro.add(Box.createVerticalStrut(20));
        centro.add(lblPrecio);

        JButton btnPagar = new JButton("Pagar");
        btnPagar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnPagar.setBackground(new Color(80, 80, 80));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setFocusPainted(false);
        btnPagar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPagar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "¡Felicidades! Ahora eres usuario premium.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            Controlador.getInstancia().setPremium(true);
            dispose();
        });

        JPanel sur = new JPanel();
        sur.setBackground(new Color(34, 34, 34));
        sur.add(btnPagar);

        panel.add(centro, BorderLayout.CENTER);
        panel.add(sur, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private void actualizarPrecio(ActionEvent e) {
        Descuento d = (Descuento) comboDescuentos.getSelectedItem();
        if (d != null) {
            double precioFinal = d.getDescuento(PRECIO_PREMIUM);
            lblPrecio.setText("Precio final: " + precioFinal + "€");
        }
    }
}
