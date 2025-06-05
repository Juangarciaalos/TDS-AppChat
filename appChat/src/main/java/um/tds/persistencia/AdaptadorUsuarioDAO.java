package um.tds.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import um.tds.clases.Contacto;
import um.tds.clases.ContactoIndividual;
import um.tds.clases.Grupo;
import um.tds.clases.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;

public class AdaptadorUsuarioDAO implements UsuarioDAO{
	
	private static final String NOMBRE = "Nombre";
	private static final String APELLIDO = "Apellido";
	private static final String TELEFONO = "Telefono";
	private static final String CONTRASEÑA = "Contraseña";
	private static final String CORREO = "Correo";
	private static final String ESTADO = "Estado";
	private static final String FECHA_NACIMIENTO = "FechaNacimiento";
	private static final String FECHA_ALTA = "FechaAlta";
	private static final String FOTO = "Foto";
	private static final String PREMIUM = "Premium";
	private static final String CONTACTOS = "Contactos";
	private static final String GRUPOS = "Grupos";
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioDAO instancia = null;
	
	public static AdaptadorUsuarioDAO getInstancia() {
		if (instancia == null) return new AdaptadorUsuarioDAO();
		else return instancia;	
	}
	
	private AdaptadorUsuarioDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	
	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		if (eUsuario != null) return;
		
		for (Contacto c : usuario.getContactos()) {
			if (c instanceof ContactoIndividual) {
				AdaptadorContactoIndividualDAO.getInstancia().registrarContactoIndividual((ContactoIndividual) c);
			} else if (c instanceof Grupo) {
				AdaptadorGrupoDAO.getInstancia().registrarGrupo((Grupo) c);
			}
		}
		
