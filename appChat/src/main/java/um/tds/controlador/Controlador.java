package um.tds.controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import um.tds.Utilidades.BuscadorMensajes;
import um.tds.Utilidades.GeneradorPDF;
import um.tds.clases.Contacto;
import um.tds.clases.ContactoIndividual;
import um.tds.clases.Grupo;
import um.tds.clases.RepositorioUsuarios;
import um.tds.clases.Usuario;
import um.tds.persistencia.ContactoIndividualDAO;
import um.tds.persistencia.DAOException;
import um.tds.persistencia.FactoriaDAO;
import um.tds.persistencia.GrupoDAO;
import um.tds.persistencia.MensajeDAO;
import um.tds.persistencia.UsuarioDAO;
import um.tds.clases.Mensaje;

public class Controlador {
	
	private static Controlador instancia = null;
	
	private UsuarioDAO usuarioDAO;
	private ContactoIndividualDAO contactoIndividualDAO;
	private GrupoDAO grupoDAO;
	private MensajeDAO mensajeDAO;
	
	private Usuario usuario;
    private RepositorioUsuarios repositorioUsuarios;
    private GeneradorPDF generadorPDF;
    private BuscadorMensajes buscadorMensajes;
    
    public static Controlador getInstancia() {
		if (instancia == null) {
			instancia = new Controlador();
		}
		return instancia;
	}
    
    private Controlador() {
		inicializarAdaptadores();
		inicializarRepositorioUsuarios();
		incializarServicios();
	}
    
    private void inicializarAdaptadores() {
    	try {
    		FactoriaDAO factoria = FactoriaDAO.getInstancia();
    		usuarioDAO = factoria.getUsuarioDAO();
    		contactoIndividualDAO = factoria.getContactoIndividualDAO();
    		grupoDAO = factoria.getGrupoDAO();
    		mensajeDAO = factoria.getMensajeDAO();
    	} catch (DAOException e) {
			e.printStackTrace();
		}
    }
    
    private void inicializarRepositorioUsuarios() {
		repositorioUsuarios = RepositorioUsuarios.INSTANCE;
	}
    
    private void incializarServicios() {
    	buscadorMensajes = BuscadorMensajes.INSTANCE;
    	generadorPDF = GeneradorPDF.INSTANCE;
    }
    
    public Usuario getUsuarioActual() {
    	return usuario;
    }
    
    public boolean iniciarSesion(int telefono, String contrasena) {
		Usuario usuario = repositorioUsuarios.getUsuarioPorTelefono(telefono);
		if (usuario != null && usuario.getContraseña().equals(contrasena)) {
			this.usuario = usuario;
			return true;
		}
		return false;
	}
    
    public boolean registrarUsuario(String nombre, String apellido, int numeroTelefono, String estado, String contraseña, String correo, LocalDate fechaNacimiento, LocalDate fechaAlta) {
    	Usuario nuevoUsuario = new Usuario(nombre, apellido, numeroTelefono, estado, contraseña, correo, fechaNacimiento, fechaAlta);
    	if (!repositorioUsuarios.existeUsuario(nuevoUsuario)) {
			usuarioDAO.registrarUsuario(nuevoUsuario);
			repositorioUsuarios.addUsuario(nuevoUsuario);
			return true;
		}
    	return false;
    }
    
    public boolean registrarUsuario(String nombre, String apellido, int numeroTelefono, String estado, String contraseña, String correo, LocalDate fechaNacimiento, LocalDate fechaAlta, String fotoCodificada) {
    	Usuario nuevoUsuario = new Usuario(nombre, apellido, numeroTelefono, estado, contraseña, correo, fechaNacimiento, fechaAlta, fotoCodificada);
    	if (!repositorioUsuarios.existeUsuario(nuevoUsuario)) {
			usuarioDAO.registrarUsuario(nuevoUsuario);
			repositorioUsuarios.addUsuario(nuevoUsuario);
			return true;
		}
    	return false;
    }
    
    public void setPremium(boolean pagado) {
    	usuario.setPremium(pagado);
    	usuarioDAO.modificarUsuario(usuario);
    }
    
    public Contacto buscarContacto(String nombre) {
    	return usuario.getContacto(nombre);
    }
    
    public Contacto buscarContacto(int telefono) {
    	return usuario.getContacto(telefono);
    }
    
    public Usuario buscarUsuario(int telefono) {
		return repositorioUsuarios.getUsuarioPorTelefono(telefono);
	}
    
    public Usuario buscarUsuario(String nombre) {
    	return repositorioUsuarios.getUsuarioPorNombre(nombre);
    }
    
    public void enviarMensaje(String texto, Contacto contacto) {
    	Mensaje mensaje = new Mensaje(texto, usuario, contacto);
    	usuario.enviarMensaje(mensaje, contacto);
    	mensajeDAO.registrarMensaje(mensaje);	
    	
    	if (contacto instanceof ContactoIndividual) {
			contactoIndividualDAO.modificarContactoIndividual((ContactoIndividual) contacto);
		} else  {
			Grupo grupo = (Grupo) contacto;
			for (Contacto c: grupo.getParticipantes()) {
				Mensaje mensajeIndividual = new Mensaje(texto, usuario, c);
				usuario.enviarMensaje(mensajeIndividual, c);
				mensajeDAO.registrarMensaje(mensajeIndividual);
				contactoIndividualDAO.modificarContactoIndividual((ContactoIndividual) c);
				grupoDAO.modificarGrupo(grupo);
			}
		}
    }
    
