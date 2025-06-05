package um.tds.clases;

import java.time.LocalDateTime;

public class Mensaje  implements Comparable<Mensaje> {
	private int codigo;
	private String texto;
	private LocalDateTime horaEnvio;
	private int emoticono;
	private Usuario emisor;
	private Contacto receptor;
	
	public Mensaje(String texto, Usuario emisor, Contacto receptor) {
		this.codigo = 0; 
		this.texto = texto;
		this.horaEnvio = LocalDateTime.now();
		this.receptor = receptor;
		this.emisor = emisor;
		this.emoticono = 0; 
	}
	
	public Mensaje(int emoticono, Usuario emisor, Contacto receptor) {
		this.codigo = 0; 
		this.texto = "";
		this.horaEnvio = LocalDateTime.now();
		this.receptor = receptor;
		this.emisor = emisor;
		this.emoticono = emoticono;
	}

	public int getCodigo() {
		return codigo;
	}
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getHoraEnvio() {
		return horaEnvio;
	}

	public void setHoraEnvio(LocalDateTime hora) {
		this.horaEnvio = hora;
	}

	public int getEmoticono() {
		return emoticono;
	}

	public void setEmoticono(int emoticono) {
		this.emoticono = emoticono;
	}	

	public Usuario getEmisor() {
		return emisor;
	}

	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}

	public Contacto getReceptor() {
		return receptor;
	}

	public void setReceptor(Contacto receptor) {
		this.receptor = receptor;
	}
	
	@Override
	public int compareTo(Mensaje otro) {
		return horaEnvio.compareTo(otro.horaEnvio);
	}
}
