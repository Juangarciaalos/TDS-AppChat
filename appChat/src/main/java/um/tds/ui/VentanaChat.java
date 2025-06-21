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

/**
 * Panel que muestra el chat con un contacto específico.
 */

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
        scrollToBottom();
    }
    
    public void mostrarChatYResaltar(Contacto contacto, Mensaje mensajeObjetivo) {
        removeAll();
        add(Box.createHorizontalStrut(500));

        Usuario actual = Controlador.getInstancia().getUsuarioActual();
        List<Mensaje> mensajes = contacto.getAllMensajes(actual);
        
        final BubbleText[] burbujaObjetivo = new BubbleText[1];

        for (Mensaje m : mensajes) {
            boolean esActual = m.getEmisor().getNumeroTelefono() == actual.getNumeroTelefono();
            String nombre = esActual ? actual.getNombre() : contacto.getNombre();
            Color color = esActual ? Color.GREEN : Color.LIGHT_GRAY;
            int tipo = esActual ? BubbleText.SENT : BubbleText.RECEIVED;
            String fecha = " - " + m.getHoraEnvio().format(formatter);

            BubbleText burbuja;
            if (isEmoticono(m)) {
                burbuja = new BubbleText(this, m.getEmoticono(), color, nombre + fecha, tipo, 12);
            } else {
                burbuja = new BubbleText(this, m.getTexto(), color, nombre + fecha, tipo, 12);
            }

            add(burbuja);

            if (m.equals(mensajeObjetivo)) {
                burbujaObjetivo[0] = burbuja;
            }
        }

        revalidate();
        repaint();

        SwingUtilities.invokeLater(() -> {
            if (burbujaObjetivo[0] != null) {
                burbujaObjetivo[0].scrollRectToVisible(new Rectangle(0, 0, burbujaObjetivo[0].getWidth(), burbujaObjetivo[0].getHeight()));
            }
        });
    }



    public void enviarMensaje(String mensaje) {
        String fecha = LocalDateTime.now().format(formatter);
        BubbleText burbuja = new BubbleText(this, mensaje, Color.GREEN, "You - " + fecha, BubbleText.SENT, 12);
        add(burbuja);
        revalidate();
        repaint();
        scrollToBottom();
    }

    public void enviarEmoticono(int emoticono) {
        String fecha = LocalDateTime.now().format(formatter);
        BubbleText burbuja = new BubbleText(this, emoticono, Color.GREEN, "You - " + fecha, BubbleText.SENT, 12);
        add(burbuja);
        revalidate();
        repaint();
        scrollToBottom();
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
    
    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            Container parent = getParent();
            if (parent instanceof JViewport viewport) {
                JScrollPane scrollPane = (JScrollPane) viewport.getParent();
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
                verticalBar.setValue(verticalBar.getMaximum());
            }
        });
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
