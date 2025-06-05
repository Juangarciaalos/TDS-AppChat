package um.tds.clases;

import java.awt.Image;
import java.util.Collections;
import java.util.List;



public abstract class Contacto {
	private String nombre;
	private List<Mensaje> listaMensaje;
	private int codigo;
	
	public Contacto(String nombre) {
		this.codigo = 0;
		this.nombre = nombre;
		this.listaMensaje = new java.util.ArrayList<Mensaje>();
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
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
	
	public void setListaMensaje(List<Mensaje> listaMensaje) {
		if (listaMensaje != null)
			this.listaMensaje = new java.util.ArrayList<>(listaMensaje);
	}
	public void addMensaje(Mensaje mensaje) {
		if (mensaje != null) {
			listaMensaje.add(mensaje);
		}
	}
	
	public abstract Image getFoto();
	
	public abstract String getEstado();
}
