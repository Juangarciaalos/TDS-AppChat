package um.tds.ui;

import um.tds.clases.Contacto;
import um.tds.clases.Mensaje;
import um.tds.clases.Usuario;
import um.tds.controlador.Controlador;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;

public class VentanaBusquedaMensajes extends JFrame {
    private JTextField textoField;
    private JTextField emisorField;
    private JTextField receptorField;
    private DefaultListModel<Mensaje> modeloMensajes;
    private JList<Mensaje> listaMensajes;
    private JComboBox<String> comboFiltro;

    public VentanaBusquedaMensajes() {
        setTitle("AppChat - Buscar Mensajes");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelMain.setBackground(new Color(34, 34, 34));
        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(panelMain);

        JLabel titulo = new JLabel("Búsqueda de Mensajes", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(titulo);
        panelMain.add(Box.createVerticalStrut(15));

        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setBackground(new Color(34, 34, 34));
        panelFiltros.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "Filtros", TitledBorder.LEADING, TitledBorder.TOP, null, Color.WHITE));

        JLabel lblTexto = new JLabel("Texto:");
        styleLabel(lblTexto);
        textoField = new JTextField(20);
        styleField(textoField);
        GridBagConstraints gbcTextoLabel = new GridBagConstraints();
        gbcTextoLabel.insets = new Insets(8, 8, 8, 8);
        gbcTextoLabel.anchor = GridBagConstraints.EAST;
        gbcTextoLabel.gridx = 0;
        gbcTextoLabel.gridy = 0;
        panelFiltros.add(lblTexto, gbcTextoLabel);

        GridBagConstraints gbcTextoField = new GridBagConstraints();
        gbcTextoField.insets = new Insets(8, 8, 8, 8);
        gbcTextoField.anchor = GridBagConstraints.WEST;
        gbcTextoField.fill = GridBagConstraints.HORIZONTAL;
        gbcTextoField.weightx = 1.0;
        gbcTextoField.gridx = 1;
        gbcTextoField.gridy = 0;
        panelFiltros.add(textoField, gbcTextoField);

        JLabel lblEmisor = new JLabel("Teléfono:");
        styleLabel(lblEmisor);
        emisorField = new JTextField(20);
        styleField(emisorField);
        GridBagConstraints gbcEmisorLabel = new GridBagConstraints();
        gbcEmisorLabel.insets = new Insets(8, 8, 8, 8);
        gbcEmisorLabel.anchor = GridBagConstraints.EAST;
        gbcEmisorLabel.gridx = 0;
        gbcEmisorLabel.gridy = 1;
        panelFiltros.add(lblEmisor, gbcEmisorLabel);

        GridBagConstraints gbcEmisorField = new GridBagConstraints();
        gbcEmisorField.insets = new Insets(8, 8, 8, 8);
        gbcEmisorField.anchor = GridBagConstraints.WEST;
        gbcEmisorField.fill = GridBagConstraints.HORIZONTAL;
        gbcEmisorField.weightx = 1.0;
        gbcEmisorField.gridx = 1;
        gbcEmisorField.gridy = 1;
        panelFiltros.add(emisorField, gbcEmisorField);

        JLabel lblReceptor = new JLabel("Contacto:");
        styleLabel(lblReceptor);
        receptorField = new JTextField(20);
        styleField(receptorField);
        GridBagConstraints gbcReceptorLabel = new GridBagConstraints();
        gbcReceptorLabel.insets = new Insets(8, 8, 8, 8);
        gbcReceptorLabel.anchor = GridBagConstraints.EAST;
        gbcReceptorLabel.gridx = 0;
        gbcReceptorLabel.gridy = 2;
        panelFiltros.add(lblReceptor, gbcReceptorLabel);

