package um.tds.Utilidades;

import java.util.List;
import java.util.stream.Collectors;

import um.tds.clases.ContactoIndividual;
import um.tds.clases.Mensaje;
import um.tds.clases.Usuario;


public enum BuscadorMensajes {
	INSTANCE;
	
	public List<Mensaje> buscarMensajesEnviados( Usuario usuario, String telefono, String nombreContacto, String texto) {
		List<Mensaje> mensajes;
		if (telefono == null || telefono.isEmpty()) {
			mensajes = usuario.getMensajesEnviados();
		} else {
			mensajes = usuario.getMensajesEnviadosTlf(Integer.parseInt(telefono));
		}
		return mensajes.stream()
				.filter(m -> nombreContacto.equals("Todos") ||		 
						m.getReceptor().getNombre().equals(nombreContacto))
				.filter(m -> m.getTexto().contains(texto))
				.distinct()
				.collect(Collectors.toList());
		
		
	}
	
	public List<Mensaje> buscarMensajesRecibidos(Usuario usuario, String telefono, String nombreContacto, String texto) {
		List<Mensaje> mensajes;
		if (telefono == null || telefono.isEmpty()) {
			mensajes = usuario.getMensajesRecibidos();
		} else {
			mensajes = usuario.getMensajesRecibidosTlf(Integer.parseInt(telefono));
		}
		
		return mensajes.stream()
				.filter(m -> nombreContacto.equals("Todos") || 
						usuario.getContacto(nombreContacto) instanceof ContactoIndividual &&
						((ContactoIndividual) (usuario.getContacto(nombreContacto))).getUsuario() == m.getEmisor())
				.filter(m -> m.getTexto().contains(texto))
				.distinct()
				.collect(Collectors.toList());
	} 
}
