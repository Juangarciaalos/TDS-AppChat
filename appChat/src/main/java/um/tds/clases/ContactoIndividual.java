package um.tds.clases;

import java.awt.Image;


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
		if (this.getNombre() == String.valueOf(usuario.getNumeroTelefono())) {
			return false;
		}
		return true;
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