        GridBagConstraints gbcReceptorField = new GridBagConstraints();
        gbcReceptorField.insets = new Insets(8, 8, 8, 8);
        gbcReceptorField.anchor = GridBagConstraints.WEST;
        gbcReceptorField.fill = GridBagConstraints.HORIZONTAL;
        gbcReceptorField.weightx = 1.0;
        gbcReceptorField.gridx = 1;
        gbcReceptorField.gridy = 2;
        panelFiltros.add(receptorField, gbcReceptorField);

        JLabel lblTipo = new JLabel("Tipo:");
        styleLabel(lblTipo);
        comboFiltro = new JComboBox<>(new String[]{"Todos", "Enviados", "Recibidos"});
        comboFiltro.setBackground(new Color(64, 64, 64));
        comboFiltro.setForeground(Color.WHITE);
        GridBagConstraints gbcTipoLabel = new GridBagConstraints();
        gbcTipoLabel.insets = new Insets(8, 8, 8, 8);
        gbcTipoLabel.anchor = GridBagConstraints.EAST;
        gbcTipoLabel.gridx = 0;
        gbcTipoLabel.gridy = 3;
        panelFiltros.add(lblTipo, gbcTipoLabel);

        GridBagConstraints gbcTipoField = new GridBagConstraints();
        gbcTipoField.insets = new Insets(8, 8, 8, 8);
        gbcTipoField.anchor = GridBagConstraints.WEST;
        gbcTipoField.fill = GridBagConstraints.HORIZONTAL;
        gbcTipoField.weightx = 1.0;
        gbcTipoField.gridx = 1;
        gbcTipoField.gridy = 3;
        panelFiltros.add(comboFiltro, gbcTipoField);

        panelMain.add(panelFiltros);
        panelMain.add(Box.createVerticalStrut(15));

        JButton botonBuscar = new JButton("Buscar");
        styleButton(botonBuscar);
        botonBuscar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonBuscar.addActionListener(this::realizarBusqueda);
        panelMain.add(botonBuscar);

        panelMain.add(Box.createVerticalStrut(15));

        modeloMensajes = new DefaultListModel<>();
        listaMensajes = new JList<>(modeloMensajes);
        listaMensajes.setBackground(new Color(34, 34, 34));
        listaMensajes.setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(listaMensajes);
        scroll.setBorder(new TitledBorder(new LineBorder(Color.GRAY), "Resultados", TitledBorder.LEADING, TitledBorder.TOP, null, Color.WHITE));
        panelMain.add(scroll);
    }

    private void realizarBusqueda(ActionEvent e) {
        String texto = textoField.getText().trim();
        String emisor = emisorField.getText().trim();
        String receptor = receptorField.getText().trim();
        boolean enviados = comboFiltro.getSelectedIndex() == 1;
        boolean recibidos = comboFiltro.getSelectedIndex() == 2;
        if (comboFiltro.getSelectedIndex() == 0) {
			enviados = recibidos = true;
		}
        
        if (texto.isEmpty() && emisor.isEmpty() && receptor.isEmpty() && !enviados && !recibidos) {
            mostrarError("Introduce al menos un filtro.");
            return;
        }

        if (!emisor.isEmpty() && !emisor.matches("\\d+")) {
            mostrarError("El teléfono del emisor debe de ser numérico.");
            return;
        }

        if (!receptor.isEmpty()) {
            Optional<Contacto> receptorContacto = Optional.ofNullable(Controlador.getInstancia().buscarContacto(receptor));
            if (receptorContacto.isEmpty()) {
                mostrarError("No se encuentra ningún receptor con ese nombre.");
                return;
            }
        }

        List<Mensaje> resultados = Controlador.getInstancia().buscarMensajes(texto, emisor, receptor, enviados, recibidos);
        modeloMensajes.clear();
        if(resultados.isEmpty()) {
			mostrarError("No se encontraron mensajes con los filtros especificados.");
			return;
		}
        resultados.forEach(modeloMensajes::addElement);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(64, 64, 64));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void styleButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(100, 100, 100)); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(new Color(80, 80, 80)); }
        });
    }
}
