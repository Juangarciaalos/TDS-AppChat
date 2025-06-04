package um.tds.clases;

import java.awt.Image;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

public abstract class Contacto {
	private String nombre;
	private List<Mensaje> listaMensaje;
	
	public Contacto(String nombre) {
		this.nombre = nombre;
		this.listaMensaje = new java.util.ArrayList<Mensaje>();
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<Mensaje> getListaMensaje() {
		return Collections.unmodifiableList(listaMensaje);
	}
	
	public void addMensaje(Mensaje mensaje) {
		if (mensaje != null) {
			listaMensaje.add(mensaje);
		}
	}
	
	public abstract Image getFoto();
	
	public abstract String getEstado();
}
