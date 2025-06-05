package um.tds.clases;

import java.util.ArrayList;
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
	
	private int codigo;	
	private String nombre;
	private String apellido;
	private int numeroTelefono;
	private String estado;
	private String contraseña;
	private String correo;
	private boolean isPremium;
	private List<Contacto> contactos;
	private List<Mensaje> mensajesEnviados;
	private List<Mensaje> mensajesRecibidos;
	private final LocalDate fechaAlta;
	private final LocalDate fechaNacimiento;
	private String foto;
	
	public Usuario(String nombre, String apellido, int numeroTelefono, String estado, String contraseña, String correo, LocalDate fechaNacimiento, LocalDate fechaAlta) {
		super();
		this.codigo = 0; 
		this.nombre = nombre;
		this.apellido = apellido;
		this.numeroTelefono = numeroTelefono;
		this.estado = estado;
		this.contraseña = contraseña;
		this.correo = correo;
		this.isPremium = false;
		this.contactos = new ArrayList<>();
		this.mensajesEnviados = new ArrayList<>();
		this.mensajesRecibidos = new ArrayList<>();
		this.fechaAlta = fechaAlta;
		this.fechaNacimiento = fechaNacimiento;
		this.foto = "https://api.dicebear.com/9.x/pixel-art/png?seed=" + nombre;
	}
	
	public Usuario(String nombre, String apellido, int numeroTelefono, String contraseña,String correo,LocalDate fechaNacimiento) {		
		this(nombre, apellido, numeroTelefono, ESTADO_BASE, contraseña, correo, fechaNacimiento, LocalDate.now());		
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
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
	
	public void setContactos(List<Contacto> contactos) {
		if (contactos != null) 
			this.contactos = new ArrayList<>(contactos);
	}

	public void addContacto(Contacto contacto) {
		this.contactos.add(contacto);
	}

	public List<Mensaje> getMensajesEnviados() {
		return Collections.unmodifiableList(mensajesEnviados);
	}
	
	public List<Mensaje> getMensajesRecibidos() {
		return Collections.unmodifiableList(mensajesRecibidos);
	}
	
	public LocalDate getFechaAlta() {
		return fechaAlta;
	}
	
	public String getStringFoto() {
		return foto;
	}

	public void setStringFoto(String foto) {
		this.foto = foto;
	}
	
	public ImageIcon getImagenInternet(String foto) {
		ImageIcon imageIcon = null;
		try {
			URL urlPerfil = new URL(foto);
			Image image = ImageIO.read(urlPerfil);
			imageIcon = new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageIcon;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	public List<Mensaje> getMensajesEnviadosTlf(int telefono) {
		List<Mensaje> mensajes = new ArrayList<>();
		for (Mensaje mensaje : mensajesEnviados) {
			if (mensaje.getEmisor().getNumeroTelefono() == telefono) {
				mensajes.add(mensaje);
			}
		}
		return mensajes;
	}
	
	public List<Mensaje> getMensajesRecibidosTlf(int telefono) {
		List<Mensaje> mensajes = new ArrayList<>();
		for (Mensaje mensaje : mensajesRecibidos) {
			if (mensaje.getReceptor() instanceof ContactoIndividual){
				ContactoIndividual contacto = (ContactoIndividual) mensaje.getReceptor();
				if (contacto.getNumeroTelefono() == telefono) {
					mensajes.add(mensaje);
				}
			} else if (mensaje.getReceptor() instanceof Grupo) {
				Grupo grupo = (Grupo) mensaje.getReceptor();
				Usuario usuario = grupo.getUsuarioTlf(telefono);
				if (usuario != null) {
					mensajes.add(mensaje);
				}
			}
			
		}
		return mensajes;
	}
	
	public List<Mensaje> getAllMensajes(int telefono) {
		List<Mensaje> mensajes = new ArrayList<>();
		mensajes.addAll(getMensajesEnviadosTlf(telefono));
		mensajes.addAll(getMensajesRecibidosTlf(telefono));
		return mensajes;
	}
	
}
