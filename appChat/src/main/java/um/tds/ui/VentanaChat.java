package um.tds.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import tds.BubbleText;

public class VentanaChat extends JFrame {

    private JPanel contentPane;
    private JPanel panelChat;
    private JTextField textFieldMensaje;
    private JScrollPane scrollPane;

    public VentanaChat() {
        BubbleText.noZoom(); 

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 600);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(30, 30, 30));
        setContentPane(contentPane);

        panelChat = new JPanel();
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.Y_AXIS));
        panelChat.setBackground(new Color(30, 30, 30));

        scrollPane = new JScrollPane(panelChat);
        scrollPane.setBorder(null);
        scrollPane.setBackground(new Color(30, 30, 30));
        scrollPane.getViewport().setBackground(new Color(30, 30, 30));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(new Color(30, 30, 30));

        textFieldMensaje = new JTextField();
        textFieldMensaje.setBackground(new Color(60, 60, 60));
        textFieldMensaje.setForeground(Color.WHITE);
        textFieldMensaje.setCaretColor(Color.WHITE);
        textFieldMensaje.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton botonEnviar = new JButton("Sent");
        botonEnviar.setBackground(new Color(80, 80, 80));
        botonEnviar.setForeground(Color.WHITE);
        botonEnviar.setFocusPainted(false);
        botonEnviar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        botonEnviar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel labelEmoji = new JLabel(BubbleText.getEmoji(0));
        labelEmoji.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        labelEmoji.setToolTipText("Haz clic para elegir un emoji");

        panelInferior.add(labelEmoji, BorderLayout.WEST);
        panelInferior.add(textFieldMensaje, BorderLayout.CENTER);
        panelInferior.add(botonEnviar, BorderLayout.EAST);
        contentPane.add(panelInferior, BorderLayout.SOUTH);

        int columnas = 6;
        int totalEmojis = BubbleText.MAXICONO + 1;
        int filas = (int) Math.ceil(totalEmojis / (double) columnas);

        JPanel gridPanel = new JPanel(new GridLayout(filas, columnas, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gridPanel.setBackground(Color.WHITE);

        JPopupMenu popup = new JPopupMenu();
        popup.setLayout(new BorderLayout());
        popup.add(gridPanel, BorderLayout.CENTER);

        for (int i = 0; i < totalEmojis; i++) {
            ImageIcon icono = BubbleText.getEmoji(i);
            JButton botonEmoji = new JButton(icono);
            botonEmoji.setMargin(new Insets(2, 2, 2, 2));
            botonEmoji.setContentAreaFilled(false);
            botonEmoji.setBorderPainted(false);
            botonEmoji.setFocusPainted(false);

            final int emojiId = i;
            botonEmoji.addActionListener(e -> {
                popup.setVisible(false);
                agregarEmoji("Usuario", emojiId, Color.GREEN, BubbleText.SENT, 24);
                agregarEmoji("Bot", (emojiId + 1) % totalEmojis, Color.LIGHT_GRAY, BubbleText.RECEIVED, 24);
            });

            gridPanel.add(botonEmoji);
        }

        labelEmoji.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popup.show(labelEmoji, 0, labelEmoji.getHeight());
            }
        });

        botonEnviar.addActionListener(e -> {
            String texto = textFieldMensaje.getText().trim();
            if (!texto.isEmpty()) {
                agregarMensaje("Usuario", texto, Color.GREEN, BubbleText.SENT);
                textFieldMensaje.setText("");
                agregarMensaje("Bot", "Respuesta automática", Color.LIGHT_GRAY, BubbleText.RECEIVED);
            }
        });
    }

    void agregarMensaje(String nombre, String texto, Color color, int tipo) {
        BubbleText burbuja = new BubbleText(panelChat, texto, color, nombre, tipo);
        panelChat.add(burbuja);
        panelChat.revalidate();
        scrollToBottom();
    }

    private void agregarEmoji(String nombre, int emojiId, Color color, int tipo, int tamaño) {
        if (emojiId >= 0 && emojiId <= BubbleText.MAXICONO) {
            BubbleText burbuja = new BubbleText(panelChat, emojiId, color, nombre, tipo, tamaño);
            panelChat.add(burbuja);
            panelChat.revalidate();
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                VentanaChat frame = new VentanaChat();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