		eUsuario = crearEntidadUsuario(usuario);
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		usuario.setCodigo(eUsuario.getId());
		PoolDAO.INSTANCE.addObject(usuario.getCodigo(), usuario);
	}
	
	public void borrarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		if (eUsuario == null) return;
		
		for (Contacto c : usuario.getContactos()) {
			if (c instanceof ContactoIndividual) {
				AdaptadorContactoIndividualDAO.getInstancia().borrarContactoIndividual((ContactoIndividual) c);
			} else if (c instanceof Grupo) {
				AdaptadorGrupoDAO.getInstancia().borrarGrupo((Grupo) c);
			}
		}
		
		servPersistencia.borrarEntidad(eUsuario);
		if (PoolDAO.INSTANCE.containsObject(usuario.getCodigo())) {
			PoolDAO.INSTANCE.removeObject(usuario.getCodigo());
		}
	}
	
	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		if (eUsuario == null) return;
		
		for (Propiedad prop : eUsuario.getPropiedades()) {
			if (prop.getNombre().equals(NOMBRE)) {
				prop.setValor(usuario.getNombre());
			} else if (prop.getNombre().equals(APELLIDO)) {
				prop.setValor(usuario.getApellido());
			} else if (prop.getNombre().equals(TELEFONO)) {
				prop.setValor(Integer.toString(usuario.getNumeroTelefono()));
			} else if (prop.getNombre().equals(CONTRASEÑA)) {
				prop.setValor(usuario.getContraseña());
			} else if (prop.getNombre().equals(CORREO)) {
				prop.setValor(usuario.getCorreo());
			} else if (prop.getNombre().equals(ESTADO)) {
				prop.setValor(usuario.getEstado());
			} else if (prop.getNombre().equals(FECHA_NACIMIENTO)) {
				prop.setValor(usuario.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			} else if (prop.getNombre().equals(FECHA_ALTA)) {
				prop.setValor(usuario.getFechaAlta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			} else if (prop.getNombre().equals(FOTO)) {
				prop.setValor(usuario.getStringFoto());
			} else if (prop.getNombre().equals(PREMIUM)) {
				prop.setValor(String.valueOf(usuario.isPremium()));
			} else if (prop.getNombre().equals(CONTACTOS)) {
				prop.setValor(contactosToString(usuario.getContactos()
						.stream().filter(c -> c instanceof ContactoIndividual).collect(Collectors.toList())));
			} else if (prop.getNombre().equals(GRUPOS)) {
				prop.setValor(contactosToString(usuario.getContactos()
						.stream().filter(c -> c instanceof Grupo).collect(Collectors.toList())));
			}
			
			servPersistencia.modificarPropiedad(prop);
		}
	}
	
	public Usuario recuperarUsuario(int codigo) {
		if (PoolDAO.INSTANCE.containsObject(codigo)) {
			return (Usuario) PoolDAO.INSTANCE.getObject(codigo);
		}
		
		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);		
		Usuario usuario = crearUsuarioDesdeEntidad(eUsuario);
		PoolDAO.INSTANCE.addObject(codigo, usuario);
		
		return usuario;
	}
	
	public List<Usuario> recuperarTodosUsuarios() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades("Usuario");
		List<Usuario> usuarios = new ArrayList<>();
		
		for (Entidad eUsuario : entidades) {
			Usuario usuario = recuperarUsuario(eUsuario.getId());
			usuarios.add(usuario);
		}
		return usuarios;
	}
	
	private Entidad crearEntidadUsuario(Usuario usuario) {
		Entidad eUsuario = new Entidad();
		eUsuario.setNombre("Usuario");
		
		ArrayList<Propiedad> propiedades = new ArrayList<>(Arrays.asList(
			new Propiedad(NOMBRE, usuario.getNombre()),
			new Propiedad(APELLIDO, usuario.getApellido()),
			new Propiedad(TELEFONO, Integer.toString(usuario.getNumeroTelefono())),
			new Propiedad(CONTRASEÑA, usuario.getContraseña()),
			new Propiedad(CORREO, usuario.getCorreo()),
			new Propiedad(ESTADO, usuario.getEstado()),
			new Propiedad(FECHA_NACIMIENTO, usuario.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
			new Propiedad(FECHA_ALTA, usuario.getFechaAlta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
			new Propiedad(FOTO, usuario.getStringFoto()),
			new Propiedad(PREMIUM, String.valueOf(usuario.isPremium())),
			new Propiedad(CONTACTOS, contactosToString(usuario.getContactos()
					.stream().filter(c -> c instanceof ContactoIndividual).collect(Collectors.toList()))),
			new Propiedad(GRUPOS, contactosToString(usuario.getContactos()
					.stream().filter(c -> c instanceof Grupo).collect(Collectors.toList())))
		));
		eUsuario.setPropiedades(propiedades);
		return eUsuario;
	}
	
	private Usuario crearUsuarioDesdeEntidad(Entidad eUsuario) {
		Usuario usuario;
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRE);
		String apellido = servPersistencia.recuperarPropiedadEntidad(eUsuario, APELLIDO);
		int numeroTelefono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eUsuario, TELEFONO));
		String contraseña = servPersistencia.recuperarPropiedadEntidad(eUsuario, CONTRASEÑA);
		String correo = servPersistencia.recuperarPropiedadEntidad(eUsuario, CORREO);
		String estado = servPersistencia.recuperarPropiedadEntidad(eUsuario, ESTADO);
		LocalDate fechaNacimiento = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHA_NACIMIENTO), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate fechaAlta = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHA_ALTA), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		String foto = servPersistencia.recuperarPropiedadEntidad(eUsuario, FOTO);
		boolean isPremium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, PREMIUM));
		List<Contacto> contactosIndividuales = stringToContactos(servPersistencia.recuperarPropiedadEntidad(eUsuario, CONTACTOS));
		List<Contacto> grupos = stringToGrupos(servPersistencia.recuperarPropiedadEntidad(eUsuario, GRUPOS));
		
		usuario = new Usuario(nombre, apellido, numeroTelefono, estado, contraseña, correo, fechaNacimiento, fechaAlta);
		usuario.setCodigo(eUsuario.getId());
		usuario.setStringFoto(foto);
		usuario.setPremium(isPremium);
		List<Contacto> contactos = contactosIndividuales;
		contactos.addAll(grupos);
		usuario.setContactos(contactos);
		
		return usuario;
		
	}
	
	private String contactosToString(List<Contacto> contactos) {
		String resultado = "";
		for (Contacto c : contactos) {
			if (c != null) {
				resultado += c.getCodigo() + ",";
			}
		}
		
		resultado = resultado.trim();
		
		return resultado;
	}
	
	private List<Contacto> stringToContactos(String contactos) {
		List<Contacto> listaContactos = new ArrayList<>();
		
		String[] partes = contactos.split(",");
		
		for (int i = 0; i < partes.length; i++) {
			String codigo = partes[i].trim();
			
			if (!codigo.isEmpty()) {
				int id = Integer.parseInt(codigo);
				Contacto contacto = AdaptadorContactoIndividualDAO.getInstancia().recuperarContactoIndividual(id);
				listaContactos.add(contacto);
			}
		}
		
		return listaContactos;
	}
	
	private List<Contacto> stringToGrupos(String grupos) {
		List<Contacto> listaGrupos = new ArrayList<>();
		
		String[] partes = grupos.split(",");
		
		for (int i = 0; i < partes.length; i++) {
			String codigo = partes[i].trim();
			
			if (!codigo.isEmpty()) {
				int id = Integer.parseInt(codigo);
				Contacto grupo = AdaptadorGrupoDAO.getInstancia().recuperarGrupo(id);
				listaGrupos.add(grupo);
			}
		}
		
		return listaGrupos;
	}
}	
