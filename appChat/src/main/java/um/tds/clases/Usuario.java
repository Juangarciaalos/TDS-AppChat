package um.tds.clases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import um.tds.Utilidades.ConversorImagenes;

import java.time.LocalDate;




public class Usuario {
	public final static String ESTADO_BASE = "Hi everyone!";
	
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
		this.foto = getImagenInternetCodificada();
	}
	
	public Usuario(String nombre, String apellido, int numeroTelefono, String estado, String contraseña, String correo, LocalDate fechaNacimiento, LocalDate fechaAlta, String fotoCodificada) {
		this(nombre, apellido, numeroTelefono, estado, contraseña, correo, fechaNacimiento, fechaAlta);
		this.foto = fotoCodificada;
	}
	
	public Usuario(String nombre, String apellido, int numeroTelefono, String contraseña, String correo, LocalDate fechaNacimiento, LocalDate fechaAlta, String fotoCodificada) {
		this(nombre, apellido, numeroTelefono, ESTADO_BASE, contraseña, correo, fechaNacimiento, fechaAlta);
		this.foto = fotoCodificada;
	}
	
	public Usuario(String nombre, String apellido, int numeroTelefono, String estado, String contraseña, String correo, LocalDate fechaNacimiento, LocalDate fechaAlta, File foto) {
		this(nombre, apellido, numeroTelefono, estado, contraseña, correo, fechaNacimiento, fechaAlta);
		this.foto = ConversorImagenes.imageToBase64(foto);
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
		List<Mensaje> mensajes = new ArrayList<>();
		for (Contacto contacto : contactos) {
			if (contacto instanceof ContactoIndividual) {
				mensajes.addAll(((ContactoIndividual) contacto).getListaMensaje());
			}
		}
		return Collections.unmodifiableList(mensajes);
	}
	
	public List<Mensaje> getMensajesRecibidos() {
		List<Mensaje> mensajes = new ArrayList<>();
		for (Contacto contacto : contactos) {
			if (contacto instanceof ContactoIndividual) {
				mensajes.addAll(((ContactoIndividual) contacto).getMensajesEnviados(this));
			}
		}
		return Collections.unmodifiableList(mensajes);
	}
	
	public void enviarMensaje(Mensaje mensaje, Contacto contacto) {
		contacto.addMensaje(mensaje);
		mensajesEnviados.add(mensaje);
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
	
	public Image getFoto() {
		return ConversorImagenes.base64ToImage(foto, 75, 75);
	}
	
	private String getImagenInternetCodificada() {
		String foto = "https://api.dicebear.com/9.x/pixel-art/png?seed=" + nombre;
		Image image = getImagenInternet(foto);
		return ConversorImagenes.imageToBase64(image);
	}
	
	public ImageIcon getImagenIcon() {
		Image image = getFoto();
		return new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	}
	
	
	public Contacto getContacto(int telefono) {
		return contactos.stream()
				.filter(c -> c instanceof ContactoIndividual && ((ContactoIndividual) c).getNumeroTelefono() == telefono)
				.findFirst()
				.orElse(null);
	}
	
	public Contacto getContacto(String nombre) {
		return contactos.stream()
				.filter(c -> c.getNombre().equals(nombre))
				.findFirst()
				.orElse(null);
	}
	
	public Image getImagenInternet(String foto) {
		Image image= null;
		try {
			URL urlPerfil = new URL(foto);
			image = ImageIO.read(urlPerfil);
		} catch (IOException e) {
			image = new ImageIcon("src/main/resources/default-user.png").getImage();
		}
		return image;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	public List<Mensaje> getMensajesEnviadosTlf(int telefono) {
		List<Mensaje> mensajes = new ArrayList<>();
		Contacto contacto = getContacto(telefono);
		if (contacto instanceof ContactoIndividual) {
			ContactoIndividual contactoInd = (ContactoIndividual) contacto;
			mensajes = contactoInd.getListaMensaje();
		}
		return mensajes;
	}
	
	public List<Mensaje> getMensajesRecibidosTlf(int telefono) {
		List<Mensaje> mensajes = new ArrayList<>();
		Contacto contacto = getContacto(telefono);
		if (contacto instanceof ContactoIndividual) {
			ContactoIndividual contactoInd = (ContactoIndividual) contacto;
			mensajes = contactoInd.getMensajesEnviados(this);
		}
		return mensajes;
	}
	
	public List<Mensaje> getAllMensajes(int telefono) {
		List<Mensaje> mensajes = new ArrayList<>();
		mensajes.addAll(getMensajesEnviadosTlf(telefono));
		mensajes.addAll(getMensajesRecibidosTlf(telefono));
		return mensajes;
	}
	
	public List<Mensaje> getAllMensajes() {
		List<Mensaje> mensajes = new ArrayList<>();
		mensajes.addAll(mensajesEnviados);
		mensajes.addAll(mensajesRecibidos);
		return mensajes;
	}

	public void editarContacto(Contacto c) {
		for (Contacto contacto : contactos) {
			if (contacto.getCodigo().equals(c.getCodigo())) {
				contactos.remove(contacto);
				contactos.add(c);
				return;
			}
		}
	}
	
	public boolean esContactoSinAgregar(ContactoIndividual contacto) {
		if (contacto.getNombre() == String.valueOf(contacto.getNumeroTelefono())) {
			return true;
		}
		return false;
	}
}
