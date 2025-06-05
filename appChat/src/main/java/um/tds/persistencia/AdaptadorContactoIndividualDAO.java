package um.tds.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import um.tds.clases.ContactoIndividual;
import um.tds.clases.Mensaje;
import um.tds.clases.Usuario;

public class AdaptadorContactoIndividualDAO implements ContactoIndividualDAO {
	
	private static final String USUARIO = "Usuario";
	private static final String NOMBRE = "Nombre";
	private static final String LISTA_MENSAJES = "ListaMensajes";
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorContactoIndividualDAO instancia = null;
	
	public static AdaptadorContactoIndividualDAO getInstancia() {
		if (instancia == null) return new AdaptadorContactoIndividualDAO();
		else return instancia;	
	}
	
	private AdaptadorContactoIndividualDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	
	public void registrarContactoIndividual(ContactoIndividual contactoIndividual) {
		Entidad eContacto = servPersistencia.recuperarEntidad(contactoIndividual.getCodigo());
		if (eContacto != null) return;
		
		AdaptadorUsuarioDAO.getInstancia().registrarUsuario(contactoIndividual.getUsuario());
		for (Mensaje m : contactoIndividual.getListaMensaje()) {
			AdaptadorMensajeDAO.getInstancia().registrarMensaje(m);
		}
		
		eContacto = crearEntidadContactoIndividual(contactoIndividual);
		eContacto = servPersistencia.registrarEntidad(eContacto);
		contactoIndividual.setCodigo(eContacto.getId());
		PoolDAO.INSTANCE.addObject(contactoIndividual.getCodigo(), contactoIndividual);
	}
	
	public void borrarContactoIndividual(ContactoIndividual contactoIndividual) {
		Entidad eContacto = servPersistencia.recuperarEntidad(contactoIndividual.getCodigo());
		if (eContacto == null) return;
		
		for (Mensaje m : contactoIndividual.getListaMensaje()) {
			AdaptadorMensajeDAO.getInstancia().borrarMensaje(m);
		}
		
		servPersistencia.borrarEntidad(eContacto);
		if (PoolDAO.INSTANCE.containsObject(contactoIndividual.getCodigo())) {
			PoolDAO.INSTANCE.removeObject(contactoIndividual.getCodigo());
		}
		
	}
	
	public void modificarContactoIndividual(ContactoIndividual contactoIndividual) {
		Entidad eContacto = servPersistencia.recuperarEntidad(contactoIndividual.getCodigo());
		if (eContacto == null) return;
		
		for (Propiedad prop : eContacto.getPropiedades()) {
			if (prop.getNombre().equals(LISTA_MENSAJES)) {
				prop.setValor(mensajesToString(contactoIndividual.getListaMensaje()));
			} else if (prop.getNombre().equals(USUARIO)) {
				prop.setValor(Integer.toString(contactoIndividual.getUsuario().getCodigo()));
			} else if (prop.getNombre().equals(NOMBRE)) {
				prop.setValor(contactoIndividual.getNombre());
			}
			
			servPersistencia.modificarPropiedad(prop);
		}
	}
	
	public ContactoIndividual recuperarContactoIndividual(int codigo) {
		if (PoolDAO.INSTANCE.containsObject(codigo)) {
			return (ContactoIndividual) PoolDAO.INSTANCE.getObject(codigo);
		}
		
		Entidad eContacto = servPersistencia.recuperarEntidad(codigo);
		ContactoIndividual contactoIndividual = crearContactoDesdeEntidad(eContacto);
		PoolDAO.INSTANCE.addObject(codigo, contactoIndividual);
		
		return contactoIndividual;
	}
	
	public List<ContactoIndividual> recuperarTodosContactosIndividuales() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades("ContactoIndividual");
		List<ContactoIndividual> contactos = new ArrayList<>();
		
		for (Entidad eContacto : entidades) {
			ContactoIndividual contactoIndividual = recuperarContactoIndividual(eContacto.getId());
			contactos.add(contactoIndividual);
		}
		
		return contactos;
	}
	
	private Entidad crearEntidadContactoIndividual(ContactoIndividual contactoIndividual) {
		Entidad eContacto = new Entidad();
		eContacto.setNombre("ContactoIndividual");
		
		ArrayList<Propiedad> propiedades = new ArrayList<>(Arrays.asList(
				new Propiedad(USUARIO, Integer.toString(contactoIndividual.getUsuario().getCodigo())),
				new Propiedad(NOMBRE, contactoIndividual.getNombre()),
				new Propiedad(LISTA_MENSAJES, mensajesToString(contactoIndividual.getListaMensaje()))));
		
		eContacto.setPropiedades(propiedades);
		return eContacto;
	}
	
	private ContactoIndividual crearContactoDesdeEntidad(Entidad eContacto) {
		ContactoIndividual contactoIndividual;
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eContacto, NOMBRE);
		List<Mensaje> mensajesRecibidos = stringToMensajes(servPersistencia.recuperarPropiedadEntidad(eContacto, LISTA_MENSAJES));
		Usuario usuario = AdaptadorUsuarioDAO.getInstancia().recuperarUsuario(Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eContacto, USUARIO)));
		
		contactoIndividual = new ContactoIndividual(nombre, usuario);
		contactoIndividual.setCodigo(eContacto.getId());
		contactoIndividual.setListaMensaje(mensajesRecibidos);
		
		return contactoIndividual;
	}
	
	private String mensajesToString(List<Mensaje> mensajesRecibidos) {
		String resultado = "";

		for (Mensaje m : mensajesRecibidos) {
		    resultado += m.getCodigo() + " ";
		}

		resultado = resultado.trim();

		return resultado;
	}
	
	private List<Mensaje> stringToMensajes(String mensajes) {
		List<Mensaje> listaMensajes = new ArrayList<>();

		String[] partes = mensajes.split(" ");

		for (int i = 0; i < partes.length; i++) {
		    String codigo = partes[i];
		    
		    if (!codigo.isEmpty()) {
		        int id = Integer.parseInt(codigo);
		        Mensaje mensaje = AdaptadorMensajeDAO.getInstancia().recuperarMensaje(id);
		        listaMensajes.add(mensaje);
		    }
		}

		return listaMensajes;
		
	}
}