    public void enviarEmoji(int emoticono, Contacto contacto) {
    	Mensaje mensaje = new Mensaje(emoticono, usuario, contacto);
    	usuario.enviarMensaje(mensaje, contacto);
    	mensajeDAO.registrarMensaje(mensaje);
    	
    	if (contacto instanceof ContactoIndividual) {
    		contactoIndividualDAO.modificarContactoIndividual((ContactoIndividual) contacto);
    	} else {
    		Grupo grupo = (Grupo) contacto;
    		for (Contacto c : grupo.getParticipantes()) {
				Mensaje mensajeIndividual = new Mensaje(emoticono, usuario, c);
				usuario.enviarMensaje(mensajeIndividual, c);
				mensajeDAO.registrarMensaje(mensajeIndividual);
				contactoIndividualDAO.modificarContactoIndividual((ContactoIndividual) c);
				grupoDAO.modificarGrupo(grupo);
			}
    	}
    }
    
    public void cambiarNombreContacto(Contacto contacto, String nuevoNombre) {
    	contacto.setNombre(nuevoNombre);
    	contactoIndividualDAO.modificarContactoIndividual((ContactoIndividual) contacto);
    	usuarioDAO.modificarUsuario(usuario);
    }
    
    public void editarGrupo(Grupo grupo, String nuevoNombre, String fotoCodificada, String estado, String nombre, List<ContactoIndividual> participantes) {
    	grupo.setNombre(nuevoNombre);
    	grupo.setStringFoto(fotoCodificada);
    	grupo.setEstado(estado);
    	grupo.setParticipantes(participantes);
    	grupoDAO.modificarGrupo(grupo);
    	usuarioDAO.modificarUsuario(usuario);
    }
    
    public void cambiarFotoPerfil(String fotoCodificada) {
		usuario.setStringFoto(fotoCodificada);
		usuarioDAO.modificarUsuario(usuario);
    }
    
    public void cambiarEstado(String estado) {
    	usuario.setEstado(estado);
    	usuarioDAO.modificarUsuario(usuario);
    }
    
    public boolean añadirContacto(String nombre, String tlf) {
    	int telefono = Integer.parseInt(tlf);
    	if (!repositorioUsuarios.existeUsuarioPorTelefono(telefono)) {
			return false;
		}
    	
    	Usuario usuarioContacto = repositorioUsuarios.getUsuarioPorTelefono(telefono);
		Optional<ContactoIndividual> contacto = Optional.ofNullable((ContactoIndividual)usuario.getContacto(telefono));

		
    	
    	if (usuarioContacto.equals(usuario)) {
    		return false;
    	} else if (!contacto.isPresent()) {
			ContactoIndividual nuevoContacto = new ContactoIndividual(nombre, usuarioContacto);
			contactoIndividualDAO.registrarContactoIndividual(nuevoContacto);
			usuario.addContacto(nuevoContacto);
			usuarioDAO.modificarUsuario(usuario);
			return true;
		} else if (contacto.get().isContactoAgregado()) {
			contacto.get().setNombre(nombre);
			contactoIndividualDAO.modificarContactoIndividual(contacto.get());
			return true;
		} else {
			return false;
    	}
    }
    
    public void crearGrupo(String nombre, List<ContactoIndividual> participantes, String fotoCodificada, String estado) {
    	Grupo nuevoGrupo = new Grupo(nombre, participantes, fotoCodificada, estado);
    	grupoDAO.registrarGrupo(nuevoGrupo);
    	usuario.addContacto(nuevoGrupo);
    	usuarioDAO.modificarUsuario(usuario);
    }
    
    public void cargarContactosSinAgregar() {
    	mensajeDAO.recuperarTodosMensajes().stream()
        .filter(m -> m.getReceptor() instanceof ContactoIndividual ci &&
                     ci.getUsuario().getNumeroTelefono() == usuario.getNumeroTelefono() &&
                     usuario.esAgregado(m.getEmisor()))
        .map(Mensaje::getEmisor)
        .distinct()
        .forEach(u -> añadirContacto(String.valueOf(u.getNumeroTelefono()), String.valueOf(u.getNumeroTelefono())));

    usuarioDAO.modificarUsuario(usuario);

    }
    
    public void cargarMensajesRecibidos() { 
    	List<Mensaje> mensajes = new ArrayList<>();

    	mensajes = mensajeDAO.recuperarTodosMensajes();
    	for (Mensaje m : mensajes) {
    		if (m.getReceptor() instanceof ContactoIndividual) {
    			if (((ContactoIndividual) m.getReceptor()).getUsuario().getNumeroTelefono() == usuario.getNumeroTelefono()) {
    				if (m.getHoraEnvio().isBefore(LocalDateTime.now()) ) {
	    				ContactoIndividual contacto = (ContactoIndividual) usuario.getContacto(m.getEmisor().getNumeroTelefono());
	    	    		contacto.addMensaje(m);
    				}
				}
    		}
    	}
    	usuarioDAO.modificarUsuario(usuario);
    }
    
    public void generarPDF(Contacto contacto, String rutaSalida) {
    	List<Mensaje> mensajes = usuario.getAllMensajes().stream()
    			.filter(m-> m.getReceptor().equals(contacto))
    			.collect(Collectors.toList());
    	try {
        	generadorPDF.generarPDF(contacto.getNombre(), mensajes, rutaSalida);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public List<Mensaje> buscarMensajes(String texto, String telefono, String nombre, boolean isEnviado, boolean isRecibido) {
    	List<Mensaje> mensajes = new ArrayList<Mensaje>();
    	if (isEnviado) {
    		mensajes.addAll(buscadorMensajes.buscarMensajesEnviados(usuario, telefono, nombre, texto));
    	}
    	if (isRecibido) {
			mensajes.addAll(buscadorMensajes.buscarMensajesRecibidos(usuario, telefono, nombre, texto));
		}
    	return mensajes;
    }
    
    public void cerrarSesion() {
    	usuario = null;
    }
}	
