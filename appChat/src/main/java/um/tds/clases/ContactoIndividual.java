package um.tds.clases;

import java.awt.Image;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ContactoIndividual extends Contacto{
	private Usuario usuario;
	
	public ContactoIndividual(String nombre, Usuario usuario) {
		super(nombre);
		this.usuario = usuario;
	}
	
	@Override
	public Image getFoto() {
		return usuario.getFoto();
	}
	
	@Override
	public String getEstado() {
		return usuario.getEstado();
	}
	
	public boolean isContactoAgregado() {
		return !this.getNombre().equals(String.valueOf(this.getNumeroTelefono()));
	}
	
	
	@Override
	public List<Mensaje> getAllMensajes(Usuario usuario) {
		return Stream.concat(getListaMensaje().stream(), getMensajesEnviados(usuario).stream())
				.sorted()
				.collect(Collectors.toList());
	}
	
	public List<Mensaje> getMensajesEnviados(Usuario usuario) {
	    return getContactoDeUsuario(usuario)
	            .map(ContactoIndividual::getListaMensaje)
	            .orElseGet(LinkedList::new);
	}
	
	private Optional<ContactoIndividual> getContactoDeUsuario(Usuario usuario) {
		return this.usuario.getContactos().stream()
				.filter(c -> c instanceof ContactoIndividual)
				.map(c -> (ContactoIndividual) c)
				.filter(c -> c.getUsuario().getNumeroTelefono() == usuario.getNumeroTelefono())
				.findFirst();	
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public int getNumeroTelefono() {
		return usuario.getNumeroTelefono();
	}
	
	
}
