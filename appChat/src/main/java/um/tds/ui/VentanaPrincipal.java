package um.tds.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.swing.*;
import javax.swing.border.*;

import um.tds.clases.*;
import um.tds.controlador.Controlador;
import tds.BubbleText;

@SuppressWarnings("serial")
/* 
 * Ventana principal de la aplicación appchat.
 * */
public class VentanaPrincipal extends JFrame {

    private JButton botonIniciarConversacion;
    private JButton botonBusqueda;
    private JButton botonAddContacto;
    private JButton botonCrearGrupo; 
    private JTextField campoBusqueda;
    private JList<Contacto> listaContactos;
    private DefaultListModel<Contacto> modelContactos;
    private JPanel panelChatContainer;
    private JPanel panelContactos;
    private VentanaChat chatPanel;
    private JTextField fieldMensaje;
    
    private static final double PRECIO_PREMIUM = 25;
    
    public VentanaPrincipal() {
        setTitle("AppChat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(34, 34, 34));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel topBar = new JPanel();
        topBar.setBackground(new Color(255, 255, 255));
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(0, 0, 10, 0));

        campoBusqueda = new JTextField();
        campoBusqueda.setText("contacto o teléfono");
        campoBusqueda.setMaximumSize(new Dimension(200, 30));
        campoBusqueda.setPreferredSize(new Dimension(200, 30));
        campoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campoBusqueda.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        campoBusqueda.setBackground(new Color(64, 64, 64));
        campoBusqueda.setForeground(Color.WHITE);
        campoBusqueda.setCaretColor(Color.WHITE);

        botonIniciarConversacion = new JButton("➤");
        botonIniciarConversacion.setToolTipText("Iniciar conversación");
        styleIconButton(botonIniciarConversacion);
        botonIniciarConversacion.addActionListener(e -> {
            String entrada = campoBusqueda.getText().trim();
            if (entrada.isEmpty()) return;

            Optional<Contacto> resultado = Controlador.getInstancia().iniciarConversacion(entrada);

            if (resultado.isPresent()) {
            	actualizarListaContactos();
                Contacto contacto = resultado.get();
                irAConversacion(contacto);
                campoBusqueda.setText("contacto o teléfono");
            } else {
                JOptionPane.showMessageDialog(this, "No se ha encontrado ningún contacto o número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        botonBusqueda = new JButton("🔍");
        botonBusqueda.setToolTipText("Buscar mensajes");
        styleIconButton(botonBusqueda);
        botonBusqueda.addActionListener(e -> {
            VentanaBusquedaMensajes ventana = new VentanaBusquedaMensajes(this);
            ventana.addWindowListener(new WindowAdapter(){});
            ventana.setVisible(true);
        });

        ImageIcon iconAdd = new ImageIcon("src/main/resources/add-user.png");
        Image imgAdd = iconAdd.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        botonAddContacto = new JButton(new ImageIcon(imgAdd));
        botonAddContacto.setPreferredSize(new Dimension(30, 30));
        styleIconButton(botonAddContacto);
        botonAddContacto.setToolTipText("Añadir nuevo contacto");
        botonAddContacto.addActionListener(e -> {
            VentanaAddContacto ventana = new VentanaAddContacto();
            ventana.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    actualizarListaContactos();
                }
            });
            ventana.setVisible(true);
        });

        ImageIcon iconAddGrupo = new ImageIcon("src/main/resources/add-grupo.png");
        Image imgAddGrupo = iconAddGrupo.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        botonCrearGrupo = new JButton(new ImageIcon(imgAddGrupo));
        styleIconButton(botonCrearGrupo);
        botonCrearGrupo.setToolTipText("Crear nuevo grupo");
        botonCrearGrupo.addActionListener(e -> {
            VentanaCrearGrupo ventana = new VentanaCrearGrupo();
            ventana.setVisible(true);
        });
        
        ImageIcon iconPremium = new ImageIcon("src/main/resources/premium.png");
        Image imgPremium = iconPremium.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton botonPremium = new JButton(new ImageIcon(imgPremium));
        styleIconButton(botonPremium);
        botonPremium.setToolTipText("Hazte Premium");
        botonPremium.addActionListener(e -> {
        	if (Controlador.getInstancia().getUsuarioActual().isPremium()) {
        		JOptionPane.showMessageDialog(this, "Ya eres un usuario Premium.", "Información", JOptionPane.INFORMATION_MESSAGE);
        	} else {
	            List<Descuento> descuentos = FactoriaDescuentos.getDescuento(Controlador.getInstancia().getUsuarioActual());
	            descuentos.stream()
	                .sorted((d1, d2) -> Double.compare(d2.getDescuento(PRECIO_PREMIUM), d1.getDescuento(PRECIO_PREMIUM)));
	            
	            VentanaPremium ventanaPremium = new VentanaPremium(descuentos);
	            ventanaPremium.setVisible(true);
        	}
        });
        
        ImageIcon iconoPDF = new ImageIcon("src/main/resources/pdf-file.png");
        Image imgPDF = iconoPDF.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton botonExportarPDF = new JButton(new ImageIcon(imgPDF));
        styleIconButton(botonExportarPDF);
        botonExportarPDF.setToolTipText("Exportar chat como PDF");
        botonExportarPDF.addActionListener(e -> {
            Contacto contactoSeleccionado = listaContactos.getSelectedValue();
            if (contactoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Selecciona una conversación antes de exportar.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.home")));
            fileChooser.setDialogTitle("Guardar chat como PDF");
            fileChooser.setSelectedFile(new File("chat_" + contactoSeleccionado.getNombre() + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                Controlador.getInstancia().generarPDF(contactoSeleccionado, fileToSave.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Chat exportado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            }
        });
        
        ImageIcon iconLogout = new ImageIcon("src/main/resources/logout.png"); 
        Image imgLogout = iconLogout.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton botonCerrarSesion = new JButton(new ImageIcon(imgLogout));
        styleIconButton(botonCerrarSesion);
        botonCerrarSesion.setToolTipText("Cerrar sesión");
        botonCerrarSesion.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Quieres cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                Controlador.getInstancia().cerrarSesion();
                dispose(); 
                new VentanaLogin().setVisible(true); 
            }
        });
        
        topBar.add(Box.createHorizontalStrut(5));
        topBar.add(campoBusqueda);
        topBar.add(Box.createHorizontalStrut(5));
        topBar.add(botonIniciarConversacion);
        topBar.add(Box.createHorizontalStrut(5));
        topBar.add(botonBusqueda);
        topBar.add(Box.createHorizontalStrut(5));
        topBar.add(botonAddContacto);
        topBar.add(Box.createHorizontalStrut(5));
        topBar.add(botonCrearGrupo); 
        topBar.add(Box.createHorizontalStrut(5));
        topBar.add(botonPremium);
        topBar.add(Box.createHorizontalStrut(5));
        topBar.add(botonExportarPDF);
        topBar.add(Box.createHorizontalGlue());
        topBar.add(botonCerrarSesion);

        contentPane.add(topBar, BorderLayout.NORTH);

        modelContactos = new DefaultListModel<>();
        Controlador.getInstancia().cargarContactosSinAgregar();
        listaContactos = new JList<>(modelContactos);
        
        listaContactos.setCellRenderer(new ContactCellRenderer());
        listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaContactos.setFixedCellHeight(75);
        listaContactos.setBackground(new Color(34, 34, 34));
        listaContactos.setForeground(Color.WHITE);
        listaContactos.setSelectionBackground(new Color(64, 64, 64));

        JScrollPane scroll = new JScrollPane(listaContactos);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        scroll.getViewport().setBackground(new Color(34, 34, 34));
        scroll.setPreferredSize(new Dimension(400, 0));

        contentPane.add(scroll, BorderLayout.WEST);
        actualizarListaContactos();

        panelContactos = new JPanel(new BorderLayout());
        panelContactos.setBackground(new Color(51, 51, 51));
        panelContactos.setBorder(new TitledBorder(new LineBorder(Color.BLACK), "Mensajes con contactos", TitledBorder.LEADING, TitledBorder.TOP, null, Color.WHITE));

        panelChatContainer = new JPanel(new BorderLayout());
        panelChatContainer.setForeground(new Color(84, 84, 84));
        panelChatContainer.setBackground(new Color(84, 84, 84));
        panelChatContainer.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        panelContactos.add(panelChatContainer, BorderLayout.CENTER);

        chatPanel = new VentanaChat();
        chatPanel.setForeground(new Color(255, 255, 255));
        chatPanel.setBackground(new Color(238, 238, 238));
        JScrollPane chatScroll = new JScrollPane(chatPanel);
        panelChatContainer.add(chatScroll, BorderLayout.CENTER);

        fieldMensaje = new JTextField();
        fieldMensaje.setBackground(new Color(255, 255, 255));
        fieldMensaje.setForeground(new Color(0, 0, 0));
        fieldMensaje.setCaretColor(Color.WHITE);
        fieldMensaje.setPreferredSize(new Dimension(400, 30));
        fieldMensaje.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        fieldMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fieldMensaje.addActionListener(e -> {
        	enviarMensaje();
        	actualizarListaContactos();
        });

        ImageIcon iconEnviar = new ImageIcon("src/main/resources/send.png");
        Image imgEnviar = iconEnviar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton btnSend = new JButton(new ImageIcon(imgEnviar));
        btnSend.setBackground(Color.WHITE);
        btnSend.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSend.addActionListener(e -> {
        	enviarMensaje();
        	actualizarListaContactos();
        });
        
        ImageIcon iconEmoji = BubbleText.getEmoji(0);
        JButton btnEmoji = new JButton(iconEmoji);
        btnEmoji.setPreferredSize(new Dimension(30, 30));
        styleIconButton(btnEmoji);
        btnEmoji.addActionListener(e -> mostrarVentanaEmojis(btnEmoji));

        
        JPanel mensajePanel = new JPanel(new BorderLayout());
        mensajePanel.setBackground(new Color(52, 52, 52));
        mensajePanel.add(btnEmoji, BorderLayout.WEST);
        mensajePanel.add(fieldMensaje, BorderLayout.CENTER);
        mensajePanel.add(btnSend, BorderLayout.EAST);
        panelContactos.add(mensajePanel, BorderLayout.SOUTH);

        contentPane.add(panelContactos, BorderLayout.CENTER);

        listaContactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = listaContactos.locationToIndex(e.getPoint());
                if (index < 0) return;

                Contacto contacto = modelContactos.getElementAt(index);
                Rectangle cellBounds = listaContactos.getCellBounds(index, index);
                Point clickInCell = new Point(e.getX() - cellBounds.x, e.getY() - cellBounds.y);


                ContactCellRenderer renderer = new ContactCellRenderer();
                Component renderizado = renderer.getListCellRendererComponent(listaContactos, contacto, index, false, false);
                renderizado.setBounds(0, 0, cellBounds.width, cellBounds.height); 

                Rectangle botonBounds = renderer.getBotonBounds(listaContactos);

                if (botonBounds.contains(clickInCell)) {
                    if (contacto instanceof ContactoIndividual ci && !ci.isContactoAgregado()) {
                        VentanaAddContacto ventana = new VentanaAddContacto();
                        ventana.setTelefono(String.valueOf(ci.getNumeroTelefono()));
                        ventana.setVisible(true);
                    }
                    return; 
                }


                cargarChat(contacto);
            }
        });
        
        Usuario usuarioActual = Controlador.getInstancia().getUsuarioActual();

        JPanel panelPerfil = new JPanel();
        panelPerfil.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelPerfil.setOpaque(false);
        ImageIcon iconoOriginal = new ImageIcon(usuarioActual.getFoto());
        Image imagen = iconoOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon iconoEscalado = new ImageIcon(imagen);
        JLabel imageLabel = new JLabel(iconoEscalado);
        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        imageLabel.setToolTipText("Haz clic para ver perfil");
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VentanaPerfil ventana = new VentanaPerfil();
                ventana.setVisible(true);
            }
        });
        
        JLabel nombreLabel = new JLabel(usuarioActual.getNombre());
        nombreLabel.setForeground(Color.WHITE);
        nombreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panelPerfil.add(imageLabel);
        panelPerfil.add(nombreLabel);
        topBar.add(Box.createHorizontalStrut(10));
        topBar.add(panelPerfil);

        SwingUtilities.invokeLater(() -> getRootPane().requestFocusInWindow());


    }

    private void actualizarListaContactos() {
        Contacto contactoSeleccionado = listaContactos.getSelectedValue(); 

        modelContactos.clear();
        List<Contacto> contactos = Controlador.getInstancia().getContactos();
        Usuario actual = Controlador.getInstancia().getUsuarioActual();

        contactos.stream()
            .sorted((c1, c2) -> {
                List<Mensaje> m1 = c1.getAllMensajes(actual);
                List<Mensaje> m2 = c2.getAllMensajes(actual);

                LocalDateTime t1 = m1.isEmpty() ? LocalDateTime.MIN : m1.get(m1.size() - 1).getHoraEnvio();
                LocalDateTime t2 = m2.isEmpty() ? LocalDateTime.MIN : m2.get(m2.size() - 1).getHoraEnvio();

                return t2.compareTo(t1);
            })
            .forEach(modelContactos::addElement);

        if (contactoSeleccionado != null && modelContactos.contains(contactoSeleccionado)) {
            listaContactos.setSelectedValue(contactoSeleccionado, true);
        }
    }


    private void enviarMensaje() {
        String texto = fieldMensaje.getText();
        Contacto contacto = listaContactos.getSelectedValue();
        if (texto != null && !texto.trim().isEmpty() && contacto != null) {
            Controlador.getInstancia().enviarMensaje(texto, contacto);
            chatPanel.enviarMensaje(texto);
            fieldMensaje.setText("");
            chatPanel.scrollRectToVisible(new Rectangle(0, chatPanel.getHeight(), 1, 1));
        }
    }
    
    public void irAConversacion(Contacto contacto) {
        listaContactos.setSelectedValue(contacto, true);
        cargarChat(contacto);
    }
    
    public void irAConversacion(Contacto contacto, Mensaje mensajeDestacado) {
        listaContactos.setSelectedValue(contacto, true);
        chatPanel.mostrarChatYResaltar(contacto, mensajeDestacado);
        SwingUtilities.invokeLater(() -> fieldMensaje.requestFocusInWindow());
    }

    
    private void mostrarVentanaEmojis(JButton origen) {
        JPopupMenu menuEmojis = new JPopupMenu();
        menuEmojis.setBackground(Color.WHITE);

        int total = BubbleText.MAXICONO;
        int columnas = 6;
        int filas = (int) Math.ceil((double) (total + 1) / columnas);

        JPanel panel = new JPanel(new GridLayout(filas, columnas, 5, 5));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setBackground(Color.WHITE);

        for (int i = 0; i <= total; i++) {
            ImageIcon icon = BubbleText.getEmoji(i);
            JButton emojiBtn = new JButton(icon);
            emojiBtn.setFocusPainted(false);
            emojiBtn.setBackground(Color.WHITE);
            emojiBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            emojiBtn.setBorder(BorderFactory.createEmptyBorder());
            int emoticono = i;

            emojiBtn.addActionListener(e -> {
                Contacto contacto = listaContactos.getSelectedValue();
                if (contacto != null) {
                    Controlador.getInstancia().enviarEmoji(emoticono, contacto);
                    chatPanel.enviarEmoticono(emoticono);
                    chatPanel.scrollRectToVisible(new Rectangle(0, chatPanel.getHeight(), 1, 1));
                    actualizarListaContactos();
                } else {
                    JOptionPane.showMessageDialog(this, "Selecciona un contacto para enviar un emoji.", "Atención", JOptionPane.WARNING_MESSAGE);
                }
                menuEmojis.setVisible(false);
            });

            panel.add(emojiBtn);
        }

        menuEmojis.add(panel);
        Dimension size = menuEmojis.getPreferredSize();
        menuEmojis.show(origen, 0, -size.height);
    }
    
    private void cargarChat(Contacto contacto) {
        listaContactos.setSelectedValue(contacto, true);
        chatPanel.mostrarChat(contacto);
        SwingUtilities.invokeLater(() -> fieldMensaje.requestFocusInWindow());
    }

    private void styleIconButton(JButton btn) {
        btn.setBackground(new Color(238, 238, 238));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(100, 100, 100));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(238, 238, 238));
            }
        });
    }
    
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}