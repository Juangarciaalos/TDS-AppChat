package um.tds.clases;

import java.time.LocalDateTime;

public class Mensaje {
	private String texto;
	private LocalDateTime hora;
	private int emoticono;
	private int tipo;
	private Usuario emisor;
	private Usuario receptor;
	
	public Mensaje(String texto, Usuario emisor, Usuario receptor) {
		this.texto = texto;
		this.hora = LocalDateTime.now();
		this.receptor = receptor;
		this.emisor = emisor;
		this.emoticono = 0; 
	}
	
	public Mensaje(int emoticono, Usuario emisor, Usuario receptor) {
		this.texto = "";
		this.hora = LocalDateTime.now();
		this.receptor = receptor;
		this.emisor = emisor;
		this.emoticono = emoticono;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getHora() {
		return hora;
	}

	public void setHora(LocalDateTime hora) {
		this.hora = hora;
	}

	public int getEmoticono() {
		return emoticono;
	}

	public void setEmoticono(int emoticono) {
		this.emoticono = emoticono;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Usuario getEmisor() {
		return emisor;
	}

	public void setEmisor(Usuario emisor) {
		this.emisor = emisor;
	}

	public Usuario getReceptor() {
		return receptor;
	}

	public void setReceptor(Usuario receptor) {
		this.receptor = receptor;
	}
	
	
}
