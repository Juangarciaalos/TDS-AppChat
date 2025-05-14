package um.tds.clases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.time.LocalDate;




public class Usuario {
	private final static String ESTADO_BASE = "Hi everyone!";
	
	private String nombre;
	private String apellido;
	private int numeroTelefono;
	private String estado;
	private String contraseña;
	private String correo;
	private boolean isPremium;
	private List<Contacto> contactos;
	private final LocalDate fechaNacimiento;
	private ImageIcon foto;
	
	public Usuario(String nombre, String apellido, int numeroTelefono, String estado, String contraseña, String correo, LocalDate fechaNacimiento) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.numeroTelefono = numeroTelefono;
		this.estado = estado;
		this.contraseña = contraseña;
		this.correo = correo;
		this.isPremium = false;
		this.contactos = new ArrayList<>();
		this.fechaNacimiento = fechaNacimiento;
		try {
			URL urlPerfil = new URL("https://api.dicebear.com/9.x/pixel-art/png?seed=" + nombre);
			Image image = ImageIO.read(urlPerfil);
			this.foto = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Usuario(String nombre, String apellido, int numeroTelefono, String contraseña,String correo,LocalDate fechaNacimiento) {		
		this(nombre, apellido, numeroTelefono, ESTADO_BASE, contraseña, correo, fechaNacimiento);		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(int numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public boolean isPremium() {
		return isPremium;
	}

	public void setPremium(boolean isPremium) {
		this.isPremium = isPremium;
	}

	public List<Contacto> getContactos() {
		return Collections.unmodifiableList(contactos);
	}

	public void addContacto(Contacto contacto) {
		this.contactos.add(contacto);
	}

	public ImageIcon getFoto() {
		return foto;
	}

	public void setFoto(ImageIcon foto) {
		this.foto = foto;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	
}
