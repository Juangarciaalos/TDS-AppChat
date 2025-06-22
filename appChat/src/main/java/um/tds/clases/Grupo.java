package um.tds.clases;

import java.awt.Image;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import um.tds.Utilidades.ConversorImagenes;


public class Grupo extends Contacto{
	private List<ContactoIndividual> participantes;
	private String foto;
	private String estado;
	
	public Grupo(String nombre, List<ContactoIndividual> participantes, String foto, String estado) {
		super(nombre);
		this.participantes = participantes;
		this.foto = foto;
		this.estado = estado;
	}
	
	public String getStringFoto() {
		return foto;
	}
	
	public void setStringFoto(String foto) {
		this.foto = foto;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public List<ContactoIndividual> getParticipantes() {
		return Collections.unmodifiableList(participantes);
	}
	
	public void setParticipantes(List<ContactoIndividual> participantes) {
		this.participantes = participantes != null ? List.copyOf(participantes) : Collections.emptyList();
	}
	
	public void addParticipante(ContactoIndividual participante) {
		if (participante != null && !participantes.contains(participante)) {
			participantes.add(participante);
		}
	}
	
	public void eliminarParticipante(ContactoIndividual participante) {
		if (participante != null) {
			participantes.remove(participante);
		}
	}
	
	public Usuario getUsuarioTlf(int telefono) {
		Optional<ContactoIndividual> contacto = participantes.stream()
				.filter(c -> c.getNumeroTelefono() == telefono)
				.findFirst();
		return contacto.isPresent() ? contacto.get().getUsuario() : null;
	}
	
	@Override
	public List<Mensaje> getAllMensajes(Usuario usuario) {
		return this.getListaMensaje();
	}
	
	@Override
	public Image getFoto() {		
		return ConversorImagenes.base64ToImage(foto, 75, 75);	
	}
	
	@Override
	public String getEstado() {
		return estado;
	}
	
}
