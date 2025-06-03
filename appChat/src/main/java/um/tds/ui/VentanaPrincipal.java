package um.tds.ui;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import um.tds.clases.Usuario;

public class VentanaPrincipal extends JPanel {

    private JComboBox<String> filtroCombo;
    private JButton botonCompartir;
    private JButton botonBusqueda;
    private JList<Usuario> listaContactos;
    private DefaultListModel<Usuario> modelContactos;
    private JPanel panelChatContainer;

    private Map<Usuario, VentanaChat> chats = new HashMap<>();

    public VentanaPrincipal(List<Usuario> contactos) {
        setLayout(new BorderLayout());
        setBackground(new Color(34, 34, 34));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setOpaque(false);

        filtroCombo = new JComboBox<>(new String[]{"Contacto o teléfono"});
        filtroCombo.setMaximumSize(new Dimension(200, 30));
        styleCombo(filtroCombo);

        botonCompartir = new JButton("➤");
        styleIconButton(botonCompartir);

        botonBusqueda = new JButton("🔍");
        styleIconButton(botonBusqueda);

        topBar.add(filtroCombo);
        topBar.add(Box.createHorizontalStrut(5));
        topBar.add(botonCompartir);
        topBar.add(Box.createHorizontalStrut(5));
        topBar.add(botonBusqueda);

        add(topBar, BorderLayout.NORTH);

        modelContactos = new DefaultListModel<>();
        for (Usuario u : contactos) modelContactos.addElement(u);

        listaContactos = new JList<>(modelContactos);
        listaContactos.setCellRenderer(new PerfilUsuario());
        listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaContactos.setFixedCellHeight(60);
        listaContactos.setBackground(new Color(34, 34, 34));
        listaContactos.setForeground(Color.WHITE);
        listaContactos.setSelectionBackground(new Color(64, 64, 64));

        JScrollPane scroll = new JScrollPane(listaContactos);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        scroll.getViewport().setBackground(new Color(34, 34, 34));

        add(scroll, BorderLayout.WEST);

        panelChatContainer = new JPanel(new BorderLayout());
        panelChatContainer.setBackground(Color.WHITE);
        panelChatContainer.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));

        add(panelChatContainer, BorderLayout.CENTER);

        listaContactos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!listaContactos.isSelectionEmpty() && e.getClickCount() == 1) {
                    Usuario seleccionado = listaContactos.getSelectedValue();
                    if (seleccionado != null) {
                        VentanaChat chat = chats.get(seleccionado);
                        if (chat == null) {
                            chat = new VentanaChat();
                            chats.put(seleccionado, chat);
                        }

                        panelChatContainer.removeAll();
                        panelChatContainer.add(chat.getContentPane(), BorderLayout.CENTER);
                        panelChatContainer.revalidate();
                        panelChatContainer.repaint();
                    }
                }
            }
        });
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setBackground(new Color(64, 64, 64));
        combo.setForeground(Color.WHITE);
        combo.setFocusable(false);
        combo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    private void styleIconButton(JButton btn) {
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD, 14f));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(100, 100, 100));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(80, 80, 80));
            }
        });
    }

    public static void main(String[] args) {
        Usuario u1 = new Usuario("Juan", "Pérez", 612216123, "1234", "Juan@gmail.com", LocalDate.of(1990, 1, 1));
        Usuario u2 = new Usuario("Ana", "García", 213415135, "1234", "Ana@gmail.com", LocalDate.of(1995, 2, 2));
        Usuario u3 = new Usuario("Carlos", "López", 213143255, "1234", "Carlos@gmail.com", LocalDate.of(1992, 3, 3));
        java.util.List<Usuario> lista = java.util.Arrays.asList(u1, u2, u3);

        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Mensajes");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1000, 600);
            f.setLocationRelativeTo(null);
            f.add(new VentanaPrincipal(lista));
            f.setVisible(true);
        });
    }
}
