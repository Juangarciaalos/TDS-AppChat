package um.tds.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import tds.BubbleText;
import um.tds.clases.Contacto;
import um.tds.clases.Mensaje;
import um.tds.clases.Usuario;
import um.tds.controlador.Controlador;

public class VentanaChat extends JPanel implements Scrollable {

    private static final long serialVersionUID = 1L;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public VentanaChat() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(255, 255, 255));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setMinimumSize(new Dimension(500, 600));
        add(Box.createRigidArea(new Dimension(500, 100)));

        JLabel lblInicio = new JLabel("Bienvenido a AppChat!");
        lblInicio.setBackground(new Color(255, 255, 255));
        lblInicio.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblInicio.setForeground(new Color(0, 0, 0));
        lblInicio.setAlignmentX(CENTER_ALIGNMENT);
        add(lblInicio);
    }

    public void mostrarChat(Contacto contacto) {
        removeAll();
        add(Box.createHorizontalStrut(500));
        MensajesToBubbleText(contacto).forEach(this::add);
        revalidate();
        repaint();
    }

    public void enviarMensaje(String mensaje) {
        String fecha = LocalDateTime.now().format(formatter);
        BubbleText burbuja = new BubbleText(this, mensaje, Color.GREEN, "You - " + fecha, BubbleText.SENT, 12);
        add(burbuja);
        revalidate();
        repaint();
    }

    public void enviarEmoticono(int emoticono) {
        String fecha = LocalDateTime.now().format(formatter);
        BubbleText burbuja = new BubbleText(this, emoticono, Color.GREEN, "You - " + fecha, BubbleText.SENT, 12);
        add(burbuja);
        revalidate();
        repaint();
    }

    private List<BubbleText> MensajesToBubbleText(Contacto contacto) {
        Usuario actual = Controlador.getInstancia().getUsuarioActual();
        List<Mensaje> mensajes = contacto.getAllMensajes(actual);
        return mensajes.stream()
                .map(m -> {
                    boolean esActual = m.getEmisor().getNumeroTelefono() == actual.getNumeroTelefono();
                    String nombre = esActual ? actual.getNombre() : contacto.getNombre();
                    Color color = esActual ? Color.GREEN : Color.LIGHT_GRAY;
                    int tipo = esActual ? BubbleText.SENT : BubbleText.RECEIVED;
                    String fecha = " - " + m.getHoraEnvio().format(formatter);

                    if (isEmoticono(m)) {
                        int emoticono = m.getEmoticono();
                        return new BubbleText(this, emoticono, color, nombre + fecha, tipo, 12);
                    } else {
                        return new BubbleText(this, m.getTexto(), color, nombre + fecha, tipo, 12);
                    }
                })
                .collect(Collectors.toList());
    }

    private boolean isEmoticono(Mensaje mensaje) {
        return mensaje.getTexto().isEmpty();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
