package um.tds.controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    
	/**
	 * Inicializa los adaptadores de acceso a datos.
	 */
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
    
    	/*
    	 * 	* Inicializa el repositorio de usuarios.
    	 */
    private void inicializarRepositorioUsuarios() {
		repositorioUsuarios = RepositorioUsuarios.INSTANCE;
	}
    
    /**
	 * Inicializa los servicios utilizados por el controlador.
	 */
    private void incializarServicios() {
    	buscadorMensajes = BuscadorMensajes.INSTANCE;
    	generadorPDF = GeneradorPDF.INSTANCE;
    }
    
    /**
	 * Devuelve el usuario actual.
	 * @return Usuario actual.
	 */
    public Usuario getUsuarioActual() {
    	return usuario;
    }
    
    /**
     * Inicia sesión con un usuario dado su número de teléfono y contraseña. 
     * @param telefono
     * @param contrasena
     * @return true si la sesión se inicia correctamente, false en caso contrario.
     */
    public boolean iniciarSesion(int telefono, String contrasena) {
		Usuario usuario = repositorioUsuarios.getUsuarioPorTelefono(telefono);
		if (usuario != null && usuario.getContraseña().equals(contrasena)) {
			this.usuario = usuario;
			return true;
		}
		return false;
	}
    
    /**
	 * Registra un nuevo usuario en el sistema.
	 * @param nombre Nombre del usuario.
	 * @param apellido Apellido del usuario.
	 * @param numeroTelefono Número de teléfono del usuario.
	 * @param estado Estado del usuario.
	 * @param contraseña Contraseña del usuario.
	 * @param correo Correo electrónico del usuario.
	 * @param fechaNacimiento Fecha de nacimiento del usuario.
	 * @param fechaAlta Fecha de alta del usuario.
	 * @return true si el registro es exitoso, false si ya existe un usuario con los mismos datos.
	 */
    public boolean registrarUsuario(String nombre, String apellido, int numeroTelefono, String estado, String contraseña, String correo, LocalDate fechaNacimiento, LocalDate fechaAlta) {
    	Usuario nuevoUsuario = new Usuario(nombre, apellido, numeroTelefono, estado, contraseña, correo, fechaNacimiento, fechaAlta);
    	if (!repositorioUsuarios.existeUsuario(nuevoUsuario)) {
			usuarioDAO.registrarUsuario(nuevoUsuario);
			repositorioUsuarios.addUsuario(nuevoUsuario);
			return true;
		}
    	return false;
    }
    
    /**
	 * Registra un nuevo usuario en el sistema.
	 * @param nombre Nombre del usuario.
	 * @param apellido Apellido del usuario.
	 * @param numeroTelefono Número de teléfono del usuario.
	 * @param estado Estado del usuario.
	 * @param contraseña Contraseña del usuario.
	 * @param correo Correo electrónico del usuario.
	 * @param fechaNacimiento Fecha de nacimiento del usuario.
	 * @param fechaAlta Fecha de alta del usuario.
	 * @param fotoCodificada Foto del usuario en formato string.
	 * @return true si el registro es exitoso, false si ya existe un usuario con los mismos datos.
	 */
    public boolean registrarUsuario(String nombre, String apellido, int numeroTelefono, String estado, String contraseña, String correo, LocalDate fechaNacimiento, LocalDate fechaAlta, String fotoCodificada) {
    	Usuario nuevoUsuario = new Usuario(nombre, apellido, numeroTelefono, estado, contraseña, correo, fechaNacimiento, fechaAlta, fotoCodificada);
    	if (!repositorioUsuarios.existeUsuario(nuevoUsuario)) {
			usuarioDAO.registrarUsuario(nuevoUsuario);
			repositorioUsuarios.addUsuario(nuevoUsuario);
			return true;
		}
    	return false;
    }
    
    /**
	 * Marca al usuario actual como premium o no premium.
	 * @param pagado true si el usuario es premium, false en caso contrario.
	 */
    public void setPremium(boolean pagado) {
    	usuario.setPremium(pagado);
    	usuarioDAO.modificarUsuario(usuario);
    }
    
    /*
     * Devuelve un contacto individual por su nombre.
     * @param nombre Nombre del contacto a buscar.
     * @return ContactoIndividual si se encuentra, null en caso contrario.
     */
    public Contacto buscarContacto(String nombre) {
    	return usuario.getContacto(nombre);
    }
    
    /* devuelve un contacto individual por su número de teléfono.
     * @param telefono Número de teléfono del contacto a buscar.
     * @return ContactoIndividual si se encuentra, null en caso contrario.
     */
    public Contacto buscarContacto(int telefono) {
    	return usuario.getContacto(telefono);
    }
    
    /*
     * Devuelve un usuario por su número de teléfono.
	 * @param telefono Número de teléfono del usuario a buscar.
	 * @return Usuario si se encuentra, null en caso contrario.
	 */
    public Usuario buscarUsuario(int telefono) {
		return repositorioUsuarios.getUsuarioPorTelefono(telefono);
	}
    
    /*
     * Devuelve un usuario por su nombre.
     * @param nombre Nombre del usuario a buscar.
     * @return Usuario si se encuentra, null en caso contrario.
     */
    public Usuario buscarUsuario(String nombre) {
    	return repositorioUsuarios.getUsuarioPorNombre(nombre);
    }
    
    // devuelve todos los contactos del usuario actual.
    public List<Contacto> getContactos() {
		return usuario.getContactos();
	}
    
    /**
	 * Envía un mensaje de texto a un contacto, ya sea individual o de grupo.
	 * @param texto Texto del mensaje a enviar.
	 * @param contacto Contacto al que se envía el mensaje.
	 */
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
    
    /**
	 * Envía un emoji a un contacto, ya sea individual o de grupo.
	 * @param emoticono Código del emoticono a enviar.
	 * @param contacto Contacto al que se envía el emoticono.
	 */
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
    
    /**
     * Modifica el nombre de un contacto individual.
     * @param contacto El contacto a renombrar.
     * @param nuevoNombre El nuevo nombre a asignar.
     */
    public void cambiarNombreContacto(Contacto contacto, String nuevoNombre) {
        contacto.setNombre(nuevoNombre);
        contactoIndividualDAO.modificarContactoIndividual((ContactoIndividual) contacto);
        usuarioDAO.modificarUsuario(usuario);
    }

    /**
     * Edita las propiedades de un grupo: nombre, foto, estado y participantes.
     * @param grupo Grupo a editar.
     * @param nuevoNombre Nuevo nombre del grupo.
     * @param fotoCodificada Foto del grupo en formato base64.
     * @param estado Nuevo estado del grupo.
     * @param nombre (NO USADO) Posible parámetro redundante.
     * @param participantes Nueva lista de participantes.
     */
    public void editarGrupo(Grupo grupo, String nuevoNombre, String fotoCodificada, String estado, String nombre, List<ContactoIndividual> participantes) {
        grupo.setNombre(nuevoNombre);
        grupo.setStringFoto(fotoCodificada);
        grupo.setEstado(estado);
        grupo.setParticipantes(participantes);
        grupoDAO.modificarGrupo(grupo);
        usuarioDAO.modificarUsuario(usuario);
    }

    /**
     * Cambia la foto de perfil del usuario actual.
     * @param fotoCodificada Imagen codificada en base64.
     */
    public void cambiarFotoPerfil(String fotoCodificada) {
        usuario.setStringFoto(fotoCodificada);
        usuarioDAO.modificarUsuario(usuario);
    }

    /**
     * Cambia el estado del usuario actual.
     * @param estado Texto del nuevo estado.
     */
    public void cambiarEstado(String estado) {
        usuario.setEstado(estado);
        usuarioDAO.modificarUsuario(usuario);
    }

    /**
     * Añade un nuevo contacto individual al usuario actual si no existía.
     * @param nombre Nombre a asignar.
     * @param tlf Número de teléfono como String.
     * @return true si se añadió o modificó, false si ya existía o no era válido.
     */
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
        } else if (!contacto.get().isContactoAgregado()) {
            contacto.get().setNombre(nombre);
            contactoIndividualDAO.modificarContactoIndividual(contacto.get());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Crea un nuevo grupo con los datos proporcionados.
     * @param nombre Nombre del grupo.
     * @param participantes Participantes del grupo.
     * @param fotoCodificada Imagen codificada en base64.
     * @param estado Estado del grupo.
     */
    public void crearGrupo(String nombre, List<ContactoIndividual> participantes, String fotoCodificada, String estado) {
        Grupo nuevoGrupo = new Grupo(nombre, participantes, fotoCodificada, estado);
        grupoDAO.registrarGrupo(nuevoGrupo);
        usuario.addContacto(nuevoGrupo);
        usuarioDAO.modificarUsuario(usuario);
    }

    /**
     * Carga automáticamente contactos individuales que hayan enviado mensajes al usuario actual
     * pero aún no hayan sido agregados a su lista de contactos.
     */
    public void cargarContactosSinAgregar() {
        mensajeDAO.recuperarTodosMensajes().stream()
            .filter(m -> m.getReceptor() instanceof ContactoIndividual ci &&
                         ci.getUsuario().getNumeroTelefono() == usuario.getNumeroTelefono())
            .map(Mensaje::getEmisor)
            .distinct()
            .forEach(u -> añadirContacto(String.valueOf(u.getNumeroTelefono()), String.valueOf(u.getNumeroTelefono())));

        usuarioDAO.modificarUsuario(usuario);
    }

    /**
     * Carga todos los mensajes recibidos para el usuario actual
     * y los asigna a los contactos correspondientes.
     */
    public void cargarMensajesRecibidos() {
        List<Mensaje> mensajes = mensajeDAO.recuperarTodosMensajes();
        for (Mensaje m : mensajes) {
            if (m.getReceptor() instanceof ContactoIndividual) {
                if (((ContactoIndividual) m.getReceptor()).getUsuario().getNumeroTelefono() == usuario.getNumeroTelefono()) {
                    if (m.getHoraEnvio().isBefore(LocalDateTime.now())) {
                        ContactoIndividual contacto = (ContactoIndividual) usuario.getContacto(m.getEmisor().getNumeroTelefono());
                        contacto.addMensaje(m);
                    }
                }
            }
        }
        usuarioDAO.modificarUsuario(usuario);
    }

    /**
     * Genera un PDF con los mensajes entre el usuario actual y un contacto.
     * @param contacto Contacto con el que se tiene la conversación.
     * @param rutaSalida Ruta del archivo PDF a generar.
     */
    public void generarPDF(Contacto contacto, String rutaSalida) {
        List<Mensaje> mensajes = contacto.getAllMensajes(usuario);
        try {
            generadorPDF.generarPDF(contacto, mensajes, rutaSalida);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca mensajes en función del contenido, teléfono o nombre y si son enviados o recibidos.
     * @param texto Texto a buscar.
     * @param telefono Teléfono a filtrar.
     * @param nombre Nombre de contacto a filtrar.
     * @param isEnviado Incluir enviados.
     * @param isRecibido Incluir recibidos.
     * @return Lista de mensajes encontrados.
     */
    public List<Mensaje> buscarMensajes(String texto, String telefono, String nombre, boolean isEnviado, boolean isRecibido) {
        List<Mensaje> mensajes = new ArrayList<>();
        if (isEnviado) {
            mensajes.addAll(buscadorMensajes.buscarMensajesEnviados(usuario, telefono, nombre, texto));
        }
        if (isRecibido) {
            mensajes.addAll(buscadorMensajes.buscarMensajesRecibidos(usuario, telefono, nombre, texto));
        }
        return mensajes;
    }

    /**
     * Inicia una conversación con un contacto existente o uno nuevo, dado un nombre o número.
     * @param entrada Nombre o número del contacto.
     * @return Contacto existente o recién creado, o vacío si no existe.
     */
    public Optional<Contacto> iniciarConversacion(String entrada) {
        if (entrada.matches("\\d+")) {
            int telefono = Integer.parseInt(entrada);

            if (telefono == usuario.getNumeroTelefono()) return Optional.empty();

            Contacto existente = usuario.getContacto(telefono);
            if (existente != null) return Optional.of(existente);

            Usuario encontrado = buscarUsuario(telefono);
            if (encontrado != null) {
                ContactoIndividual nuevo = new ContactoIndividual(entrada, encontrado);
                contactoIndividualDAO.registrarContactoIndividual(nuevo);
                usuario.addContacto(nuevo);
                usuarioDAO.modificarUsuario(usuario);
                return Optional.of(nuevo);
            }
            return Optional.empty();
        }

        Contacto contacto = usuario.getContacto(entrada);
        return Optional.ofNullable(contacto);
    }

    /**
     * Cierra sesión del usuario actual.
     * Se utiliza para limpiar el estado antes de volver a la pantalla de login.
     */
    public void cerrarSesion() {
        usuario = null;
    }

}	
