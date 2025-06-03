package um.tds.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.time.LocalDate;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import um.tds.clases.Usuario;

public class VentanaUsuario extends JFrame {

    private JList<Usuario> listaUsuarios;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaUsuario frame = new VentanaUsuario();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaUsuario() {
		setTitle("Lista de Usuarios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        DefaultListModel<Usuario> model = new DefaultListModel<>();

        model.addElement(new Usuario("Juan", "Pérez", 612216123, "1234", "Juan@gmail.com", LocalDate.of(1990, 1, 1)));
        model.addElement(new Usuario("Ana", "García", 213415135, "1234", "Ana@gmail.com", LocalDate.of(1995, 2, 2)));
        model.addElement(new Usuario("Carlos", "López", 213143255, "1234", "Carlos@gmail.com", LocalDate.of(1992, 3, 3)));

        listaUsuarios = new JList<>(model);
        listaUsuarios.setCellRenderer(new PerfilUsuario());
        JScrollPane scrollPane = new JScrollPane(listaUsuarios);

        add(scrollPane, BorderLayout.CENTER);
	}

}
