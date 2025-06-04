package um.tds.clases;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

public class ContactoIndividual extends Contacto{
	private Usuario usuario;
	
	public ContactoIndividual(String nombre, Usuario usuario) {
		super(nombre);
		this.usuario = usuario;
	}
	
	@Override
	public Image getFoto() {
		return usuario.getImagenInternet(usuario.getFoto()).getImage();
	}
	
	@Override
	public String getEstado() {
		return usuario.getEstado();
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
	
	public List<Mensaje> getListaMensaje() {
		return usuario.getAllMensajes(usuario.getNumeroTelefono()).stream().sorted().toList();
	}
}
