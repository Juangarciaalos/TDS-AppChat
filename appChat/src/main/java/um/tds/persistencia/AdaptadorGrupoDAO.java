package um.tds.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import um.tds.clases.ContactoIndividual;
import um.tds.clases.Grupo;
import um.tds.clases.Mensaje;

public class AdaptadorGrupoDAO implements GrupoDAO{
	
	private static final String NOMBRE = "Nombre";
	private static final String LISTA_MENSAJES = "ListaMensajes";
	private static final String FOTO = "Foto";
	private static final String ESTADO = "Estado";
	private static final String PARTICIPANTES = "Participantes";
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorGrupoDAO instancia = null;
	
	public static AdaptadorGrupoDAO getInstancia() {
		if (instancia == null) return new AdaptadorGrupoDAO();
		else return instancia;	
	}
	
	private AdaptadorGrupoDAO() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	
	public void registrarGrupo(Grupo grupo) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(grupo.getCodigo());
		if (eUsuario != null) return;
		
		for (ContactoIndividual c : grupo.getParticipantes()) {
			AdaptadorContactoIndividualDAO.getInstancia().registrarContactoIndividual(c);
		}
		
		for (Mensaje m : grupo.getListaMensaje()) {
			AdaptadorMensajeDAO.getInstancia().registrarMensaje(m);
		}
		
		eUsuario = crearEntidadGrupo(grupo);
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		grupo.setCodigo(eUsuario.getId());
		PoolDAO.INSTANCE.addObject(grupo.getCodigo(), grupo);
	}
	
	public void borrarGrupo(Grupo grupo) {
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		if (eGrupo == null) return;
		
		for (ContactoIndividual c : grupo.getParticipantes()) {
			AdaptadorContactoIndividualDAO.getInstancia().borrarContactoIndividual(c);
		}
		
		for (Mensaje m : grupo.getListaMensaje()) {
			AdaptadorMensajeDAO.getInstancia().borrarMensaje(m);
		}
		
		servPersistencia.borrarEntidad(eGrupo);
		if (PoolDAO.INSTANCE.containsObject(grupo.getCodigo())) {
			PoolDAO.INSTANCE.removeObject(grupo.getCodigo());
		}
	}
	
	public void modificarGrupo(Grupo grupo) {
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		if (eGrupo == null) return;
		
		for (Propiedad prop : eGrupo.getPropiedades()) {
			if (prop.getNombre().equals(NOMBRE)) {
				prop.setValor(grupo.getNombre());
			} else if (prop.getNombre().equals(LISTA_MENSAJES)) {
				prop.setValor(mensajesToString(grupo.getListaMensaje()));
			} else if (prop.getNombre().equals(FOTO)) {
				prop.setValor(grupo.getStringFoto());
			} else if (prop.getNombre().equals(ESTADO)) {
				prop.setValor(grupo.getEstado().toString());
			} else if (prop.getNombre().equals(PARTICIPANTES)) {
				prop.setValor(participantesToString(grupo.getParticipantes()));
			}
			
			servPersistencia.modificarPropiedad(prop);
		}
	}
	
	public Grupo recuperarGrupo(int codigo) {
		if (PoolDAO.INSTANCE.containsObject(codigo)) {
			return (Grupo) PoolDAO.INSTANCE.getObject(codigo);
		}
		
		Entidad eGrupo = servPersistencia.recuperarEntidad(codigo);
		Grupo grupoRecuperado = crearGrupoDesdeEntidad(eGrupo);
		
		return grupoRecuperado;
	}
	
	public List<Grupo> recuperarTodosGrupos() {
		List<Entidad> entidades = servPersistencia.recuperarEntidades("Grupo");
		List<Grupo> grupos = new ArrayList<>();
		
		for (Entidad eGrupo : entidades) {
			Grupo grupo = recuperarGrupo(eGrupo.getId());
			grupos.add(grupo);
		}
		
		return grupos;
	}
	
	private Entidad crearEntidadGrupo(Grupo grupo) {
		Entidad eGrupo = new Entidad();
		eGrupo.setNombre("Grupo");
		
		ArrayList<Propiedad> propiedades = new ArrayList<>(
				Arrays.asList(
				new Propiedad(NOMBRE, grupo.getNombre()),
				new Propiedad(LISTA_MENSAJES, mensajesToString(grupo.getListaMensaje())),
				new Propiedad(FOTO, grupo.getStringFoto()),
				new Propiedad(ESTADO, grupo.getEstado().toString()),
				new Propiedad(PARTICIPANTES, participantesToString(grupo.getParticipantes()))
			)
		);
		eGrupo.setPropiedades(propiedades);
		return eGrupo;
	}
	
	private Grupo crearGrupoDesdeEntidad(Entidad eGrupo) {
		Grupo grupo;
		
		String nombre = servPersistencia.recuperarPropiedadEntidad(eGrupo, NOMBRE);
		String foto = servPersistencia.recuperarPropiedadEntidad(eGrupo, FOTO);
		String estado = servPersistencia.recuperarPropiedadEntidad(eGrupo, ESTADO);
		grupo = new Grupo(nombre, null, foto, estado);
		grupo.setCodigo(eGrupo.getId());
		PoolDAO.INSTANCE.addObject(grupo.getCodigo(), grupo);

		List<Mensaje> listaMensajes = stringToMensajes(servPersistencia.recuperarPropiedadEntidad(eGrupo, LISTA_MENSAJES));
		List<ContactoIndividual> participantes = stringToParticipantes(servPersistencia.recuperarPropiedadEntidad(eGrupo, PARTICIPANTES));
		grupo.setListaMensaje(listaMensajes);
		grupo.setParticipantes(participantes);
		
		return grupo;
	}
	
	private String participantesToString(List<ContactoIndividual> participantes) {
		String resultado = "";

		for (ContactoIndividual c : participantes) {
		    resultado += c.getCodigo() + " ";
		}

		resultado = resultado.trim();

		return resultado;
	}
	
	private List<ContactoIndividual> stringToParticipantes(String participantes) {
		List<ContactoIndividual> listaParticipantes = new ArrayList<>();

		String[] partes = participantes.split(" ");

		for (int i = 0; i < partes.length; i++) {
		    String codigo = partes[i];
		    
		    if (!codigo.isEmpty()) {
		        int id = Integer.parseInt(codigo);
		        ContactoIndividual participante = AdaptadorContactoIndividualDAO.getInstancia().recuperarContactoIndividual(id);
		        listaParticipantes.add(participante);
		    }
		}

		return listaParticipantes;
		
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
