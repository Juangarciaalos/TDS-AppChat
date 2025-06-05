package um.tds.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import um.tds.clases.Contacto;
import um.tds.clases.ContactoIndividual;
import um.tds.clases.Grupo;
import um.tds.clases.Mensaje;
import um.tds.clases.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;

public class AdaptadorMensajeDAO implements MensajeDAO{
	
	private static final String EMISOR = "Emisor";
	private static final String RECEPTOR = "Receptor";
	private static final String TEXTO = "Texto";
	private static final String HORA_ENVIO = "HoraEnvio";
	private static final String EMOTICONO = "Emoticono";
	private static final String TIPO_RECEPTOR = "TipoReceptor";
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorMensajeDAO instancia = null;
	
	public static AdaptadorMensajeDAO getInstancia() {
		if (instancia == null) return new AdaptadorMensajeDAO();
		else return instancia;	
	}
	
	private AdaptadorMensajeDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	public void registrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		if (eMensaje != null) return;
		
		AdaptadorUsuarioDAO.getInstancia().registrarUsuario(mensaje.getEmisor());
		if (mensaje.getReceptor() instanceof ContactoIndividual) {
			AdaptadorContactoIndividualDAO.getInstancia().registrarContactoIndividual((ContactoIndividual) mensaje.getReceptor());
		} else {
			AdaptadorGrupoDAO.getInstancia().registrarGrupo((Grupo) mensaje.getReceptor());
		}
		
		eMensaje = crearEntidadMensaje(mensaje);
		eMensaje = servPersistencia.registrarEntidad(eMensaje);
		mensaje.setCodigo(eMensaje.getId());
		PoolDAO.INSTANCE.addObject(mensaje.getCodigo(), mensaje);
	}
	
	public void borrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		if (eMensaje == null) return;
		
		servPersistencia.borrarEntidad(eMensaje);
		if (PoolDAO.INSTANCE.containsObject(mensaje.getCodigo())) {
			PoolDAO.INSTANCE.removeObject(mensaje.getCodigo());
		}
	}
	
	public void modificarMensaje(Mensaje mensaje) {
		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		if (eMensaje == null) return;
		
		for (Propiedad prop : eMensaje.getPropiedades()) {
			if (prop.getNombre().equals(EMISOR)) {
				prop.setValor(Integer.toString(mensaje.getEmisor().getCodigo()));
			} else if (prop.getNombre().equals(RECEPTOR)) {
				if (mensaje.getReceptor() instanceof ContactoIndividual) {
					prop.setValor(Integer.toString(((ContactoIndividual) mensaje.getReceptor()).getCodigo()));
				} else {
					prop.setValor(Integer.toString(((Grupo) mensaje.getReceptor()).getCodigo()));
				}
			} else if (prop.getNombre().equals(TEXTO)) {
				prop.setValor(mensaje.getTexto());
			} else if (prop.getNombre().equals(HORA_ENVIO)) {
				prop.setValor(mensaje.getHoraEnvio().toString());
			} else if (prop.getNombre().equals(EMOTICONO)) {
				prop.setValor(Integer.toString(mensaje.getEmoticono()));
			}
			
			servPersistencia.modificarPropiedad(prop);
		}
		
	}
	
	public Mensaje recuperarMensaje(int codigo) {
		if (PoolDAO.INSTANCE.containsObject(codigo)) {
			return (Mensaje) PoolDAO.INSTANCE.getObject(codigo);
		}
		
		Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);
		Mensaje mensaje= crearMensajeDesdeEntidad(eMensaje);
		PoolDAO.INSTANCE.addObject(codigo, mensaje);
		
		return mensaje;
	}
	
	public List<Mensaje> recuperarTodosMensajes() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades("Mensaje");
		List<Mensaje> mensajes = new ArrayList<>();
		
		for (Entidad eMensaje : entidades) {
			Mensaje mensaje = recuperarMensaje(eMensaje.getId());
			mensajes.add(mensaje);
		}
		
		return mensajes;
	}
	private Entidad crearEntidadMensaje(Mensaje mensaje) {
		Entidad eMensaje = new Entidad();
		eMensaje.setNombre("Mensaje");
		
		ArrayList<Propiedad> propiedades = new ArrayList<>(Arrays.asList(
				new Propiedad(EMISOR, Integer.toString(mensaje.getEmisor().getCodigo())),
				new Propiedad(RECEPTOR, Integer.toString(mensaje.getReceptor().getCodigo())),
				new Propiedad(TEXTO, mensaje.getTexto()),
				new Propiedad(HORA_ENVIO, mensaje.getHoraEnvio().toString()),
				new Propiedad(EMOTICONO, Integer.toString(mensaje.getEmoticono())),
				new Propiedad(TIPO_RECEPTOR, mensaje.getReceptor() instanceof ContactoIndividual ? "ContactoIndividual" : "Grupo")
				));
		
		eMensaje.setPropiedades(propiedades);
		return eMensaje;
	}
	
	private Mensaje crearMensajeDesdeEntidad(Entidad eMensaje) {
		
		Mensaje mensaje;
		
		String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, TEXTO);
		LocalDateTime horaEnvio = LocalDateTime.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, HORA_ENVIO));
		int emoticono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, EMOTICONO));
		Usuario emisor = AdaptadorUsuarioDAO.getInstancia().recuperarUsuario(Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, EMISOR)));
		String tipoReceptor = servPersistencia.recuperarPropiedadEntidad(eMensaje, TIPO_RECEPTOR);
		Contacto receptor;
		if (tipoReceptor.equals("ContactoIndividual")) {
			receptor = AdaptadorContactoIndividualDAO.getInstancia().recuperarContactoIndividual(Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, RECEPTOR)));
		} else {
			receptor = AdaptadorGrupoDAO.getInstancia().recuperarGrupo(Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, RECEPTOR)));
		}
		
		if (emoticono == 0) {
			mensaje = new Mensaje(texto, emisor, receptor);
		} else {
			mensaje = new Mensaje(emoticono, emisor, receptor);
		}
		mensaje.setHoraEnvio(horaEnvio);
		mensaje.setCodigo(eMensaje.getId());
		
		return mensaje;
		
	}
	
}
